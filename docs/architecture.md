# Hytale Duels - Architecture Overview

## High-Level Architecture

```
+------------------+     +------------------+     +------------------+
|   Player Input   |     |   ECS Systems    |     |   Duel Engine    |
|  (Mouse/Command) |---->|  (Tick-based)    |---->|  (State Machine) |
+------------------+     +------------------+     +------------------+
        |                        |                        |
        v                        v                        v
+------------------+     +------------------+     +------------------+
| CardInteraction  |     |  Card Spawning   |     |  Event Handlers  |
|    Service       |     |  & Positioning   |     |  (Draw, Play...) |
+------------------+     +------------------+     +------------------+
```

---

## Package Structure

```
com.giusdp.htduels/
|-- DuelsPlugin              Plugin bootstrap, registers everything
|-- DuelistContext           Per-player session state + static registry
|-- DuelCleanupService       Teardown on duel end
|
|-- asset/                   Card data loading (CardAsset, CardAssetCodec, CardAssetStore)
|-- command/duel/            Chat commands (EndTurnCommand, ForfeitCommand)
|-- component/               ECS components (DuelComponent, CardComponent, CardSpatialComponent, ...)
|-- duel/                    Core game logic
|   |-- Duel                 Central state machine
|   |-- DuelBuilder          Fluent duel factory
|   |-- Card                 Card instance in play
|   |-- event/               Game events (DrawCards, PlayCard, EndMainPhase, ...)
|   |-- eventbus/            Event dispatch (GameEventBus, HytaleEventBus)
|   |-- handlers/            Event handlers (DrawCardsHandler, PlayCardHandler, ...)
|   |-- phases/              Turn phases (StartupPhase, MainPhase, TurnEndPhase, ...)
|   |-- positioning/         Board layout & card positions (BoardLayout, CardLayouts)
|   |-- zone/                Card zones (Hand, Battlefield)
|-- duelist/                 Player types (DuelPlayer, Bot)
|-- handlers/                Mouse input handlers
|-- interaction/             Board interaction, card drag/drop, duel setup
|-- spawn/                   Card entity factory (CardSpawner)
|-- system/                  ECS tick systems
|-- ui/                      UI pages (DuelModeSelectionPage, BoardGameUi)
```

---

## Duel Lifecycle

```
Player clicks board block
        |
        v
+-------------------+
| BoardInteraction   |----> Checks activeDuels registry
+-------------------+
        |
        |  No existing duel? --> Opens DuelModeSelectionPage
        |  Existing duel in WaitingPhase? --> joinAsPlayer directly (Player 2)
        |  Existing duel in other phase? --> Rejected (board in use)
        |
        v  (player picks mode from UI)
+-------------------+
| DuelSetupService   |
+-------------------+
  |  createAndSpawnDuel: empty Duel, BoardLayout, duel entity, activeDuels entry
  |  joinAsPlayer: DuelistContext + camera + BoardGameUi
  |  joinAsBot (bot mode only): Bot DuelistContext
  |
  v
+-------------------+     tick      +-------------------+
| DuelTicker        |-------------->| Duel.tick()       |
| (ECS System)      |              | (phase.tick())    |
+-------------------+              +-------------------+
                                            |
                                            v  (duel ends)
                                   +-------------------+
                                   | DuelCleanupService |
                                   +-------------------+
                                     Removes card entities
                                     Resets camera to first-person
                                     Dismisses UI
                                     Clears DuelistContext registry
```

---

## Phase State Machine

```
 WaitingPhase -----> StartupPhase -----> TurnStartPhase -----> MainPhase
 (wait for 2         (draw 5 cards       (draw 1 card,         (player/bot
  duelists or         each, pick who      update turn           takes turn)
  timeout)            goes first)         indicator)                |
                                              ^                     |
                                              |                     v
                                              +------------ TurnEndPhase
                                                            (swap active
                                                             duelist)

                          DuelEndPhase  <-- forfeit / win / timeout
                          (terminal, triggers cleanup)
```

Each phase: `onEnter()` -> `tick()` (every frame) -> `onExit()` -> next phase.

`WaitingPhase` → `StartupPhase` transition is **event-driven**: `Duel.addDuelist()` emits a `DuelistJoined` event, and `DuelistJoinedHandler` transitions to `StartupPhase` when ≥2 duelists are present. If duelists are already present at `onEnter()` (e.g. built via builder), the transition happens immediately during `setup()`. The tick loop only handles the timeout: if `MAX_WAIT_TICKS` (600 = ~30s) expires without a second player, it transitions to `DuelEndPhase(TIMEOUT)`.

---

## Event System

```
Phase / Input
     |
     |  emit(event)
     v
+-----------+       +-----------------------+
| Duel      |------>| GameEventBus          |
|           |       | (HytaleEventBus impl) |
+-----------+       +-----------------------+
                        |
                        |  dispatch to registered handlers
                        v
              +---------------------+
              | DuelEventHandler(s) |
              +---------------------+
              | DrawCardsHandler      |  -- adds cards to hand
              | PlayCardHandler       |  -- moves card to battlefield
              | EndMainPhaseHandler   |  -- transitions to TurnEndPhase
              | RandomDuelistSelect   |  -- picks who goes first
              | DuelistJoinedHandler  |  -- transitions WaitingPhase → StartupPhase when ≥2
              +---------------------+
```

Events: `DuelStarted`, `RandomDuelistSelect`, `DrawCards`, `PlayCard`, `EndMainPhase`, `DuelistJoined`

---

## ECS Systems

