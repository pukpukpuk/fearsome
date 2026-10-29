package one.autremel.fearsome.mixinhelpers;

import one.autremel.fearsome.illusions.manifestation.BottomUpManifestation;
import org.jetbrains.annotations.Nullable;

public interface IManifestationProgressHolder {
    float getManifestationProgress();

    void setManifestationProgress(float progress);

    @Nullable
    BottomUpManifestation getManifestation();

    void setManifestation(@Nullable BottomUpManifestation manifestation);
}
