package one.autremel.fearsome;

import lombok.Getter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public enum GameEndCause {
    MANUAL_STOP("fearsome.ui.title.cause.manual_stop", null),

    MURDERER_TIME("fearsome.ui.title.cause.murderer.time", true),
    MURDERER_KILLS("fearsome.ui.title.cause.murderer.kills", true),
    MURDERER_SURVIVORS_LEFT("fearsome.ui.title.cause.murderer.survivors_left", true),

    SURVIVORS_GENERATORS("fearsome.ui.title.cause.survivors.generators", false),
    SURVIVORS_MURDERER_LEFT("fearsome.ui.title.cause.survivors.murderer_left", false),
    SURVIVORS_MURDERER_DROWNED("fearsome.ui.title.cause.survivors.murderer_drowned", false),
    ;


    private static final Text
            VICTORY_TEXT = Text.translatable("fearsome.ui.title.outcome.victory"),
            DEFEAT_TEXT = Text.translatable("fearsome.ui.title.outcome.defeat"),
            DRAW_TEXT = Text.translatable("fearsome.ui.title.outcome.draw");


    private static final Text MURDERER_VICTORY = wrap(true, CharacterTeam.MURDERERS);
    private static final Text MURDERER_DEFEAT = wrap(false, CharacterTeam.MURDERERS);
    private static final Text SURVIVOR_VICTORY = wrap(false, CharacterTeam.SURVIVORS);
    private static final Text SURVIVOR_DEFEAT = wrap(true, CharacterTeam.SURVIVORS);
    private static final Text DRAW = wrap(null, CharacterTeam.MURDERERS);


    @Getter
    private final Text cause;
    @Nullable
    private final Boolean isPositiveForMurderer;

    GameEndCause(String causeKey, @Nullable Boolean isPositiveForMurderer) {
        cause = Text.translatable(causeKey);
        this.isPositiveForMurderer = isPositiveForMurderer;
    }

    private static Text wrap(@Nullable Boolean isPositiveForMurderer, CharacterTeam targetTeam) {
        var icon = "⏺";
        var style = Palette.WARNING_STYLE;
        var outcome = DRAW_TEXT;
        if (isPositiveForMurderer != null) {
            icon = isPositiveForMurderer ? "☠" : "★";
            if (isPositiveForMurderer == (targetTeam == CharacterTeam.MURDERERS)) {
                style = Palette.SUCCESS_STYLE;
                outcome = VICTORY_TEXT;
            } else {
                style = Palette.ERROR_STYLE;
                outcome = DEFEAT_TEXT;
            }
        }

        return Text.literal(icon)
                .append(" ")
                .append(outcome)
                .append(" ")
                .append(icon)
                .setStyle(style);
    }

    public Text getOutcome(CharacterTeam team) {
        if (isPositiveForMurderer == null) return DRAW;

        var isMurderer = team == CharacterTeam.MURDERERS;
        if (isPositiveForMurderer) return isMurderer ? MURDERER_VICTORY : SURVIVOR_DEFEAT;
        return isMurderer ? MURDERER_DEFEAT : SURVIVOR_VICTORY;
    }
}
