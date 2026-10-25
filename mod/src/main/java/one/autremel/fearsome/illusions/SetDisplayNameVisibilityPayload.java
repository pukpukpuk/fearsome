package one.autremel.fearsome.illusions;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetDisplayNameVisibilityPayload(
        int entityId,
        boolean isVisible,
        int color,
        @Nullable Character icon,
        boolean isCleanupRequest
) implements IServerPayload<SetDisplayNameVisibilityPayload> {
    public static final ServerPayloadId<SetDisplayNameVisibilityPayload> ID = new ServerPayloadId<>(
            SetDisplayNameVisibilityPayload.class,
            SetDisplayNameVisibilityPayload::decode
    );

    private static SetDisplayNameVisibilityPayload decode(PacketByteBuf buf) {
        var entityId = buf.readVarInt() - 1;
        if (entityId == -1) return new SetDisplayNameVisibilityPayload(-1, false, 0, null, true);

        var data = buf.readInt(); ///______iv rrrrrrrr gggggggg bbbbbbbb
        var isVisible = ((data >> 24) & 0b1) == 1;
        var hasIcon = ((data >> 25) & 0b1) == 1;
        var color = data & 0xFFFFFF;

        var icon = hasIcon ? (char) buf.readShort() : null;
        return new SetDisplayNameVisibilityPayload(entityId, isVisible, color, icon, false);
    }

    @Override
    public @NotNull ServerPayloadId<SetDisplayNameVisibilityPayload> getPayloadId() {
        return ID;
    }
}
