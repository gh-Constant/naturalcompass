package fr.constantdevs.naturalcompass.gui;

import fr.constantdevs.naturalcompass.NaturalCompass;
import fr.constantdevs.naturalcompass.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIManager {

    private final NaturalCompass plugin;

    public GUIManager(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    public void openBiomeSelectionGUI(Player player) {
        List<String> biomes = getBiomesForDimension(player.getWorld().getEnvironment());
        int pageCount = (int) Math.ceil(biomes.size() / 45.0);
        openBiomeSelectionGUI(player, 1, pageCount, biomes);
    }

    public void openBiomeSelectionGUI(Player player, int page, int pageCount, List<String> biomes) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("Select a Biome (Page " + page + "/" + pageCount + ")"));

        int startIndex = (page - 1) * 45;
        for (int i = 0; i < 45 && startIndex + i < biomes.size(); i++) {
            String biome = biomes.get(startIndex + i);
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(biome, NamedTextColor.GREEN));
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
        }

        // Navigation items
        if (page > 1) {
            ItemStack previous = new ItemStack(Material.ARROW);
            ItemMeta meta = previous.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("Previous Page", NamedTextColor.YELLOW));
                previous.setItemMeta(meta);
            }
            gui.setItem(45, previous);
        }
        if (page < pageCount) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta meta = next.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("Next Page", NamedTextColor.YELLOW));
                next.setItemMeta(meta);
            }
            gui.setItem(53, next);
        }

        player.openInventory(gui);
    }

    public List<String> getBiomesForDimension(World.Environment environment) {
        // This is a simplified list. A more robust implementation would be needed for full biome support.
        switch (environment) {
            case NORMAL:
                return Arrays.asList("PLAINS", "DESERT", "FOREST", "TAIGA", "SWAMP", "JUNGLE", "OCEAN");
            case NETHER:
                return Arrays.asList("NETHER_WASTES", "SOUL_SAND_VALLEY", "CRIMSON_FOREST", "WARPED_FOREST", "BASALT_DELTAS");
            case THE_END:
                return Arrays.asList("THE_END", "SMALL_END_ISLANDS", "END_MIDLANDS", "END_HIGHLANDS", "END_BARRENS");
            default:
                return new ArrayList<>();
        }
    }

    public void openAdminGUI(Player player) {
        Inventory gui = AdminGUI.createAdminGUI(plugin.getConfigManager());
        Utils.addStateLore(gui.getItem(14), plugin.getConfigManager().isShowCoordinates());
        player.openInventory(gui);
    }
}