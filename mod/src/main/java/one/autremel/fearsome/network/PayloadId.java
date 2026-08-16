package one.autremel.fearsome.network;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import one.autremel.fearsome.FearsomeMod;

public abstract class PayloadId<T extends IPayload<T>> {
    private static final String ID_PREFIX = "payloads/";

    @Getter
    private final CustomPayload.Id<T> id;
    @Getter
    private final PacketCodec<PacketByteBuf, T> codec;

    protected PayloadId(Class clz, PacketCodec<PacketByteBuf, T> codec) {
        id = new CustomPayload.Id<>(FearsomeMod.identifier(ID_PREFIX + getNameFromPayloadType(clz)));
        this.codec = codec;
    }

    private static String getNameFromPayloadType(Class payloadClass) {
        var name = payloadClass.getSimpleName();
        Preconditions.checkState(name.endsWith("Payload"));

        var substring = name.substring(0, name.length()-7);
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, substring);
    }
}
