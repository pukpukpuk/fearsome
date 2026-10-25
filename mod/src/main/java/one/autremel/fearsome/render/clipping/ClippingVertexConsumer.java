package one.autremel.fearsome.render.clipping;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class ClippingVertexConsumer implements VertexConsumer {
    private static final int QUAD_SIZE = 4;

    private static final int COLOR = 1;
    private static final int TEXTURE = 1 << 1;
    private static final int OVERLAY = 1 << 2;
    private static final int LIGHT = 1 << 3;
    private static final int NORMAL = 1 << 4;
    private static final int LINE_WIDTH = 1 << 5;
    private static final int BULK = COLOR | TEXTURE | OVERLAY | LIGHT | NORMAL;

    private final VertexConsumer delegate;
    private final List<ClipPlane> planes;
    private final int colorMultiplier;
    private final int overlayOverride, lightOverride;

    private final Vertex[] quad = {new Vertex(), new Vertex(), new Vertex(), new Vertex()};
    private int count;

    private final Vertex[] intersections;
    private Vertex[] polygon;
    private Vertex[] clippedPolygon;
    private final float[] distances;

    public ClippingVertexConsumer(VertexConsumer delegate, ClippingPass pass) {
        this.delegate = delegate;
        this.planes = pass.planes();
        this.colorMultiplier = pass.colorMultiplier();
        this.overlayOverride = pass.overlayOverride();
        this.lightOverride = pass.lightOverride();

        intersections = new Vertex[planes.size() * 2];
        for (int i = 0; i < intersections.length; i++) intersections[i] = new Vertex();

        var maxPolygonSize = QUAD_SIZE + planes.size();
        polygon = new Vertex[maxPolygonSize];
        clippedPolygon = new Vertex[maxPolygonSize];
        distances = new float[maxPolygonSize];
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        if (count == QUAD_SIZE) commitVertices();
        var vertex = quad[count++];
        vertex.reset(x, y, z);
        return this;
    }

    @Override
    public void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        vertex(x, y, z);

        var vertex = quad[count - 1];
        vertex.flags = BULK;
        vertex.color = color;
        vertex.u = u;
        vertex.v = v;
        vertex.overlay = overlay;
        vertex.light = light;
        vertex.normalX = normalX;
        vertex.normalY = normalY;
        vertex.normalZ = normalZ;

        if (count == QUAD_SIZE) commitVertices();
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return color(ColorHelper.getArgb(alpha, red, green, blue));
    }

    @Override
    public VertexConsumer color(int argb) {
        if (count == 0) return delegate.color(multiplyColor(argb));

        var vertex = quad[count - 1];
        vertex.flags |= COLOR;
        vertex.color = argb;
        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        if (count == 0) return delegate.texture(u, v);

        var vertex = quad[count - 1];
        vertex.flags |= TEXTURE;
        vertex.u = u;
        vertex.v = v;
        return this;
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        if (count == 0) return delegate.overlay(overrideOverlay(pack(u, v)));

        var vertex = quad[count - 1];
        vertex.flags |= OVERLAY;
        vertex.overlay = pack(u, v);
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        if (count == 0) return delegate.light(u, v);

        var vertex = quad[count - 1];
        vertex.flags |= LIGHT;
        vertex.light = pack(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        if (count == 0) return delegate.normal(x, y, z);

        var vertex = quad[count - 1];
        vertex.flags |= NORMAL;
        vertex.normalX = x;
        vertex.normalY = y;
        vertex.normalZ = z;
        return this;
    }

    @Override
    public VertexConsumer lineWidth(float width) {
        if (count == 0) return delegate.lineWidth(width);

        var vertex = quad[count - 1];
        vertex.flags |= LINE_WIDTH;
        vertex.lineWidth = width;
        return this;
    }

    public void commitVertices() {
        if (count == QUAD_SIZE) {
            emitClippedQuad();
        } else {
            for (int i = 0; i < count; i++) emit(quad[i]);
        }
        count = 0;
    }

    private void emitClippedQuad() {
        System.arraycopy(quad, 0, polygon, 0, QUAD_SIZE);
        var size = QUAD_SIZE;
        var usedIntersections = 0;

        for (var plane : planes) {
            var insideCount = 0;
            for (int i = 0; i < size; i++) {
                var vertex = polygon[i];
                distances[i] = plane.distance(vertex.x, vertex.y, vertex.z);
                if (distances[i] <= 0) insideCount++;
            }

            if (insideCount == 0) return;
            if (insideCount == size) continue;

            var clippedSize = 0;
            for (int i = 0; i < size; i++) {
                var next = (i + 1) % size;
                var isInside = distances[i] <= 0;
                var isNextInside = distances[next] <= 0;

                if (isInside) clippedPolygon[clippedSize++] = polygon[i];
                if (isInside == isNextInside || usedIntersections == intersections.length) continue;

                var delta = distances[i] / (distances[i] - distances[next]);
                var intersection = intersections[usedIntersections++];
                intersection.interpolate(polygon[i], polygon[next], delta, isInside ? polygon[i] : polygon[next]);
                clippedPolygon[clippedSize++] = intersection;
            }

            var swap = polygon;
            polygon = clippedPolygon;
            clippedPolygon = swap;
            size = clippedSize;
        }

        emitPolygon(size);
    }

    private void emitPolygon(int size) {
        if (size < 3) return;

        for (int i = 1; i + 1 < size; i += 2) {
            var last = i + 2 < size ? polygon[i + 2] : polygon[i + 1];
            emit(polygon[0]);
            emit(polygon[i]);
            emit(polygon[i + 1]);
            emit(last);
        }
    }

    private void emit(Vertex vertex) {
        if ((vertex.flags & BULK) == BULK) {
            delegate.vertex(vertex.x, vertex.y, vertex.z, multiplyColor(vertex.color), vertex.u, vertex.v,
                    overrideOverlay(vertex.overlay), overrideLight(vertex.light), vertex.normalX, vertex.normalY, vertex.normalZ);
        } else {
            delegate.vertex(vertex.x, vertex.y, vertex.z);
            if ((vertex.flags & COLOR) != 0) delegate.color(multiplyColor(vertex.color));
            if ((vertex.flags & TEXTURE) != 0) delegate.texture(vertex.u, vertex.v);
            if ((vertex.flags & OVERLAY) != 0) delegate.overlay(overrideOverlay(vertex.overlay));
            if ((vertex.flags & LIGHT) != 0) delegate.light(overrideLight(vertex.light));
            if ((vertex.flags & NORMAL) != 0) delegate.normal(vertex.normalX, vertex.normalY, vertex.normalZ);
        }

        if ((vertex.flags & LINE_WIDTH) != 0) delegate.lineWidth(vertex.lineWidth);
    }

    private int multiplyColor(int color) {
        return colorMultiplier == ClippingPass.NO_OVERRIDE ? color : ColorHelper.mix(color, colorMultiplier);
    }

    private int overrideOverlay(int overlay) {
        return overlayOverride == ClippingPass.NO_OVERRIDE ? overlay : overlayOverride;
    }

    private int overrideLight(int light) {
        return lightOverride == ClippingPass.NO_OVERRIDE ? light : lightOverride;
    }

    private static int pack(int u, int v) {
        return (u & 0xFFFF) | (v << 16);
    }

    private static class Vertex {
        private float x, y, z;
        private int flags;
        private int color;
        private float u, v;
        private int overlay, light;
        private float normalX, normalY, normalZ;
        private float lineWidth;

        private void reset(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            flags = 0;
        }

        private void interpolate(Vertex from, Vertex to, float delta, Vertex attributesSource) {
            x = MathHelper.lerp(delta, from.x, to.x);
            y = MathHelper.lerp(delta, from.y, to.y);
            z = MathHelper.lerp(delta, from.z, to.z);
            u = MathHelper.lerp(delta, from.u, to.u);
            v = MathHelper.lerp(delta, from.v, to.v);

            flags = attributesSource.flags;
            color = attributesSource.color;
            overlay = attributesSource.overlay;
            light = attributesSource.light;
            normalX = attributesSource.normalX;
            normalY = attributesSource.normalY;
            normalZ = attributesSource.normalZ;
            lineWidth = attributesSource.lineWidth;
        }
    }
}
