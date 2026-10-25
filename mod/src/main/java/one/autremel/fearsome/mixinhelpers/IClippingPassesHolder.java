package one.autremel.fearsome.mixinhelpers;

import one.autremel.fearsome.render.clipping.ClippingPass;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IClippingPassesHolder {
    @Nullable
    List<ClippingPass> getClippingPasses();

    void setClippingPasses(@Nullable List<ClippingPass> passes);
}
