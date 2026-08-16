package one.autremel.fearsome;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Timer {
    private final String key;
    private long endTime;
    private boolean isCounting;

    private long pausedAt;

    public int getRemainingTime() {
        return Math.toIntExact(isCounting
                ? endTime - System.currentTimeMillis()
                : endTime - pausedAt);
    }

    public boolean isCompleted() {
        return getRemainingTime() <= 0;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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
