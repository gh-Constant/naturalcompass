package fr.constantdevs.naturalcompass.listener;

import fr.constantdevs.naturalcompass.NaturalCompass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMovementListener implements Listener {

    private final NaturalCompass plugin;

    public PlayerMovementListener(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand != null && plugin.getItemManager().isNaturalCompass(mainHand)) {
            // Check if player has a target set
            if (plugin.getSearchManager().hasTarget(player)) {
                plugin.getItemManager().updateCompassRotation(mainHand, plugin.getSearchManager().getTargetLocation(player), player);
            }
        }
    }
}