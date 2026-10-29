package one.autremel.fearsome.illusions.dream;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetDreamPortalGlowingPayload(
        int portalId,
        boolean isGlowing,
        boolean isFocused
) implements IServerPayload<SetDreamPortalGlowingPayload> {
    public static final ServerPayloadId<SetDreamPortalGlowingPayload> ID = new ServerPayloadId<>(
            SetDreamPortalGlowingPayload.class,
            SetDreamPortalGlowingPayload::decode
    );

    private static SetDreamPortalGlowingPayload decode(PacketByteBuf buf) {
        var portalId = buf.readVarInt();
        var flags = buf.readByte(); ///______fg
        var isGlowing = (flags & 0b1) != 0;
        var isFocused = (flags & 0b10) != 0;

        return new SetDreamPortalGlowingPayload(portalId, isGlowing, isFocused);
    }

    @Override
    public @NotNull ServerPayloadId<SetDreamPortalGlowingPayload> getPayloadId() {
        return ID;
    }
}
