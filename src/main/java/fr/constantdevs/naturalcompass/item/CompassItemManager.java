package fr.constantdevs.naturalcompass.item;

import fr.constantdevs.NaturalCompass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CompassItemManager {
    private final NaturalCompass plugin;
    private static final NamespacedKey COMPASS_KEY = new NamespacedKey(NaturalCompass.getInstance(), "natural_compass");

    public CompassItemManager(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    public static ItemStack createCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§6Natural Compass");
            meta.setLore(List.of(
                "§7Right-click to select a biome",
                "§7The compass will point to the nearest location"
            ));

            // Mark this as a natural compass
            meta.getPersistentDataContainer().set(COMPASS_KEY, PersistentDataType.BOOLEAN, true);

            compass.setItemMeta(meta);
        }

        return compass;
    }

    public static boolean isNaturalCompass(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(COMPASS_KEY, PersistentDataType.BOOLEAN);
    }

    public static void giveCompass(Player player) {
        ItemStack compass = createCompass();
        player.getInventory().addItem(compass);
        player.sendMessage(NaturalCompass.getInstance().getConfigManager().getMessage("compass-given")
            .replace("%player%", player.getName()));
    }
}