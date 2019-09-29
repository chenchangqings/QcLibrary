package com.qclibrary.lib.io.gson;

import android.text.TextUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.qclibrary.lib.utils.QcFormatUtils;
import com.qclibrary.lib.utils.QcStringUtils;

import java.io.IOException;


public class LongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public Long read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0L;
        }

        if (reader.peek() == JsonToken.STRING) {
            return queryString(reader.nextString());
        }

        try {
            return (long)reader.nextDouble();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public void write(JsonWriter writer, Long value) throws IOException {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value(value.toString());
    }

    private long queryString(String data) {
        if (QcStringUtils.isEmpty(data)) {
            return 0L;
        } else {
            return QcFormatUtils.getLong(data);
        }
    }

}

