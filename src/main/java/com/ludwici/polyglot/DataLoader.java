package com.ludwici.polyglot;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

import static com.ludwici.polyglot.Polyglot.GSON;

public class DataLoader extends SimpleJsonResourceReloadListener {

    public static final DataLoader INSTANCE = new DataLoader();

    public Map<String, String> translate = new HashMap<>();

    private DataLoader() {
        super(GSON, "polyglot");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        Minecraft mc = Minecraft.getInstance();
        String lang = mc.getLanguageManager().getSelected();
        translate.clear();
        ResLoader.load(lang);
    }
}
