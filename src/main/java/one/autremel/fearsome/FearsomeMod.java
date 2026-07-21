package one.autremel.fearsome;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.ping.ModPingController;
import one.autremel.fearsome.ui.UiController;

public class FearsomeMod implements ClientModInitializer {
    public static final String NAMESPACE = "fearsome";
    public static final String VERSION = FabricLoader.getInstance()
            .getModContainer(NAMESPACE)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");

    @Override
    public void onInitializeClient() {
        NetworkController.initialize();
        ModPingController.initialize(this::onSuccessfulHandshake, this::onDisconnectFromGame);

        UiController.initialize();
    }


    private void onSuccessfulHandshake() {
        //TODO
    }

    private void onDisconnectFromGame() {
        //TODO
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
