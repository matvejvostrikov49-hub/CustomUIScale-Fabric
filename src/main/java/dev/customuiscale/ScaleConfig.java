package dev.customuiscale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ScaleConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("customuiscale.json");
    private static Data data = load();

    private ScaleConfig() {}

    private static Data load() {
        try {
            if (Files.exists(FILE)) {
                Data loaded = GSON.fromJson(Files.readString(FILE), Data.class);
                if (loaded != null) {
                    loaded.guiScale = clamp(loaded.guiScale, 1, 10);
                    loaded.inventoryScale = clamp(loaded.inventoryScale, 1, 5);
                    return loaded;
                }
            }
        } catch (Exception ignored) {
        }
        return new Data();
    }

    public static int guiScale() { return data.guiScale; }
    public static int inventoryScale() { return data.inventoryScale; }

    public static void setGuiScale(int value) {
        data.guiScale = clamp(value, 1, 10);
        save();
    }

    public static void setInventoryScale(int value) {
        data.inventoryScale = clamp(value, 1, 5);
        save();
    }

    public static float guiFactor() {
        float sliderFactor = 0.55f + (guiScale() - 1) * (0.45f / 9.0f);
        double vanillaScale = net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScale();
        return (float)(sliderFactor * vanillaScale / 3.0);
    }

    public static float inventoryFactor() {
        return 0.60f + (inventoryScale() - 1) * 0.10f;
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    private static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(data));
        } catch (IOException ignored) {
        }
    }

    private static final class Data {
        int guiScale = 10;
        int inventoryScale = 5;
    }
}
