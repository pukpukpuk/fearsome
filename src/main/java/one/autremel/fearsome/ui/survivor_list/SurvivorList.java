package one.autremel.fearsome.ui.survivor_list;

import io.wispforest.owo.ui.component.UIComponents;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.TiledTextureComponent;
import one.autremel.fearsome.ui.OverlayScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SurvivorList extends OverlayScreen {
    public static final Identifier ID = FearsomeMod.uiIdentifier("survivor_list");
    private static final Identifier SHEET_ID = FearsomeMod.textureIdentifier("survivor_list");

    private static final int HEALTH_TEXTURE_SIZE = 12;
    private static final int ICON_SIZE = HEALTH_TEXTURE_SIZE * 2 + 1;

    private final List<TextEntry> textEntriesList;
    private final HashMap<String, Entry> survivorEntryMap = new HashMap<>();

    public SurvivorList(List<TextEntry> entries) {
        super("survivor_list");
        textEntriesList = entries;
    }



    @Override
    protected void build(FlowLayout rootComponent) {

    }

    @Override
    protected void init() {
        super.init();
        if (this.uiAdapter == null) return;

        var maxWidth = 0;

        for (var textEntry : textEntriesList) {
            var entry = addSurvivorEntry(textEntry);
            entry.setHealth(10);

            survivorEntryMap.put(entry.name(), entry);
            maxWidth = Math.max(maxWidth, entry.baseElement().width());
        }

        var size = Sizing.fixed(maxWidth);
        for (var element : survivorEntryMap.values()) {
            element.baseElement().horizontalSizing(size);
        }
    }

    private Entry addSurvivorEntry(TextEntry entry) {
        var baseElement = this.model.expandTemplate(
                FlowLayout.class,
                "survivor_entry@fearsome:survivor_list",
                Map.of("display_name", entry.name())
        );

        var texture = FearsomeMod.skinsIdentifier("survivors/"+entry.survivorKey());
        var skinIconContainer = prepareSkinIconContainer(baseElement);

        createHeadTextureComponent(texture, skinIconContainer, 0);
        createHeadTextureComponent(texture, skinIconContainer, 1);

        var healthBarProgress = createHealthBar(skinIconContainer);

        this.uiAdapter.rootComponent.childById(FlowLayout.class, "root").child(baseElement);

        var result = new Entry(entry.name(), baseElement, healthBarProgress);

        return result;
    }

    private FlowLayout prepareSkinIconContainer(FlowLayout baseElement) {
        var skinIconContainer = baseElement.childById(FlowLayout.class, "skin_icon_container");
        skinIconContainer.sizing(Sizing.fixed(ICON_SIZE));

        return skinIconContainer;
    }

    private void createHeadTextureComponent(Identifier texture, FlowLayout skinIconContainer, int layer) {
        var skinIconTexture = UIComponents.texture(
                texture,
                8 + (layer * 32), 8,
                8, 8,
                64, 64);
        skinIconTexture.sizing(Sizing.fixed(ICON_SIZE - 8));
        skinIconTexture.positioning(Positioning.relative(50, 50));

        skinIconContainer.child(skinIconTexture);
    }

    private TiledTextureComponent createHealthBar(FlowLayout skinIconContainer) {
        var barBackground = UIComponents.texture(
                SHEET_ID,
                0, HEALTH_TEXTURE_SIZE,
                HEALTH_TEXTURE_SIZE, HEALTH_TEXTURE_SIZE,
                256, 256
        );
        barBackground.positioning(Positioning.relative(50, 50));
        barBackground.sizing(Sizing.fixed(ICON_SIZE));
        skinIconContainer.child(barBackground);

        var progressComponent = new TiledTextureComponent(
                SHEET_ID,
                0, 0,
                HEALTH_TEXTURE_SIZE, HEALTH_TEXTURE_SIZE,
                256, 256
        );

        progressComponent.positioning(Positioning.relative(50, 50));
        progressComponent.sizing(Sizing.fixed(ICON_SIZE));

        skinIconContainer.child(progressComponent);
        return progressComponent;
    }
}
