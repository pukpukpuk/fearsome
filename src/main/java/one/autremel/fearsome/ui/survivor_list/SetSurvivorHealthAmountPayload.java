package one.autremel.fearsome.ui.survivor_list;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jspecify.annotations.NonNull;

public record SetSurvivorHealthAmountPayload(int index, int amount) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, SetSurvivorHealthAmountPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SetSurvivorHealthAmountPayload::index,
            PacketCodecs.VAR_INT, SetSurvivorHealthAmountPayload::amount,
            SetSurvivorHealthAmountPayload::new);

    @Override
    public @NonNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.SET_SURVIVOR_HEALTH_AMOUNT;
    }
}