```
DuelTicker                  Advances duel phase state machine
CardSpawnSystem             Detects new cards in zones, spawns entities via CardSpawner
CardSpatialResolutionSystem Calculates target position per card based on zone + layout
CardMovementSystem          Moves card entities toward target position
CardRotationSystem          Rotates cards (opponent hand cards face away)
CardDragSystem              Makes dragged cards follow mouse position
CardHoverSystem             Lifts hovered cards slightly on Y axis
```

---

## Card Entity Components

```
+---------------------------+
| Card Entity               |
|---------------------------|
| CardComponent             |  card model + duel entity ref
| CardSpatialComponent      |  target position, zone cache
| CardDragComponent         |  dragged flag + dragger ref
| CardHoverComponent        |  hovered flag
| TransformComponent        |  world position + rotation
| PersistentModel           |  visual model ("Card")
| BoundingBox               |  hit detection (0.23 scale)
| NetworkId + UUID          |  identity
+---------------------------+
```

---

## Mouse Input Flow

```
PlayerMouseButtonEvent / PlayerMouseMotionEvent
        |
        v
PlayerMouseButtonHandler / PlayerMouseMotionHandler
        |
        |  lookup DuelistContext from static registry
        v
CardInteractionService
        |
        |-- screenToWorld()        Convert screen coords -> world coords
        |                          (camera pos, FOV=80, yaw rotation)
        |
        |-- findCardAt()           Find card entity under mouse
        |                          (bounding box intersection)
        |
        |-- processClick()
        |   |-- mouse down: start drag (set CardDragComponent)
        |   |-- mouse up:   check drop zone
        |       |-- in battlefield? -> emit PlayCard event
        |       |-- not valid?      -> cancel drag, card snaps back
        |
        |-- processMotion()
            |-- update mouseWorldPosition on context
            |-- update hover state on CardHoverComponent
```

---

## Board Layout & Positioning

```
        opponent hand         (opponentHandDepth)
  deck  +-----------+ graveyard
        |           |
        | opponent  |         (opponentBattlefieldDepth)
        | battlefield|
        |-----------|         <- board origin (0,0 in local space)
        | player    |
        | battlefield|        (playerBattlefieldDepth)
        |           |
  deck  +-----------+ graveyard
        player hand           (playerHandDepth)

  Local coords -> World coords via rotation transform
  BoardLayout.toWorldPosition(localX, localZ) applies rotation
```

`CardLayouts` computes local positions per zone type. `CardPositioningService` dispatches to the right layout function. `BoardLayout.toWorldPosition()` converts to world space.

---

## Duel Setup Flow (Unified)

All duels follow a create → join pattern via composable methods on `DuelSetupService`:

```
DuelSetupService.createAndSpawnDuel(ctx, store)
  |-- Builds empty Duel (0 duelists) via DuelBuilder
  |-- Creates BoardLayout, spawns duel entity
  |-- Registers in activeDuels map (Vector3i -> Ref<EntityStore>)

DuelSetupService.joinAsPlayer(playerRef, ctx, store, duelRef)
  |-- Creates DuelPlayer, sets isOpponentSide based on current duelist count
  |-- Player 1: cameraYaw = rotation.getRadians()
  |-- Player 2: cameraYaw = rotation.getRadians() + PI (opposite side)
  |-- Activates camera, registers DuelistContext + BoardGameUi

DuelSetupService.joinAsBot(duelRef, store)
  |-- Creates Bot, adds to duel, registers DuelistContext (no UI)
```

**Bot duel:** `createAndSpawnDuel` → `joinAsPlayer` → `joinAsBot` — the second `addDuelist` call emits `DuelistJoined`, handler transitions to `StartupPhase` immediately (no tick delay).

**PvP duel:** Player 1: `createAndSpawnDuel` → `joinAsPlayer` (waits in WaitingPhase). Player 2 interacts with same board → `BoardInteraction` detects existing duel in WaitingPhase → `joinAsPlayer` directly (skips mode selection) → `DuelistJoined` event fires → immediate transition to `StartupPhase`.

### Active Duels Registry

`DuelSetupService` maintains a static `Map<Vector3i, Ref<EntityStore>> activeDuels` for O(1) board→duel lookup. Used by `BoardInteraction` to detect in-progress duels and by `DuelCleanupService` to remove entries on duel end.

---

## Key Data Structures

```
Duel
 |-- duelists: ArrayList<Duelist>    (0-2, mutable via addDuelist())
 |-- boardPosition: Vector3i         (set during creation)
 |-- eventBus: GameEventBus
 |-- cardRepo: CardRepo
 |-- currentPhase: Phase
 |-- activeDuelist: Duelist
 |-- contexts: [DuelistContext, ...]
 |-- cardEntities: [Ref<EntityStore>, ...]   (shared, all cards)

DuelistContext
 |-- playerRef: PlayerRef               (null for bot)
 |-- duelRef: Ref<EntityStore>
 |-- duelist: Duelist
 |-- spatialData: {cameraPos, cameraYaw, cardY}
 |-- boardGameUi: BoardGameUi           (null for bot)
 |-- mouseWorldPosition: Vec2f
 |-- draggedCard: Ref<EntityStore>
 |-- cardRefs: [Ref<EntityStore>, ...]  (per-owner cards only)

BoardLayout (record)
 |-- boardOrigin, rotation
 |-- player/opponent battlefield/hand depths
 |-- spacing, card widths, Y offsets
```

---

## Duelist Types

```
         Duelist (abstract)
        /         \
  DuelPlayer      Bot
  takeTurn():     takeTurn():
    no-op           play first card
    (waits for      then end turn
     mouse input)
```
