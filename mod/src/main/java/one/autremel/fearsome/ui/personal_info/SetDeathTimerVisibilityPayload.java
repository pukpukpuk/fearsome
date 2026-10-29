package one.autremel.fearsome.ui.personal_info;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetDeathTimerVisibilityPayload(boolean isVisible) implements IServerPayload<SetDeathTimerVisibilityPayload> {
    public static final ServerPayloadId<SetDeathTimerVisibilityPayload> ID = new ServerPayloadId<>(
            SetDeathTimerVisibilityPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.BOOLEAN, SetDeathTimerVisibilityPayload::isVisible,
                    SetDeathTimerVisibilityPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetDeathTimerVisibilityPayload> getPayloadId() {
        return ID;
    }
}
