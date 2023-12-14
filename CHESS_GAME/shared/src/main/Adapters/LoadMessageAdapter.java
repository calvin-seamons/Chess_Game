package Adapters;

import Models.Game;
import chess.*;
import com.google.gson.*;
import webSocketMessages.serverMessages.LoadMessage;

import java.lang.reflect.Type;
import java.util.Arrays;

public class LoadMessageAdapter implements JsonDeserializer<LoadMessage> {
    public LoadMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String gameString = obj.get("gameString").getAsString();

        Game game = new Gson().fromJson(obj.get("game"), Game.class);


        // Check for null values
        if (game == null) {
            throw new JsonParseException("Invalid LoadMessage object");
        }

        return new LoadMessage(game);
    }
}
