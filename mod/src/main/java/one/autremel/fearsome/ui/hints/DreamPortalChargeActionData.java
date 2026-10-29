package one.autremel.fearsome.ui.hints;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import one.autremel.fearsome.illusions.dream.DreamPortals;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;

public class DreamPortalChargeActionData extends ActionData {
    public static final byte ID = 4;

    private static final int CHARGING_COLOR = 0x9A9A9A;
    private static final int CHARGED_COLOR = 0xC58BE8;

    private final Text chargingText;
    private final Text chargedText;

    private int portalId = -1;

    public DreamPortalChargeActionData(int actionId, int priority, String text) {
        super(actionId, priority);
        this.chargingText = Text.translatable(text);
        this.chargedText = Text.translatable(text + ".charged");
    }

    @Override
    protected void handle(SetActionProgressParametersPayload payload) {
        var buf = new PacketByteBuf(Unpooled.wrappedBuffer(payload.data()));
        portalId = buf.readVarInt();
    }

    @Override
    public void update(HintsScreen screen) {
        setCurrentProgress(DreamPortals.getChargeProgress(portalId));
        super.update(screen);
    }

    @Override
    protected Text getText() {
        return DreamPortals.isCharged(portalId) ? chargedText : chargingText;
    }

    @Override
    protected void onUpdateInternal(HintsScreen screen) {
        super.onUpdateInternal(screen);
        screen.setProgressBarColor(DreamPortals.isCharged(portalId) ? CHARGED_COLOR : CHARGING_COLOR);
    }
}
