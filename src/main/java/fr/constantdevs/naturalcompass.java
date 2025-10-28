package fr.constantdevs.naturalcompass;

import fr.constantdevs.naturalcompass.config.ConfigManager;
import fr.constantdevs.naturalcompass.crafting.CraftingManager;
import fr.constantdevs.naturalcompass.gui.GUIManager;
import fr.constantdevs.naturalcompass.items.ItemManager;
import fr.constantdevs.naturalcompass.listener.CompassInteractionListener;
import fr.constantdevs.naturalcompass.listener.GUIListener;
import fr.constantdevs.naturalcompass.search.SearchManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NaturalCompass extends JavaPlugin {

    private static NaturalCompass instance;

    private ConfigManager configManager;
    private ItemManager itemManager;
    private CraftingManager craftingManager;
    private GUIManager guiManager;
    private SearchManager searchManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.itemManager = new ItemManager(this);
        this.craftingManager = new CraftingManager(this);
        this.guiManager = new GUIManager(this);
        this.searchManager = new SearchManager(this);

        // Load configurations and recipes
        reload();

        // Register listeners
        getServer().getPluginManager().registerEvents(new CompassInteractionListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        // Register command
        getCommand("naturalcompass").setExecutor(new fr.constantdevs.naturalcompass.command.NaturalCompassCommand(this));

        getLogger().info("NaturalCompass has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NaturalCompass has been disabled!");
    }

    public void reload() {
        configManager.load();
        craftingManager.loadRecipes();
    }

    public static NaturalCompass getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public SearchManager getSearchManager() {
        return searchManager;
    }
}
