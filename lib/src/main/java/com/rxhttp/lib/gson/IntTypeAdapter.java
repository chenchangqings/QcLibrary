package com.rxhttp.lib.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.rxhttp.lib.RxHttpUtils;

import java.io.IOException;

public class IntTypeAdapter extends TypeAdapter<Integer> {
    @Override
    public Integer read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0;
        }

        if (reader.peek() == JsonToken.BOOLEAN) {
            return reader.nextBoolean() ? 1 : 0;
        }

        if (reader.peek() == JsonToken.STRING) {
            return quearyString(reader.nextString());
        }

        return reader.nextInt();
    }

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value((long) value);
    }


    private int quearyString(String data) {
        if (RxHttpUtils.isEmpty(data)) {
            return 0;
        } else {
            return Integer.parseInt(data);
        }

    }


}