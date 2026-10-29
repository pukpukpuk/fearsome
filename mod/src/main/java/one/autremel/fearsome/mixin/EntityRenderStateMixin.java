package one.autremel.fearsome.mixin;

import net.minecraft.client.render.entity.state.EntityRenderState;
import one.autremel.fearsome.illusions.manifestation.BottomUpManifestation;
import one.autremel.fearsome.mixinhelpers.IManifestationProgressHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements IManifestationProgressHolder {
    @Unique
    private float manifestationProgress = Float.NaN;
    @Unique
    @Nullable
    private BottomUpManifestation manifestation;

    @Override
    public float getManifestationProgress() {
        return manifestationProgress;
    }

    @Override
    public void setManifestationProgress(float progress) {
        manifestationProgress = progress;
    }

    @Override
    public @Nullable BottomUpManifestation getManifestation() {
        return manifestation;
    }

    @Override
    public void setManifestation(@Nullable BottomUpManifestation manifestation) {
        this.manifestation = manifestation;
    }
}
