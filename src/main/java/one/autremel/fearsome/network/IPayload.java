package one.autremel.fearsome.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public interface IPayload extends CustomPayload {

    static float readFloatFromShort(RegistryByteBuf buf) {
        var b1 = (int)buf.readByte() & 0xFF;
        var b2 = (int)buf.readByte() & 0xFF;

        var i = (b1 << 8) | b2;
        return (float) i / ((1 << 16) - 1);
    }

    @NotNull
    PayloadId<? extends IPayload> getPayloadId();

    @Override
    default Id<? extends CustomPayload> getId() {
        return PayloadCodecsContainer.ID;
    }
}
