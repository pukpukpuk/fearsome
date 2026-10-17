package one.autremel.fearsome.ui.lobby;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record CreateLobbyScreenPayload() implements IServerPayload<CreateLobbyScreenPayload> {
    public static CreateLobbyScreenPayload INSTANCE = new CreateLobbyScreenPayload();
    public static final ServerPayloadId<CreateLobbyScreenPayload> ID = new ServerPayloadId<>(
            CreateLobbyScreenPayload.class,
            false,
            CreateLobbyScreenPayload::decode
    );

    private static CreateLobbyScreenPayload decode(PacketByteBuf buf) {
        return INSTANCE;
    }

    @Override
    public @NotNull ServerPayloadId<CreateLobbyScreenPayload> getPayloadId() {
        return ID;
    }
}
