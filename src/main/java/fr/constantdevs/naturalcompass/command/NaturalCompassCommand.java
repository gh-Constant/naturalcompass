package fr.constantdevs.naturalcompass.command;

import fr.constantdevs.NaturalCompass;
import fr.constantdevs.naturalcompass.gui.AdminGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NaturalCompassCommand implements CommandExecutor {
    private final NaturalCompass plugin;

    public NaturalCompassCommand(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (!player.hasPermission("naturalcompass.admin")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("setup") || args[0].equalsIgnoreCase("settings")) {
            new AdminGUI(plugin).open(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                player.sendMessage("§cUsage: /" + label + " give <player>");
                return true;
            }

            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage("§cPlayer not found!");
                return true;
            }

            plugin.getCompassItemManager().giveCompass(target);
            return true;
        }

        player.sendMessage("§cUnknown subcommand. Use /" + label + " setup or /" + label + " give <player>");
        return true;
    }
}