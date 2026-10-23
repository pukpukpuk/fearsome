package one.autremel.fearsome.illusions.glowing;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetEntityGlowingPayload(
        int entityId,
        @Nullable Integer color,
        boolean isCleanupRequest
) implements IServerPayload<SetEntityGlowingPayload> {
    public static final ServerPayloadId<SetEntityGlowingPayload> ID = new ServerPayloadId<>(
            SetEntityGlowingPayload.class,
            SetEntityGlowingPayload::decode
    );

    private static SetEntityGlowingPayload decode(PacketByteBuf buf) {
        var entityId = buf.readVarInt() - 1;
        if (entityId == -1) return new SetEntityGlowingPayload(-1, null, true);

        var color = buf.readBoolean() ? buf.readInt() : null;
        return new SetEntityGlowingPayload(entityId, color, false);
    }

    @Override
    public @NotNull ServerPayloadId<SetEntityGlowingPayload> getPayloadId() {
        return ID;
    }
}
