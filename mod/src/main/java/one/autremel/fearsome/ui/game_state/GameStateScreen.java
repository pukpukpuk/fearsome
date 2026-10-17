package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.OverlayScreen;
import org.jetbrains.annotations.Nullable;

public class GameStateScreen extends OverlayScreen {
    public static final String NAME = "game_state_screen";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    @Getter(AccessLevel.PACKAGE) @Nullable
    private AbilitiesPart abilitiesPart;
    @Getter(AccessLevel.PACKAGE)
    private GlobalInfoPart globalInfoPart;
    @Getter(AccessLevel.PACKAGE)
    private ToolsPart toolsPart;

    private final CharacterTeam team;
    @Nullable
    private final String characterKey;

    public GameStateScreen(CharacterTeam team, @Nullable String characterKey) {
        super(NAME);
        this.team = team;
        this.characterKey = characterKey;
    }

    @Override
    protected void buildInternal(FlowLayout rootComponent) {
        var minimapRoot = rootComponent.childById(StackLayout.class, "minimap_root");
        DefaultBlurredSurface.setAsSurfaceFor(minimapRoot);

        var abilitiesRoot = rootComponent.childById(FlowLayout.class, "abilities_root");
        if (characterKey != null) {
            abilitiesPart = AbilitiesPart.initialize(model, abilitiesRoot, team, characterKey);
        } else {
            abilitiesRoot.remove();
        }

        var globalInfoRoot = rootComponent.childById(FlowLayout.class, "global_info_root");
        globalInfoPart = GlobalInfoPart.initialize(globalInfoRoot);

        var gameToolsRoot = rootComponent.childById(FlowLayout.class, "game_tools_root");
        toolsPart = ToolsPart.initialize(gameToolsRoot);
    }
}
