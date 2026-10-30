package one.autremel.fearsome.ui.menus;

import net.minecraft.item.Items;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.builds.PlayerBuilds;

public class TeamMenuScreen extends InventoryMenuScreen {
    private static final int PERKS_SLOT = slotOf(1, 3);
    private static final int CHARACTERS_SLOT = slotOf(1, 5);
    private static final int BACK_SLOT = slotOf(2, 0);
    private static final int MAIN_SLOT = slotOf(2, 8);

    private final CharacterTeam team;

    public TeamMenuScreen(CharacterTeam team) {
        super(3);
        this.team = team;
    }

    @Override
    protected void build() {
        var folder = team.getFolderName();
        setMenuTitle(MenuTexts.text("title.team." + folder));

        var isForMurderer = team == CharacterTeam.MURDERERS;
        set(PERKS_SLOT, MenuTexts.button(isForMurderer ? Items.NETHER_BRICK : Items.IRON_INGOT, "button.perks." + folder));
        set(CHARACTERS_SLOT, MenuTexts.button(isForMurderer ? Items.STRIPPED_CRIMSON_HYPHAE : Items.STRIPPED_WARPED_HYPHAE, "button.characters." + folder));
        setNavigation(BACK_SLOT, MAIN_SLOT);
    }

    @Override
    protected void onSlotClick(int slot, boolean isRightClick) {
        if (slot == PERKS_SLOT) {
            MenuSound.CLICK.play();
            MenuNavigator.open(new PerksSelectorScreen(team, PlayerBuilds.getSelectedCharacterId(team)));
        } else if (slot == CHARACTERS_SLOT) {
            MenuSound.CLICK.play();
            MenuNavigator.open(new CharacterSelectorScreen(team));
        }
    }
}
