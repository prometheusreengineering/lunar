package studio.dreamys.prometheus.util.v1;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lunarclient.websocket.cosmetic.v1.EquippedCosmetic;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileUtils {
    private static Path path = Paths.get("lunar.prometheus");

    public static void write(List<EquippedCosmetic> equippedCosmetics) {
        try (Writer writer = Files.newBufferedWriter(path)) {
            new Gson().toJson(equippedCosmetics, writer);
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to save equipped cosmetics.");
            e.printStackTrace();
        }
    }

    public static List<EquippedCosmetic> read() {
        try (Reader reader = Files.newBufferedReader(path)) {
            return new Gson().fromJson(reader, new TypeToken<List<EquippedCosmetic>>() {
            }.getType());
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to read equipped cosmetics.");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
