package one.autremel.fearsome.illusions.glowing;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetBlockGlowingPayload(
        @Nullable BlockPos blockPosition,
        @Nullable Integer color,
        boolean isCleanupRequest
) implements IServerPayload<SetBlockGlowingPayload> {
    public static final ServerPayloadId<SetBlockGlowingPayload> ID = new ServerPayloadId<>(
            SetBlockGlowingPayload.class,
            SetBlockGlowingPayload::decode
    );

    private static SetBlockGlowingPayload decode(PacketByteBuf buf) {
        if (buf.readBoolean()) return new SetBlockGlowingPayload(null, null, true);

        var blockPosition = BlockPos.fromLong(buf.readLong());
        var color = buf.readBoolean() ? buf.readInt() : null;
        return new SetBlockGlowingPayload(blockPosition, color, false);
    }

    @Override
    public @NotNull ServerPayloadId<SetBlockGlowingPayload> getPayloadId() {
        return ID;
    }
}
