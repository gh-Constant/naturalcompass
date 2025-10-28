package fr.constantdevs.naturalcompass.gui;

import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;
import fr.constantdevs.NaturalCompass;
import fr.constantdevs.naturalcompass.compass.BiomeFinder;
import fr.constantdevs.naturalcompass.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BiomeExclusionGUI {

    private final ConfigManager configManager;
    private final NaturalCompass plugin;
    private final List<Biome> items;

    public BiomeExclusionGUI(NaturalCompass plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.items = BiomeFinder.getAvailableBiomes();
    }
    
    public void open(Player player) {
        PagedGui<Biome> gui = PagedGui.items()
                .title("Exclude Biomes")
                .rows(6)
                .pageSize(45)
                .items(items)
                .itemBuilder(this::createBiomeItem)
                .build();
                
        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .build();
                
        window.open();
    }
    
    private SimpleItem createBiomeItem(Biome biome) {
        boolean isExcluded = configManager.getExcludeBiomes().contains(biome.name());
        return new SimpleItem(
            ItemBuilder.from(isExcluded ? Material.BARRIER : Material.GRASS_BLOCK)
                .setDisplayName("§a" + biome.name())
                .setLore(List.of(
                    isExcluded ? "§cCurrently excluded" : "§aCurrently included",
                    "§7Click to " + (isExcluded ? "include" : "exclude") + " this biome."
                ))
                .build(),
            event -> {
                handleItemClick((Player) event.getPlayer(), biome);
                // Refresh GUI after click
                new BiomeExclusionGUI(plugin).open((Player) event.getPlayer());
            }
        );
    }
    
    // Method removed as it's now handled in createBiomeItem
    
    private void handleItemClick(Player player, Biome biome) {
        List<String> excludedBiomes = configManager.getExcludeBiomes();
        String biomeName = biome.name();
        
        if (excludedBiomes.contains(biomeName)) {
            excludedBiomes.remove(biomeName);
            player.sendMessage("§aThe " + biomeName + " biome is now included in searches.");
        } else {
            excludedBiomes.add(biomeName);
            player.sendMessage("§cThe " + biomeName + " biome is now excluded from searches.");
        }
        
        configManager.saveConfig();
        
        // GUI will be reopened by the click handler
    }
}