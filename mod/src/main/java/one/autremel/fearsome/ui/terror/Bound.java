package one.autremel.fearsome.ui.terror;

import io.wispforest.owo.ui.core.Color;
import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IPayload;

public record Bound(Color color, int heartbeatPeriodMillis, boolean isWithHeartShaking, float beatVolume) {
    private static final int BITS_FOR_PERIOD = 15;

    public static Bound read(PacketByteBuf buf) {
        var colorCode = buf.readInt();
        var color = Color.ofRgb(colorCode);

        var s = buf.readShort();
        var period = s & ((1 << BITS_FOR_PERIOD) - 1);
        var isWithHeartShaking = (s & (1 << BITS_FOR_PERIOD)) != 0;

        var volume = IPayload.read01FloatFromShort(buf) * 2f;
        return new Bound(color, period, isWithHeartShaking, volume);
    }
}
