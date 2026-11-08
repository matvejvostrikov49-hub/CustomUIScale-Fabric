package dev.customuiscale;

import net.minecraft.client.gui.GuiGraphics;

import java.lang.reflect.Method;

public final class ScaleTransform {
    private ScaleTransform() {}

    public static void push(GuiGraphics graphics, int width, int height, float scale) {
        Object stack = graphics.pose();
        invokeNoArgs(stack, "pushPose", "pushMatrix");
        invokeXY(stack, "translate", width / 2.0f, height / 2.0f, 0.0f);
        invokeXY(stack, "scale", scale, scale, 1.0f);
        invokeXY(stack, "translate", -width / 2.0f, -height / 2.0f, 0.0f);
    }

    public static void pop(GuiGraphics graphics) {
        invokeNoArgs(graphics.pose(), "popPose", "popMatrix");
    }

    private static void invokeNoArgs(Object target, String... candidates) {
        for (String name : candidates) {
            try {
                target.getClass().getMethod(name).invoke(target);
                return;
            } catch (NoSuchMethodException ignored) {
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Could not invoke matrix stack method " + name, e);
            }
        }
        throw new IllegalStateException("No compatible matrix stack method found on " + target.getClass().getName());
    }

    private static void invokeXY(Object target, String name, float x, float y, float z) {
        boolean twoDimensional = target.getClass().getSimpleName().equals("Matrix3x2fStack");
        for (Method method : target.getClass().getMethods()) {
            if (!method.getName().equals(name)) continue;
            Class<?>[] types = method.getParameterTypes();
            if (twoDimensional && types.length == 2) {
                try {
                    method.invoke(target, x, y);
                    return;
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Could not invoke matrix stack method " + name, e);
                }
            }
            if (!twoDimensional && types.length == 3) {
                try {
                    Object a = types[0] == double.class ? (double)x : x;
                    Object b = types[1] == double.class ? (double)y : y;
                    Object c = types[2] == double.class ? (double)z : z;
                    method.invoke(target, a, b, c);
                    return;
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Could not invoke matrix stack method " + name, e);
                }
            }
            if (types.length == 2) {
                try {
                    method.invoke(target, x, y);
                    return;
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Could not invoke matrix stack method " + name, e);
                }
            }
        }
        throw new IllegalStateException("No compatible " + name + " method found on " + target.getClass().getName());
    }
}
