package one.autremel.fearsome.ui.game_state;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record CreateGameStateScreenPayload(String mapKey, int generatorsCount) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, CreateGameStateScreenPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, CreateGameStateScreenPayload::mapKey,
            PacketCodecs.VAR_INT, CreateGameStateScreenPayload::generatorsCount,
            CreateGameStateScreenPayload::new);

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.CREATE_GAME_STATE_SCREEN;
    }
}
