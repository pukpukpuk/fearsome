package one.autremel.fearsome.effects;

import lombok.Getter;
import net.minecraft.text.Text;
import one.autremel.fearsome.timer.TimerSignature;
import org.jetbrains.annotations.Nullable;

@Getter
public class DisplayedEffect {
    @Getter
    private final int uniqueId;
    private final int characterIndex;
    @Getter @Nullable
    private final TimerSignature timerSignature;
    @Nullable
    private String name;
    @Nullable
    private final Character iconSymbol;

    private Text displayNameText;

    public DisplayedEffect(int uniqueId, int characterIndex, @Nullable TimerSignature timerSignature, @Nullable String name, @Nullable Character iconSymbol) {
        this.uniqueId = uniqueId;
        this.characterIndex = characterIndex;
        this.timerSignature = timerSignature;
        this.iconSymbol = iconSymbol;

        setName(name);
    }

    public static DisplayedEffect of(AddDisplayedEffectPayload payload) {
        var characterIndex = payload.characterIndex();

        var timerKey = payload.timerKey();
        var signature = timerKey.isPresent()
                ? new TimerSignature(payload.timerKey().get(), characterIndex)
                : null;
        return new DisplayedEffect(
                payload.effectId(),
                characterIndex,
                signature,
                payload.name().orElse(null),
                payload.iconSymbol().orElse(null)
        );
    }

    void setName(String name) {
        this.name = name;
        this.displayNameText = name != null
                ? Text.of(name + (iconSymbol != null ? (" " + iconSymbol) : ""))
                : null;
    }
}
