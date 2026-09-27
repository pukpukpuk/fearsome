package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.hints.payloads.RegisterActionControlPayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionControlActiveStatePayload;
import one.autremel.fearsome.ui.hints.payloads.SetActionControlCooldownPayload;
import one.autremel.fearsome.ui.hints.payloads.UnregisterAllActionControlsPayload;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

class ActionControlsListController {
    private static final int ABSOLUTE_MAX_CONTROL_ID = 63;
    private static final String TRANSLATION_KEY_PREFIX = "fearsome.action.control.";

    private static boolean initialized;

    private static final ControlData[] registeredControls = new ControlData[ABSOLUTE_MAX_CONTROL_ID + 1];
    private static final SortedSet<ActionControlMeta> availableControls = new TreeSet<>(
            Comparator.comparingInt(ActionControlMeta::priority).thenComparingInt(Record::hashCode)
    );
    private static final ConcurrentLinkedQueue<ControlData> controlsOnCooldown = new ConcurrentLinkedQueue<>();

    private static final BitSet bindingIdsDrawnStateSet = new BitSet(BindingId.BINDINGS_COUNT);

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(RegisterActionControlPayload.ID, ActionControlsListController::handleRegisterControl);
        NetworkController.register(UnregisterAllActionControlsPayload.ID, ActionControlsListController::handleUnregisterAllControls);

        NetworkController.register(SetActionControlActiveStatePayload.ID, ActionControlsListController::handleActiveStateSet);
        NetworkController.register(SetActionControlCooldownPayload.ID, ActionControlsListController::handleCooldownSet);

        OverlayScreenRegistrar.registerBeforeScreenDrawing(HintsScreen.ID, ActionControlsListController::beforeScreenDraw);
    }

    public static void resetControls() {
        for (var data : registeredControls) {
            if (data != null) {
                data.reset();
            }
        }
        availableControls.clear();
        controlsOnCooldown.clear();
    }

    private static void beforeScreenDraw() {
        for (var data : controlsOnCooldown) {
            if (!data.isOnCooldown()) {
                data.checkAvailability();
                controlsOnCooldown.remove(data);
            }
        }
    }

    private static void updateShownControls() {
        HintsScreenController.acceptData(data -> {
            bindingIdsDrawnStateSet.clear();

            var screen = data.screen();
            screen.prepareForControlListRedraw();

            var index = 0;
            for (var control : availableControls) {
                var bindingId = control.bindingId();
                if (bindingIdsDrawnStateSet.get(bindingId.getByteId())) continue;
                bindingIdsDrawnStateSet.set(bindingId.getByteId());

                var keyBinding = KeyBindings.getData(bindingId).keyBinding();
                var label = screen.getLabel(index++);

                label.setBindingKey(keyBinding.getBoundKeyTranslationKey());
                label.setText(control.text());
                label.setVisible(true);
            }
        });
    }

    @Nullable
    private static ControlData getDataOrLogError(int controlId) {
        var data = registeredControls[controlId];
        if (data == null || data.getMeta() == null) {
            FearsomeMod.LOGGER.error(
                    "Server tried to address unregistered control with id {}! Ignoring...",
                    controlId
            );
            return null;
        }

        return data;
    }


    private static void onControlAvailabilityChange(ControlData controlData, boolean isAvailableNow) {
        if (isAvailableNow) availableControls.add(controlData.getMeta());
        else availableControls.remove(controlData.getMeta());

        updateShownControls();
    }

    private static void handleRegisterControl(RegisterActionControlPayload payload) {
        var controlId = payload.controlId();
        if (controlId > ABSOLUTE_MAX_CONTROL_ID) {
            FearsomeMod.LOGGER.error("Server tried to register control with too large id: {}. Ignoring...", controlId);
            return;
        }

        var data = registeredControls[controlId];
        if (data == null) {
            registeredControls[controlId] = data = new ControlData();
        } else if (data.meta != null) {
            FearsomeMod.LOGGER.warn(
                    "Server tried to register control over already occupied id: {}, {}. Removing the previous control...",
                    data.meta.text().getContent() instanceof TranslatableTextContent translatableTextContent
                            ? translatableTextContent.getKey()
                            : data.meta.text().getContent(),
                    controlId
            );
            data.reset();
        }

        var meta = new ActionControlMeta(
                payload.bindingId(),
                Text.translatable(TRANSLATION_KEY_PREFIX + payload.translationKey()),
                payload.priority()
        );
        data.setMeta(meta);
    }

    private static void handleUnregisterAllControls(UnregisterAllActionControlsPayload payload) {
        resetControls();
    }


    private static void handleActiveStateSet(SetActionControlActiveStatePayload payload) {
        var data = getDataOrLogError(payload.controlId());
        if (data == null) return;

        data.setActive(payload.isActive());
    }

    private static void handleCooldownSet(SetActionControlCooldownPayload payload) {
        var data = getDataOrLogError(payload.controlId());
        if (data == null) return;

        data.setOnCooldownUntil(payload.onCooldownUntil());
        if (data.isOnCooldown()) controlsOnCooldown.add(data);
    }

    private static class ControlData {
        @Getter
        private ActionControlMeta meta;
        private boolean isActive;
        private long onCooldownUntil;

        private boolean
                lastAvailabilityState,
                isForceAvailabilityChangeRequired = true;

        public void reset() {
            meta = null;
            isActive = false;
            onCooldownUntil = 0;
            isForceAvailabilityChangeRequired = true;
        }

        public void setMeta(ActionControlMeta meta) {
            Preconditions.checkState(this.meta == null);
            this.meta = meta;
        }


        public boolean isAvailable() {
            return isActive && !isOnCooldown();
        }

        public void checkAvailability() {
            var isAvailableNow = isAvailable();
            if (!isForceAvailabilityChangeRequired && isAvailableNow == lastAvailabilityState) return;

            isForceAvailabilityChangeRequired = false;
            lastAvailabilityState = isAvailableNow;
            ActionControlsListController.onControlAvailabilityChange(this, isAvailableNow);
        }

        public void setActive(boolean isActive) {
            this.isActive = isActive;
            checkAvailability();
        }

        public void setOnCooldownUntil(long onCooldownUntil) {
            this.onCooldownUntil = onCooldownUntil;
            checkAvailability();
        }

        private boolean isOnCooldown() {
            return System.currentTimeMillis() <= onCooldownUntil;
        }
    }
}
