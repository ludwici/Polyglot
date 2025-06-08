package com.ludwici.pob_polyglot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.ListIterator;

@Mod(Polyglot.MODID)
public class Polyglot {
    public static final String MODID = "pob_polyglot";

    public Polyglot(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(Polyglot::onTooltip);
//        ForgeRegistries.ITEMS.forEach(item -> {
//            String modid = item.getCreatorModId(item.getDefaultInstance());
//            if (modid.equals("cataclysm")) {
//
//            }
//        });
    }

    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack pStack = event.getItemStack();
        Minecraft mc = Minecraft.getInstance();
//        languageManager.getLanguage()
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

        while (iter.hasNext()) {
            Component c = iter.next();
            int i = 0;
            do {
                String key = "item." + res.getNamespace() + "." + res.getPath() + ".polyglot.desc" + ++i;

                MutableComponent component = Component.translatableWithFallback(key, "empty");
                if (component.getString().equals("empty")) {
                    break;
                } else {
                    String[] parts = component.getString().split("_&_", 2);
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
    }
}
