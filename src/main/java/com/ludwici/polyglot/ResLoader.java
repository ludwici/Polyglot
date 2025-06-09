package com.ludwici.polyglot;

import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.ludwici.polyglot.Polyglot.GSON;
import static com.mojang.text2speech.Narrator.LOGGER;

public class ResLoader {
    public static File dir;

    public static void load(String lang) {
        if (dir == null) {
            dir = new File("polyglot");
            if (!dir.exists()) {
                dir.mkdir();
            }
        }

        String fileName = lang + ".json";

        try (FileInputStream stream = new FileInputStream("polyglot\\" + fileName)) {
            Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            var tree = GSON.fromJson(reader, JsonObject.class);
            tree.entrySet().forEach(j -> {
                DataLoader.INSTANCE.translate.put(j.getKey(), j.getValue().getAsString());
            });
        } catch (Exception e) {
            LOGGER.error("Failed to read {} file", fileName);
        }

    }
}
