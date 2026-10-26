package one.autremel.fearsome.ui.terror;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.core.Color;
import lombok.AccessLevel;
import lombok.Getter;
import one.autremel.fearsome.InterpolatedFloat;
import one.autremel.fearsome.network.NetworkController;
import org.jetbrains.annotations.Nullable;

public class TerrorPartController {
    private static final int FADE_DURATION_MILLIS = 600;
    private static final int POSTERIZATION_AMOUNT = 7;
    private static final Color GRAY_HEART_COLOR = Color.ofRgb(0x555555);
    private static final int INTERPOLATION_DURATION = 1000;
    private static final int INITIAL_PERIOD = 2000;

    private static boolean initialized;

    @Nullable
    private static TerrorPart part;
    private static Bound[] bounds;

    @Nullable @Getter(AccessLevel.PACKAGE)
    private static Bound currentBound;

    private static final InterpolatedFloat interpolatedBeatPeriod = new InterpolatedFloat(INTERPOLATION_DURATION, INITIAL_PERIOD);

    private static long lastBeatTime;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetCurrentTerrorBoundPayload.ID, TerrorPartController::handlerBoundSetPayload);
    }

    public static void attach(TerrorPart part, Bound[] bounds) {
        TerrorPartController.part = part;
        TerrorPartController.bounds = bounds;
        currentBound = null;

        part.setVisible(false);
        updateInterpolatedValues(null);
    }

    public static void detach() {
        part = null;
        bounds = null;
        currentBound = null;
    }

    public static void update() {
        if (part == null || currentBound == null) return;

        var currentTime = System.currentTimeMillis();
        var timePassed = currentTime - lastBeatTime;
        if (timePassed >= interpolatedBeatPeriod.get()) {
            lastBeatTime = currentTime;
            BeatSoundPlayer.playBeat();
        }

        part.setFrame(getFrame(timePassed));

        var percentage = Math.min((float) timePassed / FADE_DURATION_MILLIS, 1);
        percentage = (float) (Math.floor(percentage * POSTERIZATION_AMOUNT) / POSTERIZATION_AMOUNT);

        var color = currentBound.color().interpolate(GRAY_HEART_COLOR, percentage);
        part.setColor(color);
    }

    private static void handlerBoundSetPayload(SetCurrentTerrorBoundPayload payload) {
        if (part == null) return;

        var previousBound = currentBound;

        var index = payload.boundId();
        currentBound = index == null ? null : bounds[index];
        if (previousBound == currentBound) return;

        if (currentBound == null) {
            part.setVisible(false);
        } else if (previousBound == null) {
            part.setVisible(true);
            lastBeatTime = System.currentTimeMillis();
        }

        updateInterpolatedValues(currentBound);
    }

    private static void updateInterpolatedValues(@Nullable Bound currentBound) {
        if (currentBound != null) {
            interpolatedBeatPeriod.setValue(currentBound.heartbeatPeriodMillis());
            BeatSoundPlayer.setVolume(currentBound.beatVolume());
        } else {
            interpolatedBeatPeriod.setValue(INITIAL_PERIOD);
            BeatSoundPlayer.setVolume(0);
        }
    }

    private static int getFrame(long timePassed) {
        return currentBound != null && currentBound.isWithHeartShaking()
                ? (timePassed < 40 ? 1 : timePassed < 80 ? 2 : 0)
                : 0;
    }
}
