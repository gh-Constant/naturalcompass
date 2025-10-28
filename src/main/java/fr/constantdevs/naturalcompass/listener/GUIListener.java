package fr.constantdevs.naturalcompass.listener;

import fr.constantdevs.naturalcompass.NaturalCompass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public class GUIListener implements Listener {

    private final NaturalCompass plugin;

    public GUIListener(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws SerializationException {
        Component title = event.getView().title();
        String plainTitle = PlainTextComponentSerializer.plainText().serialize(title);

        if (plainTitle.startsWith("Select a Biome")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getItemMeta() == null) {
                return;
            }

            String itemName = PlainTextComponentSerializer.plainText().serialize(clickedItem.getItemMeta().displayName());

            if (itemName.equals("Next Page")) {
                int currentPage = Integer.parseInt(plainTitle.substring(plainTitle.indexOf("Page ") + 5, plainTitle.indexOf("/")));
                int pageCount = Integer.parseInt(plainTitle.substring(plainTitle.indexOf("/") + 1, plainTitle.indexOf(")")));
                List<String> biomes = plugin.getGuiManager().getBiomesForDimension(player.getWorld().getEnvironment());
                plugin.getGuiManager().openBiomeSelectionGUI(player, currentPage + 1, pageCount, biomes);
            } else if (itemName.equals("Previous Page")) {
                int currentPage = Integer.parseInt(plainTitle.substring(plainTitle.indexOf("Page ") + 5, plainTitle.indexOf("/")));
                int pageCount = Integer.parseInt(plainTitle.substring(plainTitle.indexOf("/") + 1, plainTitle.indexOf(")")));
                List<String> biomes = plugin.getGuiManager().getBiomesForDimension(player.getWorld().getEnvironment());
                plugin.getGuiManager().openBiomeSelectionGUI(player, currentPage - 1, pageCount, biomes);
            } else {
                // Set target biome
                plugin.getSearchManager().setTargetBiome(player, itemName);
                player.closeInventory();
                player.sendMessage(Component.text("Target biome set to " + itemName, net.kyori.adventure.text.format.NamedTextColor.GREEN));
            }
        } else if (plainTitle.equals("NaturalCompass Admin")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getItemMeta() == null) {
                return;
            }

            String itemName = PlainTextComponentSerializer.plainText().serialize(clickedItem.getItemMeta().displayName());

            switch (itemName) {
                case "Biome Exclusion":
                    // Open Biome Exclusion GUI
                    break;
                case "Toggle Recipes":
                    plugin.getConfigManager().setRecipesEnabled(!plugin.getConfigManager().isRecipesEnabled());
                    plugin.getConfigManager().save();
                    player.sendMessage(Component.text("Recipes toggled.", NamedTextColor.GREEN));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    plugin.getGuiManager().openAdminGUI(player);
                    break;
                case "Toggle Coordinates":
                    plugin.getConfigManager().setShowCoordinates(!plugin.getConfigManager().isShowCoordinates());
                    plugin.getConfigManager().save();
                    player.sendMessage(Component.text("Coordinates toggled.", NamedTextColor.GREEN));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    plugin.getGuiManager().openAdminGUI(player);
                    break;
                case "Reload Config":
                    plugin.reload();
                    player.sendMessage(Component.text("Config reloaded.", NamedTextColor.GREEN));
                    break;
            }
        }
    }
}