package server;


import com.google.gson.JsonObject;

import java.util.List;

public class database {
    private String type;

    private String key;

    private String response;
    private JsonObject value;

    private String reason;

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public JsonObject getValue() {
        return value;
    }

    public String getResponse() {
        return response;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(JsonObject value) {
        this.value = value;
    }
    public String getReason() {
        return reason;
    }

}
