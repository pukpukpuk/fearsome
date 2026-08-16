package one.autremel.fearsome.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public interface IPayload<T extends IPayload<T>> extends CustomPayload {

    static float readFloatFromShort(PacketByteBuf buf) {
        var s = buf.readShort() & 0xFFFF;
        return (float) s / ((1 << 16) - 1);
    }

    static Float readOptionalFloatFromShort(PacketByteBuf buf) {
        var s = buf.readShort() & 0xFFFF;
        if ((s & 0b1) == 0) return null;

        s >>= 1;
        return (float) s / ((1 << 15) - 1);
    }

    @NotNull
    PayloadId<T> getPayloadId();

    default Id<? extends CustomPayload> getId() {
        return getPayloadId().getId();
    }
}
