package dev.customuiscale.mixin;

import dev.customuiscale.ScaleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsMixin {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$drawGuiScaleSlider(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int center = width / 2;
        int y = height - 25;
        int left = center - 55;
        int value = ScaleConfig.guiScale();
        graphics.drawString(Minecraft.getInstance().font, Component.literal("Custom UI scale: " + value + "/10"), center - 55, y - 12, 0xFFFFFF, true);
        graphics.fill(left, y, left + 110, y + 3, 0xFF555555);
        graphics.fill(left, y, left + (value - 1) * 12, y + 3, 0xFF55AAFF);
        graphics.fill(left + (value - 1) * 12 - 2, y - 2, left + (value - 1) * 12 + 3, y + 5, 0xFFFFFFFF);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void customuiscale$clickGuiScaleSlider(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0) return;
        double x = width / 2.0 + (mouseX - width / 2.0) / ScaleConfig.guiFactor();
        double y = height / 2.0 + (mouseY - height / 2.0) / ScaleConfig.guiFactor();
        int left = width / 2 - 55;
        int top = height - 25;
        if (x >= left - 4 && x <= left + 114 && y >= top - 10 && y <= top + 9) {
            int value = Math.max(1, Math.min(10, (int)Math.round((x - left) / 12.0) + 1));
            ScaleConfig.setGuiScale(value);
            cir.setReturnValue(true);
        }
    }
}
