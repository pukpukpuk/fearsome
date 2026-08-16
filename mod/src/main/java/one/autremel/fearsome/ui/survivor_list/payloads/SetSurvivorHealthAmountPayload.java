package one.autremel.fearsome.ui.survivor_list.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetSurvivorHealthAmountPayload(
        int index,
        int amount
) implements IServerPayload<SetSurvivorHealthAmountPayload> {
    public static final ServerPayloadId<SetSurvivorHealthAmountPayload> ID = new ServerPayloadId<>(
            SetSurvivorHealthAmountPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetSurvivorHealthAmountPayload::index,
                    PacketCodecs.VAR_INT, SetSurvivorHealthAmountPayload::amount,
                    SetSurvivorHealthAmountPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetSurvivorHealthAmountPayload> getPayloadId() {
        return ID;
    }
}
