package Adapters;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;

public class ChessGameAdapter implements JsonDeserializer<ChessGame> {
    public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String teamColor = obj.get("teamColor").getAsString();
        ChessGame.TeamColor pieceTeamColor = ChessGame.TeamColor.valueOf(teamColor);

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        Gson gson = builder.create();
        ChessBoard board = gson.fromJson(obj.get("board"), Chess_Board.class);

        // Check for null values
        if (board == null) {
            throw new JsonParseException("Invalid ChessGame object");
        }

        return new Chess_Game(pieceTeamColor, board);
    }

}
