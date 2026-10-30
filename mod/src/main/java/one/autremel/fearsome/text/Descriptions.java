package one.autremel.fearsome.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.catalog.CharacterEntry;
import one.autremel.fearsome.catalog.DescriptionPage;
import one.autremel.fearsome.catalog.PerkEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Descriptions {
    private static final String SPECIFICITIES_HEADER_KEY = "fearsome.menu.specificities";
    private static final String SPECIFICITY_PREFIX = "<accent>⏺</accent> ";
    private static final String CONTINUATION_INDENT = "  ";

    public static String getPerkKey(CharacterTeam team, PerkEntry perk) {
        return "fearsome.perk." + team.getFolderName() + "." + perk.key();
    }

    public static String getCharacterKey(CharacterTeam team, CharacterEntry character) {
        return "fearsome.character." + team.getFolderName() + "." + character.key();
    }

    public static String getPageKey(CharacterTeam team, CharacterEntry character, DescriptionPage page) {
        return getCharacterKey(team, character) + "." + page.key();
    }

    public static MutableText getPerkName(CharacterTeam team, PerkEntry perk) {
        return FearText.format(getPerkKey(team, perk) + ".name", Map.of());
    }

    public static MutableText getCharacterName(CharacterTeam team, CharacterEntry character) {
        return FearText.format(getCharacterKey(team, character) + ".name", Map.of());
    }

    public static MutableText getPageName(CharacterTeam team, CharacterEntry character, DescriptionPage page) {
        return FearText.format(getPageKey(team, character, page) + ".name", Map.of());
    }

    public static List<Text> getPerkLore(CharacterTeam team, PerkEntry perk) {
        return getLore(getPerkKey(team, perk), perk.params());
    }

    public static List<Text> getPageLore(CharacterTeam team, CharacterEntry character, DescriptionPage page) {
        return getLore(getPageKey(team, character, page), page.params());
    }

    public static List<Text> getLore(String baseKey, Map<String, ?> params) {
        var lore = new ArrayList<>(FearText.formatWrapped(baseKey + ".description", params));

        var specificityIndex = 1;
        var specificityKey = baseKey + ".specificities." + specificityIndex;
        if (!FearText.exists(specificityKey)) return lore;

        lore.add(Text.empty());
        lore.add(FearText.format(SPECIFICITIES_HEADER_KEY, Map.of()));

        while (FearText.exists(specificityKey)) {
            var raw = SPECIFICITY_PREFIX + FearText.formatString(specificityKey, params);
            var lines = FearText.wrap(TaggedTextParser.parse(raw, FearText.DEFAULT_STYLE), FearText.DEFAULT_WRAP_WIDTH);
            for (int i = 0; i < lines.size(); i++) {
                lore.add(i == 0 ? lines.get(i) : Text.literal(CONTINUATION_INDENT).setStyle(FearText.DEFAULT_STYLE).append(lines.get(i)));
            }

            specificityIndex++;
            specificityKey = baseKey + ".specificities." + specificityIndex;
        }

        return lore;
    }
}
