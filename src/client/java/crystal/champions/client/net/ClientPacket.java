package crystal.champions.client.net;

import crystal.champions.net.Payload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientPacket {
    @SuppressWarnings("java:S2386")
    public static final Map<UUID, ChampionDisplayInfo> activeChampions = new ConcurrentHashMap<>();
    @SuppressWarnings("java:S2386")
    public static final Map<UUID, ChampionDisplayInfo> activeChampionsCl = new ConcurrentHashMap<>();

    public record ChampionData(
            Text name,
            int tier,
            String affixes,
            float percent,
            int color
    ) {}

    public record ChampionDisplayInfo(
            Text name,
            int tier,
            String affixes,
            float health,
            float maxHealth,
            long lastUpdate,
            long lastUpdateClient
    ) {}

    private ClientPacket() {}

    /**
     * Registering "server" and "client" packets
     * Server is for all champions in radius
     * Client is for champions you are looking at
     */
    @SuppressWarnings("AutoCloseable")
    public static void registerNetworkPackets() {
        ClientPlayNetworking.registerGlobalReceiver(Payload.ChampionSRemove.REMOVE_ID_SERVER, (buf, context) -> context.client().execute(() -> activeChampions.remove(buf.uuid())));
        ClientPlayNetworking.registerGlobalReceiver(Payload.ChampionCRemove.REMOVE_ID_CLIENT, (buf, context) -> context.client().execute(() -> activeChampionsCl.remove(buf.uuid())));

        ClientPlayNetworking.registerGlobalReceiver(Payload.ChampionUpdateCl.CLIENT_UPDATE_ID, (buf, context) -> context.client().execute(() -> {
            ChampionDisplayInfo info = new ChampionDisplayInfo(
                    buf.name(),
                    buf.tier(),
                    buf.affixes(),
                    buf.health(),
                    buf.maxHealth(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );
            context.client().execute(() -> activeChampionsCl.put(buf.uuid(), info));
        }));

        ClientPlayNetworking.registerGlobalReceiver(Payload.ChampionUpdate.SERVER_UPDATE_ID, (buf, context) -> context.client().execute(() -> {
                ChampionDisplayInfo info = new ChampionDisplayInfo(
                    buf.name(),
                    buf.tier(),
                    buf.affixes(),
                    buf.health(),
                    buf.maxHealth(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                );
                context.client().execute(() -> activeChampions.put(buf.uuid(), info));
        }));
    }
}