package fr.constantdevs.naturalcompass.items;

import fr.constantdevs.naturalcompass.NaturalCompass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final NaturalCompass plugin;
    private final NamespacedKey tierKey;

    public ItemManager(NaturalCompass plugin) {
        this.plugin = plugin;
        this.tierKey = new NamespacedKey(plugin, "compass_tier");
    }

    public ItemStack createCompass(int tier) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();

        if (meta != null) {
            // Set display name and lore
            meta.displayName(Component.text("Natural Compass", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Tier " + tier, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.empty());
            lore.add(Component.text("Right-click to select a biome.", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Left-click to find it.", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            meta.lore(lore);

            // Store tier in PDC
            meta.getPersistentDataContainer().set(tierKey, PersistentDataType.INTEGER, tier);

            // Set custom model data for resource pack
            meta.setCustomModelData(1000 + tier); // Example: Tier 1 = 1001, Tier 2 = 1002

            compass.setItemMeta(meta);
        }

        return compass;
    }

    public int getCompassTier(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return 0;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(tierKey, PersistentDataType.INTEGER, 0);
    }

    public boolean isNaturalCompass(ItemStack item) {
        return getCompassTier(item) > 0;
    }
}