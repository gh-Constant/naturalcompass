package fr.constantdevs.naturalcompass.listener;

import fr.constantdevs.NaturalCompass;
import fr.constantdevs.naturalcompass.gui.BiomeSelectionGUI;
import fr.constantdevs.naturalcompass.item.CompassItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CompassInteractionListener implements Listener {
    private final NaturalCompass plugin;

    public CompassInteractionListener(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null || !CompassItemManager.isNaturalCompass(item)) {
            return;
        }

        // Cancel the default compass behavior
        event.setCancelled(true);

        // Open biome selection GUI
        new BiomeSelectionGUI(plugin).open(event.getPlayer());
    }
}