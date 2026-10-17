package one.autremel.fearsome.ui.lobby;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetLobbyPlayerCountPayload(
        int playerCount,
        int inQueueCount
) implements IServerPayload<SetLobbyPlayerCountPayload> {
    public static final ServerPayloadId<SetLobbyPlayerCountPayload> ID = new ServerPayloadId<>(
            SetLobbyPlayerCountPayload.class,
            SetLobbyPlayerCountPayload::decode
    );

    private static SetLobbyPlayerCountPayload decode(PacketByteBuf buf) {
        var playerCount = buf.readVarInt();
        var inQueueCount = buf.readVarInt();
        return new SetLobbyPlayerCountPayload(playerCount, inQueueCount);
    }

    @Override
    public @NotNull ServerPayloadId<SetLobbyPlayerCountPayload> getPayloadId() {
        return ID;
    }
}
