package one.autremel.fearsome.ui.survivor_list;

import net.minecraft.text.Text;
import one.autremel.fearsome.effects.DisplayedEffect;
import one.autremel.fearsome.effects.EffectController;
import one.autremel.fearsome.effects.EffectVisiblePart;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.game_state.GameStateScreen;

class EffectsPartController {
    private static int ADDITION_COLOR = 0xFF98D895;
    private static int REMOVAL_COLOR = 0xFFD6A38F;

    static void initialize() {
        EffectController.subscribeToEffectAddition(e -> onEffect(e, true));
        EffectController.subscribeToEffectRemoval(e -> onEffect(e, false));

        EntryPinger.initialize();

        OverlayScreenRegistrar.registerBeforeScreenDrawing(GameStateScreen.ID, EffectsPartController::update);
    }

    private static void updateEffectsLine(int characterIndex, Entry entry) {
        var effects = EffectController.getEffects(characterIndex, EffectVisiblePart.ICON);
        var chars = new char[effects.size()];
        var index = 0;
        for (var effect : effects) {
            chars[index++] = effect.getIconSymbol();
        }

        entry.effectsLine().text(Text.of(new String(chars)));
    }

    private static void onEffect(DisplayedEffect effect, boolean isAdded) {
        SurvivorListController.acceptList(list -> {
            var icon = effect.getIconSymbol();
            if (icon == null) return;

            var characterIndex = effect.getCharacterIndex();
            var entry = list.getEntry(characterIndex);
            updateEffectsLine(characterIndex, entry);

            EntryPinger.ping(entry);

            var textString = (isAdded ? "+" : "-") + icon;
            var color = isAdded ? ADDITION_COLOR : REMOVAL_COLOR;
            entry.notificationsContainer().add(Text.of(textString), color);
        });
    }

    private static void update() {
        var list = SurvivorListController.getListNow();
        if (list == null) return;

        list.getEntries().forEach(entry -> entry.notificationsContainer().update());
    }
}
