package one.autremel.fearsome.illusions;

import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetSelectedItemPayload(
        int entityId,
        Item item,
        Long shownUntil
) implements IServerPayload<SetSelectedItemPayload> {
    public static final ServerPayloadId<SetSelectedItemPayload> ID = new ServerPayloadId<>(
            SetSelectedItemPayload.class,
            SetSelectedItemPayload::decode
    );

    private static SetSelectedItemPayload decode(PacketByteBuf buf) {
        var targetId = buf.readVarInt();
        var item = Registries.ITEM.get(buf.readVarInt());
        var shownUntil = buf.readBoolean() ? TimeSynchronizer.toLocalTime(buf.readLong()) : null;

        return new SetSelectedItemPayload(targetId, item, shownUntil);
    }

    @Override
    public @NotNull ServerPayloadId<SetSelectedItemPayload> getPayloadId() {
        return ID;
    }
}
