package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.parsing.UIModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.ColorableTextureComponent;
import one.autremel.fearsome.ui.components.HideableStackLayout;
import one.autremel.fearsome.ui.components.RadialProgressBar;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AbilitiesPart {
    private static final HashMap<String, List<Identifier>> cachedSprites = new HashMap<>();

    private final ArrayList<AbilityCard> abilityCards = new ArrayList<>();

    static AbilitiesPart initialize(UIModel model, FlowLayout root, CharacterTeam team, String characterKey) {
        DefaultBlurredSurface.setAsSurfaceFor(root);

        var result = new AbilitiesPart();

        var sprites = getSprites(team, characterKey);
        for (var sprite : sprites) {
            var card = createAbilityCard(model, root, team, sprite);
            result.abilityCards.add(card);
        }

        return result;
    }

    private static List<Identifier> getSprites(CharacterTeam team, String characterKey) {
        var cached = cachedSprites.get(characterKey);
        if (cached != null) return cached;

        var manager = MinecraftClient.getInstance().getResourceManager();
        var path = String.format("ui/abilities/%s/%s", team.getFolderName(), characterKey);

        var singleIdentifier = FearsomeMod.textureIdentifier(path);
        if (manager.getResource(singleIdentifier).isPresent()) return Collections.singletonList(singleIdentifier);

        var result = new ArrayList<Identifier>();
        for (var i = 1; i <= 2; i++) {
            var id = FearsomeMod.textureIdentifier(path + "/" + i);
            result.add(id);
        }

        cachedSprites.put(characterKey, result);
        return result;
    }

    private static AbilityCard createAbilityCard(UIModel model, FlowLayout root, CharacterTeam team, Identifier texture) {
        var cardRoot = model.expandTemplate(
                StackLayout.class,
                "ability_card@fearsome:game_state_screen",
                Collections.emptyMap()
        );

        var bar = cardRoot.childById(RadialProgressBar.class, "bar");
        var margin = bar.width() + DefaultBlurredSurface.MARGIN;
        var side = cardRoot.horizontalSizing().get().value - 4*margin;
        var icon = new ColorableTextureComponent(texture, side);
        icon.sizing(Sizing.fill());
        icon.margins(Insets.of(margin));
        cardRoot.child(1, icon);

        var hideableCountRoot = cardRoot.childById(HideableStackLayout.class, "ability_count_root");
        hideableCountRoot.surface(Surface.flat(team.getAbilityCountNumberBackgroundColor()));

        var countLabel = cardRoot.childById(LabelComponent.class, "ability_count");

        root.child(cardRoot);
        var card = new AbilityCard(team, bar, hideableCountRoot, countLabel, icon);
        card.setActiveState(true);

        return card;
    }

    @Nullable
    public AbilityCard getCard(int index) {
        return index >= 0 && index < abilityCards.size()
                ? abilityCards.get(index)
                : null;
    }
}
