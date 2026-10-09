package kesslercascade.rdpmouse.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kesslercascade.rdpmouse.RDPMouse;
import kesslercascade.rdpmouse.RDPMouseState;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class RDPMouseConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("rdpmouse.json").toFile();

    private static RDPMouseConfig INSTANCE = new RDPMouseConfig();

    public boolean rdpModeEnabled = true;

    public static RDPMouseConfig getInstance() {
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(CONFIG_FILE), StandardCharsets.UTF_8)) {
                RDPMouseConfig loaded = GSON.fromJson(reader, RDPMouseConfig.class);
                if (loaded != null) {
                    INSTANCE = loaded;
                }
            } catch (Exception e) {
                RDPMouse.LOGGER.error("Failed to load RDPMouse config", e);
            }
        } else {
            save();
        }
        RDPMouseState.enabled = INSTANCE.rdpModeEnabled;
    }

    public static void save() {
        try {
            File parent = CONFIG_FILE.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            RDPMouse.LOGGER.error("Failed to save RDPMouse config", e);
        }
    }
}
