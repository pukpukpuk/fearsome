package one.autremel.fearsome.ui.survivor_list.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.survivor_list.TextEntry;
import org.jspecify.annotations.NonNull;

public record CreateSurvivorListPayload(TextEntry[] entries) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, CreateSurvivorListPayload> CODEC
            = PacketCodec.ofStatic(CreateSurvivorListPayload::encode, CreateSurvivorListPayload::decode);


    private static void encode(RegistryByteBuf buf, CreateSurvivorListPayload payload) {
        throw new UnsupportedOperationException();
    }

    private static CreateSurvivorListPayload decode(RegistryByteBuf buf) {
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
    public @NonNull PayloadId<CreateSurvivorListPayload> getPayloadId() {
        return PayloadId.CREATE_SURVIVOR_LIST;
    }
}
