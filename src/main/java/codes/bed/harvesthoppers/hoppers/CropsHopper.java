package codes.bed.harvesthoppers.hoppers;

import codes.bed.harvesthoppers.utils.Config;
import codes.bed.harvesthoppers.utils.Logger;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static codes.bed.harvesthoppers.HarvestHoppers.Instance;
import static codes.bed.harvesthoppers.utils.Text.format;

public class CropsHopper implements Listener {

    private final int customModeldata = 512673812;
    private BukkitTask harvestTask;
    private Connection connection;

    public CropsHopper() {
        Logger.debug("Initializing CropsHopper");
        try {
            Logger.debug("Loading SQLite JDBC driver");
            Class.forName("org.sqlite.JDBC");
            Logger.debug("Connecting to database");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/HarvestHoppers/hoppers.db");
            createTable();
        } catch (ClassNotFoundException | SQLException e) {
            Logger.error("&cFailed to connect to database");
        }
    }

    private void createTable() throws SQLException {
        Logger.debug("Creating hoppers table if not exists");
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS hoppers (id INTEGER PRIMARY KEY, x INTEGER, y INTEGER, z INTEGER, world TEXT)");
        statement.executeUpdate();
        Logger.debug("Table created or already exists");
    }

    public ItemStack item(int amount) {
        Logger.debug("Creating item with amount: " + amount);
        ItemStack item = new ItemStack(Material.HOPPER, amount);
        ItemMeta meta = item.getItemMeta();

        Logger.debug("Setting item meta display name and lore");
        meta.setDisplayName(format(Config.String("crops.name", "&b&sCrops Hopper")));
        meta.setCustomModelData(customModeldata);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> defaultlore = new ArrayList<>(Arrays.asList(
                format("&eA magical hopper imbued with the essence of nature."),
                format("&eAutomatically harvests crops in its vicinity."),
                format("&eA valuable tool for any farmer or adventurer.")
        ));
        List<String> lore = format(Config.List("crops.lore", defaultlore));

        meta.setLore(lore);

        item.setItemMeta(meta);
        Logger.debug("Item meta set");
        return item;
    }

    public ItemStack item() {
        Logger.debug("Creating item with default amount");
        return item(1);
    }

    private void harvestCrops(Block block) {
        Logger.debug("Harvesting crops for block at: " + block.getLocation());
        Chunk chunk = block.getChunk();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        for (int x = chunkX * 16; x < (chunkX + 1) * 16; x++) {
            for (int z = chunkZ * 16; z < (chunkZ + 1) * 16; z++) {
                Block b = block.getWorld().getBlockAt(x, block.getY(), z);

                if (b.getBlockData() instanceof Ageable) {
                    Ageable crop = (Ageable) b.getBlockData();

                    Logger.debug("Found crop at: " + b.getLocation() + " of type " + b.getType());
                    Logger.debug("Maximum age: " + crop.getMaximumAge());
                    Logger.debug("Age: " + crop.getAge());
                    Logger.debug("Is stackable: " + isStackable(crop));
                    if(isStackable(crop)) {
                        harvestStackablePlant(b, block);
                    }
                    else if (crop.getAge() == crop.getMaximumAge()) {
                        Logger.debug("Crop is fully grown, harvesting");
                        ItemStack cropItem = new ItemStack(b.getType(), 1);
                        Hopper cont = (Hopper) block.getState();
                        Inventory inv = cont.getInventory();
                        inv.addItem(cropItem);

                        crop.setAge(0);
                        b.setBlockData(crop);

                        Logger.debug("Crop harvested and reset");
                    }
                }
            }
        }
    }

    private boolean isStackable(Ageable crop) {
        Logger.debug("Checking if type is stackable plant: " + crop.getMaterial());
        int age = crop.getMaximumAge();
        return age == 1 || age == 15;
    }

