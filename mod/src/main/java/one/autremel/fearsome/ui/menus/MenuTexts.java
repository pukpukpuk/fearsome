package one.autremel.fearsome.ui.menus;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.text.FearText;
import one.autremel.fearsome.text.TaggedTextParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class MenuTexts {
    private static final String PREFIX = "fearsome.menu.";
    private static final String SLOT_DOT = "⏺";
    private static final int STAT_DECIMALS = 3;

    private static final HashMap<String, ProfileComponent> cachedProfiles = new HashMap<>();

    static MutableText text(String key) {
        return FearText.format(PREFIX + key, Map.of());
    }

    static MutableText text(String key, Map<String, ?> params) {
        return FearText.format(PREFIX + key, params);
    }

    static String raw(String fullKey) {
        return Language.getInstance().get(fullKey);
    }

    static List<Text> button(String key) {
        var tooltip = new ArrayList<Text>();
        tooltip.add(text(key + ".name"));

        var descriptionKey = PREFIX + key + ".description";
        if (FearText.exists(descriptionKey)) tooltip.addAll(FearText.formatWrapped(descriptionKey, Map.of()));
        return tooltip;
    }

    static MenuSlot button(Item item, String key) {
        return MenuSlot.of(item, button(key));
    }

    static MenuSlot back() {
        return button(Items.SLIME_BALL, "button.back");
    }

    static MenuSlot main() {
        return button(Items.SPIDER_EYE, "button.main");
    }

    static MenuSlot previousPage() {
        return button(Items.ARROW, "button.previous_page");
    }

    static MenuSlot nextPage() {
        return button(Items.SPECTRAL_ARROW, "button.next_page");
    }

    static String teamTag(CharacterTeam team) {
        return team == CharacterTeam.SURVIVORS ? "surv" : "murd";
    }

    static MutableText itemName(CharacterTeam team, String name) {
        return TaggedTextParser.parse("⏵> <" + teamTag(team) + ">" + name, FearText.DEFAULT_STYLE);
    }

    static MutableText styled(String raw) {
        return TaggedTextParser.parse(raw, FearText.DEFAULT_STYLE);
    }

    static String dots(int count) {
        return SLOT_DOT.repeat(Math.max(count, 0));
    }

    static MenuSlot head(CharacterTeam team, String characterKey, List<Text> tooltip) {
        var profile = cachedProfiles.computeIfAbsent(team.getFolderName() + "/" + characterKey, MenuTexts::createProfile);

        var stack = new ItemStack(Items.PLAYER_HEAD);
        stack.set(DataComponentTypes.PROFILE, profile);
        return new MenuSlot(stack, tooltip);
    }

    private static ProfileComponent createProfile(String skinKey) {
        var json = new JsonObject();
        json.addProperty("texture", FearsomeMod.identifier("skins/" + skinKey).toString());
        return ProfileComponent.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    static String formatStat(float value, float middle, boolean isInverted) {
        var rounded = round(value);
        var roundedMiddle = round(middle);

        var comparison = rounded.compareTo(roundedMiddle);
        String tag;
        if (comparison == 0) tag = "warning";
        else tag = (comparison > 0) != isInverted ? "success" : "error";

        return "<" + tag + ">" + rounded.toPlainString() + "</" + tag + ">";
    }

    private static BigDecimal round(float value) {
        return new BigDecimal(Float.toString(value)).setScale(STAT_DECIMALS, RoundingMode.HALF_UP).stripTrailingZeros();
    }
}
