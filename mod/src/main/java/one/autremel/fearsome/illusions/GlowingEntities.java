package one.autremel.fearsome.illusions;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class GlowingEntities {
    private static final IntOpenHashSet glowedEntities = new IntOpenHashSet();

    public static void setGlowing(int entityId) {
        glowedEntities.add(entityId);
    }

    public static void unsetGlowing(int entityId) {
        glowedEntities.remove(entityId);
    }

    public static boolean isGlowed(int entityId) {
        return glowedEntities.contains(entityId);
    }

    public static void reset() {
        glowedEntities.clear();
    }
}
