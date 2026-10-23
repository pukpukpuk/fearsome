package one.autremel.fearsome.illusions;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetEntitySkinPayload(
        int entityId,
        @Nullable String skinKey,
        boolean isSlim,
        boolean isCleanupRequest
) implements IServerPayload<SetEntitySkinPayload> {
    public static final ServerPayloadId<SetEntitySkinPayload> ID = new ServerPayloadId<>(
            SetEntitySkinPayload.class,
            SetEntitySkinPayload::decode
    );

    private static SetEntitySkinPayload decode(PacketByteBuf buf) {
        var entityId = buf.readVarInt() - 1;
        if (entityId == -1) return new SetEntitySkinPayload(-1, null, false, true);

        if (!buf.readBoolean()) return new SetEntitySkinPayload(entityId, null, false, false);

        var skinKey = buf.readString();
        var isSlim = buf.readBoolean();
        return new SetEntitySkinPayload(entityId, skinKey, isSlim, false);
    }

    @Override
    public @NotNull ServerPayloadId<SetEntitySkinPayload> getPayloadId() {
        return ID;
    }
}
