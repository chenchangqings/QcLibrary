package com.qclibrary.lib.io.gson;

import android.text.TextUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.qclibrary.lib.utils.QcStringUtils;

import java.io.IOException;

public class BooleanTypeAdapter extends TypeAdapter<Boolean> {
    @Override
    public Boolean read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return false;
        }

        if (reader.peek() == JsonToken.NUMBER) {
            return reader.nextInt() == 0 ? false : true;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        return reader.nextBoolean();
    }

    @Override
    public void write(JsonWriter writer, Boolean value) throws IOException {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value(value);
    }


    private boolean queryString(String data) {
        if (QcStringUtils.isEmpty(data)) {
            return false;
        } else {
            return Boolean.parseBoolean(data);
        }

    }

}