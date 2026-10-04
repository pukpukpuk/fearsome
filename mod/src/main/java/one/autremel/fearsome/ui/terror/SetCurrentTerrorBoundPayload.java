package one.autremel.fearsome.ui.terror;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetCurrentTerrorBoundPayload(
        @Nullable Integer boundId
) implements IServerPayload<SetCurrentTerrorBoundPayload> {
    public static final ServerPayloadId<SetCurrentTerrorBoundPayload> ID = new ServerPayloadId<>(
            SetCurrentTerrorBoundPayload.class,
            SetCurrentTerrorBoundPayload::decode
    );


    private static SetCurrentTerrorBoundPayload decode(PacketByteBuf buf) {
        // 0 - not in TR, 1 - in 0th bound, 2 - 1st, etc...
        var b = Byte.toUnsignedInt(buf.readByte());
        var boundId = b == 0 ? null : b - 1;
        return new SetCurrentTerrorBoundPayload(boundId);
    }

    @Override
    public @NotNull ServerPayloadId<SetCurrentTerrorBoundPayload> getPayloadId() {
        return ID;
    }
}
