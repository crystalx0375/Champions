package crystal.champions.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public abstract class Payload implements CustomPayload {
    private static final String CHAMPIONS = "champions";

    public record ChampionUpdate(UUID uuid, Text name, int tier, String affixes, float health, float maxHealth) implements CustomPayload {
        public static final Id<ChampionUpdate> SERVER_UPDATE_ID = new Id<>(Identifier.of(CHAMPIONS, "update_hud"));
        public static final PacketCodec<RegistryByteBuf, ChampionUpdate> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionUpdate::uuid,
                TextCodecs.REGISTRY_PACKET_CODEC, ChampionUpdate::name,
                PacketCodecs.VAR_INT, ChampionUpdate::tier,
                PacketCodecs.STRING, ChampionUpdate::affixes,
                PacketCodecs.FLOAT, ChampionUpdate::health,
                PacketCodecs.FLOAT, ChampionUpdate::maxHealth,
                ChampionUpdate::new
        );
        @Override public Id<? extends CustomPayload> getId() { return SERVER_UPDATE_ID; }
    }

    public record ChampionUpdateCl(UUID uuid, Text name, int tier, String affixes, float health, float maxHealth) implements CustomPayload {
        public static final Id<ChampionUpdateCl> CLIENT_UPDATE_ID = new Id<>(Identifier.of(CHAMPIONS, "update_client_hud"));
        public static final PacketCodec<RegistryByteBuf, ChampionUpdateCl> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionUpdateCl::uuid,
                TextCodecs.REGISTRY_PACKET_CODEC, ChampionUpdateCl::name,
                PacketCodecs.VAR_INT, ChampionUpdateCl::tier,
                PacketCodecs.STRING, ChampionUpdateCl::affixes,
                PacketCodecs.FLOAT, ChampionUpdateCl::health,
                PacketCodecs.FLOAT, ChampionUpdateCl::maxHealth,
                ChampionUpdateCl::new
        );
        @Override public Id<? extends CustomPayload> getId() { return CLIENT_UPDATE_ID; }
    }

    public record ChampionSRemove(UUID uuid) implements CustomPayload {
        public static final Id<ChampionSRemove> REMOVE_ID_SERVER = new Id<>(Identifier.of(CHAMPIONS, "remove_hud_server"));
        public static final PacketCodec<RegistryByteBuf, ChampionSRemove> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionSRemove::uuid,
                ChampionSRemove::new
        );
        @Override public Id<? extends CustomPayload> getId() { return REMOVE_ID_SERVER; }
    }

    public record ChampionCRemove(UUID uuid) implements CustomPayload {
        public static final Id<ChampionCRemove> REMOVE_ID_CLIENT = new Id<>(Identifier.of(CHAMPIONS, "remove_hud_client"));
        public static final PacketCodec<RegistryByteBuf, ChampionCRemove> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionCRemove::uuid,
                ChampionCRemove::new
        );
        @Override public Id<? extends CustomPayload> getId() { return REMOVE_ID_CLIENT; }
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(Payload.ChampionUpdate.SERVER_UPDATE_ID, Payload.ChampionUpdate.CODEC);
        PayloadTypeRegistry.playS2C().register(Payload.ChampionUpdateCl.CLIENT_UPDATE_ID, Payload.ChampionUpdateCl.CODEC);
        PayloadTypeRegistry.playS2C().register(Payload.ChampionSRemove.REMOVE_ID_SERVER, Payload.ChampionSRemove.CODEC);
        PayloadTypeRegistry.playS2C().register(Payload.ChampionCRemove.REMOVE_ID_CLIENT, Payload.ChampionCRemove.CODEC);
    }
}
