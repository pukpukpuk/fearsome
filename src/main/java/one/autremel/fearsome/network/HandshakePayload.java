package one.autremel.fearsome.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import one.autremel.fearsome.FearsomeMod;

public record HandshakePayload(String serverVersion) implements CustomPayload {
    public static final Id<HandshakePayload> ID = new Id<>(FearsomeMod.payloadIdentifier("handshake"));

    public static final PacketCodec<RegistryByteBuf, HandshakePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, HandshakePayload::serverVersion,
            HandshakePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
