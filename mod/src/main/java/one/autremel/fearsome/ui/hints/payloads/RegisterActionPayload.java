package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record RegisterActionPayload(
        int actionId,
        int priority,
        String key,
        byte type
) implements IServerPayload<RegisterActionPayload> {
    public static final ServerPayloadId<RegisterActionPayload> ID = new ServerPayloadId<>(
            RegisterActionPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RegisterActionPayload::actionId,
                    PacketCodecs.INTEGER, RegisterActionPayload::priority,
                    PacketCodecs.STRING, RegisterActionPayload::key,
                    PacketCodecs.BYTE, RegisterActionPayload::type,
                    RegisterActionPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<RegisterActionPayload> getPayloadId() {
        return ID;
    }
}
