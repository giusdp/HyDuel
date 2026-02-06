# HytaleDuels Architecture

A card dueling game built as a Hytale server plugin. Two players (or one player vs a bot) sit across a board, draw cards, and drag them onto the battlefield.

---

## The Big Picture

The codebase has three layers. Think of it like a sandwich:

```
  +-------------------------------------------------+
  |              PRESENTATION LAYER                  |
  |   What you see and touch: entities, cameras,     |
  |   mouse input, UI pages, card animations         |
  |   (com.giusdp.htduels.hytale)              |
  +-------------------------------------------------+
                        |
                        | reads state, translates events
                        |
  +-------------------------------------------------+
  |                DOMAIN LAYER                      |
  |   The rules of the game: duels, duelists,        |
  |   cards, turns, phases, zones                    |
  |   (com.giusdp.htduels.match)                     |
  +-------------------------------------------------+
                        |
                        | gets card definitions from
                        |
  +-------------------------------------------------+
  |               CATALOG LAYER                      |
  |   Card data loaded from JSON files               |
  |   (com.giusdp.htduels.asset)                   |
  +-------------------------------------------------+
```

**The domain layer knows nothing about Hytale.** It's pure game logic — you could test it without a server. The presentation layer is the one that talks to Hytale's ECS, camera system, and UI.

---

## How It All Gets Wired Together

`DuelsPlugin.setup()` is the entry point. It creates every service as a plain object and passes them around via constructors — no magic, no global lookups:

```
DuelsPlugin.setup()
  |
  |-- DuelRegistry           (stores active duels by position and ID)
  |-- DuelService            (creates duels, adds players/bots)
  |-- CardInteractionService (handles mouse clicks and hovers on cards)
  |-- DuelPresentationService(bridges domain <-> Hytale entities/UI)
  |-- DomainEventSync        (turns domain events into entity changes)
  |
  |-- registers 6 ECS components
  |-- registers 6 ECS systems
  |-- registers mouse event handlers
  |-- registers /duel chat command
  |-- registers board block interaction
  |-- loads card assets from JSON
```

Everything is instance-based. No static registries, no service locators.

---

## The Domain Layer (the rules of the game)

### Duel — the heart of it all

A `Duel` is one game session. Imagine two kids sitting at a table with a deck of cards:

```
        +---------------------------+
        |          DUEL             |
        |                           |
        |  id: DuelId (unique)      |
        |  duelists: [Player, Bot]  |
        |  currentPhase: MainPhase  |
        |  activeDuelist: Player    |
        |  cardIndex: {all cards}   |
        |  accumulatedEvents: [...]  |
        +---------------------------+
```

The `Duel` knows how to:
- **Draw cards** — creates `Card` objects and puts them in a player's hand
- **Play a card** — moves a card from hand to battlefield
- **Select who goes first** — coin flip
- **End a turn** — swap whose turn it is
- **Forfeit** — game over

Every time something interesting happens, the `Duel` records a domain event (like `CardsDrawn` or `CardPlayed`). These events pile up during a tick and get flushed out for the presentation layer to react to.

### Duelist — a player at the table

```
           Duelist
          /       \
   has a Hand    has a Battlefield
   (cards you    (cards you've
    can see)      played)

   has a TurnStrategy:
     - HumanTurnStrategy: does nothing (waits for mouse input)
     - BotTurnStrategy:   plays first card, ends turn immediately
```

### Card — one card in play

A `Card` has an `id`, knows its `owner` (which duelist), and knows which `Zone` it's currently in. Zones are containers:

```
  +--------+     +-------------+
  |  HAND  |---->| BATTLEFIELD |
  +--------+     +-------------+
   (your cards    (cards you've
    in hand)       played down)
```

When you drag a card from your hand to the battlefield, the domain moves it from one zone to the other.

### Phases — the turn structure

A duel goes through phases like a board game:

```
  WAITING ──> STARTUP ──> TURN START ──> MAIN PHASE ──> TURN END ──┐
  (need 2      (draw 5     (draw 1,       (player        (swap      |
   players)     each)       show turn)     acts)         players)   |
                               ^                                    |
                               └────────────────────────────────────┘

                            DUEL END  <── forfeit / win / timeout
```

Each phase has three hooks: `onEnter()`, `tick()` (runs every frame), `onExit()`. A phase transitions to the next by calling `duel.transitionTo(new NextPhase())`.

**WaitingPhase** is special: when a second player joins, the `Duel` records a `DuelistJoined` event and the phase immediately transitions to `StartupPhase`. If nobody joins within 600 ticks (~30 seconds), it times out.

---

## The Presentation Layer (what you see)

This layer takes the abstract domain state and turns it into things on screen: 3D card entities, a top-down camera, drag-and-drop interaction, UI overlays.

### The flow: domain events become visual changes

Here's the key insight — **the domain is the source of truth**. The presentation layer is just a view. Here's how changes flow:

