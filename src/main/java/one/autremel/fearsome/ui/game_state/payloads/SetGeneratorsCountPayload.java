package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record SetGeneratorsCountPayload(int count) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, SetGeneratorsCountPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SetGeneratorsCountPayload::count,
            SetGeneratorsCountPayload::new);

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.SET_GENERATORS_COUNT;
    }
}
