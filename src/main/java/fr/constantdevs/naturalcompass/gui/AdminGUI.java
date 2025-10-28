package fr.constantdevs.naturalcompass.gui;

import fr.constantdevs.NaturalCompass;
import fr.constantdevs.naturalcompass.config.ConfigManager;
import fr.constantdevs.naturalcompass.item.CompassItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class AdminGUI {

    private final NaturalCompass plugin;
    private final ConfigManager configManager;

    public AdminGUI(NaturalCompass plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void open(Player player) {
        Gui gui = Gui.normal()
                .title("Natural Compass Setup")
                .rows(3)
                .create();

        gui.setItem(10, createToggleExternalBiomesItem());
        gui.setItem(13, createExcludeBiomesItem());
        gui.setItem(16, createGiveCompassItem());

        gui.open(player);
    }

    private SimpleItem createToggleExternalBiomesItem() {
        boolean useExternalBiomes = configManager.isUseExternalBiomes();
        return new SimpleItem(
                ItemBuilder.from(useExternalBiomes ? Material.GRASS_BLOCK : Material.BARRIER)
                        .setDisplayName("§aUse External Biomes")
                        .setLore(
                                "§7Include biomes from datapacks and plugins.",
                                "§7Currently: " + (useExternalBiomes ? "§aEnabled" : "§cDisabled"),
                                "§7Click to toggle."
                        )
                        .build(),
                event -> {
                    configManager.setUseExternalBiomes(!configManager.isUseExternalBiomes());
                    new AdminGUI(plugin).open((Player) event.getWhoClicked());
                }
        );
    }

    private SimpleItem createExcludeBiomesItem() {
        return new SimpleItem(
                ItemBuilder.from(Material.BARRIER)
                        .setDisplayName("§cExclude Biomes")
                        .setLore(
                                "§7Current excluded biomes:",
                                "§8" + String.join(", ", configManager.getExcludeBiomes()),
                                "§7Click to manage excluded biomes."
                        )
                        .build(),
                event -> {
                    new BiomeExclusionGUI(plugin).open((Player) event.getWhoClicked());
                }
        );
    }

    private SimpleItem createGiveCompassItem() {
        return new SimpleItem(
                ItemBuilder.from(Material.COMPASS)
                        .setDisplayName("§6Give Natural Compass")
                        .setLore("§7Click to give yourself a compass.")
                        .build(),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    CompassItemManager.giveCompass(player);
                }
        );
    }
}