```
  1. Domain: duel.drawCards()
       |
       v
  2. Domain: records CardsDrawn event
       |
       v
  3. ECS: DuelTicker calls duel.flushEvents()
       |
       v
  4. Presentation: DomainEventSync sees CardsDrawn
       |            -> spawns a 3D card entity for each new card
       |
       v
  5. ECS systems run each frame:
       CardSpatialResolutionSystem -> calculates where the card should be
       CardMovementSystem          -> moves it there
       CardRotationSystem          -> flips opponent cards face-down
       CardHoverSystem             -> lifts hovered cards slightly
       CardDragSystem              -> dragged cards follow the mouse
```

### Card entities — what makes up a card on screen

Each card in the game world is an ECS entity with these components:

```
  +---------------------------+
  |       Card Entity         |
  |---------------------------|
  |  CardComponent            |  which card, which zone, whose side
  |  CardSpatialComponent     |  where it should be (target position)
  |  CardDragComponent        |  is it being dragged? by whom?
  |  CardHoverComponent       |  is the mouse over it?
  |  TransformComponent       |  actual world position + rotation
  |  BoundingBox              |  for mouse hit detection
  |  PersistentModel          |  the 3D "Card" model
  +---------------------------+
```

The systems form a pipeline that runs every tick:

```
  Spatial Resolution ──> Movement ──> Rotation ──> Drag ──> Hover
  "where should it be?"  "move it"   "flip it?"   "follow  "lift
                                                   mouse"   it up"
```

### Mouse input — dragging cards to play them

When you click and drag a card, here's what happens step by step:

```
  Mouse press
    |
    v
  PlayerMouseButtonHandler
    -> finds your session (DuelistSessionManager)
    -> CardInteractionService.processClick()
       -> converts screen coords to world coords (screenToWorld)
       -> finds card under cursor (findCardAt - bounding box check)
       -> is it YOUR card? is it in your HAND?
       -> yes: start dragging (set CardDragComponent.dragged = true)

  Mouse move (every frame while dragging)
    |
    v
  PlayerMouseMotionHandler
    -> CardInteractionService.processMotion()
       -> updates mouseWorldPosition on your session
       -> CardDragSystem reads this and moves the card entity to follow

  Mouse release
    |
    v
  CardInteractionService.processClick()
    -> stop dragging
    -> is the card over the battlefield zone?
       -> YES: duel.playCard(duelist, cardId)  <-- calls into domain!
               card snaps to battlefield position on next tick
       -> NO:  card snaps back to hand position on next tick
```

### The board layout

The board is a rectangle in the world, viewed from above:

```
        opponent hand
  deck  +-----------+  graveyard
        |  opponent |
        |battlefield|
        |-----------|  <-- board center
        |  player   |
        |battlefield|
  deck  +-----------+  graveyard
        player hand
```

`BoardLayout` is a record with 13 parameters describing the geometry (depths, spacing, card widths, Y offsets). `CardLayouts` uses these measurements to compute where each card should sit based on its zone, index, and which side of the board it's on.

The board can be rotated (0, 90, 180, 270 degrees) — `toWorldPosition()` applies the rotation transform to convert local board coordinates to world coordinates.

### The camera

When you start a duel, the camera snaps to a top-down view:

```
         Camera (looking straight down)
              |
              |  Y_OFFSET = 1.75 blocks above
              |
              v
        +-----------+
        |   Board   |
        +-----------+
```

Player 1's camera yaw matches the board rotation. Player 2's yaw is flipped 180 degrees so they see the board from the opposite side. `screenToWorld()` projects screen coordinates back to the board plane using this camera setup (FOV = 80 degrees, 16:9 aspect ratio).

### Sessions — per-player state

Each human player in a duel gets a `DuelistSessionManager`:

```
  DuelistSessionManager
    |-- playerRef          who is this player
    |-- duelRef            the ECS duel entity
    |-- duelist            their domain Duelist
    |-- duel               the domain Duel
    |-- spatialData        camera position, yaw, card Y height
    |-- cardRefs           their card entities (for hit testing)
    |-- boardGameUi        the UI overlay
    |-- mouseWorldPosition last mouse position in world coords
    |-- draggedCard        currently dragged card (if any)
```

Bots get a minimal session with no spatial data, no player ref, and no UI.

### UI

Two UI pages:
- **DuelModeSelectionPage** — shows when you right-click the board. Pick "Bot" or "PvP".
- **BoardGameUi** — the in-game overlay. Shows whose turn it is ("Your Turn" / "Opponent's Turn" / "Waiting for opponent...").

### Chat commands

