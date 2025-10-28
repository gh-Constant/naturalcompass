package fr.constantdevs.naturalcompass.gui;

import fr.constantdevs.naturalcompass.config.ConfigManager;
import fr.constantdevs.naturalcompass.util.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminGUI {

    public static Inventory createAdminGUI(ConfigManager configManager) {
        Inventory gui = Bukkit.createInventory(null, 27, Component.text("NaturalCompass Admin"));

        // Biome Exclusion Item
        ItemStack biomeExclusion = new ItemStack(Material.BARRIER);
        ItemMeta biomeExclusionMeta = biomeExclusion.getItemMeta();
        if (biomeExclusionMeta != null) {
            biomeExclusionMeta.displayName(Component.text("Biome Exclusion"));
            biomeExclusion.setItemMeta(biomeExclusionMeta);
        }
        gui.setItem(10, biomeExclusion);

        // Show Coordinates Enabled/Disabled Item
        ItemStack coordinates = new ItemStack(Material.MAP);
        ItemMeta coordinatesMeta = coordinates.getItemMeta();
        if (coordinatesMeta != null) {
            coordinatesMeta.displayName(Component.text("Toggle Coordinates"));
            coordinates.setItemMeta(coordinatesMeta);
        }
        Utils.addStateLore(coordinates, configManager.isShowCoordinates());
        gui.setItem(14, coordinates);

        return gui;
    }
}