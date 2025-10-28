package fr.constantdevs;

import fr.constantdevs.naturalcompass.command.NaturalCompassCommand;
import fr.constantdevs.naturalcompass.compass.BiomeFinder;
import fr.constantdevs.naturalcompass.config.ConfigManager;
import fr.constantdevs.naturalcompass.item.CompassItemManager;
import fr.constantdevs.naturalcompass.listener.CompassInteractionListener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xenondevs.invui.InvUI;

public final class NaturalCompass extends JavaPlugin {
    private static NaturalCompass instance;
    private ConfigManager configManager;
    private CompassItemManager compassItemManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize InvUI
        InvUI.getInstance().setPlugin(this);

        // Initialize managers
        configManager = new ConfigManager(this);
        compassItemManager = new CompassItemManager(this);

        // Register commands
        getCommand("naturalcompass").setExecutor(new NaturalCompassCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new CompassInteractionListener(this), this);

        getLogger().info("Natural Compass plugin enabled!");
    }

    @Override
    public void onDisable() {
        BiomeFinder.stopAllTasks();
        getLogger().info("Natural Compass plugin disabled!");
    }

    public static NaturalCompass getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CompassItemManager getCompassItemManager() {
        return compassItemManager;
    }
}
