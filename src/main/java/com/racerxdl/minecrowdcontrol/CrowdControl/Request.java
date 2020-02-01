package com.racerxdl.minecrowdcontrol.CrowdControl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Request {
    public int id;
    public String code;
    public String viewer;
    public RequestType type;

    public static Request FromJSON(String jsonData) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonData, Request.class);
    }

    public String ToJSON() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(this);
    }
}