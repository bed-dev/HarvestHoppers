package codes.bed.harvesthoppers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import codes.bed.harvesthoppers.utils.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import static codes.bed.harvesthoppers.HarvestHoppers.Instance;
import static codes.bed.harvesthoppers.HarvestHoppers.hopper;
import static codes.bed.harvesthoppers.utils.Text.format;

@SuppressWarnings("unused")
@CommandAlias("harvesthoppers|harvesthopper|hh|hhoppers|hoppers")
public class HarvestHoppersCommand extends BaseCommand {


    @Default
    @Subcommand("help")
    public void help(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(format("&f&m                                        "));
            sender.sendMessage(format("&b&sHarvestHoppers"));
            sender.sendMessage(format("&a&sVersion: &e&s" + Instance.getDescription().getVersion()));
            if(sender.hasPermission("harvesthoppers.admin")) {
                sender.sendMessage(format("&f&sCommands:"));
                sender.sendMessage(format("&6◦ &b&s/hh &f&shelp &8- &7&sView this help message"));
                sender.sendMessage(format("&6◦ &b&s/hh &f&sreload &8- &7&sReload the plugin"));
                sender.sendMessage(format("&6◦ &b&s/hh &f&sgive &e&s<item> <player> &8- &7&sGive a player a Crops Hopper"));
            }
            sender.sendMessage(format("&f&m                                        "));


        } else {
            sender.sendMessage(format("&f&m                                        "));
            sender.sendMessage(format("&bHarvestHoppers"));
            sender.sendMessage(format("&aVersion: &e" + Instance.getDescription().getVersion()));
            sender.sendMessage(format("&fCommands:"));
            sender.sendMessage(format("&6◦ &b/hh &fhelp &8- &7View this help message"));
            sender.sendMessage(format("&6◦ &b/hh &freload &8- &7Reload the plugin"));
            sender.sendMessage(format("&6◦ &b/hh &fgive &e<item> <player> &8- &7Give a player a Crops Hopper"));
            sender.sendMessage(format("&f&m                                        "));
        }

    }


    @Subcommand("give")
    @Description("Give a player a certain amount of items")
    @CommandCompletion("@hoppers")
    @CommandPermission("harvesthoppers.admin")
    public void give(CommandSender sender, String item, Player player) {
        if (item.equalsIgnoreCase("crops")) {
            ItemStack cropsHopper = hopper.item();
            player.getInventory().addItem(cropsHopper);
            player.sendMessage(format("&7&sYou have been given " + 1 + " Crops Hoppers."));
        } else {
            sender.sendMessage(format("&sInvalid item. Please use 'crops' to give a Crops Hopper."));
        }
    }


    @Subcommand("reload")
    @CommandPermission("harvesthoppers.admin")
    @Description("Reloads the HarvestHoppers plugin configuration")
    public void reload(CommandSender sender) {
        Config.reloadConfig();

        if (sender instanceof Player) {
            sender.sendMessage(format("&a&sHarvestHoppers configuration reloaded successfully!"));
        } else {
            sender.sendMessage(format("&aHarvestHoppers configuration reloaded successfully!"));
        }

    }


}