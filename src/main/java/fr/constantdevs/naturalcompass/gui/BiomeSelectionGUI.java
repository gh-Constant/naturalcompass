package fr.constantdevs.naturalcompass.gui;

import fr.constantdevs.NaturalCompass;
import fr.constantdevs.naturalcompass.compass.BiomeFinder;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class BiomeSelectionGUI {

    private final NaturalCompass plugin;
    private final List<Biome> items;

    public BiomeSelectionGUI(NaturalCompass plugin) {
        this.plugin = plugin;
        this.items = BiomeFinder.getAvailableBiomes();
    }
    
    public void open(Player player) {
        PagedGui<Biome> gui = PagedGui.items()
                .title("Select a Biome")
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
        return new SimpleItem(
            ItemBuilder.from(getBiomeMaterial(biome))
                .setDisplayName("§a" + formatBiomeName(biome.name()))
                .setLore(List.of(
                    "§7Click to find this biome.",
                    "§8" + biome.getKey()
                ))
                .build(),
            event -> handleItemClick((Player) event.getPlayer(), biome)
        );
    }
    
    private void handleItemClick(Player player, Biome biome) {
         player.closeInventory();
            
            // Check if there's a previous search and send stop message
            Biome previousBiome = BiomeFinder.getCurrentBiome(player);
            if (previousBiome != null) {
                player.sendMessage("§cStopped previous search for " + formatBiomeName(previousBiome.name()) + ".");
            }

            player.sendMessage("§aSearching for " + biome.name() + "...");

            BiomeFinder.findNearestBiome(player, biome).thenAccept(location -> {
                if (location != null) {
                    player.setCompassTarget(location);
                    if (plugin.getConfigManager().isDisplayCoords()) {
                        player.sendMessage("§aCompass target set to " + biome.name() + " at " +
                                location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
                    } else {
                        player.sendMessage("§aCompass target set to " + biome.name() + ".");
                    }
                } else {
                    player.sendMessage("§cCould not find " + biome.name() + " within a reasonable distance.");
                }
            }).exceptionally(throwable -> {
                player.sendMessage("§cAn error occurred while searching for the biome.");
                throwable.printStackTrace();
                return null;
            });
        }

    private String formatBiomeName(String biomeName) {
        String[] words = biomeName.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return formattedName.toString().trim();
    }

    private Material getBiomeMaterial(Biome biome) {
        String biomeName = biome.name();
        return switch (biomeName) {
            case "PLAINS", "SUNFLOWER_PLAINS" -> Material.GRASS_BLOCK;
            case "FOREST", "FLOWER_FOREST", "WINDSWEPT_FOREST" -> Material.OAK_LOG;
            case "TAIGA", "OLD_GROWTH_PINE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA" -> Material.SPRUCE_LOG;
            case "SWAMP" -> Material.LILY_PAD;
            case "RIVER" -> Material.WATER_BUCKET;
            case "NETHER_WASTES" -> Material.NETHERRACK;
            case "THE_END" -> Material.END_STONE;
            case "FROZEN_OCEAN" -> Material.ICE;
            case "FROZEN_RIVER" -> Material.BLUE_ICE;
            case "SNOWY_TUNDRA", "SNOWY_BEACH" -> Material.SNOW_BLOCK;
            case "MUSHROOM_FIELDS", "MUSHROOM_FIELD_SHORE" -> Material.MYCELIUM;
            case "BEACH", "DESERT" -> Material.SAND;
            case "SAVANNA", "WINDSWEPT_SAVANNA", "SAVANNA_PLATEAU" -> Material.ACACIA_LOG;
            case "BADLANDS", "WOODED_BADLANDS", "ERODED_BADLANDS" -> Material.RED_SAND;
            case "DARK_FOREST" -> Material.DARK_OAK_LOG;
            case "WOODED_HILLS", "FOREST_HILLS", "WINDSWEPT_HILLS" -> Material.OAK_LOG;
            case "TAIGA_HILLS" -> Material.SPRUCE_LOG;
            case "JUNGLE", "JUNGLE_HILLS", "MODIFIED_JUNGLE", "JUNGLE_EDGE", "MODIFIED_JUNGLE_EDGE" ->
                    Material.JUNGLE_LOG;
            case "BAMBOO_JUNGLE", "BAMBOO_JUNGLE_HILLS" -> Material.BAMBOO;
            case "BIRCH_FOREST", "BIRCH_FOREST_HILLS", "TALL_BIRCH_FOREST", "TALL_BIRCH_HILLS", "OLD_GROWTH_BIRCH_FOREST" ->
                    Material.BIRCH_LOG;
            case "GIANT_TREE_TAIGA", "GIANT_TREE_TAIGA_HILLS", "GIANT_SPRUCE_TAIGA", "GIANT_SPRUCE_TAIGA_HILLS" ->
                    Material.PODZOL;
            case "GRASSLAND" -> Material.GRASS_BLOCK;
            case "ICE_SPIKES" -> Material.PACKED_ICE;
            case "WINDSWEPT_GRAVELLY_HILLS" -> Material.GRAVEL;
            case "STONY_SHORE" -> Material.STONE;
            case "MEADOW" -> Material.POPPY;
            case "LUSH_CAVES" -> Material.MOSS_BLOCK;
            case "DRIPSTONE_CAVES" -> Material.DRIPSTONE_BLOCK;
            case "DEEP_DARK" -> Material.SCULK;
            case "MANGROVE_SWAMP" -> Material.MANGROVE_LOG;
            case "CHERRY_GROVE" -> Material.CHERRY_LOG;
            default -> Material.GRASS_BLOCK;
        };
    }
}