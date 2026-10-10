package kesslercascade.rdpmouse.mixin;

import kesslercascade.rdpmouse.RDPMouseState;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow private double cursorDeltaX;
    @Shadow private double cursorDeltaY;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void rdpmouse$onUpdateMouse(double timeDelta, CallbackInfo ci) {
        if (RDPMouseState.enabled && RDPMouseState.sensitivityMultiplier != 1.0) {
            cursorDeltaX *= RDPMouseState.sensitivityMultiplier;
            cursorDeltaY *= RDPMouseState.sensitivityMultiplier;
        }

        if (RDPMouseState.panDX != 0 || RDPMouseState.panDY != 0) {
            cursorDeltaX += RDPMouseState.panDX;
            cursorDeltaY += RDPMouseState.panDY;
            RDPMouseState.panDX = 0;
            RDPMouseState.panDY = 0;
        }
    }
}
