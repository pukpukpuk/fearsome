package one.autremel.fearsome.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.ConfigWrapper.BuilderConsumer;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GameConfig extends ConfigWrapper<one.autremel.fearsome.config.GameConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Integer> deathTimerInitialValue = this.optionForKey(this.keys.deathTimerInitialValue);

    private GameConfig() {
        super(one.autremel.fearsome.config.GameConfigModel.class);
    }

    private GameConfig(BuilderConsumer consumer) {
        super(one.autremel.fearsome.config.GameConfigModel.class, consumer);
    }

    public static GameConfig createAndLoad() {
        var wrapper = new GameConfig();
        wrapper.load();
        return wrapper;
    }

    public static GameConfig createAndLoad(BuilderConsumer consumer) {
        var wrapper = new GameConfig(consumer);
        wrapper.load();
        return wrapper;
    }

    public int deathTimerInitialValue() {
        return deathTimerInitialValue.value();
    }

    public void deathTimerInitialValue(int value) {
        deathTimerInitialValue.set(value);
    }


    public static class Keys {
        public final Option.Key deathTimerInitialValue = new Option.Key("deathTimerInitialValue");
    }
}

