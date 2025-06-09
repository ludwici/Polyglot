package com.ludwici.polyglot.mixin;

import com.ludwici.polyglot.DataLoader;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {

    @Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
    private void translatable(String key, String defaultValue, CallbackInfoReturnable<String> cir) {
        var entry = DataLoader.INSTANCE.translate.get(key);
        if (entry != null) {
            cir.setReturnValue(entry);
        }
    }

}
