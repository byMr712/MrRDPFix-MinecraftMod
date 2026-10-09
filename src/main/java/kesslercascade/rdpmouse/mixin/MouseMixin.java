package kesslercascade.rdpmouse.mixin;

import kesslercascade.rdpmouse.RDPMouseCursor;
import kesslercascade.rdpmouse.RDPMouseState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow private double cursorDeltaX;
    @Shadow private double cursorDeltaY;
    @Shadow private boolean cursorLocked;

    @Unique private GLFWCursorPosCallback rdpmouse$vanillaCallback;

    @Inject(method = "setup", at = @At("RETURN"))
    private void rdpmouse$onSetup(long window, CallbackInfo ci) {
        rdpmouse$vanillaCallback = GLFW.glfwSetCursorPosCallback(window, (win, x, y) -> {
            if (RDPMouseState.enabled && cursorLocked) {
                int winW = this.client.getWindow().getWidth();
                int winH = this.client.getWindow().getHeight();

                if (winW <= 0 || winH <= 0) return;

                if (RDPMouseState.lastX == RDPMouseState.UNSET) {
                    RDPMouseState.lastX = x;
                    RDPMouseState.lastY = y;
                    return;
                }

                if (RDPMouseState.justRecenter) {
                    double distSq = Math.pow(x - RDPMouseState.recenterTargetX, 2) + Math.pow(y - RDPMouseState.recenterTargetY, 2);
                    double threshold = Math.max(16.0, winW * 0.05);
                    if (distSq <= threshold * threshold) {
                        RDPMouseState.justRecenter = false;
                        RDPMouseState.lastX = x;
                        RDPMouseState.lastY = y;
                        return;
                    }
                }

                double dx = x - RDPMouseState.lastX;
                double dy = y - RDPMouseState.lastY;
                RDPMouseState.lastX = x;
                RDPMouseState.lastY = y;

                // Skip teleport delta jumps when recentering
                if (Math.abs(dx) > winW * 0.25 || Math.abs(dy) > winH * 0.25) {
                    return;
                }

                cursorDeltaX += dx * RDPMouseState.sensitivityMultiplier;
                cursorDeltaY += dy * RDPMouseState.sensitivityMultiplier;

                // Seamlessly recenter cursor when it approaches window boundaries
                int marginX = Math.max(20, (int) (winW * 0.15));
                int marginY = Math.max(20, (int) (winH * 0.15));

                if (x < marginX || x > winW - marginX || y < marginY || y > winH - marginY) {
                    double centerX = winW / 2.0;
                    double centerY = winH / 2.0;
                    RDPMouseState.justRecenter = true;
                    RDPMouseState.recenterTargetX = centerX;
                    RDPMouseState.recenterTargetY = centerY;
                    GLFW.glfwSetCursorPos(win, centerX, centerY);
                }
            } else if (rdpmouse$vanillaCallback != null) {
                rdpmouse$vanillaCallback.invoke(win, x, y);
            }
        });
    }

    @Inject(method = "lockCursor", at = @At("TAIL"))
    private void rdpmouse$onLockCursor(CallbackInfo ci) {
        if (RDPMouseState.enabled) {
            long window = this.client.getWindow().getHandle();
            if (window != 0L) {
                RDPMouseState.reset();
                if (InputUtil.isRawMouseMotionSupported()) {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_FALSE);
                }
                GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
                RDPMouseCursor.clipCursor(window);

                int winW = this.client.getWindow().getWidth();
                int winH = this.client.getWindow().getHeight();
                if (winW > 0 && winH > 0) {
                    double centerX = winW / 2.0;
                    double centerY = winH / 2.0;
                    RDPMouseState.justRecenter = true;
                    RDPMouseState.recenterTargetX = centerX;
                    RDPMouseState.recenterTargetY = centerY;
                    GLFW.glfwSetCursorPos(window, centerX, centerY);
                }
            }
        }
    }

    @Inject(method = "unlockCursor", at = @At("HEAD"))
    private void rdpmouse$onUnlockCursor(CallbackInfo ci) {
        if (!this.cursorLocked) return;
        RDPMouseState.reset();
        long window = this.client.getWindow().getHandle();
        if (window != 0L && RDPMouseState.enabled) {
            RDPMouseCursor.releaseClip();
        }
    }

    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void rdpmouse$onResolutionChanged(CallbackInfo ci) {
        if (RDPMouseState.enabled && this.cursorLocked) {
            long window = this.client.getWindow().getHandle();
            if (window != 0L) {
                RDPMouseCursor.clipCursor(window);
            }
        }
    }

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void rdpmouse$onUpdateMouse(double timeDelta, CallbackInfo ci) {
        if (RDPMouseState.panDX != 0 || RDPMouseState.panDY != 0) {
            cursorDeltaX += RDPMouseState.panDX;
            cursorDeltaY += RDPMouseState.panDY;
            RDPMouseState.panDX = 0;
            RDPMouseState.panDY = 0;
        }
    }
}
