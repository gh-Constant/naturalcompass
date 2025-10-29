package fr.constantdevs.naturalcompass.gui;

import fr.constantdevs.naturalcompass.NaturalCompass;
import fr.constantdevs.naturalcompass.biome.BiomeManager;
import fr.constantdevs.naturalcompass.util.Utils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class GUIManager {

    private final NaturalCompass plugin;
    private final Map<UUID, BiomeExclusionGUI> exclusionGUIs = new HashMap<>();

    public GUIManager(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    public void openBiomeSelectionGUI(Player player) {
        BiomeSelectionGUI biomeSelectionGUI = new BiomeSelectionGUI(player);
        biomeSelectionGUI.displayPage(0);
        biomeSelectionGUI.open(player);
    }

    public List<String> getBiomesForDimension(World.Environment environment) {
        return BiomeManager.getAllBiomeNamesInDimension(environment);
    }

    public void openAdminGUI(Player player) {
        Inventory gui = AdminGUI.createAdminGUI(plugin.getConfigManager());
        Utils.addStateLore(Objects.requireNonNull(gui.getItem(14)), plugin.getConfigManager().isShowCoordinates());
        player.openInventory(gui);
    }
    public void openBiomeExclusionGUI(Player player, World.Environment environment) {
        BiomeExclusionGUI biomeExclusionGUI = new BiomeExclusionGUI(plugin, environment);
        exclusionGUIs.put(player.getUniqueId(), biomeExclusionGUI);
        biomeExclusionGUI.displayPage(0);
        biomeExclusionGUI.open(player);
    }

    public void openWorldSelectionGUI(Player player) {
        WorldSelectionGUI worldSelectionGUI = new WorldSelectionGUI();
        worldSelectionGUI.open(player);
    }

    public BiomeExclusionGUI getBiomeExclusionGUI(Player player) {
        return exclusionGUIs.get(player.getUniqueId());
    }

    public Map<UUID, BiomeExclusionGUI> getExclusionGUIs() {
        return exclusionGUIs;
    }
}