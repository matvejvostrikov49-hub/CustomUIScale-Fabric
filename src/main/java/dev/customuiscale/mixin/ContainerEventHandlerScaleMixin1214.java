package dev.customuiscale.mixin;

import dev.customuiscale.ScaleConfig;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ContainerEventHandler.class)
public interface ContainerEventHandlerScaleMixin1214 {
    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$clickX(double x) {
        return mapX(x);
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$clickY(double y) {
        return mapY(y);
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$releaseX(double x) {
        return mapX(x);
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$releaseY(double y) {
        return mapY(y);
    }

    @ModifyVariable(method = "mouseDragged", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$dragX(double x) {
        return mapX(x);
    }

    @ModifyVariable(method = "mouseDragged", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$dragY(double y) {
        return mapY(y);
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$scrollX(double x) {
        return mapX(x);
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$scrollY(double y) {
        return mapY(y);
    }

    private double mapX(double x) {
        if ((Object)this instanceof Screen screen) {
            return screen.width / 2.0 + (x - screen.width / 2.0) / ScaleConfig.guiFactor();
        }
        return x;
    }

    private double mapY(double y) {
        if ((Object)this instanceof Screen screen) {
            return screen.height / 2.0 + (y - screen.height / 2.0) / ScaleConfig.guiFactor();
        }
        return y;
    }
}
