package one.autremel.fearsome.ui.survivor_list;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.UIComponents;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.*;
import lombok.Getter;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.*;
import one.autremel.fearsome.ui.components.ColorableTextureComponent;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.components.TiledTextureComponent;

import java.util.*;

public class SurvivorList extends OverlayScreen {
    public static final String NAME = "survivor_list";
    public static final String ENTRY_TEMPLATE_ID = "survivor_entry@fearsome:"+NAME;
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    private static final Identifier SHEET_ID = FearsomeMod.textureIdentifier("ui/survivor_list");
    private static final int HEALTH_TEXTURE_SIZE = 12;
    private static final int ICON_SIZE = 18;

    private TextEntry[] textEntries;
    private final Entry[] entries;
    private TextBlocksResizer textBlocksResizer;

    private final List<Entry> unmodifiableEntryList;

    @Getter
    private boolean isCollapsed;

    public SurvivorList(TextEntry[] textEntries) {
        super(NAME);
        this.textEntries = textEntries;

        entries = new Entry[textEntries.length];
        unmodifiableEntryList = Collections.unmodifiableList(Arrays.asList(entries));
    }

    public Entry getEntry(int characterIndex) {
        return entries[characterIndex - 1];
    }

    public List<Entry> getEntries() {
        return unmodifiableEntryList;
    }

    public void setCollapsed(boolean isCollapsed) {
        if (this.isCollapsed == isCollapsed) return;
        this.isCollapsed = isCollapsed;

        textBlocksResizer.setExpandedState(isCollapsed);
    }


    @Override
    protected void buildInternal(FlowLayout rootComponent) {
        for (int i = 0; i < textEntries.length; i++) {
            var entry = addSurvivorEntry(textEntries[i]);
            entries[i] = entry;
        }

        textEntries = null;

        textBlocksResizer = new TextBlocksResizer(entries);
        textBlocksResizer.calculateMaxWidth();
        textBlocksResizer.setExpandedState(false);
    }

    private Entry addSurvivorEntry(TextEntry entry) {
        var baseElement = this.model.expandTemplate(
                FlowLayout.class,
                ENTRY_TEMPLATE_ID,
                Map.of("display_name", entry.name())
        );

        var surface = DefaultBlurredSurface.setAsSurfaceFor(baseElement, true);
        baseElement.verticalSizing(Sizing.fixed(ICON_SIZE + baseElement.padding().get().vertical()));

        var texture = FearsomeMod.textureIdentifier("skins/survivors/"+entry.survivorKey());
        var skinIconContainer = prepareSkinIconContainer(baseElement);

        addHeadIcon(texture, skinIconContainer, 0);
        addHeadIcon(texture, skinIconContainer, 1);
        var healthBarProgress = createHealthBar(skinIconContainer);
        var deadMarkTexture = addDeadMarkTexture(skinIconContainer);

        this.uiAdapter.rootComponent.childById(FlowLayout.class, "root").child(baseElement);
        return new Entry(
                entry.name(),
                baseElement,
                healthBarProgress,
                deadMarkTexture,
                baseElement.childById(FlowLayout.class, "text_block"),
                baseElement.childById(LinearBar.class, "death_timer"),
                baseElement.childById(LabelComponent.class, "effects_line"),
                surface,
                createNotificationsContainer(baseElement)
        );
    }

    private StackLayout prepareSkinIconContainer(FlowLayout baseElement) {
        var skinIconContainer = baseElement.childById(StackLayout.class, "skin_icon_container");
        skinIconContainer.sizing(Sizing.fixed(ICON_SIZE));

        return skinIconContainer;
    }

    private void addHeadIcon(Identifier texture, StackLayout skinIconContainer, int layer) {
        var skinIconTexture = UIComponents.texture(
                texture,
                8 + (layer * 32), 8,
                8, 8,
                64, 64);
        skinIconTexture.margins(Insets.of(avatarMargin()));
        skinIconTexture.sizing(Sizing.fill());

        skinIconContainer.child(skinIconTexture);
    }

    private ColorableTextureComponent addDeadMarkTexture(StackLayout skinIconContainer) {
        var deadMarkTexture = new ColorableTextureComponent(
                SHEET_ID,
                0, HEALTH_TEXTURE_SIZE*2,
                8, 8,
                256, 256
        );
        deadMarkTexture.margins(Insets.of(avatarMargin()));
        deadMarkTexture.sizing(Sizing.fill());

        deadMarkTexture.setBlend(true);
        skinIconContainer.child(deadMarkTexture);

        return deadMarkTexture;
    }

    private TiledTextureComponent createHealthBar(StackLayout skinIconContainer) {
        var progressComponent = new TiledTextureComponent(
                SHEET_ID,
                HEALTH_TEXTURE_SIZE, HEALTH_TEXTURE_SIZE,
                256, 256
        );

        progressComponent.sizing(Sizing.fill());

        skinIconContainer.child(progressComponent);
        return progressComponent;
    }

    private NotificationsContainer createNotificationsContainer(FlowLayout baseElement) {
        var result = new NotificationsContainer();
        result.positioning(Positioning.relative(0, 50));

        baseElement.child(result);
        return result;
    }

    private static int avatarMargin() {
        return Math.ceilDiv(2 * ICON_SIZE, HEALTH_TEXTURE_SIZE);
    }
}
