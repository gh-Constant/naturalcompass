package fr.constantdevs.naturalcompass.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WorldSelectionGUI {

    private final Inventory inventory;

    public WorldSelectionGUI() {
        this.inventory = Bukkit.createInventory(null, 27, Component.text("Select World for Biome Exclusion"));

        List<World> worlds = Bukkit.getWorlds();
        int slot = 10; // center-ish
        for (World world : worlds) {
            ItemStack item = new ItemStack(getWorldIcon(world.getEnvironment()));
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(world.getName()));
                item.setItemMeta(meta);
            }
            inventory.setItem(slot++, item);
        }
    }

    private Material getWorldIcon(World.Environment environment) {
        return switch (environment) {
            case NORMAL -> Material.GRASS_BLOCK;
            case NETHER -> Material.NETHERRACK;
            case THE_END -> Material.END_STONE;
            default -> Material.STONE;
        };
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
