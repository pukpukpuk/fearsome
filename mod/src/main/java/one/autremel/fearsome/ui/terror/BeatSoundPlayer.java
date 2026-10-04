package one.autremel.fearsome.ui.terror;

import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.InterpolatedFloat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

class BeatSoundPlayer {
    private static final float VOLUME_FADE_AMOUNT = 0.15f;
    private static final int INTERPOLATION_DURATION = 1000;
    private static final int DELAY_MILLIS = 100;

    private static final InterpolatedFloat interpolatedVolume = new InterpolatedFloat(INTERPOLATION_DURATION, 0);


    public static void setVolume(float newTargetVolume) {
        interpolatedVolume.setValue(newTargetVolume);
    }

    public static void playBeat() {
        playBeatSound(0);
        CompletableFuture.runAsync(
                () -> MinecraftClient.getInstance().execute(() -> playBeatSound(1)),
                CompletableFuture.delayedExecutor(DELAY_MILLIS, TimeUnit.MILLISECONDS)
        );

    }

    private static void playBeatSound(int n) {
        var volume = interpolatedVolume.get() - n*VOLUME_FADE_AMOUNT;
        var sound = new BeatSoundInstance(volume);
        MinecraftClient.getInstance().getSoundManager().play(sound);
    }
}
