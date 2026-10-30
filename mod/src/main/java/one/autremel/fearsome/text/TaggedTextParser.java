package one.autremel.fearsome.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import one.autremel.fearsome.Palette;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TaggedTextParser {
    private static final Map<String, Integer> TAG_COLORS = Map.ofEntries(
            Map.entry("murd", Palette.MURDERER),
            Map.entry("surv", Palette.SURVIVOR),
            Map.entry("term", Palette.TERM),
            Map.entry("effect", Palette.EFFECT),
            Map.entry("slots", Palette.SLOTS),
            Map.entry("accent", Palette.ACCENT),
            Map.entry("success", Palette.SUCCESS),
            Map.entry("warning", Palette.WARNING),
            Map.entry("error", Palette.ERROR),
            Map.entry("control_keys", Palette.CONTROL_KEYS),
            Map.entry("light_blue", Palette.LIGHT_BLUE),
            Map.entry("fear", Palette.FEAR)
    );
    private static final List<Keyword> KEYWORDS = List.of(
            Keyword.of(Palette.HEART, "❤"),
            Keyword.of(0xFF555555, "»", "▶", "⏵>")
    );

    public static MutableText parse(String input, Style baseStyle) {
        var root = Text.empty();
        var frames = new ArrayDeque<Frame>();
        var style = baseStyle;
        var buffer = new StringBuilder();

        var i = 0;
        while (i < input.length()) {
            var c = input.charAt(i);
            var end = c == '<' ? input.indexOf('>', i) : -1;
            if (end > i + 1) {
                var tag = input.substring(i + 1, end);
                var newStyle = tag.startsWith("/")
                        ? tryClose(frames, tag.substring(1))
                        : tryOpen(frames, tag, style);

                if (newStyle != null) {
                    appendWithKeywords(root, buffer.toString(), style);
                    buffer.setLength(0);
                    style = newStyle;
                    i = end + 1;
                    continue;
                }
            }

            buffer.append(c);
            i++;
        }

        appendWithKeywords(root, buffer.toString(), style);
        return root;
    }

    private static Style tryOpen(ArrayDeque<Frame> frames, String tag, Style current) {
        var color = getTagColor(tag);
        if (color == null) return null;

        frames.push(new Frame(tag, current));
        return current.withColor(color);
    }

    private static Style tryClose(ArrayDeque<Frame> frames, String tag) {
        if (frames.stream().noneMatch(frame -> frame.tag.equals(tag))) return null;

        Frame frame;
        do frame = frames.pop();
        while (!frame.tag.equals(tag));

        return frame.previousStyle;
    }

    private static Integer getTagColor(String tag) {
        var color = TAG_COLORS.get(tag);
        if (color != null) return color;

        if (tag.length() == 7 && tag.charAt(0) == '#') {
            try {
                return Integer.parseInt(tag.substring(1), 16);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        var formatting = Formatting.byName(tag);
        return formatting != null && formatting.isColor() ? formatting.getColorValue() : null;
    }

    private static void appendWithKeywords(MutableText root, String string, Style style) {
        if (string.isEmpty()) return;

        var start = 0;
        while (start < string.length()) {
            Keyword nearest = null;
            var nearestStart = Integer.MAX_VALUE;
            var nearestEnd = 0;

            for (var keyword : KEYWORDS) {
                var matcher = keyword.pattern.matcher(string);
                if (matcher.find(start) && matcher.start() < nearestStart) {
                    nearest = keyword;
                    nearestStart = matcher.start();
                    nearestEnd = matcher.end();
                }
            }

            if (nearest == null) {
                root.append(Text.literal(string.substring(start)).setStyle(style));
                return;
            }

            if (nearestStart > start) root.append(Text.literal(string.substring(start, nearestStart)).setStyle(style));
            root.append(Text.literal(string.substring(nearestStart, nearestEnd)).setStyle(style.withColor(nearest.color)));
            start = nearestEnd;
        }
    }

    private record Frame(String tag, Style previousStyle) {}

    private record Keyword(Pattern pattern, int color) {
        static Keyword of(int color, String... words) {
            var quoted = new String[words.length];
            for (int i = 0; i < words.length; i++) quoted[i] = Pattern.quote(words[i]);

            var pattern = Pattern.compile("(?<!\\w)(" + String.join("|", quoted) + ")(?!\\w)", Pattern.UNICODE_CHARACTER_CLASS);
            return new Keyword(pattern, color);
        }
    }
}
