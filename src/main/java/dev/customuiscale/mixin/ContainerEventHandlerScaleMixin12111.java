package dev.customuiscale.mixin;

import dev.customuiscale.ScaleConfig;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
public interface ContainerEventHandlerScaleMixin12111 {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void customuiscale$clickGuiSlider(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object)this instanceof VideoSettingsScreen) || event.button() != 0) return;
        double x = ((VideoSettingsScreen)(Object)this).width / 2.0
                + (event.x() - ((VideoSettingsScreen)(Object)this).width / 2.0) / ScaleConfig.guiFactor();
        double y = ((VideoSettingsScreen)(Object)this).height / 2.0
                + (event.y() - ((VideoSettingsScreen)(Object)this).height / 2.0) / ScaleConfig.guiFactor();
        int left = ((VideoSettingsScreen)(Object)this).width / 2 - 55;
        int top = ((VideoSettingsScreen)(Object)this).height - 25;
        if (x >= left - 4 && x <= left + 114 && y >= top - 10 && y <= top + 9) {
            ScaleConfig.setGuiScale(Math.max(1, Math.min(10, (int)Math.round((x - left) / 12.0) + 1)));
            cir.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "mouseClicked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private MouseButtonEvent customuiscale$mapClick(MouseButtonEvent event) {
        if ((Object)this instanceof VideoSettingsScreen) return map(event);
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

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double customuiscale$mapScrollX(double x) {
        if ((Object)this instanceof net.minecraft.client.gui.screens.Screen screen) {
            return screen.width / 2.0 + (x - screen.width / 2.0) / ScaleConfig.guiFactor();
        }
        return x;
    }

    @ModifyVariable(method = "mouseScrolled", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double customuiscale$mapScrollY(double y) {
        if ((Object)this instanceof net.minecraft.client.gui.screens.Screen screen) {
            return screen.height / 2.0 + (y - screen.height / 2.0) / ScaleConfig.guiFactor();
        }
        return y;
    }

    private MouseButtonEvent map(MouseButtonEvent event) {
        double x = event.x();
        double y = event.y();
        if ((Object)this instanceof net.minecraft.client.gui.screens.Screen screen) {
            x = screen.width / 2.0 + (x - screen.width / 2.0) / ScaleConfig.guiFactor();
            y = screen.height / 2.0 + (y - screen.height / 2.0) / ScaleConfig.guiFactor();
        }
        return new MouseButtonEvent(x, y, event.buttonInfo());
    }
}
