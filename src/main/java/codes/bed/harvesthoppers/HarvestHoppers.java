package codes.bed.harvesthoppers;

import co.aikar.commands.BukkitCommandManager;

import codes.bed.harvesthoppers.commands.HarvestHoppersCommand;
import codes.bed.harvesthoppers.hoppers.CropsHopper;
import codes.bed.harvesthoppers.utils.Config;
import codes.bed.harvesthoppers.utils.Logger;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HarvestHoppers extends JavaPlugin {

    public BukkitCommandManager manager;
    public static HarvestHoppers Instance;
    public static CropsHopper hopper;

    @Override
    public void onEnable() {
        Instance = this;
        Config.init();

        Logger.setInstance(this);
        hopper = new CropsHopper();
        hopper.startHarvesters();

        manager = new BukkitCommandManager(this);

        Logger.log("§aHarvestHoppers has been enabled.");



        manager.getCommandCompletions().registerCompletion("hoppers", c -> {
            return ImmutableList.of("crops");
        });

        manager.registerCommand(new HarvestHoppersCommand());

        Bukkit.getServer().getPluginManager().registerEvents(hopper, this);

    }

    @Override
    public void onDisable() {
        Logger.log("§4HarvestHoppers has been disabled.");

    }
}