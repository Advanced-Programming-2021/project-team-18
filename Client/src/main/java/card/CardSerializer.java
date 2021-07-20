package card;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CardSerializer implements JsonSerializer<Card> , JsonDeserializer<Card> {


    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(card.getClass().getSimpleName()));
        result.add("properties", jsonSerializationContext.serialize(card, card.getClass()));
        return result;
    }
    @Override
    public Card deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            String fullName = typeOfT.getTypeName();
            String packageText = fullName.substring(0, fullName.lastIndexOf(".") + 1);
            return jsonDeserializationContext.deserialize(element, Class.forName(packageText + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
