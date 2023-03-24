package server;

import com.google.gson.JsonElement;

public class JsonElementData {

    private String response;
    private JsonElement value;

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

}
