package one.autremel.fearsome.timer;

import lombok.Getter;
import lombok.Setter;
import one.autremel.fearsome.network.ping.TimeSynchronizer;

public class Timer {
    @Setter
    private long endTime, pausedAt;
    @Getter
    private boolean isCounting;

    public Timer(TimerUpdatePayload payload) {
        this.endTime = TimeSynchronizer.toLocalTime(payload.endTime());
        this.pausedAt = TimeSynchronizer.toLocalTime(payload.pausedAt());
        this.isCounting = payload.isCounting();
    }

    public int getRemainingTime() {
        var unclamped = Math.toIntExact(isCounting
                ? endTime - System.currentTimeMillis()
                : endTime - pausedAt);
        return Math.max(unclamped, 0);
    }

    public void setCounting(boolean isCounting) {
        if (this.isCounting == isCounting) return;
        this.isCounting = isCounting;

        var currentTime = System.currentTimeMillis();
        if (isCounting) {
            endTime += currentTime - pausedAt;
        } else {
            pausedAt = currentTime;
        }
    }
}
