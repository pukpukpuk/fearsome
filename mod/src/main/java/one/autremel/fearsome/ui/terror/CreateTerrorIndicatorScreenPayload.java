package one.autremel.fearsome.ui.terror;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record CreateTerrorIndicatorScreenPayload(
        Bound[] bounds
) implements IServerPayload<CreateTerrorIndicatorScreenPayload> {
    public static final ServerPayloadId<CreateTerrorIndicatorScreenPayload> ID = new ServerPayloadId<>(
            CreateTerrorIndicatorScreenPayload.class,
            CreateTerrorIndicatorScreenPayload::decode
    );

    private static CreateTerrorIndicatorScreenPayload decode(PacketByteBuf buf) {
        var count = Byte.toUnsignedInt(buf.readByte());
        var bounds = new Bound[count];
        for (int i = 0; i < count; i++) {
            bounds[i] = Bound.read(buf);
        }

        return new CreateTerrorIndicatorScreenPayload(bounds);
    }

    @Override
    public @NotNull ServerPayloadId<CreateTerrorIndicatorScreenPayload> getPayloadId() {
        return ID;
    }
}
