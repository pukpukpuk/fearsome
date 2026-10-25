package one.autremel.fearsome;

public class Easings {
    public static float easeOut(float x) {
        return (float)(1d - Math.pow(1 - x, 2));
    }

    public static float easeAndBack(float x) {
        return (float)(1d - Math.pow(1 - 2*x, 2));
    }
}
