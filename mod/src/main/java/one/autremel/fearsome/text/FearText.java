package one.autremel.fearsome.text;

import com.google.common.base.Preconditions;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.WrappedEvents;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.ui.components.binding.KeyboardBindingDrawer;

import java.util.*;
import java.util.regex.Pattern;

public class FearText {
    public static final int DEFAULT_WRAP_WIDTH = 250;
    public static final Style DEFAULT_STYLE = Style.EMPTY.withColor(Formatting.GRAY).withItalic(false);

    private static final String BINDING_ARG_PREFIX = "key_";
    private static final BindingId[] BINDING_IDS = BindingId.values();

    private static boolean initialized;

    private static final HashMap<String, MessageFormat> cachedFormats = new HashMap<>();
    private static String localeCode;
    private static ULocale locale;
    private static Pattern groupingSeparatorPattern, decimalSeparatorPattern;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        WrappedEvents.registerOnResourceReload(cachedFormats::clear);
    }

    public static boolean exists(String key) {
        return Language.getInstance().hasTranslation(key);
    }

    public static MutableText format(String key, Map<String, ?> params) {
        return format(key, params, DEFAULT_STYLE);
    }

    public static MutableText format(String key, Map<String, ?> params, Style baseStyle) {
        return TaggedTextParser.parse(formatString(key, params), baseStyle);
    }

    public static List<Text> formatWrapped(String key, Map<String, ?> params) {
        return wrap(format(key, params), DEFAULT_WRAP_WIDTH);
    }

    public static String formatString(String key, Map<String, ?> params) {
        var pattern = Language.getInstance().get(key);

        var args = new HashMap<String, Object>(params);
        addBindingArgs(args);

        try {
            return normalizeNumbers(getFormat(pattern).format(args));
        } catch (IllegalArgumentException e) {
            FearsomeMod.LOGGER.warn("Failed to format text {}: {}", key, e.getMessage());
            return pattern;
        }
    }

    public static List<Text> wrap(Text text, int width) {
        var handler = MinecraftClient.getInstance().textRenderer.getTextHandler();
        var lines = handler.wrapLines(text, width, Style.EMPTY);

        var result = new ArrayList<Text>(lines.size());
        for (var line : lines) result.add(toText(line));
        return result;
    }

    private static MessageFormat getFormat(String pattern) {
        updateLocale();
        return cachedFormats.computeIfAbsent(pattern, p -> new MessageFormat(p, locale));
    }

    private static void updateLocale() {
        var code = MinecraftClient.getInstance().getLanguageManager().getLanguage();
        if (code.equals(localeCode)) return;

        localeCode = code;
        locale = ULocale.forLanguageTag(code.replace('_', '-'));
        cachedFormats.clear();

        var symbols = DecimalFormatSymbols.getInstance(locale);
        var grouping = Pattern.quote(symbols.getGroupingSeparatorString());
        var decimal = Pattern.quote(symbols.getDecimalSeparatorString());
        groupingSeparatorPattern = Pattern.compile("(?<=\\d)" + grouping + "(?=\\d)");
        decimalSeparatorPattern = Pattern.compile("(?<=\\d)" + decimal + "(?=\\d)");
    }

    private static String normalizeNumbers(String string) {
        var withoutGrouping = groupingSeparatorPattern.matcher(string).replaceAll("");
        return decimalSeparatorPattern.matcher(withoutGrouping).replaceAll(".");
    }

    private static void addBindingArgs(Map<String, Object> args) {
        for (var id : BINDING_IDS) {
            var data = KeyBindings.getData(id);
            if (data == null) continue;

            var keyName = KeyboardBindingDrawer.tinify(data.keyBinding().getBoundKeyTranslationKey());
            args.put(BINDING_ARG_PREFIX + id.getId(), "<control_keys>" + keyName + "</control_keys>");
        }
    }

    private static Text toText(StringVisitable visitable) {
        var text = Text.empty();
        visitable.visit((style, string) -> {
            text.append(Text.literal(string).setStyle(style));
            return Optional.empty();
        }, Style.EMPTY);
        return text;
    }
}
