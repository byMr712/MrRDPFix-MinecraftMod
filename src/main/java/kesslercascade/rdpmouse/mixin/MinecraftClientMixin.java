package kesslercascade.rdpmouse.mixin;

import kesslercascade.rdpmouse.RDPMouseClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void rdpmouse$onClientTick(CallbackInfo ci) {
        RDPMouseClient.INSTANCE.onClientTick((MinecraftClient) (Object) this);
    }
}
