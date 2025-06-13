package com.ludwici.polyglot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;


import java.util.List;
import java.util.ListIterator;

@Mod(Polyglot.MODID)
public class Polyglot {
    public static final String MODID = "polyglot";

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

    private static int ticks = 0;
    private static int lastTick = 0;

    public static final Logger LOGGER = LogUtils.getLogger();


    public Polyglot(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(Polyglot::onTooltip);
        MinecraftForge.EVENT_BUS.addListener(Polyglot::onKeyPress);
    }


    public static void onKeyPress(InputEvent.Key event) {
        if (event.getAction() == InputConstants.PRESS) {
            if (KeyRegistry.PRINT_TOOLTIP.getKey().getValue() == event.getKey()) {
                ticks++;
            } else {
                ticks = 0;
            }
        }
    }

    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack pStack = event.getItemStack();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        ResourceLocation res = ForgeRegistries.ITEMS.getKey(pStack.getItem());
        if (res == null) {
            return;
        }
        if (res.getNamespace().equals("minecraft")) {
            return;
        }

        List<Component> orig = event.getToolTip();
        if (orig.isEmpty()) {
            return;
        }

        ListIterator<Component> iter = orig.listIterator();

        if (GLFW.glfwGetKey(mc.getWindow().getWindow(), KeyRegistry.PRINT_TOOLTIP.getKey().getValue()) == 1) {
            if (lastTick != ticks) {
                LOGGER.info("---Polyglot: [item.{}.{}]---", res.getNamespace(), res.getPath());
                orig.forEach(component -> {
                    LOGGER.info("|{}",component.getString());
                });
                LOGGER.info("---");
                lastTick = ticks;
            }
        }

        while (iter.hasNext()) {
            Component c = iter.next();
            int i = 0;
            do {
                String key = "item." + res.getNamespace() + "." + res.getPath() + ".polyglot.desc" + ++i;

                var entry = DataLoader.INSTANCE.translate.getOrDefault(key, "empty");

                if (entry.equals("empty")) {
                    break;
                } else {
                    String[] parts = entry.split("_&_", 2);
                    if (parts.length == 2) {
                        if (c.getString().equals(parts[0])) {
                            iter.set(Component.literal(parts[1]));
                        }
                    }
                }
            } while (true);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(DataLoader.INSTANCE);
        }
    }
}
