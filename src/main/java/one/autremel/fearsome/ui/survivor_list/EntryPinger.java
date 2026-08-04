package one.autremel.fearsome.ui.survivor_list;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.ui.DefaultBlurredSurface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class EntryPinger {
    private static final int ANIMATION_DURATION = 600;
    private static final int PING_COLOR = 0xBB777777;

    private static final HashMap<Entry, Long> animationStartTimeMap = new HashMap<>();

    static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(EntryPinger::onTick);
    }

    static void ping(Entry entry) {
        animationStartTimeMap.put(entry, System.currentTimeMillis());
    }


    private static float ease(float progress) {
        var a = 2*progress - 1;
        return -a*a + 1;
    }

    private static int lerpColor(int first, int second, float k) {
        var a1 = (first >> 24) & 0xFF;
        var r1 = (first >> 16) & 0xFF;
        var g1 = (first >> 8) & 0xFF;
        var b1 = first & 0xFF;

        var a2 = (second >> 24) & 0xFF;
        var r2 = (second >> 16) & 0xFF;
        var g2 = (second >> 8) & 0xFF;
        var b2 = second & 0xFF;

        return ((int)(a1 + (a2-a1) * k) << 24) |
               ((int)(r1 + (r2-r1) * k) << 16) |
               ((int)(g1 + (g2-g1) * k) << 8) |
                (int)(b1 + (b2-b1) * k);
    }

    private static void onTick(MinecraftClient client) {
        List<Entry> withEndedAnimation = null;

        var currentTime = System.currentTimeMillis();
        for (var mapEntry : animationStartTimeMap.entrySet()) {
            var elapsed = currentTime - mapEntry.getValue();
            var progress = (float)elapsed / ANIMATION_DURATION;
            var eased = ease(progress);

            var color = lerpColor(DefaultBlurredSurface.DEFAULT_COLOR, PING_COLOR, eased);
            mapEntry.getKey().surface().setColor(color);

            if (progress >= 1) {
                if (withEndedAnimation == null) withEndedAnimation = new ArrayList<>();
                withEndedAnimation.add(mapEntry.getKey());
            }
        }

        if (withEndedAnimation == null) return;
        for (var entry : withEndedAnimation) {
            entry.surface().setColor(DefaultBlurredSurface.DEFAULT_COLOR);
            animationStartTimeMap.remove(entry);
        }
    }
}
