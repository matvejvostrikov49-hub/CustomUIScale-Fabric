package dev.customuiscale.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.customuiscale.ScaleConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
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
        PoseStack pose = graphics.pose();
        pose.pushPose();
        float scale = ScaleConfig.guiFactor();
        pose.translate(width / 2.0, height / 2.0, 0);
        pose.scale(scale, scale, 1.0f);
        pose.translate(-width / 2.0, -height / 2.0, 0);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$endScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        graphics.pose().popPose();
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
}
