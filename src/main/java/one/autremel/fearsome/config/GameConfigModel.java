package one.autremel.fearsome.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Sync;

@Config(name = "fear-game-config", wrapperName = "GameConfig")
public class GameConfigModel {
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public int deathTimerInitialValue;
}
