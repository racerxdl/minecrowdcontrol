package com.racerxdl.minecrowdcontrol.CrowdControl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class IntEnumAdapter<T> extends TypeAdapter<T> {
    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null || !value.getClass().isEnum()) {
            out.nullValue();
            return;
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            Method m = value.getClass().getMethod("getValue");
            out.jsonValue(gson.toJson(m.invoke(value)));
        } catch (Exception e) {
            out.jsonValue(gson.toJson(value)); // Default serialize
        }
    }

    public T read(JsonReader in) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type t = new TypeToken<T>(){}.getType();

        return gson.fromJson(in, t);
    }
}
