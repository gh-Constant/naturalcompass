package fr.constantdevs.naturalcompass.listener;

import fr.constantdevs.NaturalCompass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record PlayerMovementListener(NaturalCompass plugin) implements Listener {

    private static final Map<UUID, Location> lastLocations = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.distance(from) < 0.5 && Math.abs(to.getYaw() - from.getYaw()) < 10) return;

        lastLocations.put(playerId, to);

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (plugin.getItemManager().isNaturalCompass(mainHand)) {
            // Check if player has a target set
            if (plugin.getSearchManager().hasTarget(player)) {
                plugin.getItemManager().updateCompassRotation(mainHand, plugin.getSearchManager().getTargetLocation(player), player);
            }
        }
    }
}