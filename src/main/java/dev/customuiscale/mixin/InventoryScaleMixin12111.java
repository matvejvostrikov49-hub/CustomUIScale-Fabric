package dev.customuiscale.mixin;

import dev.customuiscale.ScaleConfig;
import dev.customuiscale.ScaleTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryScaleMixin12111 {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "render", at = @At("HEAD"))
    private void customuiscale$beginInventoryScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScaleTransform.push(graphics, width, height, ScaleConfig.inventoryFactor());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void customuiscale$endInventoryScale(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int centerX = width / 2;
        int y = Math.min(height - 14, height / 2 + 93);
        int left = centerX - 38;
        int value = ScaleConfig.inventoryScale();
        graphics.drawString(Minecraft.getInstance().font, Component.literal("Inventory " + value + "/5"), centerX - 36, y - 10, 0xFFFFFF, true);
        graphics.fill(left, y, left + 76, y + 3, 0xFF555555);
        graphics.fill(left, y, left + (value - 1) * 19, y + 3, 0xFF55AAFF);
        graphics.fill(left + (value - 1) * 19 - 2, y - 2, left + (value - 1) * 19 + 3, y + 5, 0xFFFFFFFF);
        ScaleTransform.pop(graphics);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void customuiscale$inventorySliderClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (event.button() != 0) return;
        double x = width / 2.0 + (event.x() - width / 2.0) / (ScaleConfig.inventoryFactor());
        double y = height / 2.0 + (event.y() - height / 2.0) / (ScaleConfig.inventoryFactor());
        int centerX = width / 2;
        int sliderY = Math.min(height - 14, height / 2 + imageHeight / 2 + 10);
        int left = centerX - 38;
        if (x >= left - 5 && x <= left + 81 && y >= sliderY - 8 && y <= sliderY + 9) {
            ScaleConfig.setInventoryScale(Math.max(1, Math.min(5, (int)Math.round((x - left) / 19.0) + 1)));
            cir.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private MouseButtonEvent customuiscale$mapClick(MouseButtonEvent event) {
        return map(event);
    }

    @ModifyVariable(method = "mouseReleased", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private MouseButtonEvent customuiscale$mapRelease(MouseButtonEvent event) {
        return map(event);
    }

    @ModifyVariable(method = "mouseDragged", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private MouseButtonEvent customuiscale$mapDrag(MouseButtonEvent event) {
        return map(event);
    }

    private MouseButtonEvent map(MouseButtonEvent event) {
        double scale = ScaleConfig.inventoryFactor();
        double x = width / 2.0 + (event.x() - width / 2.0) / scale;
        double y = height / 2.0 + (event.y() - height / 2.0) / scale;
        return new MouseButtonEvent(x, y, event.buttonInfo());
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$scrollX(double x) {
        return width / 2.0 + (x - width / 2.0) / (ScaleConfig.inventoryFactor());
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$scrollY(double y) {
        return height / 2.0 + (y - height / 2.0) / (ScaleConfig.inventoryFactor());
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int customuiscale$renderMouseX(int x) {
        return (int)Math.round(width / 2.0 + (x - width / 2.0) / (ScaleConfig.inventoryFactor()));
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int customuiscale$renderMouseY(int y) {
        return (int)Math.round(height / 2.0 + (y - height / 2.0) / (ScaleConfig.inventoryFactor()));
    }
}
