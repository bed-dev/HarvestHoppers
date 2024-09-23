package codes.bed.harvesthoppers.services;

import codes.bed.harvesthoppers.utils.Logger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Placeholders {


    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Logger.log("§7PlaceholderAPI hooked");
        } else {
            Logger.log("§cPlaceholderAPI not found");
        }
    }
    public static String format(Player player, String string) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, string);
        }
        return string;
    }}
