package com.giusdp.htduels.duel.asset;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.asset.CardAssetCodec;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardAssetCodecTest {

    @Test
    void decodeCardFromBsonDocument() {
        BsonDocument doc = new BsonDocument()
            .append("Id", new BsonString("Fireball"))
            .append("Name", new BsonString("Fireball"))
            .append("Cost", new BsonInt32(4))
            .append("Attack", new BsonInt32(0))
            .append("Health", new BsonInt32(0))
            .append("Type", new BsonString("Spell"));

        CardAssetCodec cardCodec = new CardAssetCodec();
        CardAsset card = cardCodec.decode(doc);

        assertNotNull(card);
        assertEquals("Fireball", card.id());
        assertEquals("Fireball", card.name());
        assertEquals(4, card.cost());
        assertEquals(0, card.attack());
        assertEquals(0, card.health());
        assertEquals("Spell", card.type());
    }

    @Test
    void encodeThenDecodeRoundTrip() {
        CardAsset original = new CardAsset("Wisp", "Wisp", 0, 1, 1, "Minion");
        CardAssetCodec cardCodec = new CardAssetCodec();

        BsonValue encoded = cardCodec.encode(original);
        CardAsset decoded = cardCodec.decode(encoded);

        assertEquals(original, decoded);
    }
}
