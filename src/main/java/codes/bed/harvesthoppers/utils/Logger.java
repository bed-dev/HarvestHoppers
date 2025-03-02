package codes.bed.harvesthoppers.utils;

import codes.bed.harvesthoppers.HarvestHoppers;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.function.Supplier;

import static codes.bed.harvesthoppers.HarvestHoppers.Instance;

@SuppressWarnings({"unused"})
public class Logger {
    private static HarvestHoppers instance;
    private static ConsoleCommandSender console;
    private static String PREFIX;


    public static void setInstance(HarvestHoppers instance) {
        Logger.instance = instance;

        console = instance.getServer().getConsoleSender();
        PREFIX = getPluginPrefix();
    }


    public static void log(Supplier<String> message) {
        String msg = message.get();
        log(msg);
    }

    public static void log(String message) {
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void raw(Supplier<String> message) {
        String msg = message.get();
        raw(msg);
    }

    public static void raw(String message) {
        console.sendMessage(message);
    }

    public static void info(Supplier<String> message) {
        String msg = message.get();
        info(msg);
    }

    public static void info(String message) {
        console.sendMessage(formatMessage(message));
    }

    public static void fine(Supplier<String> message) {
        String msg = message.get();
        fine(msg);
    }

    public static void fine(String message) {
        console.sendMessage(formatMessage(ChatColor.GRAY, message));
    }

    public static void config(Supplier<String> message) {
        String msg = message.get();
        config(msg);
    }

    public static void config(String message) {
        console.sendMessage(formatMessage(ChatColor.BLUE, message));
    }

    public static void finer(Supplier<String> message) {
        String msg = message.get();
        finer(msg);
    }

    public static void finer(String message) {
        console.sendMessage(formatMessage(ChatColor.DARK_GRAY, message));
    }

    public static void finest(Supplier<String> message) {
        String msg = message.get();
        finest(msg);
    }

    public static void finest(String message) {
        console.sendMessage(formatMessage(ChatColor.DARK_GRAY, message));
    }

    public static void warning(Supplier<String> message) {
        String msg = message.get();
        warning(msg);
    }

    public static void warning(String message) {
        console.sendMessage(formatMessage(ChatColor.YELLOW, message));
    }

    public static void severe(Supplier<String> message) {
        String msg = message.get();
        severe(msg);
    }

    public static void severe(String message) {
        console.sendMessage(formatMessage(ChatColor.RED, message));
    }

    public static void error(Supplier<String> message) {
        String msg = message.get();
        error(msg);
    }

    public static void error(String message) {
        console.sendMessage(ChatColor.RED + message);
    }

    public static void debug(Supplier<String> message) {
        String msg = message.get();
        debug(msg);
    }

    public static void debug(String message) {
        boolean debugging = Config.Boolean("debug", false);
        if(debugging) console.sendMessage(formatMessage(ChatColor.LIGHT_PURPLE, message));
    }

    private static String formatMessage(ChatColor color, String message) {
        return color + PREFIX + " " + ChatColor.translateAlternateColorCodes('&', message);
    }
    private static String formatMessage(String message) {
        return PREFIX + " " + ChatColor.translateAlternateColorCodes('&', message);
    }
    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    private static String getPluginPrefix() {
        JavaPlugin plugin = Instance;
        return ChatColor.translateAlternateColorCodes('&', plugin.getDescription().getPrefix() != null
                ? "[" + plugin.getDescription().getPrefix() + "]"
                : "[]");
    }
}
