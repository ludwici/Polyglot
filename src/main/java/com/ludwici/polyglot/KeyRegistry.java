package com.ludwici.polyglot;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static com.ludwici.polyglot.Polyglot.MODID;
import static org.lwjgl.glfw.GLFW.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyRegistry {
    private static final String CATEGORY = "Polyglot";

    public static final KeyMapping PRINT_TOOLTIP = new KeyMapping("key." + MODID + ".print_tooltip", GLFW_KEY_P, CATEGORY);

    @SubscribeEvent
    public static void onKeyBindingRegistry(RegisterKeyMappingsEvent event) {
        event.register(PRINT_TOOLTIP);
    }
}
