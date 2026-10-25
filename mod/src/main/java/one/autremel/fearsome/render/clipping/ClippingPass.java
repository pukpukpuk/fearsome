package one.autremel.fearsome.render.clipping;

import net.minecraft.client.render.LightmapTextureManager;
import one.autremel.fearsome.render.CustomOverlay;

import java.util.List;

public record ClippingPass(
        List<ClipPlane> planes,
        int colorMultiplier,
        int overlayOverride,
        int lightOverride
) {
    public static final int NO_OVERRIDE = -1;

    public static ClippingPass of(ClipPlane... planes) {
        return of(NO_OVERRIDE, planes);
    }

    public static ClippingPass of(int colorMultiplier, ClipPlane... planes) {
        return new ClippingPass(List.of(planes), colorMultiplier, NO_OVERRIDE, NO_OVERRIDE);
    }

    public static ClippingPass of(CustomOverlay customOverlay, ClipPlane... planes) {
        return new ClippingPass(List.of(planes), NO_OVERRIDE, customOverlay.getUv(), NO_OVERRIDE);
    }

    public static ClippingPass of(CustomOverlay customOverlay, int blockLight, int skyLight, ClipPlane... planes) {
        return new ClippingPass(
                List.of(planes),
                NO_OVERRIDE,
                customOverlay.getUv(),
                LightmapTextureManager.pack(blockLight, skyLight)
        );
    }

    public static ClippingPass of(int colorMultiplier, CustomOverlay customOverlay, int blockLight, int skyLight, ClipPlane... planes) {
        return new ClippingPass(
                List.of(planes),
                colorMultiplier,
                customOverlay.getUv(),
                LightmapTextureManager.pack(blockLight, skyLight)
        );
    }
}
