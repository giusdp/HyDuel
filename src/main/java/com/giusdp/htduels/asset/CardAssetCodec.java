package com.giusdp.htduels.asset;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.EmptyExtraInfo;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import com.hypixel.hytale.codec.util.RawJsonReader;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;

public class CardAssetCodec implements AssetCodec<String, CardAsset> {
    public static final CardAssetCodec INSTANCE = new CardAssetCodec();

    private static final KeyedCodec<String> ID = new KeyedCodec<>("Id", Codec.STRING, true);
    private static final KeyedCodec<String> NAME = new KeyedCodec<>("Name", Codec.STRING, true);
    private static final KeyedCodec<Integer> COST = new KeyedCodec<>("Cost", Codec.INTEGER, true);
    private static final KeyedCodec<Integer> ATTACK = new KeyedCodec<>("Attack", Codec.INTEGER, true);
    private static final KeyedCodec<Integer> HEALTH = new KeyedCodec<>("Health", Codec.INTEGER, true);
    private static final KeyedCodec<String> TYPE = new KeyedCodec<>("Type", Codec.STRING, true);

    @Override
    public KeyedCodec<String> getKeyCodec() {
        return ID;
    }

    @Override
    public KeyedCodec<String> getParentCodec() {
        return null;
    }

    @Nullable
    @Override
    public AssetExtraInfo.Data getData(CardAsset asset) {
        return null;
    }

    @Override
    public CardAsset decodeJsonAsset(RawJsonReader reader, AssetExtraInfo<String> extraInfo) throws IOException {
        return decodeJson(reader, extraInfo);
    }

    @Override
    public CardAsset decodeAndInheritJsonAsset(RawJsonReader reader, CardAsset parent, AssetExtraInfo<String> extraInfo)
            throws IOException {
        return decodeJsonAsset(reader, extraInfo);
    }

    @Nullable
    @Override
    public CardAsset decodeAndInherit(BsonDocument doc, CardAsset parent, ExtraInfo extraInfo) {
        return decode(doc, extraInfo);
    }

    @Override
    public void decodeAndInherit(BsonDocument doc, CardAsset parent, CardAsset target, ExtraInfo extraInfo) {
    }

    // RawJsonInheritCodec methods
    @Nullable
    @Override
    public CardAsset decodeAndInheritJson(RawJsonReader reader, CardAsset parent, ExtraInfo extraInfo)
            throws IOException {
        return decodeJson(reader, extraInfo);
    }

    @Override
    public void decodeAndInheritJson(RawJsonReader reader, CardAsset parent, CardAsset target, ExtraInfo extraInfo)
            throws IOException {
    }

    // ValidatableCodec methods
    @Override
    public void validate(CardAsset value, ExtraInfo extraInfo) {
    }

    @Override
    public void validateDefaults(ExtraInfo extraInfo, Set<Codec<?>> tested) {
    }

    // SchemaConvertable
    @Nonnull
    @Override
    public Schema toSchema(@Nonnull SchemaContext context) {
        return new Schema();
    }

    // Codec methods - using KeyedCodec for encoding
    @Override
    public BsonValue encode(CardAsset card, ExtraInfo extraInfo) {
        BsonDocument doc = new BsonDocument();
        ID.put(doc, card.id(), extraInfo);
        NAME.put(doc, card.name(), extraInfo);
        COST.put(doc, card.cost(), extraInfo);
        ATTACK.put(doc, card.attack(), extraInfo);
        HEALTH.put(doc, card.health(), extraInfo);
        TYPE.put(doc, card.type(), extraInfo);
        return doc;
    }

    @Override
    public BsonValue encode(CardAsset card) {
        return encode(card, EmptyExtraInfo.EMPTY);
    }

    // Decode from BsonDocument using KeyedCodec
    @Override
    public CardAsset decode(BsonValue value, ExtraInfo extraInfo) {
        BsonDocument doc = value.asDocument();
        return new CardAsset(ID.getNow(doc, extraInfo), NAME.getNow(doc, extraInfo), COST.getNow(doc, extraInfo),
                ATTACK.getNow(doc, extraInfo), HEALTH.getNow(doc, extraInfo), TYPE.getNow(doc, extraInfo));
    }

    @Override
    public CardAsset decode(BsonValue value) {
        return decode(value, EmptyExtraInfo.EMPTY);
    }

    @Override
    public CardAsset decodeJson(@NonNull RawJsonReader reader, ExtraInfo extraInfo) throws IOException {
        BsonDocument doc = RawJsonReader.readBsonDocument(reader);
        return decode(doc, extraInfo);
    }
}
