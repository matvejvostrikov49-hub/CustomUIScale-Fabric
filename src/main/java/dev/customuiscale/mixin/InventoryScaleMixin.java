package dev.customuiscale.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.customuiscale.ScaleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryScaleMixin {
    @Shadow public int width;
    @Shadow public int height;
    @Shadow protected int imageHeight;

    @Inject(method = "render", at = @At("HEAD"))
    private void customuiscale$beginInventoryScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        float scale = ScaleConfig.inventoryFactor();
        pose.translate(width / 2.0, height / 2.0, 0);
        pose.scale(scale, scale, 1.0f);
        pose.translate(-width / 2.0, -height / 2.0, 0);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$endInventoryScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        graphics.pose().popPose();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$drawInventorySlider(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int centerX = width / 2;
        int y = Math.min(height - 14, height / 2 + imageHeight / 2 + 10);
        int left = centerX - 38;
        int value = ScaleConfig.inventoryScale();
        graphics.drawString(Minecraft.getInstance().font, Component.literal("Inventory " + value + "/5"), centerX - 36, y - 10, 0xFFFFFF, true);
        graphics.fill(left, y, left + 76, y + 3, 0xFF555555);
        graphics.fill(left, y, left + (value - 1) * 19, y + 3, 0xFF55AAFF);
        graphics.fill(left + (value - 1) * 19 - 2, y - 2, left + (value - 1) * 19 + 3, y + 5, 0xFFFFFFFF);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void customuiscale$inventorySliderClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0) return;
        double x = width / 2.0 + (mouseX - width / 2.0) / (ScaleConfig.guiFactor() * ScaleConfig.inventoryFactor());
        double y = height / 2.0 + (mouseY - height / 2.0) / (ScaleConfig.guiFactor() * ScaleConfig.inventoryFactor());
        int centerX = width / 2;
        int sliderY = Math.min(height - 14, height / 2 + imageHeight / 2 + 10);
        int left = centerX - 38;
        if (x >= left - 5 && x <= left + 81 && y >= sliderY - 8 && y <= sliderY + 9) {
            int value = Math.max(1, Math.min(5, (int)Math.round((x - left) / 19.0) + 1));
            ScaleConfig.setInventoryScale(value);
            cir.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$inventoryMouseX(double x) {
        return width / 2.0 + (x - width / 2.0) / ScaleConfig.inventoryFactor();
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$inventoryMouseY(double y) {
        return height / 2.0 + (y - height / 2.0) / ScaleConfig.inventoryFactor();
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$inventoryReleaseX(double x) {
        return width / 2.0 + (x - width / 2.0) / ScaleConfig.inventoryFactor();
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$inventoryReleaseY(double y) {
        return height / 2.0 + (y - height / 2.0) / ScaleConfig.inventoryFactor();
    }
}
