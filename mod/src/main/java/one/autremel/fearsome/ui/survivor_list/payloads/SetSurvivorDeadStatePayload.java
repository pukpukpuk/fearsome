package one.autremel.fearsome.ui.survivor_list.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetSurvivorDeadStatePayload(
        int index,
        boolean isDead
) implements IServerPayload<SetSurvivorDeadStatePayload> {
    public static final ServerPayloadId<SetSurvivorDeadStatePayload> ID = new ServerPayloadId<>(
            SetSurvivorDeadStatePayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetSurvivorDeadStatePayload::index,
                    PacketCodecs.BOOLEAN, SetSurvivorDeadStatePayload::isDead,
                    SetSurvivorDeadStatePayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetSurvivorDeadStatePayload> getPayloadId() {
        return ID;
    }
}
