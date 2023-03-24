package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class SpecialData {

    private String type;
    private JsonArray key;

    private JsonElement value;

    public String getType() {
        return type;
    }

    public JsonArray getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }
}
