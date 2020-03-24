package com.racerxdl.minecrowdcontrol.CrowdControl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Response {
    public int id;
    public EffectResult status;
    public String message;

    public Response() {
        this.id = 0;
        this.status = EffectResult.Unavailable;
        this.message = "Not available";
    }

    public static Response FromJSON(String jsonData) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonData, Response.class);
    }

    public String ToJSON() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EffectResult.class, new IntEnumAdapter<EffectResult>());
        builder.registerTypeAdapter(RequestType.class, new IntEnumAdapter<RequestType>());
        Gson gson = builder.create();

        return gson.toJson(this);
    }
}