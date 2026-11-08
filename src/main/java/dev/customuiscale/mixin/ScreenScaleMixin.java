package dev.customuiscale.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.customuiscale.ScaleConfig;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenScaleMixin {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "render", at = @At("HEAD"))
    private void customuiscale$beginScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        float scale = ScaleConfig.guiFactor();
        pose.translate(width / 2.0, height / 2.0, 0);
        pose.scale(scale, scale, 1.0f);
        pose.translate(-width / 2.0, -height / 2.0, 0);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$endScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Object)this instanceof VideoSettingsScreen) {
            int center = width / 2;
            int y = height - 25;
            int left = center - 55;
            int value = ScaleConfig.guiScale();
            graphics.drawString(Minecraft.getInstance().font, Component.literal("Custom UI scale: " + value + "/10"), left, y - 12, 0xFFFFFF, true);
            graphics.fill(left, y, left + 110, y + 3, 0xFF555555);
            graphics.fill(left, y, left + (value - 1) * 12, y + 3, 0xFF55AAFF);
            graphics.fill(left + (value - 1) * 12 - 2, y - 2, left + (value - 1) * 12 + 3, y + 5, 0xFFFFFFFF);
        }
        graphics.pose().popPose();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void customuiscale$clickGuiScaleSlider(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0 || !((Object)this instanceof VideoSettingsScreen)) return;
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

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$mouseX(double x) {
        return width / 2.0 + (x - width / 2.0) / ScaleConfig.guiFactor();
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$mouseY(double y) {
        return height / 2.0 + (y - height / 2.0) / ScaleConfig.guiFactor();
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$releaseX(double x) {
        return width / 2.0 + (x - width / 2.0) / ScaleConfig.guiFactor();
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$releaseY(double y) {
        return height / 2.0 + (y - height / 2.0) / ScaleConfig.guiFactor();
    }
    @ModifyVariable(method = "mouseDragged", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$dragX(double x) {
        return width / 2.0 + (x - width / 2.0) / ScaleConfig.guiFactor();
    }

    @ModifyVariable(method = "mouseDragged", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$dragY(double y) {
        return height / 2.0 + (y - height / 2.0) / ScaleConfig.guiFactor();
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
