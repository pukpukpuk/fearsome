package one.autremel.fearsome.illusions;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.illusions.dream.DreamPortals;
import one.autremel.fearsome.illusions.glowing.GlowingBlocks;
import one.autremel.fearsome.illusions.glowing.GlowingEntities;
import one.autremel.fearsome.illusions.manifestation.Manifestations;

public class Illusions {
    private static boolean initialized;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        SelectedItemDisplayer.initialize();
        NameDisplayer.initialize();
        SkinOverrides.initialize();
        GlowingEntities.initialize();
        GlowingBlocks.initialize();

        Manifestations.initialize();
        DreamPortals.initialize();

        GameEndCauseDisplayer.initialize();
    }

    public static void reset() {
        SelectedItemDisplayer.reset();
        NameDisplayer.reset();
        SkinOverrides.reset();
        GlowingEntities.reset();
        GlowingBlocks.reset();

        Manifestations.reset();
        DreamPortals.reset();
    }
}
