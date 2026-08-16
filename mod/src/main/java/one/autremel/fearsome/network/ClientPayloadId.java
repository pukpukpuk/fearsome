package one.autremel.fearsome.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketEncoder;

public final class ClientPayloadId<T extends IClientPayload<T>> extends PayloadId<T> {
    public ClientPayloadId(Class payloadClass, PacketCodec<PacketByteBuf, T> codec) {
        super(payloadClass, codec);
    }

    public ClientPayloadId(Class payloadClass, PacketEncoder<PacketByteBuf, T> encoder) {
        this(payloadClass, new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, T value) {
                encoder.encode(buf, value);
            }

            @Override
            public T decode(PacketByteBuf buf) {
                throw new UnsupportedOperationException(
                        "Decoding is an unsupported operation for payload " + payloadClass.getSimpleName()
                );
            }
        });
    }
}
