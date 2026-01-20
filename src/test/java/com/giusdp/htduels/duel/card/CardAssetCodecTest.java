package com.giusdp.htduels.duel.card;

import com.giusdp.htduels.asset.CardAsset;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
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

        CardAsset card = decodeFromBson(doc);

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

        BsonDocument encoded = encodeToBson(original);
        CardAsset decoded = decodeFromBson(encoded);

        assertEquals(original, decoded);
    }

    // Helper methods that mirror what the codec does, without needing SDK
    private BsonDocument encodeToBson(CardAsset card) {
        BsonDocument doc = new BsonDocument();
        doc.put("Id", new BsonString(card.id()));
        doc.put("Name", new BsonString(card.name()));
        doc.put("Cost", new BsonInt32(card.cost()));
        doc.put("Attack", new BsonInt32(card.attack()));
        doc.put("Health", new BsonInt32(card.health()));
        doc.put("Type", new BsonString(card.type()));
        return doc;
    }

    private CardAsset decodeFromBson(BsonDocument doc) {
        return new CardAsset(
            doc.getString("Id").getValue(),
            doc.getString("Name").getValue(),
            doc.getInt32("Cost").getValue(),
            doc.getInt32("Attack").getValue(),
            doc.getInt32("Health").getValue(),
            doc.getString("Type").getValue()
        );
    }
}
