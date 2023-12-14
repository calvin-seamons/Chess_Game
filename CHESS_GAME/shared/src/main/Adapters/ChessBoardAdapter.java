package Adapters;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessBoardAdapter implements JsonDeserializer<ChessBoard> {
    public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessMove.class, new ChessMoveAdapter());
        builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        Gson gson = builder.create();
        Chess_Move move = gson.fromJson(obj.get("lastMove"), Chess_Move.class);
        ChessPiece[][] board = gson.fromJson(obj.get("board"), ChessPiece[][].class);

        return new Chess_Board(board, move);
    }
}
