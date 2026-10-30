package one.autremel.fearsome.ui.menus;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class InventoryMenuScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/generic_54.png");
    private static final int TEXTURE_SIZE = 256;
    private static final int WIDTH = 176;
    private static final int HEADER_HEIGHT = 17;
    private static final int FOOTER_HEIGHT = 7;
    private static final int FOOTER_V = 215;
    private static final int SLOT_SIZE = 18;
    private static final int ITEM_SIZE = 16;
    private static final int SLOTS_X = 8;
    private static final int SLOTS_Y = 18;
    private static final int TITLE_X = 8;
    private static final int TITLE_Y = 6;
    private static final int TITLE_COLOR = 0xFF404040;
    private static final Identifier SLOT_HIGHLIGHT_BACK = Identifier.ofVanilla("container/slot_highlight_back");
    private static final Identifier SLOT_HIGHLIGHT_FRONT = Identifier.ofVanilla("container/slot_highlight_front");
    private static final int HIGHLIGHT_OFFSET = 4;
    private static final int HIGHLIGHT_SIZE = 24;
    private static final float TOOLTIP_SCALE = 0.65f;
    private static final TooltipPositioner SCALED_POSITIONER = (screenWidth, screenHeight, x, y, width, height) ->
            HoveredTooltipPositioner.INSTANCE.getPosition(
                    Math.round(screenWidth / TOOLTIP_SCALE), Math.round(screenHeight / TOOLTIP_SCALE),
                    x, y, width, height);

    protected static final int COLUMNS = 9;

    protected final int rows;
    private final MenuSlot[] slots;
    private Text menuTitle = Text.empty();
    private int backSlot = -1, mainSlot = -1;

    private int left, top;

    protected InventoryMenuScreen(int rows) {
        super(Text.empty());
        this.rows = rows;
        this.slots = new MenuSlot[rows * COLUMNS];
    }

    protected abstract void build();

    protected abstract void onSlotClick(int slot, boolean isRightClick);

    public final void refresh() {
        Arrays.fill(slots, null);
        build();
    }

    protected final void set(int slot, @Nullable MenuSlot menuSlot) {
        if (slot >= 0 && slot < slots.length) slots[slot] = menuSlot;
    }

    protected final void setMenuTitle(Text title) {
        menuTitle = title;
    }

    protected final void setNavigation(int backSlot, int mainSlot) {
        this.backSlot = backSlot;
        this.mainSlot = mainSlot;
        set(backSlot, MenuTexts.back());
        set(mainSlot, MenuTexts.main());
    }

    protected static int slotOf(int row, int column) {
        return row * COLUMNS + column;
    }

    @Override
    protected void init() {
        left = (width - WIDTH) / 2;
        top = (height - getMenuHeight()) / 2;
        refresh();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        var slotsHeight = rows * SLOT_SIZE;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, left, top, 0, 0, WIDTH, HEADER_HEIGHT + slotsHeight, TEXTURE_SIZE, TEXTURE_SIZE);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, left, top + HEADER_HEIGHT + slotsHeight, 0, FOOTER_V, WIDTH, FOOTER_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        context.drawText(textRenderer, menuTitle, left + TITLE_X, top + TITLE_Y, TITLE_COLOR, false);

        var hovered = getSlotAt(mouseX, mouseY);
        if (hovered != -1) drawHighlight(context, SLOT_HIGHLIGHT_BACK, hovered);

        for (int i = 0; i < slots.length; i++) {
            var slot = slots[i];
            if (slot != null) context.drawItem(slot.icon(), getSlotX(i), getSlotY(i));
        }

        if (hovered != -1) {
            drawHighlight(context, SLOT_HIGHLIGHT_FRONT, hovered);
            if (slots[hovered] != null && !slots[hovered].tooltip().isEmpty()) {
                drawTooltip(context, slots[hovered].tooltip(), mouseX, mouseY);
            }
        }
    }

    private void drawTooltip(DrawContext context, List<Text> tooltip, int mouseX, int mouseY) {
        var components = new ArrayList<TooltipComponent>(tooltip.size());
        for (var line : tooltip) components.add(TooltipComponent.of(line.asOrderedText()));

        context.createNewRootLayer();
        var matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.scale(TOOLTIP_SCALE, TOOLTIP_SCALE);

        var x = Math.round(mouseX / TOOLTIP_SCALE);
        var y = Math.round(mouseY / TOOLTIP_SCALE);
        context.drawTooltipImmediately(textRenderer, components, x, y, SCALED_POSITIONER, null);

        matrices.popMatrix();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        var slot = getSlotAt(click.x(), click.y());
        if (slot == -1) return super.mouseClicked(click, doubled);

        if (slot == backSlot) {
            MenuSound.CLICK.play();
            MenuNavigator.back();
        } else if (slot == mainSlot) {
            MenuSound.CLICK.play();
            MenuNavigator.toRoot();
        } else {
            onSlotClick(slot, click.button() == 1);
        }
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        MenuNavigator.clear();
        super.close();
    }

    private void drawHighlight(DrawContext context, Identifier sprite, int slot) {
        var x = getSlotX(slot) - HIGHLIGHT_OFFSET;
        var y = getSlotY(slot) - HIGHLIGHT_OFFSET;
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, sprite, x, y, HIGHLIGHT_SIZE, HIGHLIGHT_SIZE);
    }

    private int getMenuHeight() {
        return HEADER_HEIGHT + rows * SLOT_SIZE + FOOTER_HEIGHT;
    }

    private int getSlotX(int slot) {
        return left + SLOTS_X + (slot % COLUMNS) * SLOT_SIZE;
    }

    private int getSlotY(int slot) {
        return top + SLOTS_Y + (slot / COLUMNS) * SLOT_SIZE;
    }

    private int getSlotAt(double mouseX, double mouseY) {
        for (int i = 0; i < slots.length; i++) {
            var x = getSlotX(i);
            var y = getSlotY(i);
            if (mouseX >= x && mouseX < x + ITEM_SIZE && mouseY >= y && mouseY < y + ITEM_SIZE) return i;
        }
        return -1;
    }
}
