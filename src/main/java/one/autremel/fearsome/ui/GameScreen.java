package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import one.autremel.fearsome.FearsomeMod;

public class GameScreen extends BaseUIModelScreen<FlowLayout> {
    protected GameScreen() {
        super(FlowLayout.class, DataSource.asset(FearsomeMod.createIdentifier("game_screen_model")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {

    }
}
