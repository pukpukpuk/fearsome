package one.autremel.fearsome.builds;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record OpenChoiceMenuPayload() implements IServerPayload<OpenChoiceMenuPayload> {
    private static final OpenChoiceMenuPayload INSTANCE = new OpenChoiceMenuPayload();

    public static final ServerPayloadId<OpenChoiceMenuPayload> ID = new ServerPayloadId<>(
            OpenChoiceMenuPayload.class,
            false,
            (PacketByteBuf buf) -> INSTANCE
    );

    @Override
    public @NotNull ServerPayloadId<OpenChoiceMenuPayload> getPayloadId() {
        return ID;
    }
}
