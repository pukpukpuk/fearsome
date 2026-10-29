package one.autremel.fearsome.illusions.manifestation;

import one.autremel.fearsome.render.CustomOverlay;
import one.autremel.fearsome.render.clipping.ClipPlane;
import one.autremel.fearsome.render.clipping.ClippingPass;
import org.joml.Matrix4fc;

import java.util.List;

public class BottomUpManifestation {
    private static final float CLIP_TOP_MARGIN = 0.25f;
    private static final float RING_HEIGHT = 1/16f;
    private static final int
            FIRST_RING_LIGHT = 3,
            SECOND_RING_LIGHT = 7,
            THIRD_RING_LIGHT = 10;

    private final int firstRingColor, secondRingColor, thirdRingColor;
    private final CustomOverlay thirdRingOverlay;

    public BottomUpManifestation(int firstRingColor, int secondRingColor, int thirdRingColor, CustomOverlay thirdRingOverlay) {
        this.firstRingColor = firstRingColor;
        this.secondRingColor = secondRingColor;
        this.thirdRingColor = thirdRingColor;
        this.thirdRingOverlay = thirdRingOverlay;
    }

    public List<ClippingPass> createClippingPasses(Matrix4fc basePositionMatrix, float entityY, float entityHeight, float progress) {
        if (progress <= 0) return List.of(ClippingPass.of(ClipPlane.keepBelow(basePositionMatrix, entityY - entityHeight)));

        var cutY = entityY + progress * (entityHeight + CLIP_TOP_MARGIN);
        var firstRingStartY = cutY - RING_HEIGHT * 2;
        var secondRingStartY = cutY - RING_HEIGHT;
        var thirdRingEndY = cutY + RING_HEIGHT;

        return List.of(
                ClippingPass.of(ClipPlane.keepBelow(basePositionMatrix, firstRingStartY)),
                ClippingPass.of(
                        firstRingColor,
                        FIRST_RING_LIGHT, FIRST_RING_LIGHT,
                        ClipPlane.keepAbove(basePositionMatrix, firstRingStartY),
                        ClipPlane.keepBelow(basePositionMatrix, secondRingStartY)
                ),
                ClippingPass.of(
                        secondRingColor,
                        SECOND_RING_LIGHT, SECOND_RING_LIGHT,
                        ClipPlane.keepAbove(basePositionMatrix, secondRingStartY),
                        ClipPlane.keepBelow(basePositionMatrix, cutY)
                ),
                ClippingPass.of(
                        thirdRingColor,
                        thirdRingOverlay,
                        THIRD_RING_LIGHT, THIRD_RING_LIGHT,
                        ClipPlane.keepAbove(basePositionMatrix, cutY),
                        ClipPlane.keepBelow(basePositionMatrix, thirdRingEndY)
                )
        );
    }
}
