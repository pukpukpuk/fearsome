package one.autremel.fearsome.network;

import net.minecraft.network.packet.CustomPayload;

public interface IPayload extends CustomPayload {
    PayloadId<? extends IPayload> getPayloadId();

    @Override
    default Id<? extends CustomPayload> getId() {
        return PayloadCodecsContainer.ID;
    }
}
