package com.ludwici.polyglot;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.ludwici.polyglot.Polyglot.GSON;
import static com.ludwici.polyglot.Polyglot.LOGGER;

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

        readFile("polyglot\\" + fileName);

        File[] subdirs = dir.listFiles(File::isDirectory);
        if (subdirs == null) {
            return;
        }

        for (File d : subdirs) {
            if (d.getName().equals(lang)) {
                walkFiles(d);
                break;
            }
        }
    }

    private static void readFile(String fileName) {
        try (FileInputStream stream = new FileInputStream(fileName)) {
            Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            var tree = GSON.fromJson(reader, JsonObject.class);
            tree.entrySet().forEach(j -> {
                var old = DataLoader.INSTANCE.translate.put(j.getKey(), j.getValue().getAsString());
                if (old != null) {
                    LOGGER.warn(Component.translatable("polyglot.warn.exist_key", j.getValue()).getString());
                }
            });
        } catch (Exception e) {
            LOGGER.error(Component.translatable("polyglot.error.read_file_error", fileName).getString());
        }
    }

    private static void walkFiles(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                walkFiles(file);
            } else {
                readFile(file.getAbsolutePath());
            }
        }
    }
}
