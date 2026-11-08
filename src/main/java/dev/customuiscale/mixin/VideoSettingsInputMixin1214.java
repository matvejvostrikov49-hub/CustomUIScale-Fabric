package dev.customuiscale.mixin;

import dev.customuiscale.ScaleConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsInputMixin1214 {
    @Unique private double customuiscale$rawClickX;

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$mapVideoClickX(double x) {
        customuiscale$rawClickX = x;
        Screen screen = (Screen)(Object)this;
        return screen.width / 2.0 + (x - screen.width / 2.0) / ScaleConfig.guiFactor();
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$mapVideoClickY(double y) {
        Screen screen = (Screen)(Object)this;
        double factor = ScaleConfig.guiFactor();
        double x = screen.width / 2.0 + (customuiscale$rawClickX - screen.width / 2.0) / factor;
        double mappedY = screen.height / 2.0 + (y - screen.height / 2.0) / factor;
        int left = screen.width / 2 - 55;
        int top = screen.height - 25;
        if (x >= left - 4 && x <= left + 114 && mappedY >= top - 10 && mappedY <= top + 9) {
            ScaleConfig.setGuiScale(Math.max(1, Math.min(10, (int)Math.round((x - left) / 12.0) + 1)));
            return -10000.0;
        }
        return mappedY;
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$mapVideoScrollX(double x) {
        Screen screen = (Screen)(Object)this;
        return screen.width / 2.0 + (x - screen.width / 2.0) / ScaleConfig.guiFactor();
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$mapVideoScrollY(double y) {
        Screen screen = (Screen)(Object)this;
        return screen.height / 2.0 + (y - screen.height / 2.0) / ScaleConfig.guiFactor();
    }
}
