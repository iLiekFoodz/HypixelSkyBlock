package net.swofty.types.generic.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {
    public static char[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z'
    };

    public static String formatTimeAsAgo(long millis) {
        long timeDifference = System.currentTimeMillis() - millis;

        if (timeDifference < 60000) {
            return "Just now";
        } else if (timeDifference < 3600000) {
            return timeDifference / 60000 + "m ago";
        } else if (timeDifference < 86400000) {
            return timeDifference / 3600000 + "h ago";
        } else {
            return timeDifference / 86400000 + "d ago";
        }
    }

    public static String shortenNumber(double number) {
        if (number < 1000) return String.valueOf(Math.round(number));
        if (number < 1000000) return String.format("%.1fK", number / 1000);
        if (number < 1000000000) return String.format("%.1fM", number / 1000000);
        return String.format("%.1fb", number / 1000000000);
    }

    public static String formatTime(long millis) {
        // Get in format wd xh ym zs
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0) sb.append(seconds).append("s");
        return sb.toString();
    }

    public static String commaify(int i) {
        return NumberFormat.getInstance().format(i);
    }

    public static Material getMaterialFromBlock(Block block) {
        return Material.fromNamespaceId(block.namespace());
    }

    public static String profileAge(long tbf) {
        if (tbf > 86400000) return commaify(tbf / 86400000) + "d ";
        if (tbf > 3600000) return commaify(tbf / 3600000) + "h ";
        if (tbf > 60000) return commaify(tbf / 60000) + "m ";
        if (tbf > 1000) return commaify(tbf / 1000) + "s";
        if (tbf < 1000) return commaify(tbf) + "ms";
        return "";
    }

    public static String getAsRomanNumeral(int num) {
        if (num == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        int times;
        String[] romans = new String[]{"I", "IV", "V", "IX", "X", "XL", "L",
                "XC", "C", "CD", "D", "CM", "M"};
        int[] ints = new int[]{1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500,
                900, 1000};
        for (int i = ints.length - 1; i >= 0; i--) {
            times = num / ints[i];
            num %= ints[i];
            while (times > 0) {
                sb.append(romans[i]);
                times--;
            }
        }
        return sb.toString();
    }

    public static String getTextFromComponent(Component component) {
        if (!(component instanceof TextComponent))
            throw new IllegalArgumentException("Component must be a TextComponent");
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getAuctionSetupFormattedTime(long millis) {
        String dur;
        if (millis >= 8.64E7) {
            long days = Math.round(millis / 8.64E7);
            dur = days + " Day";
            if (days != 1) dur += "s";
        } else if (millis >= 3600000) {
            long hours = Math.round(millis / 3600000.0);
            dur = hours + " Hour";
            if (hours != 1) dur += "s";
        } else {
            long minutes = Math.round(millis / 60000.0);
            dur = minutes + " Minute";
            if (minutes != 1) dur += "s";
        }
        return dur;
    }

    public static String toNormalCase(String string) {
        if (Acronym.isAcronym(string)) return string.toUpperCase();
        string = string.replaceAll("_", " ");
        String[] spl = string.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < spl.length; i++) {
            String s = spl[i];
            if (s.isEmpty()) {
                continue;
            }
            if (s.length() == 1) {
                s = s.toUpperCase();
            } else {
                s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            }
            // Append the processed string to the StringBuilder
            // Only add a space if it's not the first word
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String commaify(double d) {
        if (d < 1) {
            return "0";
        }
        return new DecimalFormat("#,###.0").format(d);
    }

    public static List<String> splitByWordAndLength(String string, int splitLength, String separator) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\G" + separator + "*(.+," + splitLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find())
            result.add(matcher.group(1));
        return result;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static String zeroed(long l) {
        return l > 9 ? "" + l : "0" + l;
    }

    public static String commaify(long l) {
        return NumberFormat.getInstance().format(l);
    }

    public static String limitStringLength(String s, int charLimit) {
        if (s.length() <= charLimit) return s;
        return s.substring(0, charLimit - 1);
    }

    public static String ntify(int i) {
        if (i == 11 || i == 12 || i == 13)
            return i + "th";
        String s = String.valueOf(i);
        char last = s.charAt(s.length() - 1);
        return switch (last) {
            case '1' -> i + "st";
            case '2' -> i + "nd";
            case '3' -> i + "rd";
            default -> i + "th";
        };
    }
}
