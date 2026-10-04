package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import lombok.Getter;
import lombok.Setter;
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

    @Override
    protected final void build(FlowLayout rootComponent) {
        super.init();
        if (this.uiAdapter == null) return;

        buildInternal(rootComponent);
    }

    protected abstract void buildInternal(FlowLayout rootComponent);
}
