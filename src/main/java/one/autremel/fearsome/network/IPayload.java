package one.autremel.fearsome.network;

import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public interface IPayload extends CustomPayload {
    @NotNull
    PayloadId<? extends IPayload> getPayloadId();

    @Override
    default Id<? extends CustomPayload> getId() {
        return PayloadCodecsContainer.ID;
    }
}
