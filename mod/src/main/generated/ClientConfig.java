package one.autremel.fearsome.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.ConfigWrapper.BuilderConsumer;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClientConfig extends ConfigWrapper<one.autremel.fearsome.config.ClientConfigModel> {

    public final Keys keys = new Keys();

    private final Option<one.autremel.fearsome.config.ViewMethodType> survivorListExpandMethod = this.optionForKey(this.keys.survivorListExpandMethod);
    private final Option<one.autremel.fearsome.config.ViewMethodType> generatorsHighlightMethod = this.optionForKey(this.keys.generatorsHighlightMethod);
    private final Option<java.lang.Boolean> useChatSizePreset = this.optionForKey(this.keys.useChatSizePreset);
    private final Option<java.lang.Boolean> allowMessageVerifications = this.optionForKey(this.keys.allowMessageVerifications);

    private ClientConfig() {
        super(one.autremel.fearsome.config.ClientConfigModel.class);
    }

    private ClientConfig(BuilderConsumer consumer) {
        super(one.autremel.fearsome.config.ClientConfigModel.class, consumer);
    }

    public static ClientConfig createAndLoad() {
        var wrapper = new ClientConfig();
        wrapper.load();
        return wrapper;
    }

    public static ClientConfig createAndLoad(BuilderConsumer consumer) {
        var wrapper = new ClientConfig(consumer);
        wrapper.load();
        return wrapper;
    }

    public one.autremel.fearsome.config.ViewMethodType survivorListExpandMethod() {
        return survivorListExpandMethod.value();
    }

    public void survivorListExpandMethod(one.autremel.fearsome.config.ViewMethodType value) {
        survivorListExpandMethod.set(value);
    }

    public one.autremel.fearsome.config.ViewMethodType generatorsHighlightMethod() {
        return generatorsHighlightMethod.value();
    }

    public void generatorsHighlightMethod(one.autremel.fearsome.config.ViewMethodType value) {
        generatorsHighlightMethod.set(value);
    }

    public boolean useChatSizePreset() {
        return useChatSizePreset.value();
    }

    public void useChatSizePreset(boolean value) {
        useChatSizePreset.set(value);
    }

    public boolean allowMessageVerifications() {
        return allowMessageVerifications.value();
    }

    public void allowMessageVerifications(boolean value) {
        allowMessageVerifications.set(value);
    }


    public static class Keys {
        public final Option.Key survivorListExpandMethod = new Option.Key("survivorListExpandMethod");
        public final Option.Key generatorsHighlightMethod = new Option.Key("generatorsHighlightMethod");
        public final Option.Key useChatSizePreset = new Option.Key("useChatSizePreset");
        public final Option.Key allowMessageVerifications = new Option.Key("allowMessageVerifications");
    }
}

