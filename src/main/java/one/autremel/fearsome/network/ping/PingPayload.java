package one.autremel.fearsome.network.ping;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import one.autremel.fearsome.FearsomeMod;

record PingPayload(String version) implements CustomPayload {
    public static final Id<PingPayload> ID = new Id<>(FearsomeMod.payloadIdentifier("mod_ping"));

    public static final PacketCodec<RegistryByteBuf, PingPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, PingPayload::version,
            PingPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
