package com.qclibrary.lib.io.gson;

import android.text.TextUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.qclibrary.lib.utils.QcStringUtils;

import java.io.IOException;

public class DoubleTypeAdapter extends TypeAdapter<Double> {
    @Override
    public Double read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0.0;
        }

        if (reader.peek() == JsonToken.BOOLEAN) {
            return reader.nextBoolean() ? 1.0 : 0.0;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        return reader.nextDouble();
    }

    @Override
    public void write(JsonWriter writer, Double value) throws IOException {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value(value);
    }


    private double queryString(String data) {
        if (QcStringUtils.isEmpty(data)) {
            return 0.0;
        } else {
            return Double.parseDouble(data);
        }

    }

}