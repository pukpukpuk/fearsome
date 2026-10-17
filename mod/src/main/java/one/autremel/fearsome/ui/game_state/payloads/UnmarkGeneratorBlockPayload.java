package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

/// Payload for removing generator mark from the specified block
@Payload
public record UnmarkGeneratorBlockPayload(
        BlockPos blockPosition
) implements IServerPayload<UnmarkGeneratorBlockPayload> {
    public static final ServerPayloadId<UnmarkGeneratorBlockPayload> ID = new ServerPayloadId<>(
            UnmarkGeneratorBlockPayload.class,
            UnmarkGeneratorBlockPayload::decode
    );

    private static UnmarkGeneratorBlockPayload decode(PacketByteBuf buf) {
        return new UnmarkGeneratorBlockPayload(BlockPos.fromLong(buf.readLong()));
    }

    @Override
    public @NotNull ServerPayloadId<UnmarkGeneratorBlockPayload> getPayloadId() {
        return ID;
    }
}
