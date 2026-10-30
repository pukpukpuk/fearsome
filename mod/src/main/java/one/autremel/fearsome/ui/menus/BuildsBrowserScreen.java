package one.autremel.fearsome.ui.menus;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import one.autremel.fearsome.builds.GameBuilds;
import one.autremel.fearsome.builds.GameBuildsPayload;
import one.autremel.fearsome.catalog.CatalogContainer;
import one.autremel.fearsome.text.Descriptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildsBrowserScreen extends InventoryMenuScreen {
    private static final int PERKS_PER_ROW = COLUMNS - 1; // one for head
    private static final int PERKS_IN_ARROWED_ROW = PERKS_PER_ROW - 2; // two for arrow buttons
    private static final int PREVIOUS_COLUMN = 7;
    private static final int NEXT_COLUMN = 8;
    private static final int EMPTY_COLUMN = 4;

    private int page;
    private int pagesCount;
    private int previousSlot = -1, nextSlot = -1;

    public BuildsBrowserScreen(int rows) {
        super(Math.max(rows, 1));
    }

    @Override
    protected void build() {
        var entries = GameBuilds.getEntries();
        pagesCount = calculatePagesCount(entries);
        page = Math.clamp(page, 0, pagesCount - 1);

        var arrowsAreShown = pagesCount > 1;
        previousSlot = nextSlot = -1;

        for (int row = 0; row < entries.size() && row < rows; row++) {
            var isRowWithArrows = arrowsAreShown && row == entries.size() - 1;
            var perksPerPage = isRowWithArrows ? PERKS_IN_ARROWED_ROW : PERKS_PER_ROW;
            drawRow(row, entries.get(row), page * perksPerPage, perksPerPage);

            if (isRowWithArrows) {
                if (page > 0) set(previousSlot = slotOf(row, PREVIOUS_COLUMN), MenuTexts.previousPage());
                if (page < pagesCount - 1) set(nextSlot = slotOf(row, NEXT_COLUMN), MenuTexts.nextPage());
            }
        }

        if (entries.stream().allMatch(entry -> entry.perkIds().isEmpty())) {
            set(slotOf(entries.size() / 2, EMPTY_COLUMN), MenuTexts.button(Items.MANGROVE_SIGN, "button.without_any_perk"));
        }

        setMenuTitle(arrowsAreShown
                ? MenuTexts.text("title.builds.paged", Map.of("page", page + 1, "pages", pagesCount))
                : MenuTexts.text("title.builds"));
    }

    @Override
    protected void onSlotClick(int slot, boolean isRightClick) {
        if (slot == previousSlot && slot != -1) {
            page = isRightClick ? 0 : page - 1;
        } else if (slot == nextSlot && slot != -1) {
            page = isRightClick ? pagesCount - 1 : page + 1;
        } else {
            return;
        }

        MenuSound.PAGE_TURN.play();
        refresh();
    }

    private void drawRow(int row, GameBuildsPayload.Entry entry, int startIndex, int count) {
        var catalog = CatalogContainer.get(entry.team());
        var character = catalog.getCharacter(entry.characterId());

        if (character != null) {
            var name = Descriptions.getCharacterName(entry.team(), character).getString();
            var tooltip = List.<Text>of(
                    MenuTexts.styled("<" + MenuTexts.teamTag(entry.team()) + ">" + name),
                    MenuTexts.styled(entry.playerName())
            );
            set(slotOf(row, 0), MenuTexts.head(entry.team(), character.key(), tooltip));
        }

        var end = Math.min(startIndex + count, entry.perkIds().size());
        for (int i = startIndex; i < end; i++) {
            var perk = catalog.getPerk(entry.perkIds().get(i));
            if (perk == null) continue;

            var tooltip = new ArrayList<Text>();
            tooltip.add(MenuTexts.itemName(entry.team(), Descriptions.getPerkName(entry.team(), perk).getString()));
            tooltip.addAll(Descriptions.getPerkLore(entry.team(), perk));
            set(slotOf(row, 1 + i - startIndex), MenuSlot.of(perk.icon(), tooltip));
        }
    }

    private static int calculatePagesCount(List<GameBuildsPayload.Entry> entries) {
        var pages = 1;
        for (var entry : entries) pages = Math.max(pages, Math.ceilDiv(entry.perkIds().size(), PERKS_PER_ROW));
        if (pages > 1) {
            pages = Math.max(pages, Math.ceilDiv(entries.getLast().perkIds().size(), PERKS_IN_ARROWED_ROW));
        }
        return pages;
    }
}
