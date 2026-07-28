package one.autremel.fearsome.ui.survivor_list;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record SetSurvivorDeathTimerStatePayload(int index, boolean isRunning, long value, long serverTime) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, SetSurvivorDeathTimerStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SetSurvivorDeathTimerStatePayload::index,
            PacketCodecs.BOOLEAN, SetSurvivorDeathTimerStatePayload::isRunning,
            PacketCodecs.VAR_LONG, SetSurvivorDeathTimerStatePayload::value,
            PacketCodecs.LONG, SetSurvivorDeathTimerStatePayload::serverTime,
            SetSurvivorDeathTimerStatePayload::new);

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.SET_SURVIVOR_DEATH_TIMER_STATE;
    }
}
