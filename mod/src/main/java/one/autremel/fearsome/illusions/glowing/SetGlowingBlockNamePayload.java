package one.autremel.fearsome.illusions.glowing;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetGlowingBlockNamePayload(
        BlockPos blockPosition,
        @Nullable String name
) implements IServerPayload<SetGlowingBlockNamePayload> {
    public static final ServerPayloadId<SetGlowingBlockNamePayload> ID = new ServerPayloadId<>(
            SetGlowingBlockNamePayload.class,
            SetGlowingBlockNamePayload::decode
    );

    private static SetGlowingBlockNamePayload decode(PacketByteBuf buf) {
        var blockPosition = BlockPos.fromLong(buf.readLong());
        var name = buf.readBoolean() ? buf.readString() : null;
        return new SetGlowingBlockNamePayload(blockPosition, name);
    }

    @Override
    public @NotNull ServerPayloadId<SetGlowingBlockNamePayload> getPayloadId() {
        return ID;
    }
}
