package kesslercascade.rdpmouse;

import org.lwjgl.system.Configuration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RDPMouseCore {

    private static volatile boolean initialized = false;

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        String os = System.getProperty("os.name", "").toLowerCase();
        if (!os.contains("win")) {
            return;
        }

        String arch = System.getProperty("os.arch", "").toLowerCase();
        String archDir;
        if (arch.contains("arm") || arch.contains("aarch64")) {
            archDir = "arm64";
        } else if (arch.contains("64")) {
            archDir = "x64";
        } else {
            archDir = "x86";
        }

        try {
            String resourcePath = "/natives/windows/" + archDir + "/glfw.dll";
            try (InputStream in = RDPMouseCore.class.getResourceAsStream(resourcePath)) {
                if (in == null) {
                    System.err.println("[MR True RDP Fix] Bundled GLFW native not found for: " + resourcePath);
                    return;
                }

                Path cacheDir = Paths.get(System.getProperty("java.io.tmpdir"), "mrrdpfix-natives", "windows", archDir);
                Files.createDirectories(cacheDir);
                Path targetDll = cacheDir.resolve("glfw-3.4.2-rdpfix.dll");

                try {
                    Files.copy(in, targetDll, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    if (!Files.exists(targetDll)) {
                        throw e;
                    }
                }

                String absoluteDll = targetDll.toAbsolutePath().toString();
                Configuration.GLFW_LIBRARY_NAME.set(absoluteDll);
                System.setProperty("org.lwjgl.glfw.libname", absoluteDll);
                log("[MR True RDP Fix] Backported GLFW 3.4+ native library loaded for RDP fix: " + absoluteDll);
            }

            // NeoForge compatibility: disable fmlearlywindow so it does not lock vanilla GLFW before mod injection
            try {
                Path configDir = Paths.get("config");
                Path fmlConfig = configDir.resolve("fml.toml");
                if (Files.exists(fmlConfig)) {
                    String content = Files.readString(fmlConfig);
                    if (content.contains("earlyWindowControl = true")) {
                        content = content.replace("earlyWindowControl = true", "earlyWindowControl = false");
                        Files.writeString(fmlConfig, content);
                        log("[MR True RDP Fix] Disabled NeoForge earlyWindowControl in config/fml.toml for GLFW compatibility.");
                    }
                } else {
                    if (!Files.exists(configDir)) {
                        Files.createDirectories(configDir);
                    }
                    Files.writeString(fmlConfig, "earlyWindowControl = false\n");
                    log("[MR True RDP Fix] Created config/fml.toml with earlyWindowControl = false for GLFW compatibility.");
                }
            } catch (Throwable ignored) {
            }
        } catch (Throwable t) {
            System.err.println("[MR True RDP Fix] Failed to inject GLFW 3.4+ native library:");
            t.printStackTrace();
        }
    }

    private static void log(String message) {
        System.out.println(message);
        try {
            Class<?> logUtils = Class.forName("com.mojang.logging.LogUtils");
            org.slf4j.Logger logger = (org.slf4j.Logger) logUtils.getMethod("getLogger").invoke(null);
            logger.info(message);
        } catch (Throwable ignored) {
        }
    }
}
