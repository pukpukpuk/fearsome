package one.autremel.fearsome;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.config.ClientConfig;
import one.autremel.fearsome.config.GameConfig;
import one.autremel.fearsome.effects.EffectController;
import one.autremel.fearsome.illusions.GlowingBlocks;
import one.autremel.fearsome.illusions.GlowingEntities;
import one.autremel.fearsome.illusions.NameDisplayer;
import one.autremel.fearsome.illusions.SelectedItemDisplayer;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.input.BindingEvents;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.ping.ModPingController;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import one.autremel.fearsome.timer.TimerUpdater;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.game_state.GameStateScreen;
import one.autremel.fearsome.ui.game_state.ToolsPartController;
import one.autremel.fearsome.ui.hints.HintsScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FearsomeMod implements ClientModInitializer {
    public static final String NAMESPACE = "fearsome";
    public static final String VERSION = FabricLoader.getInstance()
            .getModContainer(NAMESPACE)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");
    public static final ClientConfig CONFIG = ClientConfig.createAndLoad();
    public static final GameConfig GAME_CONFIG = GameConfig.createAndLoad();

    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @Override
    public void onInitializeClient() {
        KeyBindings.initialize();
        BindingEvents.initialize();

        NetworkController.initialize();
        ModPingController.initialize(this::onSuccessfulHandshake, this::onDisconnectFromGame);

        WrappedEvents.initialize();
        TimerUpdater.initialize();
        EffectController.initialize();
        SelectedItemDisplayer.initialize();
        NameDisplayer.initialize();

        UiController.initialize();
    }

    private void onSuccessfulHandshake() {
        HintsScreenController.onSuccessfulHandshake();
        //OverlayScreenRegistrar.create(GameStateScreen.ID, new GameStateScreen(CharacterTeam.MURDERERS, "dream"));//TODO
    }

    private void onDisconnectFromGame() {
        UiController.reset();
        TimeSynchronizer.reset();
        HintsScreenController.reset();
        SelectedItemDisplayer.reset();
        NameDisplayer.reset();
        TimerUpdater.reset();

        ToolsPartController.reset();
        GlowingEntities.reset();
        GlowingBlocks.reset();
    }

    public static String bindingTextIdentifier(String path) {
        return NAMESPACE+".binding."+path;
    }

    public static Identifier uiIdentifier(String path) {
        return identifier("ui/"+path);
    }
    public static Identifier textureIdentifier(String path) {
        return identifier("textures/"+path+".png");
    }
    public static Identifier skinsIdentifier(String path) {
        return identifier("skins/"+path+".png");
    }

    public static Identifier identifier(String path) {
        return Identifier.of(NAMESPACE, path);
    }
}
