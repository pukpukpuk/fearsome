package one.autremel.fearsome.ui.menus;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.builds.PlayerBuilds;
import one.autremel.fearsome.catalog.CatalogContainer;
import one.autremel.fearsome.catalog.CharacterEntry;
import one.autremel.fearsome.catalog.PerkEntry;
import one.autremel.fearsome.text.Descriptions;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PerksSelectorScreen extends PagedMenuScreen<PerkEntry> {
    private static final int VISUALIZATION_CENTER = slotOf(0, 4);
    private static final int GLASS_CENTER = slotOf(1, 4);
    private static final int MAX_VISUALIZED = COLUMNS;

    private final int characterId;
    private final Int2ObjectOpenHashMap<PerkEntry> visualizedPerks = new Int2ObjectOpenHashMap<>();

    public PerksSelectorScreen(CharacterTeam team, int characterId) {
        super(6, 2, team, "perks");
        this.characterId = characterId;
    }

    @Override
    protected List<PerkEntry> getElements() {
        return CatalogContainer.get(team).perks();
    }

    @Override
    protected boolean isAvailable(PerkEntry perk) {
        return perk.isEnabled();
    }

    @Override
    protected int getId(PerkEntry perk) {
        return perk.id();
    }

    @Override
    protected String getName(PerkEntry perk) {
        return Descriptions.getPerkName(team, perk).getString();
    }

    @Override
    protected int getSlotsCost(PerkEntry perk) {
        return perk.slots();
    }

    @Override
    protected MenuSlot createSlot(PerkEntry perk) {
        return createPerkSlot(perk, isEquipped(perk));
    }

    @Override
    protected void onElementClick(PerkEntry perk, boolean isRightClick) {
        if (!isEquipped(perk) && !perk.isEnabled()) {
            MenuSound.ERROR.play();
            return;
        }

        var isEquipped = PlayerBuilds.togglePerk(team, characterId, perk.id());
        (isEquipped ? MenuSound.EQUIP_PERK : MenuSound.UNEQUIP_PERK).play();
        refresh();
    }

    @Override
    protected Text createTitle(int page, int pagesCount) {
        var character = getCharacter();
        var name = character != null
                ? MenuTexts.raw(Descriptions.getCharacterKey(team, character) + ".short_name").toUpperCase(Locale.ROOT)
                : "";
        return MenuTexts.text("title.perks." + team.getFolderName(), Map.of("name", name, "page", page, "pages", pagesCount));
    }

    @Override
    protected MenuSlot createHead() {
        var character = getCharacter();
        if (character == null) return null;

        var name = MenuTexts.styled("<" + MenuTexts.teamTag(team) + ">" + Descriptions.getCharacterName(team, character).getString());
        return MenuTexts.head(team, character.key(), List.of(name));
    }

    @Override
    protected void buildExtra() {
        drawVisualization();
        drawGlass();
    }

    private void drawVisualization() {
        visualizedPerks.clear();

        var catalog = CatalogContainer.get(team);
        var equipped = new ArrayList<PerkEntry>();
        for (var perkId : PlayerBuilds.getBuild(team, characterId)) {
            var perk = catalog.getPerk(perkId);
            if (perk != null) equipped.add(perk);
        }
        equipped.sort(Comparator.comparingInt(PerkEntry::id));

        var count = Math.min(equipped.size(), MAX_VISUALIZED);
        var half = count / 2;
        for (int i = 0; i < count; i++) {
            int slot;
            if (count % 2 == 1) slot = VISUALIZATION_CENTER - half + i;
            else slot = i < half ? VISUALIZATION_CENTER - half + i : VISUALIZATION_CENTER - half + i + 1;

            var perk = equipped.get(i);
            visualizedPerks.put(slot, perk);
            set(slot, createPerkSlot(perk, false));
        }
    }

    @Override
    protected void onOtherSlotClick(int slot, boolean isRightClick) {
        var perk = visualizedPerks.get(slot);
        if (perk != null) onElementClick(perk, isRightClick);
    }

    private void drawGlass() {
        var character = getCharacter();
        if (character == null) return;

        var maxSlots = character.slots();
        var usedSlots = getUsedSlots();
        var start = GLASS_CENTER - maxSlots / 2;
        for (int i = 0; i < maxSlots; i++) {
            var item = usedSlots > maxSlots ? Items.RED_STAINED_GLASS_PANE
                    : i < usedSlots ? Items.LIME_STAINED_GLASS_PANE
                    : Items.GRAY_STAINED_GLASS_PANE;
            set(start + i, MenuSlot.of(item));
        }
    }

    private int getUsedSlots() {
        var catalog = CatalogContainer.get(team);
        var used = 0;
        for (var perkId : PlayerBuilds.getBuild(team, characterId)) {
            var perk = catalog.getPerk(perkId);
            if (perk != null) used += perk.slots();
        }
        return used;
    }

    private MenuSlot createPerkSlot(PerkEntry perk, boolean isEquipped) {
        var tooltip = new ArrayList<Text>();
        tooltip.add(MenuTexts.itemName(team, getName(perk)));
        tooltip.addAll(Descriptions.getPerkLore(team, perk));
        tooltip.add(Text.empty());
        tooltip.add(MenuTexts.text("perk_cost", Map.of("dots", MenuTexts.dots(perk.slots()))));
        if (isEquipped) tooltip.add(MenuTexts.text("status.selected"));
        else if (!perk.isEnabled()) tooltip.add(MenuTexts.text("status.unavailable"));

        var item = perk.isEnabled() ? perk.icon() : Items.STRUCTURE_VOID;
        return MenuSlot.of(item, isEquipped, tooltip);
    }

    private boolean isEquipped(PerkEntry perk) {
        return PlayerBuilds.getBuild(team, characterId).contains(perk.id());
    }

    @Nullable
    private CharacterEntry getCharacter() {
        return CatalogContainer.get(team).getCharacter(characterId);
    }
}
