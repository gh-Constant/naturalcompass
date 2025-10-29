package fr.constantdevs.naturalcompass.gui;

import fr.constantdevs.naturalcompass.NaturalCompass;
import fr.constantdevs.naturalcompass.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BiomeSelectionGUI extends PaginatedGUI {

    private final NaturalCompass plugin;
    private final List<String> biomes;

    public BiomeSelectionGUI(Player player) {
        this(player, 0);
    }

    public BiomeSelectionGUI(Player player, int initialPage) {
        this.plugin = NaturalCompass.getInstance();
        this.biomes = new ArrayList<>(plugin.getGuiManager().getBiomesForDimension(player.getWorld().getEnvironment()));
        this.biomes.removeAll(plugin.getConfigManager().getExcludedBiomes());
        Collections.sort(this.biomes);
        this.totalPages = (int) Math.ceil((double) this.biomes.size() / maxItemsPerPage);
        this.page = Math.max(0, Math.min(initialPage, totalPages - 1)); // Clamp to valid range
    }

    @Override
    public void displayPage(int page) {
        this.page = page;
        this.inventory = Bukkit.createInventory(null, 54, Component.text("Select a Biome (" + (page + 1) + "/" + totalPages + ")"));

        addBorder();
        addNavigationButtons();

        int startIndex = page * maxItemsPerPage;
        for (int i = 0; i < maxItemsPerPage; i++) {
            int biomeIndex = startIndex + i;
            if (biomeIndex >= biomes.size()) {
                break;
            }
            String biomeName = biomes.get(biomeIndex);
            ItemStack biomeItem = createBiomeItem(biomeName);
            addItem(biomeItem);
        }
    }

    private ItemStack createBiomeItem(String biomeName) {
        Material material = getBiomeMaterial(biomeName);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(Utils.formatBiomeName(biomeName), NamedTextColor.GREEN));
            item.setItemMeta(meta);
        }
        return item;
    }

    private Material getBiomeMaterial(String biomeName) {
        switch (biomeName) {
            case "minecraft:plains": return Material.GRASS_BLOCK;
            case "minecraft:sunflower_plains": return Material.SUNFLOWER;
            case "minecraft:snowy_plains": return Material.SNOW_BLOCK;
            case "minecraft:ice_spikes": return Material.ICE;
            case "minecraft:desert": return Material.SAND;
            case "minecraft:swamp": return Material.LILY_PAD;
            case "minecraft:mangrove_swamp": return Material.MANGROVE_ROOTS;
            case "minecraft:forest": return Material.OAK_LOG;
            case "minecraft:flower_forest": return Material.POPPY;
            case "minecraft:birch_forest": return Material.BIRCH_LOG;
            case "minecraft:dark_forest": return Material.DARK_OAK_LOG;
            case "minecraft:taiga": return Material.SPRUCE_LOG;
            case "minecraft:snowy_taiga": return Material.SNOW;
            case "minecraft:jungle": return Material.JUNGLE_LOG;
            case "minecraft:bamboo_jungle": return Material.BAMBOO;
            case "minecraft:badlands": return Material.RED_SAND;
            case "minecraft:savanna": return Material.ACACIA_LOG;
            case "minecraft:mushroom_fields": return Material.RED_MUSHROOM_BLOCK;
            case "minecraft:mountain": return Material.STONE;
            case "minecraft:ocean": return Material.WATER_BUCKET;
            case "minecraft:deep_ocean": return Material.BLUE_STAINED_GLASS;
            case "minecraft:cold_ocean": return Material.BLUE_ICE;
            case "minecraft:lukewarm_ocean": return Material.LIGHT_BLUE_STAINED_GLASS;
            case "minecraft:warm_ocean": return Material.YELLOW_STAINED_GLASS;
            case "minecraft:deep_cold_ocean": return Material.PACKED_ICE;
            case "minecraft:deep_lukewarm_ocean": return Material.CYAN_STAINED_GLASS;
            case "minecraft:deep_warm_ocean": return Material.ORANGE_STAINED_GLASS;
            case "minecraft:dripstone_caves": return Material.DRIPSTONE_BLOCK;
            case "minecraft:lush_caves": return Material.MOSS_BLOCK;
            case "minecraft:crimson_forest": return Material.CRIMSON_STEM;
            case "minecraft:soul_sand_valley": return Material.SOUL_SAND;
            case "minecraft:basalt_deltas": return Material.BASALT;
            case "minecraft:nether_wastes": return Material.NETHERRACK;
            case "minecraft:warped_forest": return Material.WARPED_STEM;
            case "minecraft:the_end": return Material.END_STONE;
            case "minecraft:end_highlands": return Material.END_STONE;
            case "minecraft:end_midlands": return Material.END_STONE;
            case "minecraft:end_barrens": return Material.END_STONE;
            case "minecraft:cherry_grove": return Material.CHERRY_LOG;
            case "minecraft:pale_garden": return Material.PALE_OAK_LOG;
            default: return Material.GRASS_BLOCK;
        }
    }

    public void handleClick(Player player, ItemStack clickedItem) {
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        if (clickedItem.isSimilar(fr.constantdevs.naturalcompass.items.ItemManager.NEXT_PAGE)) {
            if (page < totalPages - 1) {
                BiomeSelectionGUI newGUI = new BiomeSelectionGUI(player, page + 1);
                newGUI.displayPage(page + 1);
                newGUI.open(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            }
        } else if (clickedItem.isSimilar(fr.constantdevs.naturalcompass.items.ItemManager.PREVIOUS_PAGE)) {
            if (page > 0) {
                BiomeSelectionGUI newGUI = new BiomeSelectionGUI(player, page - 1);
                newGUI.displayPage(page - 1);
                newGUI.open(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            }
        } else if (isBiomeItem(clickedItem)) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;

            String biomeName = Utils.revertFormattedBiomeName(Utils.componentToString(meta.displayName()));
            plugin.getSearchManager().setTargetBiome(player, biomeName);
            player.closeInventory();
            player.sendMessage(Component.text("Target biome set to " + biomeName, NamedTextColor.GREEN));
        }
    }

    private boolean isBiomeItem(ItemStack item) {
        if (item == null) return false;
        Material type = item.getType();
        return type != Material.AIR && type != Material.BLACK_STAINED_GLASS_PANE;
    }
}
