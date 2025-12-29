package studio.dreamys.prometheus.util;

import com.google.protobuf.util.JsonFormat;
import com.lunarclient.websocket.cosmetic.v2.Outfit;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    private static Path path = Paths.get("lunar.prometheus");

    public static void write(Outfit outfit) {
        try (Writer writer = Files.newBufferedWriter(path)) {
            String json = JsonFormat.printer().includingDefaultValueFields().print(outfit);
            writer.write(json);
            System.out.println("[Prometheus] Successfully serialized saved outfit to file.");
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to serialize saved outfit to file.");
            e.printStackTrace();
        }
    }

    public static Outfit read() {
        Outfit.Builder builder = Outfit.newBuilder();

        try {
            String json = new String(Files.readAllBytes(path));
            JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
            System.out.println("[Prometheus] Successfully deserialized saved outfit from file.");
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to deserialize saved outfit from file.");
            e.printStackTrace();
        }

        return builder.setName("Prometheus").setFavorite(true).build();
    }
}
