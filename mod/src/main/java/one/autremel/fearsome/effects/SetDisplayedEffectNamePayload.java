package one.autremel.fearsome.effects;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetDisplayedEffectNamePayload(
        int effectId,
        int characterIndex,
        String newName
) implements IServerPayload<SetDisplayedEffectNamePayload> {
    public static final ServerPayloadId<SetDisplayedEffectNamePayload> ID = new ServerPayloadId<>(
            SetDisplayedEffectNamePayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetDisplayedEffectNamePayload::effectId,
                    PacketCodecs.VAR_INT, SetDisplayedEffectNamePayload::characterIndex,
                    PacketCodecs.STRING, SetDisplayedEffectNamePayload::newName,
                    SetDisplayedEffectNamePayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetDisplayedEffectNamePayload> getPayloadId() {
        return ID;
    }
}
