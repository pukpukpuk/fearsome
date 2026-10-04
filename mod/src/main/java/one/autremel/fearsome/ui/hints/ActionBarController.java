package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.AnimationData;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.hints.payloads.RegisterActionPayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressAnimationPayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionVisibilityPayload;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

class ActionBarController {
    private static final String KEY_PREFIX = "fearsome.action.progress.";

    private static boolean initialized;

    private static final HashMap<Integer, ActionData> registeredActions = new HashMap<>();
    private static final HashSet<ActionData> visibleActions = new HashSet<>();
    @Nullable
    private static ActionData currentlyShownAction;

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(RegisterActionPayload.ID, ActionBarController::handleRegister);
        NetworkController.register(SetActionProgressAnimationPayload.ID, ActionBarController::handleSetAnimation);
        NetworkController.register(SetActionVisibilityPayload.ID, ActionBarController::handleSetVisibility);
        NetworkController.register(SetActionProgressParametersPayload.ID, ActionBarController::handleSetParameters);

        UiController.registerForScreensRemoval(ActionBarController::handleScreensRemove, true);
        OverlayScreenRegistrar.registerBeforeScreenDrawing(HintsScreen.ID, ActionBarController::update);
    }

    private static void updateCurrentlyShownAction() {
        if (visibleActions.isEmpty()) {
            currentlyShownAction = null;
            return;
        }

        if (visibleActions.size() == 1) {
            currentlyShownAction = visibleActions.iterator().next();
            return;
        }

        currentlyShownAction = visibleActions.stream().max(Comparator.comparingInt(ActionData::getPriority)).orElseThrow();
    }

    private static void handleRegister(RegisterActionPayload payload) {
        var actionId = payload.actionId();
        var priority = payload.priority();
        var text = KEY_PREFIX+payload.key();

        var actionData = switch (payload.type()) {
            case 0 -> new SimpleActionData(actionId, priority, text);
            case 1 -> new GeneratorFixActionData(actionId, priority, text);
            case 2 -> new HiddenBarActionData(actionId, priority, text);
            default -> throw new IllegalStateException("Unexpected action progress type id: " + payload.type());
        };

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

        updateCurrentlyShownAction();
    }

    private static void handleSetParameters(SetActionProgressParametersPayload payload) {
        if (currentlyShownAction != null) {
            currentlyShownAction.handle(payload);
        }
    }

    private static void handleScreensRemove() {
        registeredActions.clear();
        visibleActions.clear();
        currentlyShownAction = null;
    }

    private static void update() {
        var screen = HintsScreenController.getScreen();
        if (screen == null) return;

        if (currentlyShownAction != null) {
            screen.setProgressVisible(true);
            currentlyShownAction.update(screen);
        } else {
            screen.setProgressVisible(false);
        }
    }
}
