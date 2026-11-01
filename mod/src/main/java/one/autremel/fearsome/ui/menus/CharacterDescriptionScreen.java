package one.autremel.fearsome.ui.menus;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.builds.PlayerBuilds;
import one.autremel.fearsome.catalog.CatalogContainer;
import one.autremel.fearsome.catalog.CharacterEntry;
import one.autremel.fearsome.catalog.DescriptionPage;
import one.autremel.fearsome.text.Descriptions;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CharacterDescriptionScreen extends InventoryMenuScreen {
    private static final int SPECS_SLOT = slotOf(0, 5);
    private static final int HEAD_SLOT = slotOf(1, 2);
    private static final int ABILITY_SLOT = slotOf(1, 4);
    private static final int PASSIVE_SLOT = slotOf(1, 6);
    private static final int BACK_SLOT = slotOf(2, 0);
    private static final int GO_TO_PERKS_SLOT = slotOf(2, 5);
    private static final int MAIN_SLOT = slotOf(2, 8);

    private static final HashMap<String, Integer> pageStates = new HashMap<>();

    private final CharacterTeam team;
    private final int characterId;

    public CharacterDescriptionScreen(CharacterTeam team, int characterId) {
        super(3);
        this.team = team;
        this.characterId = characterId;
    }

    @Override
    protected void build() {
        var character = getCharacter();
        if (character == null) return;

        var name = Descriptions.getCharacterName(team, character).getString();
        setMenuTitle(MenuTexts.text("title.description." + team.getFolderName(), Map.of("name", name)));

        set(SPECS_SLOT, createSpecs(character));
        set(HEAD_SLOT, MenuTexts.head(team, character.key(), List.of(MenuTexts.styled("<" + MenuTexts.teamTag(team) + ">" + name))));
        set(ABILITY_SLOT, createPageSlot(character, character.abilityPages(), true));
        set(PASSIVE_SLOT, createPageSlot(character, character.passivePages(), false));
        set(GO_TO_PERKS_SLOT, createGoToPerks(character));
        setNavigation(BACK_SLOT, MAIN_SLOT);
    }

    @Override
    protected void onSlotClick(int slot, boolean isRightClick) {
        var character = getCharacter();
        if (character == null) return;

        if (slot == ABILITY_SLOT) turnPage(character.abilityPages(), true, isRightClick);
        else if (slot == PASSIVE_SLOT) turnPage(character.passivePages(), false, isRightClick);
        else if (slot == GO_TO_PERKS_SLOT) {
            MenuSound.CLICK.play();
            MenuNavigator.open(new PerksSelectorScreen(team, characterId));
        }
    }

    private void turnPage(List<DescriptionPage> pages, boolean isAbility, boolean isForward) {
        var current = getPage(isAbility, pages.size());
        var next = Math.clamp(current + (isForward ? 1 : -1), 0, pages.size() - 1);
        if (next == current) return;

        pageStates.put(getPageStateKey(isAbility), next);
        MenuSound.PAGE_TURN.play();
        refresh();
    }

    @Nullable
    private MenuSlot createPageSlot(CharacterEntry character, List<DescriptionPage> pages, boolean isAbility) {
        if (pages.isEmpty()) return null;

        var index = getPage(isAbility, pages.size());
        var page = pages.get(index);

        var tooltip = new ArrayList<Text>();
        var pageName = Descriptions.getPageName(team, character, page).getString().toUpperCase(Locale.ROOT);
        tooltip.add(MenuTexts.text(isAbility ? "description.ability" : "description.passive", Map.of("name", pageName)));
        if (pages.size() > 1) tooltip.add(MenuTexts.text("description.page", Map.of("page", index + 1, "pages", pages.size())));
        tooltip.addAll(Descriptions.getPageLore(team, character, page));

        return MenuSlot.of(page.icon(), tooltip);
    }

    private MenuSlot createSpecs(CharacterEntry character) {
        var middle = CatalogContainer.get(team).middleValues();
        var isSurvivors = team == CharacterTeam.SURVIVORS;

        var tooltip = new ArrayList<Text>();
        tooltip.add(MenuTexts.text("specs.name"));
        tooltip.add(MenuTexts.text("specs.speed", Map.of("value", MenuTexts.formatStat(character.speed(), middle.speed(), false))));
        tooltip.add(isSurvivors
                ? MenuTexts.text("specs.fix_accuracy", Map.of("value", MenuTexts.formatStat(character.teamStat(), middle.teamStat(), false)))
                : MenuTexts.text("specs.terror_radius", Map.of("value", MenuTexts.formatStat(character.teamStat(), middle.teamStat(), true))));
        tooltip.add(MenuTexts.text("specs.slots", Map.of("value", MenuTexts.formatStat(character.slots(), middle.slots(), false))));

        return MenuSlot.of(Items.MAP, tooltip);
    }

    private MenuSlot createGoToPerks(CharacterEntry character) {
        var catalog = CatalogContainer.get(team);
        var build = PlayerBuilds.getBuild(team, characterId);

        var usedSlots = 0;
        var perkNames = new ArrayList<String>();
        for (var perkId : build) {
            var perk = catalog.getPerk(perkId);
            if (perk == null) continue;

            usedSlots += perk.slots();
            perkNames.add(Descriptions.getPerkName(team, perk).getString());
        }

        var maxSlots = character.slots();
        var usedTag = usedSlots <= maxSlots ? "slots" : "error";
        var dots = "<" + usedTag + ">" + MenuTexts.dots(Math.min(usedSlots, maxSlots)) + "</" + usedTag + ">"
                + "<dark_gray>" + MenuTexts.dots(maxSlots - usedSlots) + "</dark_gray>";

        var tooltip = new ArrayList<Text>();
        tooltip.add(MenuTexts.text("go_to_perks.name"));
        tooltip.add(MenuTexts.text("go_to_perks.slots", Map.of("dots", dots)));
        for (var perkName : perkNames) {
            tooltip.add(MenuTexts.styled("<" + MenuTexts.teamTag(team) + ">   " + perkName));
        }

        var item = team == CharacterTeam.SURVIVORS ? Items.IRON_INGOT : Items.NETHER_BRICK;
        return MenuSlot.of(item, tooltip);
    }

    private int getPage(boolean isAbility, int pagesCount) {
        var page = pageStates.getOrDefault(getPageStateKey(isAbility), 0);
        return Math.clamp(page, 0, Math.max(pagesCount - 1, 0));
    }

    private String getPageStateKey(boolean isAbility) {
        return team.name() + "." + characterId + "." + (isAbility ? "ability" : "passive");
    }

    @Nullable
    private CharacterEntry getCharacter() {
        return CatalogContainer.get(team).getCharacter(characterId);
    }
}
