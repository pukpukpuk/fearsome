package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.ParentUIComponent;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.ColorableTextureComponent;
import one.autremel.fearsome.ui.DefaultSurface;
import one.autremel.fearsome.ui.OverlayScreen;

public class GameStateScreen extends OverlayScreen {
    public static final Identifier ID = FearsomeMod.uiIdentifier("game_state_screen");

    @Getter(AccessLevel.PACKAGE)
    private GlobalInfoPart globalInfoPart;

    public GameStateScreen() {
        super("game_state_screen");
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.init();
        if (this.uiAdapter == null) return;

        var minimapRoot = rootComponent.childById(StackLayout.class, "minimap_root");
        DefaultSurface.setAsSurfaceFor(minimapRoot);

        var personalInfoRoot = rootComponent.childById(FlowLayout.class, "personal_info_root");
        DefaultSurface.setAsSurfaceFor(personalInfoRoot);

        var globalInfoRoot = rootComponent.childById(FlowLayout.class, "global_info_root");
        initializeGlobalInfo(globalInfoRoot);
    }

    private void initializeGlobalInfo(FlowLayout root) {
        DefaultSurface.setAsSurfaceFor(root);

        var timerLabel = root.childById(LabelComponent.class, "until_end_timer");

        var generatorsCountLabel = root.childById(LabelComponent.class, "generators_count");

        var questsCountLabel = root.childById(LabelComponent.class, "quests_count");
        var questsIcon = new ColorableTextureComponent(FearsomeMod.textureIdentifier("ui/icons/quest"), 7);
        ((FlowLayout)questsCountLabel.parent()).child(0, questsIcon);

        globalInfoPart = new GlobalInfoPart(timerLabel, generatorsCountLabel, questsCountLabel, questsIcon);
    }
}
