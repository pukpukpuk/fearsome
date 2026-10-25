package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.input.KeyBindings;

public class ToolsPartController {
    private static boolean initialized;

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        KeyBindings.registerForBoundKeyChange(ToolsPartController::onBoundKeyChange);

        GeneratorsHighlighter.initialize();
    }

    public static void reset() {
        GeneratorsHighlighter.reset();
    }

    private static void onBoundKeyChange(BindingId bindingId) {
        var data = GameStateScreenController.getData();
        if (data == null) return;

        var gameTools = data.screen().getToolsPart();
        for (var withBindingStackLayout : gameTools.getWithBindingStackLayoutList()) {
            if (withBindingStackLayout.getBindingId() == bindingId) withBindingStackLayout.onChildMutated(null);
        }
    }
}
