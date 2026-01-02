package com.example.urlshortener.infra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.example.urlshortener.core.model.Link;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonFileStorage {

    private static final Path STORAGE_FILE = Paths.get(
            System.getProperty("user.home"), ".urlshortener", "links.json"
    );

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .setPrettyPrinting()
            .create();

    public static Map<String, Link> load() {
        if (!Files.exists(STORAGE_FILE)) {
            return new ConcurrentHashMap<>();
        }
        try (Reader reader = Files.newBufferedReader(STORAGE_FILE)) {
            Type type = new TypeToken<ConcurrentHashMap<String, Link>>(){}.getType();
            Map<String, Link> data = GSON.fromJson(reader, type);
            return data != null ? data : new ConcurrentHashMap<>();
        } catch (IOException e) {
            System.err.println("Ошибка загрузки данных: " + e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    public static void save(Map<String, Link> links) {
        try {
            Files.createDirectories(STORAGE_FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(STORAGE_FILE)) {
                GSON.toJson(links, writer);
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных: " + e.getMessage());
        }
    }
}
