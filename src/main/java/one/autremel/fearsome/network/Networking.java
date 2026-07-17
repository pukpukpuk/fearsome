package one.autremel.fearsome.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.survivor_list.SurvivorList;
import one.autremel.fearsome.ui.survivor_list.TextEntry;

import java.util.List;

public class Networking {
    public static void initializeHandshake() {
        PayloadTypeRegistry.playS2C().register(HandshakePayload.ID, HandshakePayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(HandshakePayload.ID, Networking::handle);

        PayloadTypeRegistry.playC2S().register(HandshakePayload.ID, HandshakePayload.CODEC);
//        ClientPlayNetworking.send(new HandshakePayload(FearsomeMod.VERSION));
        //TODO ожидание хэндшейка, выключение мода
    }

    private static void handle(HandshakePayload payload, ClientPlayNetworking.Context context) {//TODO
        var text = Text.literal(FearsomeMod.VERSION + " пакетус " + payload.serverVersion()).formatted(Formatting.BLUE);
        context.player().sendMessage(text, false);

        var list = new SurvivorList(
                List.of(
                        new TextEntry("pukpukpuk671", "buterenko"),
                        new TextEntry("debil", "nun"),
                        new TextEntry("ghghghghghggggggg", "mechanic"),
                        new TextEntry("123", "nurse")
                )
        );

        OverlayScreenRegistrar.create(SurvivorList.ID, list);
    }
}
