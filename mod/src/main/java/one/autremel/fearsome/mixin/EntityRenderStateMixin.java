package one.autremel.fearsome.mixin;

import net.minecraft.client.render.entity.state.EntityRenderState;
import one.autremel.fearsome.mixinhelpers.IManifestationProgressHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements IManifestationProgressHolder {
    @Unique
    private float manifestationProgress = Float.NaN;

    @Override
    public float getManifestationProgress() {
        return manifestationProgress;
    }

    @Override
    public void setManifestationProgress(float progress) {
        manifestationProgress = progress;
    }
}
