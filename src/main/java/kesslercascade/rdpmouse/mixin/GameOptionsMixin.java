package kesslercascade.rdpmouse.mixin;

import kesslercascade.rdpmouse.RDPMouseClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Shadow @Final @Mutable
    public KeyBinding[] allKeys;

    @Inject(method = "load", at = @At("HEAD"))
    private void rdpmouse$registerKeyBindings(CallbackInfo ci) {
        if (this.allKeys == null) {
            return;
        }
        for (KeyBinding key : RDPMouseClient.ALL_KEYS) {
            if (!ArrayUtils.contains(this.allKeys, key)) {
                this.allKeys = ArrayUtils.add(this.allKeys, key);
            }
        }
    }
}
