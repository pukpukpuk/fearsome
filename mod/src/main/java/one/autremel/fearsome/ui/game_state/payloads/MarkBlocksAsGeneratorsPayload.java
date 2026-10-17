package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/// Payload for setting new positions of generator blocks (overriding the previous ones)
@Payload
public record MarkBlocksAsGeneratorsPayload(
        @Nullable List<BlockPos> blockPositions
) implements IServerPayload<MarkBlocksAsGeneratorsPayload> {
    public static final ServerPayloadId<MarkBlocksAsGeneratorsPayload> ID = new ServerPayloadId<>(
            MarkBlocksAsGeneratorsPayload.class,
            MarkBlocksAsGeneratorsPayload::decode
    );

    private static MarkBlocksAsGeneratorsPayload decode(PacketByteBuf buf) {
        var count = buf.readVarInt();
        if (count == 0) return new MarkBlocksAsGeneratorsPayload(null);

        var blockPositions = new ArrayList<BlockPos>(count);
        while (count-- > 0) blockPositions.add(BlockPos.fromLong(buf.readLong()));

        return new MarkBlocksAsGeneratorsPayload(blockPositions);
    }

    @Override
    public @NotNull ServerPayloadId<MarkBlocksAsGeneratorsPayload> getPayloadId() {
        return ID;
    }
}
