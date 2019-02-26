package com.rxhttp.lib.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonSerializer {


    private static GsonSerializer mInstance;

    public static GsonSerializer getInstance() {
        synchronized (GsonSerializer.class) {
            if (mInstance == null) {
                synchronized (GsonSerializer.class) {
                    mInstance = new GsonSerializer();
                }
            }
        }
        return mInstance;
    }

    public Gson mGson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonAdapterFactory())
            .create();

    public Gson getGson() {
        return mGson;
    }


    public class GsonAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType == String.class) {
                return (TypeAdapter<T>) new StringTypeAdapter();
            }
            if (rawType == Long.class) {
                return (TypeAdapter<T>) new LongTypeAdapter();
            }
            if (rawType == long.class) {
                return (TypeAdapter<T>) new LongTypeAdapter();
            }
            if (rawType == Integer.class) {
                return (TypeAdapter<T>) new IntTypeAdapter();
            }
            if (rawType == int.class) {
                return (TypeAdapter<T>) new IntTypeAdapter();
            }
            if (rawType == Double.class) {
                return (TypeAdapter<T>) new DoubleTypeAdapter();
            }
            if (rawType == double.class) {
                return (TypeAdapter<T>) new DoubleTypeAdapter();
            }
            if (rawType == Boolean.class) {
                return (TypeAdapter<T>) new BooleanTypeAdapter();
            }
            if (rawType == boolean.class) {
                return (TypeAdapter<T>) new BooleanTypeAdapter();
            }

            return null;
        }
    }


    public String toJson(Map object) {
        return mGson.toJson(object);
    }


    public String toJson(Object object) {
        return mGson.toJson(object);
    }


    public <T> Object fromJsonT(String json, Class<T> cls) {
        return mGson.fromJson(json, cls);
    }

    public Object fromJson(String json, Class<?> cls) {
        return mGson.fromJson(json, cls);
    }

    public <T> T fromJsonSafe(String json, Type typeOfT) {
        try {
            return mGson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object fromJson(String json, Type type) {
        return mGson.fromJson(json, type);
    }


}
