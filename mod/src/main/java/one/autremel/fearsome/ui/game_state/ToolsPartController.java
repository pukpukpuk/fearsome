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

    private static void onBoundKeyChange(BindingId bindingId) {
        var data = GameStateScreenController.getData();
        if (data == null) return;

        var gameTools = data.screen().getToolsPart();
        var anyAffected = gameTools.getWithBindingStackLayoutList().stream()
                .anyMatch(withBindingStackLayout -> withBindingStackLayout.getBindingId() == bindingId);
        if (anyAffected) gameTools.getRoot().onChildMutated(null);
    }
}
