package com.ludwici.pob_polyglot.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getDescriptionId()Ljava/lang/String;", at = @At("RETURN"))
    private void getDescription(CallbackInfoReturnable<String> cir) {
//        System.out.println(cir.getReturnValue());
//        String key = cir.getReturnValue();
//        MutableComponent res = Component.translatable(key);
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null) {
//            return;
//        }
//        mc.player.sendSystemMessage(res);
//        System.out.println(res.getContents());
    }

}
