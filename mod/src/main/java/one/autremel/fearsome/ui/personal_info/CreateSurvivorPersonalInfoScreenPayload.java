package one.autremel.fearsome.ui.personal_info;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.ui.terror.Bound;
import org.jetbrains.annotations.NotNull;

@Payload
public record CreateSurvivorPersonalInfoScreenPayload(
        int characterIndex,
        Bound[] bounds
) implements IServerPayload<CreateSurvivorPersonalInfoScreenPayload> {
    public static final ServerPayloadId<CreateSurvivorPersonalInfoScreenPayload> ID = new ServerPayloadId<>(
            CreateSurvivorPersonalInfoScreenPayload.class,
            CreateSurvivorPersonalInfoScreenPayload::decode
    );

    private static CreateSurvivorPersonalInfoScreenPayload decode(PacketByteBuf buf) {
        var characterIndex = Byte.toUnsignedInt(buf.readByte());

        var count = Byte.toUnsignedInt(buf.readByte());
        var bounds = new Bound[count];
        for (int i = 0; i < count; i++) {
            bounds[i] = Bound.read(buf);
        }

        return new CreateSurvivorPersonalInfoScreenPayload(characterIndex, bounds);
    }

    @Override
    public @NotNull ServerPayloadId<CreateSurvivorPersonalInfoScreenPayload> getPayloadId() {
        return ID;
    }
}
