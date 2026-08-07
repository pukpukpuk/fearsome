package one.autremel.fearsome.network;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.effects.*;
import one.autremel.fearsome.network.ping.PingPayload;
import one.autremel.fearsome.timer.*;
import one.autremel.fearsome.ui.game_state.payloads.*;
import one.autremel.fearsome.ui.survivor_list.payloads.*;
import org.apache.logging.log4j.util.Lazy;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class PayloadId<T extends IPayload> {
    private static final Lazy<List<PayloadId<? extends IPayload>>> VALUES = Lazy.lazy(PayloadId::calculateValues);
    private static final Lazy<PayloadId<? extends IPayload>[]> VALUES_ID_MAP = Lazy.lazy(PayloadId::calculateValuesIdMap);
    private static final Lazy<Integer> LAZY_MAX_ID = Lazy.lazy(PayloadId::calculateMaxId);


    public static final PayloadId<PingPayload> PING = new PayloadId<>(0, PingPayload.CODEC, false);

    public static final PayloadId<CreateSurvivorListPayload> CREATE_SURVIVOR_LIST = new PayloadId<>(1, CreateSurvivorListPayload.CODEC);
    public static final PayloadId<SetSurvivorHealthAmountPayload> SET_SURVIVOR_HEALTH_AMOUNT = new PayloadId<>(2, SetSurvivorHealthAmountPayload.CODEC);
    public static final PayloadId<SetSurvivorDeadStatePayload> SET_SURVIVOR_DEAD_STATE = new PayloadId<>(3, SetSurvivorDeadStatePayload.CODEC);

    public static final PayloadId<TimerUpdatePayload> UPDATE_TIMER = new PayloadId<>(4, TimerUpdatePayload.CODEC);
    public static final PayloadId<TimerDeletePayload> DELETE_TIMER = new PayloadId<>(5, TimerDeletePayload.CODEC);

    public static final PayloadId<CreateGameStateScreenPayload> CREATE_GAME_STATE_SCREEN = new PayloadId<>(6, CreateGameStateScreenPayload.CODEC);
    public static final PayloadId<SetGeneratorsCountPayload> SET_GENERATORS_COUNT = new PayloadId<>(7, SetGeneratorsCountPayload.CODEC);
    public static final PayloadId<SetQuestsCountPayload> SET_QUESTS_COUNT = new PayloadId<>(8, SetQuestsCountPayload.CODEC);

    public static final PayloadId<AddDisplayedEffectPayload> ADD_DISPLAYED_EFFECT = new PayloadId<>(9, AddDisplayedEffectPayload.CODEC);
    public static final PayloadId<RemoveDisplayedEffectPayload> REMOVE_DISPLAYED_EFFECT = new PayloadId<>(10, RemoveDisplayedEffectPayload.CODEC);
    public static final PayloadId<SetDisplayedEffectNamePayload> SET_DISPLAYED_EFFECT_NAME = new PayloadId<>(11, SetDisplayedEffectNamePayload.CODEC);

    public static final PayloadId<RemoveScreensPayload> REMOVE_SCREENS = new PayloadId<>(12, RemoveScreensPayload.CODEC, false);

    public static final PayloadId<PlayAbilityProgressAnimationPayload> PLAY_ABILITY_PROGRESS_ANIMATION = new PayloadId<>(13, PlayAbilityProgressAnimationPayload.CODEC, false);
    public static final PayloadId<SetAbilityCountPayload> SET_ABILITY_COUNT = new PayloadId<>(14, SetAbilityCountPayload.CODEC, false);


    @Getter
    private final int intId;
    @Getter
    private final boolean onlyWhileGameIsActive;
    @Getter
    private final PacketCodec<RegistryByteBuf, T> codec;

    private PayloadId(int id, PacketCodec<RegistryByteBuf, T> codec) {
        this(id, codec, true);
    }

    private PayloadId(int id, PacketCodec<RegistryByteBuf, T> codec, boolean onlyWhileGameIsActive) {
        Preconditions.checkArgument(id >= 0 && id <= 255);
        this.intId = id;
        this.codec = codec;
        this.onlyWhileGameIsActive = onlyWhileGameIsActive;
    }

    public byte getByteId() {
        return (byte)intId;
    }


    public static int getMaxId() {
        return LAZY_MAX_ID.get();
    }

    public static List<PayloadId<? extends IPayload>> getValues() {
        return VALUES.get();
    }

    public static PayloadId<? extends IPayload> get(int intId) {
        return VALUES_ID_MAP.get()[intId];
    }

    private static int calculateMaxId() {
        var max = -1;
        for (PayloadId<?> value : getValues()) {
            max = Math.max(value.getIntId(), max);
        }

        return max;
    }

    private static List<PayloadId<? extends IPayload>> calculateValues() {
        return Arrays.stream(PayloadId.class.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && field.getType() == PayloadId.class)
                .map(field -> {
                    try {
                        return (PayloadId<? extends IPayload>)field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toUnmodifiableList());
    }

    private static PayloadId<? extends IPayload>[] calculateValuesIdMap() {
        var result = new PayloadId[PayloadId.getMaxId() + 1];
        for (var payloadId : getValues()) {
            var index = payloadId.getIntId();
            Preconditions.checkState(result[index] == null);

            result[index] = payloadId;
        }

        return result;
    }
}
