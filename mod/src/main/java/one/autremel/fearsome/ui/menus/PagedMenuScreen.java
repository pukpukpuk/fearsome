package one.autremel.fearsome.ui.menus;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import one.autremel.fearsome.CharacterTeam;

import java.util.*;

public abstract class PagedMenuScreen<T> extends InventoryMenuScreen {
    private static final int GRID_COLUMN = 2;
    private static final int GRID_COLUMNS = 5;
    private static final int GRID_ROWS = 3;
    private static final int PER_PAGE = GRID_COLUMNS * GRID_ROWS;

    private static final FilterType[] FILTERS = FilterType.values();
    private static final SortType[] SORTS = SortType.values();
    private static final HashMap<String, PagedState> states = new HashMap<>();

    protected final CharacterTeam team;
    protected final int headSlot;
    private final int gridRow;
    private final int previousSlot, nextSlot, filterSlot, sortSlot, backSlot, mainSlot;
    private final PagedState state;

    private final Int2ObjectOpenHashMap<T> slotElements = new Int2ObjectOpenHashMap<>();
    private int pagesCount;

    protected PagedMenuScreen(int rows, int gridRow, CharacterTeam team, String stateKey) {
        super(rows);
        this.team = team;
        this.gridRow = gridRow;

        var bottomRow = rows - 1;
        previousSlot = slotOf(gridRow + 1, 1);
        nextSlot = slotOf(gridRow + 1, 7);
        backSlot = slotOf(bottomRow, 0);
        filterSlot = slotOf(bottomRow, 3);
        headSlot = slotOf(bottomRow, 4);
        sortSlot = slotOf(bottomRow, 5);
        mainSlot = slotOf(bottomRow, 8);

        state = states.computeIfAbsent(stateKey + "." + team.name(), key -> new PagedState());
    }

    protected abstract List<T> getElements();

    protected abstract boolean isAvailable(T element);

    protected abstract int getId(T element);

    protected abstract String getName(T element);

    protected abstract int getSlotsCost(T element);

    protected abstract MenuSlot createSlot(T element);

    protected abstract void onElementClick(T element, boolean isRightClick);

    protected abstract Text createTitle(int page, int pagesCount);

    protected abstract MenuSlot createHead();

    protected void buildExtra() {}

    protected void onOtherSlotClick(int slot, boolean isRightClick) {}

    @Override
    protected final void build() {
        var elements = getSortedElements();
        pagesCount = Math.max(1, Math.ceilDiv(elements.size(), PER_PAGE));
        state.page = Math.clamp(state.page, 0, pagesCount - 1);

        slotElements.clear();
        var start = state.page * PER_PAGE;
        var end = Math.min(start + PER_PAGE, elements.size());
        for (int i = start; i < end; i++) {
            var index = i - start;
            var slot = slotOf(gridRow + index / GRID_COLUMNS, GRID_COLUMN + index % GRID_COLUMNS);

            var element = elements.get(i);
            slotElements.put(slot, element);
            set(slot, createSlot(element));
        }

        if (state.page > 0) set(previousSlot, MenuTexts.previousPage());
        if (state.page < pagesCount - 1) set(nextSlot, MenuTexts.nextPage());

        set(filterSlot, createChoice(Items.HOPPER, "filter", state.filter.ordinal(), FILTERS));
        set(sortSlot, createChoice(Items.COMPARATOR, "sort", state.sort.ordinal(), SORTS));
        set(headSlot, createHead());
        setNavigation(backSlot, mainSlot);

        buildExtra();
        setMenuTitle(createTitle(state.page + 1, pagesCount));
    }

    @Override
    protected final void onSlotClick(int slot, boolean isRightClick) {
        var element = slotElements.get(slot);
        if (element != null) {
            onElementClick(element, isRightClick);
            return;
        }

        if (slot == previousSlot && state.page > 0) {
            state.page = isRightClick ? 0 : state.page - 1;
            MenuSound.PAGE_TURN.play();
            refresh();
        } else if (slot == nextSlot && state.page < pagesCount - 1) {
            state.page = isRightClick ? pagesCount - 1 : state.page + 1;
            MenuSound.PAGE_TURN.play();
            refresh();
        } else if (slot == filterSlot) {
            state.filter = FILTERS[cycle(state.filter.ordinal(), FILTERS.length, isRightClick)];
            MenuSound.CLICK.play();
            refresh();
        } else if (slot == sortSlot) {
            state.sort = SORTS[cycle(state.sort.ordinal(), SORTS.length, isRightClick)];
            MenuSound.CLICK.play();
            refresh();
        } else {
            onOtherSlotClick(slot, isRightClick);
        }
    }

    private List<T> getSortedElements() {
        var result = new ArrayList<T>();
        for (var element : getElements()) {
            if (state.filter == FilterType.ALL || isAvailable(element)) result.add(element);
        }

        Comparator<T> byId = Comparator.comparingInt(this::getId);
        var comparator = switch (state.sort) {
            case BY_HISTORY -> byId;
            case ALPHABET -> Comparator.comparing((T element) -> getName(element).toLowerCase()).thenComparing(byId);
            case SLOTS -> Comparator.comparingInt(this::getSlotsCost).thenComparing(byId);
        };
        result.sort(comparator);
        return result;
    }

    private static int cycle(int index, int count, boolean isBackward) {
        return isBackward ? (index - 1 + count) % count : (index + 1) % count;
    }

    private static MenuSlot createChoice(Item item, String kind, int selected, Enum<?>[] values) {
        var tooltip = new ArrayList<Text>();
        tooltip.add(MenuTexts.text("choice.name", Map.of("name", MenuTexts.raw("fearsome.menu.choice." + kind))));

        for (int i = 0; i < values.length; i++) {
            var value = MenuTexts.raw("fearsome.menu." + kind + "." + values[i].name().toLowerCase());
            tooltip.add(MenuTexts.text(i == selected ? "choice.selected" : "choice.other", Map.of("value", value)));
        }

        return MenuSlot.of(item, tooltip);
    }

    private static class PagedState {
        private int page;
        private FilterType filter = FilterType.ALL;
        private SortType sort = SortType.BY_HISTORY;
    }

    private enum FilterType {
        ALL,
        ONLY_AVAILABLE
    }

    private enum SortType {
        BY_HISTORY,
        ALPHABET,
        SLOTS
    }
}
