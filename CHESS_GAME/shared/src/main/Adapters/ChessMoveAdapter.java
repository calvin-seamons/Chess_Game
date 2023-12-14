package Adapters;
import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessMoveAdapter implements JsonDeserializer<ChessMove>{
    public ChessMove deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        return builder.create().fromJson(jsonElement, Chess_Move.class);
    }
}
