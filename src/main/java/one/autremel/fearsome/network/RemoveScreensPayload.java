package one.autremel.fearsome.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jspecify.annotations.NonNull;

public record RemoveScreensPayload(boolean isForGameScreens) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, RemoveScreensPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, RemoveScreensPayload::isForGameScreens,
            RemoveScreensPayload::new);

    @Override
    public @NonNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.REMOVE_SCREENS;
    }
}
