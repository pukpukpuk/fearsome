package one.autremel.fearsome.illusions;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import org.jetbrains.annotations.Nullable;

public class SkinOverrides {
    private static boolean initialized;

    private static final Int2ObjectOpenHashMap<SkinTextures> overrides = new Int2ObjectOpenHashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetEntitySkinPayload.ID, SkinOverrides::handleSetPayload);
    }

    @Nullable
    public static SkinTextures get(int entityId) {
        return overrides.get(entityId);
    }

    public static void reset() {
        overrides.clear();
    }

    private static void handleSetPayload(SetEntitySkinPayload payload) {
        if (payload.isCleanupRequest()) {
            overrides.clear();
            return;
        }

        if (payload.skinKey() == null) overrides.remove(payload.entityId());
        else overrides.put(payload.entityId(), createSkinTextures(payload.skinKey(), payload.isSlim()));
    }

    private static SkinTextures createSkinTextures(String skinKey, boolean isSlim) {
        var path = "skins/" + skinKey;
        var body = new AssetInfo.TextureAssetInfo(FearsomeMod.identifier(path), FearsomeMod.textureIdentifier(path));
        var model = isSlim ? PlayerSkinType.SLIM : PlayerSkinType.WIDE;
        return new SkinTextures(body, null, null, model, false);
    }
}
