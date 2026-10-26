package one.autremel.fearsome.render.clipping;

import net.minecraft.client.render.LightmapTextureManager;
import one.autremel.fearsome.render.CustomOverlay;

import java.util.List;

public record ClippingPass(
        List<ClipPlane> planes,
        int colorMultiplier,
        int overlayOverride,
        int lightBonus
) {
    public static final int NO_OVERRIDE = -1;
    public static final int NO_LIGHT_BONUS = 0;

    public static ClippingPass of(ClipPlane... planes) {
        return of(NO_OVERRIDE, planes);
    }

    public static ClippingPass of(int colorMultiplier, ClipPlane... planes) {
        return new ClippingPass(List.of(planes), colorMultiplier, NO_OVERRIDE, NO_LIGHT_BONUS);
    }

    public static ClippingPass of(CustomOverlay customOverlay, ClipPlane... planes) {
        return new ClippingPass(List.of(planes), NO_OVERRIDE, customOverlay.getUv(), NO_LIGHT_BONUS);
    }

    public static ClippingPass of(int colorMultiplier, int blockLightBonus, int skyLightBonus, ClipPlane... planes) {
        return new ClippingPass(
                List.of(planes),
                colorMultiplier,
                NO_OVERRIDE,
                LightmapTextureManager.pack(blockLightBonus, skyLightBonus)
        );
    }

    public static ClippingPass of(CustomOverlay customOverlay, int blockLightBonus, int skyLightBonus, ClipPlane... planes) {
        return new ClippingPass(
                List.of(planes),
                NO_OVERRIDE,
                customOverlay.getUv(),
                LightmapTextureManager.pack(blockLightBonus, skyLightBonus)
        );
    }

    public static ClippingPass of(int colorMultiplier, CustomOverlay customOverlay, int blockLightBonus, int skyLightBonus, ClipPlane... planes) {
        return new ClippingPass(
                List.of(planes),
                colorMultiplier,
                customOverlay.getUv(),
                LightmapTextureManager.pack(blockLightBonus, skyLightBonus)
        );
    }
}
