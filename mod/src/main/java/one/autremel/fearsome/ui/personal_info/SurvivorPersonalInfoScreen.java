package one.autremel.fearsome.ui.personal_info;

import io.wispforest.owo.ui.container.FlowLayout;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.OverlayScreen;
import one.autremel.fearsome.ui.components.HideableFlowLayout;
import one.autremel.fearsome.ui.terror.TerrorPart;

public class SurvivorPersonalInfoScreen extends OverlayScreen {
    public static final String NAME = "survivor_personal_info";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    @Getter(AccessLevel.PACKAGE)
    private DeathTimerPart deathTimerPart;
    @Getter(AccessLevel.PACKAGE)
    private TerrorPart terrorPart;

    public SurvivorPersonalInfoScreen() {
        super(NAME);
    }

    @Override
    protected void buildInternal(FlowLayout rootComponent) {
        var deathTimerRoot = rootComponent.childById(HideableFlowLayout.class, "death_timer_root");
        deathTimerPart = DeathTimerPart.initialize(deathTimerRoot);

        var terrorRoot = rootComponent.childById(HideableFlowLayout.class, "terror_root");
        terrorPart = TerrorPart.initialize(terrorRoot);
    }
}
