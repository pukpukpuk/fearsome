package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import net.minecraft.text.Text;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.AnimationData;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.hints.payloads.RegisterActionPayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressAnimationPayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionVisibilityPayload;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

class ActionBarController {
    private static final String KEY_PREFIX = "fearsome.game.action.progress.";

    private static boolean initialized;

    private static final HashMap<Integer, ActionData> registeredActions = new HashMap<>();
    private static final HashSet<ActionData> visibleActions = new HashSet<>();
    @Nullable
    private static ActionData currentlyShowedAction;

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(RegisterActionPayload.ID, ActionBarController::handleRegister);
        NetworkController.register(SetActionProgressAnimationPayload.ID, ActionBarController::handleSetAnimation);
        NetworkController.register(SetActionVisibilityPayload.ID, ActionBarController::handleSetVisibility);

        UiController.registerForScreensRemoval(ActionBarController::handleScreensRemove, true);
        OverlayScreenRegistrar.registerBeforeScreenDrawing(HintsScreen.ID, ActionBarController::update);
    }

    private static void updateCurrentlyShowedAction() {
        if (visibleActions.isEmpty()) {
            currentlyShowedAction = null;
            return;
        }

        if (visibleActions.size() == 1) {
            currentlyShowedAction = visibleActions.iterator().next();
            return;
        }

        currentlyShowedAction = visibleActions.stream().max(Comparator.comparingInt(ActionData::getPriority)).orElseThrow();
    }

    private static void handleRegister(RegisterActionPayload payload) {
        var actionId = payload.actionId();
        var actionData = new ActionData(
                actionId,
                payload.priority(),
                Text.translatable(KEY_PREFIX+payload.key())
        );

        registeredActions.put(actionId, actionData);
    }

    private static void handleSetAnimation(SetActionProgressAnimationPayload payload) {
        var actionData = registeredActions.get(payload.actionId());
        if (actionData == null) return;

        if (payload.duration() == 0) {
            actionData.setCurrentProgress(payload.target());
            return;
        }

        var startValue = payload.start() != null ? payload.start() : actionData.getCurrentProgress();
        var animation = new AnimationData(
                startValue,
                payload.target(),
                payload.duration(),
                true,
                System.currentTimeMillis()
        );
        actionData.setAnimation(animation);
    }

    private static void handleSetVisibility(SetActionVisibilityPayload payload) {
        var actionData = registeredActions.get(payload.actionId());
        if (actionData == null) return;

        actionData.setVisible(payload.isVisible());
        if (actionData.isVisible()) visibleActions.add(actionData);
        else visibleActions.remove(actionData);

        updateCurrentlyShowedAction();
    }

    private static void handleScreensRemove() {
        registeredActions.clear();
        visibleActions.clear();
        currentlyShowedAction = null;
    }

    private static void update() {
        var data = HintsScreenController.getDataNow();
        if (data == null) return;

        var screen = data.screen();

        var isVisible = currentlyShowedAction != null;
        screen.setProgressBarVisible(isVisible);
        if (!isVisible) return;

        screen.setProgressBarText(currentlyShowedAction.getText());

        currentlyShowedAction.updateAnimation();
        screen.setActionProgressAmount(currentlyShowedAction.getCurrentProgress());
    }
}
