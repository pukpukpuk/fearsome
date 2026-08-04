package one.autremel.fearsome;

public class Utils {
    public static String convertTimeToText(int remainingMillis) {
        var totalSeconds = Math.ceilDiv(remainingMillis, 1000);

        var seconds = totalSeconds % 60;
        var secondsText = seconds < 10 ? ("0" + seconds) : seconds;

        return (totalSeconds / 60) + ":" + secondsText;
    }
}
