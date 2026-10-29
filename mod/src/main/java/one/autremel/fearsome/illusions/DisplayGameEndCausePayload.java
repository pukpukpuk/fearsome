package one.autremel.fearsome.illusions;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.GameEndCause;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record DisplayGameEndCausePayload(
        GameEndCause cause,
        CharacterTeam ownTeam
) implements IServerPayload<DisplayGameEndCausePayload> {
    public static final ServerPayloadId<DisplayGameEndCausePayload> ID = new ServerPayloadId<>(
            DisplayGameEndCausePayload.class,
            DisplayGameEndCausePayload::decode
    );

    private static final GameEndCause[] VALUES = GameEndCause.values();

    private static DisplayGameEndCausePayload decode(PacketByteBuf buf) {
        var data = buf.readVarInt();
        var ownTeam = (data & 1) == 1 ? CharacterTeam.MURDERERS : CharacterTeam.SURVIVORS;
        var cause = VALUES[data >> 1];

        return new DisplayGameEndCausePayload(cause, ownTeam);
    }

    @Override
    public @NotNull ServerPayloadId<DisplayGameEndCausePayload> getPayloadId() {
        return ID;
    }
}
