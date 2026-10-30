package one.autremel.fearsome.text;

public class RomanNumerals {
    private static final int MAX = 399;
    private static final int[] VALUES = {100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final String[] CACHE = new String[MAX + 1];

    static {
        for (int i = 1; i <= MAX; i++) CACHE[i] = convert(i);
    }

    public static String toRoman(int number) {
        return number > 0 && number <= MAX ? CACHE[number] : Integer.toString(number);
    }

    private static String convert(int number) {
        var builder = new StringBuilder();
        for (int i = 0; i < VALUES.length; i++) {
            while (number >= VALUES[i]) {
                number -= VALUES[i];
                builder.append(SYMBOLS[i]);
            }
        }

        return builder.toString();
    }
}
