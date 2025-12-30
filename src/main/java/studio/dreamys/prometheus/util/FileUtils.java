package studio.dreamys.prometheus.util;

import com.lunarclient.websocket.cosmetic.v2.Outfit;
import com.lunarclient.websocket.emote.v1.EquippedEmote;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static Path outfitPath = Paths.get("lunar.outfit.prometheus");
    private static Path emotesPath = Paths.get("lunar.emotes.prometheus");

    public static void writeOutfit(Outfit outfit) {
        try (OutputStream os = Files.newOutputStream(outfitPath)) {
            outfit.writeTo(os);
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to serialize outfit to file.");
            e.printStackTrace();
        }
    }

    public static Outfit readOutfit() {
        //create default outfit
        Outfit.Builder builder = Outfit.newBuilder()
                .setName("Prometheus")
                .setFavorite(true);

        try (InputStream is = Files.newInputStream(outfitPath.toFile().toPath())) {
            Outfit outfit = Outfit.parseFrom(is);
            builder.mergeFrom(outfit);
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to deserialize outfit from file.");
            e.printStackTrace();
        }

        return builder.build();
    }

    public static void writeEquippedEmotes(List<EquippedEmote> equippedEmotes) {
        try (OutputStream os = Files.newOutputStream(emotesPath)) {
            for (EquippedEmote emote : equippedEmotes) {
                emote.writeDelimitedTo(os);
            }
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to serialize equipped emote list to file.");
            e.printStackTrace();
        }
    }

    public static List<EquippedEmote> readEquippedEmotes() {
        List<EquippedEmote> equippedEmotes = new ArrayList<>();

        try (InputStream is = Files.newInputStream(emotesPath.toFile().toPath())) {
            while (is.available() > 0) {
                EquippedEmote emote = EquippedEmote.parseDelimitedFrom(is);
                if (emote != null) {
                    equippedEmotes.add(emote);
                }
            }
        } catch (Throwable e) {
            System.out.println("[Prometheus] Failed to deserialize equipped emote list from file.");
            e.printStackTrace();
        }

        return equippedEmotes;
    }
}
