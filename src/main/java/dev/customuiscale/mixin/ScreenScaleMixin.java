package dev.customuiscale.mixin;

import dev.customuiscale.ScaleConfig;
import dev.customuiscale.ScaleTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenScaleMixin {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "render", at = @At("HEAD"))
    private void customuiscale$beginScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScaleTransform.push(graphics, width, height, ScaleConfig.guiFactor());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$endScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Object)this instanceof VideoSettingsScreen) {
            int left = width / 2 - 55;
            int y = height - 25;
            int value = ScaleConfig.guiScale();
            graphics.drawString(Minecraft.getInstance().font, Component.literal("Custom UI scale: " + value + "/10"), left, y - 12, 0xFFFFFF, true);
            graphics.fill(left, y, left + 110, y + 3, 0xFF555555);
            graphics.fill(left, y, left + (value - 1) * 12, y + 3, 0xFF55AAFF);
            graphics.fill(left + (value - 1) * 12 - 2, y - 2, left + (value - 1) * 12 + 3, y + 5, 0xFFFFFFFF);
        }
        ScaleTransform.pop(graphics);
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int customuiscale$renderMouseX(int x) {
        return (int)Math.round(width / 2.0 + (x - width / 2.0) / ScaleConfig.guiFactor());
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int customuiscale$renderMouseY(int y) {
        return (int)Math.round(height / 2.0 + (y - height / 2.0) / ScaleConfig.guiFactor());
    }
}
