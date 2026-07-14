package one.autremel.fearsome;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;


public class FearsomeMod implements ClientModInitializer {
    public static final String NAMESPACE = "fearsome";

    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(Payload.ID, Payload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(Payload.ID, this::handle);
    }

    private void handle(Payload payload, ClientPlayNetworking.Context context) {
        var text = Text.literal("пакетус " + payload.aboba()).formatted(Formatting.BLUE);
        context.player().sendMessage(text, false);
    }

    public static Identifier createIdentifier(String path) {
        return Identifier.of(NAMESPACE, path);
    }
}
