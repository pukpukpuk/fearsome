package one.autremel.fearsome.render.clipping;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public record ClipPlane(float a, float b, float c, float d) {
    public static ClipPlane keepBelow(Matrix4fc basePositionMatrix, float y) {
        var inverted = new Matrix4f(basePositionMatrix).invert();
        return new ClipPlane(inverted.m01(), inverted.m11(), inverted.m21(), inverted.m31() - y);
    }

    public static ClipPlane keepAbove(Matrix4fc basePositionMatrix, float y) {
        var below = keepBelow(basePositionMatrix, y);
        return new ClipPlane(-below.a, -below.b, -below.c, -below.d);
    }

    public float distance(float x, float y, float z) {
        return a * x + b * y + c * z + d;
    }
}
