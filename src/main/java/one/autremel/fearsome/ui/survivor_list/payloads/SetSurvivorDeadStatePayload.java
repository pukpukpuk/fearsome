package one.autremel.fearsome.ui.survivor_list.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jspecify.annotations.NonNull;

public record SetSurvivorDeadStatePayload(int index, boolean isDead) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, SetSurvivorDeadStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SetSurvivorDeadStatePayload::index,
            PacketCodecs.BOOLEAN, SetSurvivorDeadStatePayload::isDead,
            SetSurvivorDeadStatePayload::new);

    @Override
    public @NonNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.SET_SURVIVOR_DEAD_STATE;
    }
}
