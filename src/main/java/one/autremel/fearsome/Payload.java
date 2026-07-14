package one.autremel.fearsome;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record Payload(int aboba) implements CustomPayload {
    public static final CustomPayload.Id<Payload> ID =
            new CustomPayload.Id<>(Identifier.of("fearsome", "configuration"));

    public static final PacketCodec<RegistryByteBuf, Payload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, Payload::aboba,
            Payload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
