package one.autremel.fearsome.network;

import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketDecoder;

public final class ServerPayloadId<T extends IServerPayload<T>> extends PayloadId<T> {
    @Getter
    private final boolean isOnlyWhileGameActive;

    public ServerPayloadId(Class payloadClass, boolean isOnlyWhileGameActive, PacketCodec<PacketByteBuf, T> codec) {
        super(payloadClass, codec);
        this.isOnlyWhileGameActive = isOnlyWhileGameActive;
    }

    public ServerPayloadId(Class payloadClass, boolean isOnlyWhileGameActive, PacketDecoder<PacketByteBuf, T> decoder) {
        this(payloadClass, isOnlyWhileGameActive, new PacketCodec<>() {
            @Override
            public void encode(PacketByteBuf buf, T value) {
                throw new UnsupportedOperationException(
                        "Encoding is an unsupported operation for payload " + payloadClass.getSimpleName()
                );
            }

            @Override
            public T decode(PacketByteBuf buf) {
                return decoder.decode(buf);
            }
        });
    }

    public ServerPayloadId(Class payloadClass, PacketDecoder<PacketByteBuf, T> decoder) {
        this(payloadClass, true, decoder);
    }


}
