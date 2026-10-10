package kesslercascade.rdpmouse;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.lwjgl.system.Configuration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RDPMousePreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
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
            try (InputStream in = RDPMousePreLaunch.class.getResourceAsStream(resourcePath)) {
                if (in == null) {
                    System.err.println("[MR True RDP Fix] Bundled GLFW native not found for: " + resourcePath);
                    return;
                }

                Path cacheDir = Paths.get(System.getProperty("java.io.tmpdir"), "mrrdpfix-natives", "windows", archDir);
                Files.createDirectories(cacheDir);
                Path targetDll = cacheDir.resolve("glfw-3.4.1.dll");

                if (!Files.exists(targetDll) || Files.size(targetDll) == 0) {
                    try {
                        Files.copy(in, targetDll, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        if (!Files.exists(targetDll)) {
                            throw e;
                        }
                    }
                }

                String absoluteDll = targetDll.toAbsolutePath().toString();
                Configuration.GLFW_LIBRARY_NAME.set(absoluteDll);
                System.setProperty("org.lwjgl.glfw.libname", absoluteDll);
                System.out.println("[MR True RDP Fix] Backported GLFW 3.4+ native library loaded for RDP fix: " + absoluteDll);
            }
        } catch (Throwable t) {
            System.err.println("[MR True RDP Fix] Failed to inject GLFW 3.4+ native library:");
            t.printStackTrace();
        }
    }
}
