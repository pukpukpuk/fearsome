package one.autremel.fearsome.network.ping;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;

public record PingPayload(String version) implements IPayload { //TODO
    public static final PacketCodec<RegistryByteBuf, PingPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, PingPayload::version,
            PingPayload::new
    );

    @Override
    public PayloadId<PingPayload> getPayloadId() {
        return PayloadId.PING;
    }
}
