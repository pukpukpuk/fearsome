package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.ColorableTextureComponent;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.OverlayScreen;

public class GameStateScreen extends OverlayScreen {
    public static final String NAME = "game_state_screen";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    @Getter(AccessLevel.PACKAGE)
    private GlobalInfoPart globalInfoPart;

    public GameStateScreen() {
        super(NAME);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.init();
        if (this.uiAdapter == null) return;

        var minimapRoot = rootComponent.childById(StackLayout.class, "minimap_root");
        DefaultBlurredSurface.setAsSurfaceFor(minimapRoot);

        var personalInfoRoot = rootComponent.childById(FlowLayout.class, "personal_info_root");
        DefaultBlurredSurface.setAsSurfaceFor(personalInfoRoot);

        var globalInfoRoot = rootComponent.childById(FlowLayout.class, "global_info_root");
        initializeGlobalInfo(globalInfoRoot);
    }

    private void initializeGlobalInfo(FlowLayout root) {
        DefaultBlurredSurface.setAsSurfaceFor(root);

        var timerLabel = root.childById(LabelComponent.class, "until_end_timer");

        var generatorsCountLabel = root.childById(LabelComponent.class, "generators_count");

        var questsCountLabel = root.childById(LabelComponent.class, "quests_count");
        var questsIcon = new ColorableTextureComponent(FearsomeMod.textureIdentifier("ui/icons/quest"), 7);
        ((FlowLayout)questsCountLabel.parent()).child(0, questsIcon);

        var nameColumn = root.childById(FlowLayout.class, "effect_name_column");
        var timeColumn = root.childById(FlowLayout.class, "effect_time_column");
        var effectTable = new EffectTable(nameColumn, timeColumn);

        globalInfoPart = new GlobalInfoPart(timerLabel, generatorsCountLabel, questsCountLabel, questsIcon, effectTable);
    }
}
