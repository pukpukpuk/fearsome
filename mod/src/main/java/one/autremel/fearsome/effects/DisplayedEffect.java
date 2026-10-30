package one.autremel.fearsome.effects;

import lombok.Getter;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import one.autremel.fearsome.text.FearText;
import one.autremel.fearsome.timer.TimerSignature;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Getter
public class DisplayedEffect {
    @Getter
    private final int uniqueId;
    private final int characterIndex;
    @Getter @Nullable
    private final TimerSignature timerSignature;
    @Nullable
    private final String nameKey;
    @Nullable
    private final Character iconSymbol;

    private Text displayNameText;

    public DisplayedEffect(int uniqueId, int characterIndex, @Nullable TimerSignature timerSignature, @Nullable String nameKey, List<Integer> nameArgs, @Nullable Character iconSymbol) {
        this.uniqueId = uniqueId;
        this.characterIndex = characterIndex;
        this.timerSignature = timerSignature;
        this.nameKey = nameKey;
        this.iconSymbol = iconSymbol;

        setNameArgs(nameArgs);
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
                payload.nameKey().orElse(null),
                payload.nameArgs(),
                payload.iconSymbol().orElse(null)
        );
    }

    void setNameArgs(List<Integer> nameArgs) {
        if (nameKey == null) {
            displayNameText = null;
            return;
        }

        var params = new HashMap<String, Object>();
        for (int i = 0; i < nameArgs.size(); i++) {
            params.put(String.valueOf(i), nameArgs.get(i));
        }

        var text = FearText.format(nameKey, params, Style.EMPTY);
        if (iconSymbol != null) text.append(" " + iconSymbol);
        displayNameText = text;
    }
}
