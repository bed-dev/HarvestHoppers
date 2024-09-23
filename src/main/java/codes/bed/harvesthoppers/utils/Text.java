package codes.bed.harvesthoppers.utils;

import codes.bed.harvesthoppers.services.Placeholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused","deprecation"})
public class Text {

    private static final Map<Character, Character> SMALL_CAPS_MAP = new HashMap<>();
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder().character('§').build();

    static {
        SMALL_CAPS_MAP.put('a', 'ᴀ');
        SMALL_CAPS_MAP.put('b', 'ʙ');
        SMALL_CAPS_MAP.put('c', 'ᴄ');
        SMALL_CAPS_MAP.put('d', 'ᴅ');
        SMALL_CAPS_MAP.put('e', 'ᴇ');
        SMALL_CAPS_MAP.put('f', 'ꜰ');
        SMALL_CAPS_MAP.put('g', 'ɢ');
        SMALL_CAPS_MAP.put('h', 'ʜ');
        SMALL_CAPS_MAP.put('i', 'ɪ');
        SMALL_CAPS_MAP.put('j', 'ᴊ');
        SMALL_CAPS_MAP.put('k', 'ᴋ');
        SMALL_CAPS_MAP.put('l', 'ʟ');
        SMALL_CAPS_MAP.put('m', 'ᴍ');
        SMALL_CAPS_MAP.put('n', 'ɴ');
        SMALL_CAPS_MAP.put('o', 'ᴏ');
        SMALL_CAPS_MAP.put('p', 'ᴘ');
        SMALL_CAPS_MAP.put('q', 'ǫ');
        SMALL_CAPS_MAP.put('r', 'ʀ');
        SMALL_CAPS_MAP.put('s', 'ꜱ');
        SMALL_CAPS_MAP.put('t', 'ᴛ');
        SMALL_CAPS_MAP.put('u', 'ᴜ');
        SMALL_CAPS_MAP.put('v', 'ᴠ');
        SMALL_CAPS_MAP.put('w', 'ᴡ');
        SMALL_CAPS_MAP.put('x', 'x');
        SMALL_CAPS_MAP.put('y', 'ʏ');
        SMALL_CAPS_MAP.put('z', 'ᴢ');
    }

    public static String format(String string) {
        if (string == null) return "";
        return color(string);
    }

    public static String format(String string, Player player) {
        if (string == null) return "";
        String colored = color(string);
        return Placeholders.format(player, colored);
    }
    public static List<String> format(List<String> strings) {
        if (strings == null) return new ArrayList<>();
        List<String> formattedStrings = new ArrayList<>();
        for (String string : strings) {
            formattedStrings.add(format(string));
        }
        return formattedStrings;
    }

    public static List<String> format(List<String> strings, Player player) {
        if (strings == null) return new ArrayList<>();
        List<String> formattedStrings = new ArrayList<>();
        for (String string : strings) {
            formattedStrings.add(format(string, player));
        }
        return formattedStrings;
    }

    public static String small(String string) {
        if (string == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : string.toLowerCase().toCharArray()) {
            sb.append(SMALL_CAPS_MAP.getOrDefault(c, c));
        }
        return sb.toString();
    }
    public static String smallTag(String string) {
        Pattern smallTagPattern = Pattern.compile("<small>(.*?)</small>");
        Matcher matcher = smallTagPattern.matcher(string);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            result.append(string, lastEnd, matcher.start());
            result.append(small(matcher.group(1)));
            lastEnd = matcher.end();
        }
        result.append(string.substring(lastEnd));
        return result.toString();
    }

    public static String smallMC(String input) {
        StringBuilder output = new StringBuilder();
        boolean isSmallActive = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == '&' && i + 1 < input.length() || currentChar == '§' && i + 1 < input.length()) {
                char nextChar = input.charAt(i + 1);

                if (nextChar == 's') {
                    isSmallActive = true;
                    i++;
                } else if (nextChar == 'r' || (nextChar >= '0' && nextChar <= 'f')) {
                    isSmallActive = false;
                    output.append(currentChar);
                    output.append(nextChar);
                    i++;
                } else {
                    output.append(currentChar);
                }
            } else {
                if (isSmallActive) {
                    output.append(small(String.valueOf(currentChar)));
                } else {
                    output.append(currentChar);
                }
            }
        }

        return output.toString();
    }


    public static String colorMC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String color(String input) {
        if (input == null) return "";
        input = smallTag(smallMC(input));
        String miniMessageInput = input.replace("§", "&");
        Component component = MM.deserialize(miniMessageInput);
        String miniMessageOutput = LEGACY_SERIALIZER.serialize(component);

        return colorMC(miniMessageOutput);
    }
}
