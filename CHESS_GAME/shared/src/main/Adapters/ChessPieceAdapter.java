package Adapters;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
public class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
    public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        // get pieceType and teamColor from jsonElement
        JsonObject obj = jsonElement.getAsJsonObject();
        String pieceType = obj.get("PieceType").getAsString();
        String teamColor = obj.get("teamColor").getAsString();
        ChessGame.TeamColor pieceTeamColor = ChessGame.TeamColor.valueOf(teamColor);
        ChessPiece.PieceType piecePieceType = ChessPiece.PieceType.valueOf(pieceType);

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        Gson gson = builder.create();
        ChessPosition position = gson.fromJson(obj.get("position"), Chess_Position.class);

        // Check for null values
        if (position == null) {
            throw new JsonParseException("Invalid ChessPiece object");
        }

        // create new piece
        return new Chess_Piece(pieceTeamColor, piecePieceType, position);
    }
}
