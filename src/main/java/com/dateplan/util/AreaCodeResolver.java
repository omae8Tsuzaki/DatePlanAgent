package com.dateplan.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AreaCodeResolver {
    private static final Map<String, String> EXACT_MAP = new HashMap<>();
    private static final Map<String, String> ALIAS_MAP = new HashMap<>();

    static {
        loadAreaCodes();
    }

    private static void loadAreaCodes() {
        try (InputStream is = AreaCodeResolver.class.getResourceAsStream("/area_codes.json")) {
            if (is == null) {
                throw new RuntimeException("area_codes.json not found in resources");
            }
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), JsonObject.class);

            JsonObject exact = root.getAsJsonObject("exact");
            for (String key : exact.keySet()) {
                EXACT_MAP.put(key, exact.get(key).getAsString());
            }

            JsonObject alias = root.getAsJsonObject("alias");
            for (String key : alias.keySet()) {
                ALIAS_MAP.put(key, alias.get(key).getAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load area_codes.json", e);
        }
    }

    public static String resolve(String areaName) {
        if (areaName == null || areaName.isBlank()) {
            return null;
        }

        String trimmed = areaName.trim();

        // 1. exact match (都道府県名)
        if (EXACT_MAP.containsKey(trimmed)) {
            return EXACT_MAP.get(trimmed);
        }

        // 2. alias match (エリア名・地名)
        if (ALIAS_MAP.containsKey(trimmed)) {
            return ALIAS_MAP.get(trimmed);
        }

        // 3. partial match in alias keys
        for (Map.Entry<String, String> entry : ALIAS_MAP.entrySet()) {
            if (entry.getKey().contains(trimmed) || trimmed.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 4. partial match in exact keys
        for (Map.Entry<String, String> entry : EXACT_MAP.entrySet()) {
            if (entry.getKey().contains(trimmed) || trimmed.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
