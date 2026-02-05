# AGENTS.md

This is **HyDuels**, a card game mod for Hytale inspired by Hearthstone.

## Key Resources

- **`.hytale-sources/`** - Contains decompiled Hytale source code for reference
- **`docs/`** - Project documentation
- **`docs/architecture.md`** - **Important**: Core architecture document explaining the domain-driven design, entity relationships, and system structure

## Overview

The mod implements a turn-based card game where players build decks and battle using cards with various effects. The codebase follows domain-driven design principles with clear separation between game logic, events, and rendering.

## Package Structure

```
com.giusdp.htduels/
├── DuelsPlugin.java              # Entry point, wires all services via constructors
│
├── catalog/                      # CATALOG LAYER - Card definitions from JSON
│   ├── CardAsset.java            # Record: id, name, cost, attack, health, type
│   ├── CardAssetCodec.java       # JSON serialization
│   ├── CardAssetStore.java       # Asset loader
│   └── CardAssetRepo.java        # Bridges catalog → domain CardRepo interface
│
├── match/                        # DOMAIN LAYER - Pure game logic (no Hytale imports)
│   ├── Duel.java                 # Aggregate root: game state, rules, event recording
│   ├── DuelId.java               # Value object (UUID)
│   ├── Duelist.java              # Player in a duel (has Hand + Battlefield)
│   ├── Card.java                 # Card instance with owner and zone
│   ├── CardId.java               # Value object (UUID)
│   ├── DuelBuilder.java          # Fluent factory
│   ├── DuelService.java          # Application service: create/join duels
│   ├── DuelRegistry.java         # In-memory store (by ID and position)
│   ├── CardRepo.java             # Interface for card definitions
│   ├── TurnStrategy.java         # Strategy interface for turn behavior
│   ├── HumanTurnStrategy.java    # No-op (waits for mouse input)
│   ├── BotTurnStrategy.java      # Plays first card, ends turn
│   │
│   ├── event/                    # Domain events (accumulated, then flushed)
│   │   ├── DuelEvent.java        # Base class
│   │   ├── CardsDrawn.java
│   │   ├── CardPlayed.java
│   │   ├── DuelStarted.java
│   │   ├── DuelistJoined.java
│   │   ├── MainPhaseEnded.java
│   │   └── StartingDuelistSelected.java
│   │
│   ├── phases/                   # State machine for turn structure
│   │   ├── DuelPhase.java        # Base: onEnter(), tick(), onExit()
│   │   ├── WaitingPhase.java     # Waits for 2nd player (600 tick timeout)
│   │   ├── StartupPhase.java     # Draws 5 cards each
│   │   ├── TurnStartPhase.java   # Draws 1 card, shows turn indicator
│   │   ├── MainPhase.java        # Active player acts
│   │   ├── TurnEndPhase.java     # Swaps active duelist
│   │   └── DuelEndPhase.java     # Terminal state
│   │
│   └── zone/                     # Card containers
│       ├── Zone.java             # Interface
│       ├── ZoneType.java         # Enum: HAND, BATTLEFIELD, DECK, GRAVEYARD
│       ├── Hand.java
│       └── Battlefield.java
│
└── hytale/                       # HYTALE LAYER - Hytale ECS, UI, input
    ├── DuelPresentationService.java   # Orchestrator: setup, join, cleanup
    ├── DuelistSessionManager.java     # Per-player state (camera, mouse, cards)
    ├── DomainEventSync.java           # Domain events → ECS mutations
    │
    ├── camera/
    │   └── BoardCameraService.java    # Top-down camera control
    │
    ├── command/
    │   ├── DuelCommand.java           # /duel parent command
    │   ├── EndTurnCommand.java        # /duel endturn
    │   └── ForfeitCommand.java        # /duel forfeit
    │
    ├── deck/
    │   ├── DeckOpenInteraction.java   # Opens deck with 2-copy filter
    │   └── DeckSlotFilterAdapter.java # Enforces deck building rules
    │
    ├── ecs/
    │   ├── CardSpawner.java           # Factory for card entities
    │   │
    │   ├── component/
    │   │   ├── CardComponent.java         # cardId, zone info, opponent side
    │   │   ├── CardSpatialComponent.java  # target position, resolution state
    │   │   ├── CardDragComponent.java     # drag state
    │   │   ├── CardHoverComponent.java    # hover state
    │   │   ├── DuelComponent.java         # duelId, card entity refs
    │   │   └── BoardLayoutComponent.java  # layout reference
    │   │
    │   └── system/
    │       ├── DuelTicker.java                  # Main loop: tick + event sync
    │       ├── CardSpatialResolutionSystem.java # Zone → target position
    │       ├── CardMovementSystem.java          # Smooth position interpolation
    │       ├── CardRotationSystem.java          # Flip opponent cards
    │       ├── CardDragSystem.java              # Follow mouse during drag
    │       └── CardHoverSystem.java             # Lift on hover
    │
    ├── input/
    │   ├── CardInteractionService.java    # Mouse click/hover on cards
    │   ├── PlayerMouseButtonHandler.java  # Mouse button events
    │   ├── PlayerMouseMotionHandler.java  # Mouse motion events
    │   ├── BoardInteraction.java          # Board block right-click
    │   ├── BoardContext.java              # Board position + rotation
    │   └── InteractionNames.java          # Interaction name constants
    │
    ├── layout/
    │   ├── BoardLayout.java               # Board geometry (13 params)
    │   ├── CardLayouts.java               # Position math per zone
    │   ├── CardPositioningService.java    # Dispatch to layout functions
    │   └── CardPositionFunction.java      # Functional interface
    │
    └── ui/
        ├── BoardGameUi.java               # In-game overlay (turn indicator)
        └── DuelModeSelectionPage.java     # Mode picker (Bot/PvP)
```

## Key Patterns

- **Event Sourcing**: Domain records events via `recordEvent()`, flushed each tick for presentation
- **State Machine**: Phases transition via `duel.transitionTo(newPhase)`
- **Strategy Pattern**: `TurnStrategy` for Human vs Bot behavior
- **Constructor Injection**: No static registries; all wired in `DuelsPlugin.setup()`
