package one.autremel.fearsome.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class WrongVersionDisconnectScreen extends Screen {
    private static final Text GO_TO_GITHUB = Text.translatable("gui.go_to_mod_github");
    private static final Text TO_MENU_TEXT = Text.translatable("gui.toMenu");

    private final Text reason;
    private final Screen parent = new MultiplayerScreen(new TitleScreen(true));
    private final DirectionalLayoutWidget grid = DirectionalLayoutWidget.vertical();

    public WrongVersionDisconnectScreen(Text reason) {
        super(Text.of(Text.translatable("disconnect.lost")));
        this.reason = reason;
    }


    @Override
    protected void init() {
        grid.getMainPositioner().alignHorizontalCenter().margin(10);
        grid.add(new TextWidget(title, textRenderer));
        grid.add((new MultilineTextWidget(reason, this.textRenderer)).setMaxWidth(width - 50).setCentered(true));
        grid.getMainPositioner().margin(2);

        grid.add(ButtonWidget.builder(GO_TO_GITHUB, this::toGithubButtonPress).width(200).build());
        grid.add(ButtonWidget.builder(TO_MENU_TEXT, this::toMenuButtonPress).width(200).build());

        grid.refreshPositions();
        grid.forEachChild(this::addDrawableChild);
        refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        SimplePositioningWidget.setPos(this.grid, this.getNavigationFocus());
    }

    private void toGithubButtonPress(ButtonWidget button) {
        Util.getOperatingSystem().open("https://github.com/pukpukpuk/fearsome/releases");
        openParent();
    }

    private void toMenuButtonPress(ButtonWidget button) {
        openParent();
    }

    private void openParent() {
        client.setScreen(parent);
    }
}
