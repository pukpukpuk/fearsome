package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.*;
import org.jetbrains.annotations.NotNull;

@Payload
public record InputPayload(BindingId id, boolean isPressed) implements IClientPayload<InputPayload> {
    public static final ClientPayloadId<InputPayload> ID = new ClientPayloadId<>(
            InputPayload.class,
            InputPayload::encode
    );

    private static void encode(PacketByteBuf buf, InputPayload inputPayload) {
        var id = inputPayload.id().getByteId();
        Preconditions.checkState((id & 0b1000_0000) == 0);

        buf.writeByte((id << 1) | (inputPayload.isPressed() ? 1 : 0));
    }

    @Override
    public @NotNull ClientPayloadId<InputPayload> getPayloadId() {
        return ID;
    }
}
