package one.autremel.fearsome.illusions;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.text.Text;
import one.autremel.fearsome.network.NetworkController;
import org.jetbrains.annotations.Nullable;

public class NameDisplayer {
    private static boolean initialized;

    private static final Int2ObjectOpenHashMap<DisplayedName> displayedNames = new Int2ObjectOpenHashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetDisplayNameVisibilityPayload.ID, NameDisplayer::handleSetPayload);
    }

    public static void reset() {
        displayedNames.clear();
    }

    public static boolean isNameVisible(int entityId) {
        return displayedNames.containsKey(entityId);
    }

    @Nullable
    public static Text decorate(int entityId, Text name) {
        var displayedName = displayedNames.get(entityId);
        if (displayedName == null) return null;

        var coloredName = Text.literal(name.getString()).withColor(displayedName.color);
        return displayedName.icon == null
                ? coloredName
                : Text.empty()
                        .append(Text.literal(displayedName.icon + " "))
                        .append(coloredName);
    }

    private static void handleSetPayload(SetDisplayNameVisibilityPayload payload) {
        if (payload.isCleanupRequest()) {
            displayedNames.clear();
            return;
        }

        if (payload.isVisible()) displayedNames.put(payload.entityId(), new DisplayedName(payload.color(), payload.icon()));
        else displayedNames.remove(payload.entityId());
    }

    private record DisplayedName(int color, @Nullable Character icon) {}
}
