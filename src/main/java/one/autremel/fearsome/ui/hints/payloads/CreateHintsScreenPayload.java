package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record CreateHintsScreenPayload() implements IPayload {
    public static final PacketCodec<RegistryByteBuf, CreateHintsScreenPayload> CODEC
            = PacketCodec.unit(new CreateHintsScreenPayload());

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.CREATE_HINTS_SCREEN;
    }
}