    private void harvestStackablePlant(Block baseBlock, Block hopper) {
        Logger.debug("Harvesting stackable plant " + baseBlock.getType() +" at: " + baseBlock.getLocation());
        Hopper cont = (Hopper) hopper.getState();
        Inventory inv = cont.getInventory();
        List<Block> blocks = new ArrayList<>();
        int amount = 0;
        for(int i = 1; i < 100; i++) {
            Block b = baseBlock.getWorld().getBlockAt((int) baseBlock.getLocation().getX(), (int) (baseBlock.getLocation().getY()+  i), (int) baseBlock.getLocation().getZ());

            if(b.getType() == baseBlock.getType() || b.getType().toString().startsWith(baseBlock.getType().toString())) {
                blocks.add(b);
                amount++;
            }
            if(b.getType() != baseBlock.getType()) {
                Logger.debug("Found empty space above plant, found " + amount + " plants");

                for (Block b2 : blocks.reversed()) {
                    b2.setType(Material.AIR);
                };

                if(amount != 0) inv.addItem(new ItemStack(baseBlock.getType(), amount));
                Logger.debug("Added " + amount + " to hopper");
                break;
            }
        }
    }

    public void saveHopper(Block block) {
        Logger.debug("Saving hopper at: " + block.getLocation());
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO hoppers (x, y, z, world) VALUES (?, ?, ?, ?)");
            statement.setInt(1, block.getX());
            statement.setInt(2, block.getY());
            statement.setInt(3, block.getZ());
            statement.setString(4, block.getWorld().getName());
            statement.executeUpdate();
            Logger.debug("Hopper saved to database");
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }

    public void startHarvesting(Block block) {
        Logger.debug("Starting harvesting for block at: " + block.getLocation());
        stopHarvesting();
        harvestTask = new BukkitRunnable() {
            @Override
            public void run() {
                Logger.debug("Running harvest task for block at: " + block.getLocation());
                Particle particle = new ParticleBuilder(Particle.VILLAGER_HAPPY).particle();
                block.getLocation().getWorld().spawnParticle(particle, new Location(block.getLocation().getWorld(), block.getLocation().getX(), block.getLocation().getY(), block.getLocation().getZ()), 5);
                harvestCrops(block);
            }
        }.runTaskTimer(Instance, 20, 20);
    }

    public void stopHarvesting() {
        Logger.debug("Stopping harvesting task");
        if (harvestTask != null) {
            harvestTask.cancel();
            harvestTask = null;
            Logger.debug("Harvest task canceled");
        }
    }

    public void startHarvesters() {
        Logger.debug("Starting all harvesters from database");
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hoppers");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                int z = resultSet.getInt("z");
                String worldName = resultSet.getString("world");

                Logger.debug("Found hopper in database at: (" + x + ", " + y + ", " + z + ")");
                org.bukkit.World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    Logger.error("World '" + worldName + "' not found! Skipping hopper at (" + x + ", " + y + ", " + z + ").");
                    continue;
                }

                Block block = world.getBlockAt(x, y, z);
                startHarvesting(block);
            }
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        Logger.debug("Block placed by player at: " + block.getLocation());
        if (item.getType() == Material.HOPPER && item.getItemMeta().hasCustomModelData() &&
                item.getItemMeta().getCustomModelData() == customModeldata) {
            Logger.debug("Custom hopper placed, saving and starting harvesting");
            saveHopper(block);
            startHarvesting(block);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Logger.debug("Block broken at: " + block.getLocation());
        if (block.getType() == Material.HOPPER) {
            try {
                Logger.debug("Hopper broken, checking database for matching entry");
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM hoppers WHERE x = ? AND y = ? AND z = ? AND world = ?");
                statement.setInt(1, block.getX());
                statement.setInt(2, block.getY());
                statement.setInt(3, block.getZ());
                statement.setString(4, block.getWorld().getName());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Logger.debug("Hopper found in database, stopping harvesting and removing");
                    stopHarvesting();
                    removeHopper(block);
                    event.setDropItems(false);
                    block.getWorld().dropItemNaturally(block.getLocation(), item());
                }
            } catch (SQLException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    private void removeHopper(Block block) {
        Logger.debug("Removing hopper from database at: " + block.getLocation());
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM hoppers WHERE x = ? AND y = ? AND z = ? AND world = ?");
            statement.setInt(1, block.getX());
            statement.setInt(2, block.getY());
            statement.setInt(3, block.getZ());
            statement.setString(4, block.getWorld().getName());
            statement.executeUpdate();
            Logger.debug("Hopper removed from database");
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
    }
}
