package one.autremel.fearsome.ui.survivor_list.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.ui.survivor_list.TextEntry;
import org.jetbrains.annotations.NotNull;

@Payload
public record CreateSurvivorListPayload(TextEntry[] entries) implements IServerPayload<CreateSurvivorListPayload> {
    public static final ServerPayloadId<CreateSurvivorListPayload> ID = new ServerPayloadId<>(
            CreateSurvivorListPayload.class,
            CreateSurvivorListPayload::decode
    );

    private static CreateSurvivorListPayload decode(PacketByteBuf buf) {
        var survivorsCount = buf.readVarInt();
        var entries = new TextEntry[survivorsCount];
        for (var i = 0; i < survivorsCount; i++) {
            var name = buf.readString();
            var characterKey = buf.readString();
            entries[i] = new TextEntry(name, characterKey);
        }

        return new CreateSurvivorListPayload(entries);
    }

    @Override
    public @NotNull ServerPayloadId<CreateSurvivorListPayload> getPayloadId() {
        return ID;
    }
}
