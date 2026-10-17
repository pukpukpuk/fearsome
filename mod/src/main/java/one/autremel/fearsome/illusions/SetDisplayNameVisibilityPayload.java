package one.autremel.fearsome.illusions;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetDisplayNameVisibilityPayload(
        int entityId,
        boolean isVisible,
        boolean isCleanupRequest
) implements IServerPayload<SetDisplayNameVisibilityPayload> {
    public static final ServerPayloadId<SetDisplayNameVisibilityPayload> ID = new ServerPayloadId<>(
            SetDisplayNameVisibilityPayload.class,
            SetDisplayNameVisibilityPayload::decode
    );

    private static SetDisplayNameVisibilityPayload decode(PacketByteBuf buf) {
        var entityId = buf.readVarInt() - 1;
        if (entityId == -1) return new SetDisplayNameVisibilityPayload(-1, false, true);

        var isVisible = buf.readBoolean();
        return new SetDisplayNameVisibilityPayload(entityId, isVisible, false);
    }

    @Override
    public @NotNull ServerPayloadId<SetDisplayNameVisibilityPayload> getPayloadId() {
        return ID;
    }
}