- `/duel endturn` — ends your turn (only works during MainPhase when it's your turn)
- `/duel forfeit` — immediately ends the duel

---

## The Catalog Layer (card definitions)

Card stats come from JSON files loaded at startup:

```
  CardAsset (record)
    id: "fire_elemental"
    name: "Fire Elemental"
    cost: 3
    attack: 4
    health: 3
    type: "Creature"
```

`CardAssetStore` loads them from the `Cards/` asset path. `CardAssetRepo` implements the domain's `CardRepo` interface, bridging Hytale's asset system to the domain layer.

---

## The Full Lifecycle of a Duel

Putting it all together — from clicking the board to the game ending:

```
  1. Player right-clicks board block
     -> BoardInteraction fires
     -> No duel here? Open DuelModeSelectionPage

  2. Player picks "Bot" mode
     -> DuelPresentationService.createAndSpawnDuel()
        creates Duel domain object + ECS duel entity
     -> joinAsPlayer(): creates Duelist, activates camera, opens BoardGameUi
     -> joinAsBot(): creates bot Duelist with BotTurnStrategy

  3. Second duelist joins -> DuelistJoined event -> StartupPhase
     -> Draws 5 cards each (over 10 ticks)
     -> Each CardsDrawn event spawns card entities via DomainEventSync

  4. TurnStartPhase -> draws 1 card -> MainPhase
     -> Human's turn: waits for mouse input
     -> Bot's turn: immediately plays first card and ends turn

  5. Player drags a card to the battlefield
     -> domain: duel.playCard()
     -> event: CardPlayed
     -> sync: DomainEventSync updates CardComponent zone
     -> ECS: card repositions from hand to battlefield

  6. Turns keep cycling: TurnEnd -> TurnStart -> Main -> TurnEnd -> ...

  7. Someone forfeits (or wins)
     -> DuelEndPhase
     -> DuelTicker calls presentationService.cleanup()
        -> removes all card entities
        -> resets camera to first person
        -> dismisses UI
        -> unregisters from registry
```

---

## Package Map

```
com.giusdp.htduels/
|-- DuelsPlugin                        Entry point, wires everything
|
|-- match/                             DOMAIN LAYER
|   |-- Duel                           Aggregate root: game state + rules
|   |-- DuelId                         Value object: unique duel identity
|   |-- Duelist                        A player in the duel
|   |-- Card                           A card instance in play
|   |-- CardId                         Value object: unique card identity
|   |-- DuelBuilder                    Fluent factory for Duel
|   |-- DuelService                    Application service: create/join duels
|   |-- DuelRegistry                   In-memory store of active duels
|   |-- CardRepo                       Interface: card definitions
|   |-- TurnStrategy                   Strategy interface for turn behavior
|   |-- HumanTurnStrategy              No-op (human acts via mouse)
|   |-- BotTurnStrategy                Plays first card, ends turn
|   |-- event/                         Domain events (CardsDrawn, CardPlayed, ...)
|   |-- phases/                        Phase state machine
|   |-- zone/                          Hand, Battlefield, ZoneType enum
|
|-- catalog/                           CATALOG LAYER
|   |-- CardAsset                      Card data record
|   |-- CardAssetCodec                 JSON codec
|   |-- CardAssetStore                 Asset loader
|   |-- CardAssetRepo                  Bridges catalog -> domain CardRepo
|
|-- presentation/                      PRESENTATION LAYER
|   |-- DuelPresentationService        Orchestrator: setup, join, cleanup
|   |-- DuelistSessionManager          Per-player session state
|   |-- DomainEventSync                Domain events -> ECS mutations
|   |-- camera/
|   |   |-- BoardCameraService         Top-down camera control
|   |-- command/
|   |   |-- DuelCommand                /duel parent command
|   |   |-- EndTurnCommand             /duel endturn
|   |   |-- ForfeitCommand             /duel forfeit
|   |-- ecs/
|   |   |-- CardSpawner                Creates card entities with all components
|   |   |-- component/                 CardComponent, CardDrag, CardHover, CardSpatial,
|   |   |                              DuelComponent, BoardLayoutComponent
|   |   |-- system/                    DuelTicker, CardSpatialResolution, CardMovement,
|   |                                  CardRotation, CardDrag, CardHover
|   |-- input/
|   |   |-- CardInteractionService     Mouse click/hover logic on cards
|   |   |-- PlayerMouseButtonHandler   Global mouse click listener
|   |   |-- PlayerMouseMotionHandler   Global mouse motion listener
|   |   |-- BoardInteraction           Right-click board block handler
|   |   |-- BoardContext               Board position + rotation + player ref
|   |-- layout/
|   |   |-- BoardLayout                Board geometry (13 parameters)
|   |   |-- CardLayouts                Position math per zone type
|   |   |-- CardPositioningService     Dispatches to correct layout function
|   |   |-- CardPositionFunction       Functional interface
|   |-- ui/
|       |-- BoardGameUi                In-game overlay (turn indicator)
|       |-- DuelModeSelectionPage      Mode picker (Bot / PvP)
```

---

## Design Decisions

1. **DDD layering** — Domain has zero Hytale imports. Testable in isolation.
2. **Events bridge the gap** — Domain records events, presentation reacts. No callbacks, no observers inside the domain.
3. **ECS is a view** — Card entities are derived from domain state, not the other way around. The domain `Card` is the source of truth.
4. **Phase state machine** — Clean lifecycle hooks (`onEnter`/`tick`/`onExit`). Self-transitioning.
5. **Strategy pattern for turn behavior** — Humans and bots share the same `Duelist` class, differing only in `TurnStrategy`.
6. **No static registries** — All services are instances, wired via constructors in `DuelsPlugin.setup()`.
7. **Builder pattern** — `DuelBuilder` for flexible duel construction.
8. **Lightweight event sourcing** — `recordEvent()` + `flushEvents()` decouples domain logic from presentation side-effects.
