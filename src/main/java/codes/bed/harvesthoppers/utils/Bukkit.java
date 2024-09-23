package codes.bed.harvesthoppers.utils;

import org.bukkit.Material;

public class Bukkit {


    public static Material getMaterial(String string) {
        try {
            return Material.valueOf(string);
        } catch (Exception e) {
            Logger.error("Error retrieving material  '" + string + "' Defaulting to the provided default.");
            return Material.BARRIER;
        }
    }
}
