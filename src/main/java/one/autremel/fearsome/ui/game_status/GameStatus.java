package one.autremel.fearsome.ui.game_status;

import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.OverlayScreen;

public class GameStatus extends OverlayScreen {
    public static final Identifier ID = FearsomeMod.uiIdentifier("game_status");

    public GameStatus() {
        super("game_status");
    }

    @Override
    protected void build(FlowLayout rootComponent) {

    }
}
