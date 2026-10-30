package one.autremel.fearsome.builds;

import com.google.common.base.Preconditions;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.catalog.CatalogContainer;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.menus.MainMenuScreen;
import one.autremel.fearsome.ui.menus.MenuNavigator;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PlayerBuilds {
    private static boolean initialized;

    @Nullable
    private static PlayerBuildsPayload current;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(PlayerBuildsPayload.ID, PlayerBuilds::handleBuildsPayload);
        NetworkController.register(OpenChoiceMenuPayload.ID, payload -> openChoiceMenu());
    }

    public static boolean isLoaded() {
        return current != null;
    }

    public static int getSelectedCharacterId(CharacterTeam team) {
        return getTeam(team).selectedCharacterId();
    }

    public static Set<Integer> getBuild(CharacterTeam team, int characterId) {
        var builds = getTeam(team).builds();
        return characterId >= 0 && characterId < builds.size() ? builds.get(characterId) : Set.of();
    }

    public static void selectCharacter(CharacterTeam team, int characterId) {
        var teamBuilds = getTeam(team);
        var updated = new PlayerBuildsPayload.TeamBuilds(characterId, teamBuilds.builds());
        replaceTeam(team, updated);

        NetworkController.send(new SelectCharacterPayload(team, characterId));
    }

    public static boolean togglePerk(CharacterTeam team, int characterId, int perkId) {
        var build = getBuild(team, characterId);
        var isEquipping = !build.contains(perkId);
        if (isEquipping) build.add(perkId);
        else build.remove(perkId);

        NetworkController.send(new TogglePerkPayload(team, characterId, perkId));
        return isEquipping;
    }

    public static void reset() {
        current = null;
    }

    private static PlayerBuildsPayload.TeamBuilds getTeam(CharacterTeam team) {
        Preconditions.checkState(current != null, "Builds are not loaded yet!");
        return team == CharacterTeam.SURVIVORS ? current.survivors() : current.murderers();
    }

    private static void replaceTeam(CharacterTeam team, PlayerBuildsPayload.TeamBuilds teamBuilds) {
        Preconditions.checkState(current != null);
        current = team == CharacterTeam.SURVIVORS
                ? new PlayerBuildsPayload(teamBuilds, current.murderers())
                : new PlayerBuildsPayload(current.survivors(), teamBuilds);
    }

    private static void handleBuildsPayload(PlayerBuildsPayload payload) {
        current = payload;
        MenuNavigator.refreshCurrent();
    }

    private static void openChoiceMenu() {
        if (!isLoaded() || !CatalogContainer.isLoaded()) return;
        if (MinecraftClient.getInstance().currentScreen != null) return;

        MenuNavigator.openRoot(new MainMenuScreen());
    }
}
