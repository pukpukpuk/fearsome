package one.autremel.fearsome.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import one.autremel.fearsome.FearsomeMod;

public class PayloadCodecsContainer {
    public static final CustomPayload.Id<IPayload> ID = new CustomPayload.Id<>(FearsomeMod.identifier("payloads"));
    public static PacketCodec<RegistryByteBuf, IPayload> MAIN_CODEC
            = PacketCodec.ofStatic(PayloadCodecsContainer::encode, PayloadCodecsContainer::decode);

    public static IPayload decode(RegistryByteBuf buf) {
        var intId = Byte.toUnsignedInt(buf.readByte());
        var payloadId = PayloadId.get(intId);

        var codec = payloadId.getCodec();
        return codec.decode(buf);
    }

    public static void encode(RegistryByteBuf buf, IPayload payload) {
        buf.writeByte(payload.getPayloadId().getByteId());

        wrappedEncode(buf, payload);
    }

    @SuppressWarnings("unchecked")
    private static <T extends IPayload> void wrappedEncode(RegistryByteBuf buf, IPayload payload) {
        PacketCodec<RegistryByteBuf, T> codec = (PacketCodec<RegistryByteBuf, T>) payload.getPayloadId().getCodec();
        codec.encode(buf, (T)payload);
    }
}
