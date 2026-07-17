package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;

public abstract class OverlayScreen extends BaseUIModelScreen<FlowLayout> {
    @Getter @Setter
    private boolean isVisible;

    protected OverlayScreen(String path) {
        super(FlowLayout.class, FearsomeMod.identifier(path));
    }

    public final OwoUIAdapter<FlowLayout> getAdapter() {
        return (OwoUIAdapter<FlowLayout>)getFocused();
    }
}
