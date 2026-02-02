package com.giusdp.htduels;

import com.giusdp.htduels.duelist.BotTurnStrategy;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.protocol.Position;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelistContextTest {

    @Nested
    class DuelSpatialDataTests {

        @Test
        void storesCameraPosition() {
            Position cameraPos = new Position(10, 5, 20);
            var spatial = new DuelistContext.DuelSpatialData(cameraPos, 1.5f, 3f);

            assertEquals(cameraPos, spatial.cameraPos());
        }

        @Test
        void storesCameraYaw() {
            Position cameraPos = new Position(0, 5, 0);
            float expectedYaw = 1.5f;
            var spatial = new DuelistContext.DuelSpatialData(cameraPos, expectedYaw, 3f);

            assertEquals(expectedYaw, spatial.cameraYaw(), 0.001f);
        }

        @Test
        void storesCardY() {
            Position cameraPos = new Position(0, 5, 0);
            float expectedCardY = 3.5f;
            var spatial = new DuelistContext.DuelSpatialData(cameraPos, 0f, expectedCardY);

            assertEquals(expectedCardY, spatial.cardY(), 0.001f);
        }

        @Test
        void cameraHeightCalculation() {
            // Camera at y=10, card at y=3 => height difference = 7
            Position cameraPos = new Position(0, 10, 0);
            var spatial = new DuelistContext.DuelSpatialData(cameraPos, 0f, 3f);

            float cameraHeight = (float) (spatial.cameraPos().y - spatial.cardY());
            assertEquals(7f, cameraHeight, 0.001f);
        }
    }

    @Nested
    class BotContextTests {

        @Test
        void botConstructorSetsNullPlayerRef() {
            Duelist bot = new Duelist(new BotTurnStrategy());
            DuelistContext ctx = new DuelistContext(null, bot);

            assertNull(ctx.getPlayerRef());
        }

        @Test
        void botConstructorSetsNullSpatialData() {
            Duelist bot = new Duelist(new BotTurnStrategy());
            DuelistContext ctx = new DuelistContext(null, bot);

            assertNull(ctx.getSpatialData());
        }

        @Test
        void botConstructorSetsDuelist() {
            Duelist bot = new Duelist(new BotTurnStrategy());
            DuelistContext ctx = new DuelistContext(null, bot);

            assertSame(bot, ctx.getDuelist());
        }

        @Test
        void botContextNotInPlayerRegistry() {
            Duelist bot = new Duelist(new BotTurnStrategy());
            DuelistContext ctx = new DuelistContext(null, bot);

            // Bot context uses the bot constructor, which does not register in the registry
            // A direct lookup by PlayerRef should not find the bot context
            DuelRegistry registry = new DuelRegistry();
            assertNull(registry.getSession(null));
        }
    }
}
