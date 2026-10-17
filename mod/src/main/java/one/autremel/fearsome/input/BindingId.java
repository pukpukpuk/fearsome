package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.wispforest.owo.ui.parsing.UIModelParsingException;
import lombok.Getter;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum BindingId {
    EXPAND_SURVIVOR_LIST("expand_survivor_list"),
    OPEN_BUILDS_BROWSER("open_builds_browser"),
    HIGHLIGHT_GENERATORS("highlight_generators"),

    ATTACK(0, "attack"),
    FIRST_ABILITY(1, "first_ability"),
    INTERACTION(2, "interaction"),
    SECOND_ABILITY(3, "second_ability"),
    SNEAK_KEY(4, "sneak_key"),
    ;

    public static final PacketCodec<? super ByteBuf, BindingId> CODEC = new PacketCodec<>() {
        @Override
        public BindingId decode(ByteBuf buf) {
            return get(buf.readByte());
        }

        @Override
        public void encode(ByteBuf buf, BindingId value) {
            buf.writeByte(value.getByteId());
        }
    };
    public static final int BINDINGS_COUNT = 32;
    private static final List<BindingId> CACHED_VALUES;
    private static final BindingId[] CACHED_BY_ID = new BindingId[BINDINGS_COUNT];

    @Getter
    private final byte byteId;
    @Getter @NotNull
    private final String id;

    static {
        CACHED_VALUES = List.of(values());
        for (var bindingType : CACHED_VALUES) {
            var byteId = bindingType.getByteId();
            if (byteId == -1) continue;

            Preconditions.checkState(CACHED_BY_ID[byteId] == null);
            CACHED_BY_ID[byteId] = bindingType;
        }
    }

    BindingId(@NotNull String id) {
        this.byteId = -1;
        this.id = id;
    }

    BindingId(int byteId, @NotNull String id) {
        Preconditions.checkArgument(byteId >= 0 && byteId < BINDINGS_COUNT);
        this.byteId = (byte)byteId;
        this.id = id;
    }

    @Nullable
    public static BindingId get(int byteId) {
        return byteId >= 0 && byteId < CACHED_BY_ID.length ? CACHED_BY_ID[byteId] : null;
    }

    public static BindingId get(String stringId) {
        for (var value : CACHED_VALUES) {
            if (Objects.equals(stringId, value.getId())) return value;
        }

        throw new UIModelParsingException("No such BindingId with id " + stringId);
    }
}
