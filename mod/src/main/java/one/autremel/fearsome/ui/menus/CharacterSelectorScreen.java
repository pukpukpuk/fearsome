package one.autremel.fearsome.ui.menus;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.builds.PlayerBuilds;
import one.autremel.fearsome.catalog.CatalogContainer;
import one.autremel.fearsome.catalog.CharacterEntry;
import one.autremel.fearsome.text.Descriptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterSelectorScreen extends PagedMenuScreen<CharacterEntry> {
    public CharacterSelectorScreen(CharacterTeam team) {
        super(4, 0, team, "characters");
    }

    @Override
    protected List<CharacterEntry> getElements() {
        return CatalogContainer.get(team).characters();
    }

    @Override
    protected boolean isAvailable(CharacterEntry character) {
        return character.isPlayable();
    }

    @Override
    protected int getId(CharacterEntry character) {
        return character.id();
    }

    @Override
    protected String getName(CharacterEntry character) {
        return Descriptions.getCharacterName(team, character).getString();
    }

    @Override
    protected int getSlotsCost(CharacterEntry character) {
        return character.slots();
    }

    @Override
    protected MenuSlot createSlot(CharacterEntry character) {
        var isSelected = PlayerBuilds.getSelectedCharacterId(team) == character.id();

        var tooltip = new ArrayList<Text>();
        tooltip.add(MenuTexts.itemName(team, getName(character)));
        if (!character.isPlayable()) tooltip.add(MenuTexts.text("status.unavailable"));
        else tooltip.add(MenuTexts.text(isSelected ? "status.selected" : "status.select"));
        tooltip.add(MenuTexts.text("status.info"));

        var item = character.isPlayable() ? character.icon() : Items.STRUCTURE_VOID;
        return MenuSlot.of(item, isSelected, tooltip);
    }

    @Override
    protected void onElementClick(CharacterEntry character, boolean isRightClick) {
        if (isRightClick) {
            MenuSound.CLICK.play();
            MenuNavigator.open(new CharacterDescriptionScreen(team, character.id()));
            return;
        }

        if (!character.isPlayable()) {
            MenuSound.ERROR.play();
            return;
        }
        if (PlayerBuilds.getSelectedCharacterId(team) == character.id()) return;

        PlayerBuilds.selectCharacter(team, character.id());
        MenuSound.EQUIP_CHARACTER.play();
        refresh();
    }

    @Override
    protected Text createTitle(int page, int pagesCount) {
        return MenuTexts.text("title.characters." + team.getFolderName(), Map.of("page", page, "pages", pagesCount));
    }

    @Override
    protected MenuSlot createHead() {
        var selected = CatalogContainer.get(team).getCharacter(PlayerBuilds.getSelectedCharacterId(team));
        if (selected == null) return null;

        var name = MenuTexts.styled("<" + MenuTexts.teamTag(team) + ">" + getName(selected));
        return MenuTexts.head(team, selected.key(), List.of(name));
    }
}
