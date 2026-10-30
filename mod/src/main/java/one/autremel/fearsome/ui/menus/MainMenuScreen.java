package one.autremel.fearsome.ui.menus;

import net.minecraft.item.Items;
import one.autremel.fearsome.CharacterTeam;

public class MainMenuScreen extends InventoryMenuScreen {
    private static final int SURVIVORS_SLOT = slotOf(1, 1);
    private static final int BESTIARY_SLOT = slotOf(1, 4);
    private static final int MURDERERS_SLOT = slotOf(1, 7);

    public MainMenuScreen() {
        super(3);
    }

    @Override
    protected void build() {
        setMenuTitle(MenuTexts.text("title.main"));
        set(SURVIVORS_SLOT, MenuTexts.button(Items.DIAMOND, "button.team.survivors"));
        set(BESTIARY_SLOT, MenuTexts.button(Items.BOOK, "button.bestiary"));
        set(MURDERERS_SLOT, MenuTexts.button(Items.REDSTONE, "button.team.murderers"));
    }

    @Override
    protected void onSlotClick(int slot, boolean isRightClick) {
        if (slot == SURVIVORS_SLOT) open(CharacterTeam.SURVIVORS);
        else if (slot == MURDERERS_SLOT) open(CharacterTeam.MURDERERS);
    }

    private void open(CharacterTeam team) {
        MenuSound.CLICK.play();
        MenuNavigator.open(new TeamMenuScreen(team));
    }
}
