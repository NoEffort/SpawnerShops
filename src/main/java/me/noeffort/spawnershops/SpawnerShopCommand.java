package me.noeffort.spawnershops;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnerShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        // Only players can do this command, if they are not a player, tell the log <Console> that it cannot be completed through it)
        if (!(sender instanceof Player))
        {
            Main.get().getLogger().info("§cSpawnerShops can only be used by players!");
            return true;
        }
        // Creating variable p as the Player. Command cannot be used in Creative Mode as well
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("spawnershops")) {
            if (!p.hasPermission("ss.use") || p.getGameMode().equals(GameMode.CREATIVE)) {
                p.sendMessage("§cYou cannot use SpawnerShops!");
            } else {
                Main.get().openSpawnerGUI(p);
            }
        }
        // Command for reloading the config.yml for SpawnerShops
        if (cmd.getName().equalsIgnoreCase("reloadshop")) {
            if (!p.hasPermission("ss.admin"))
            {
                p.sendMessage("§cInvalid Permissions!");
            }
            else
            {
                Main.get().reloadConfig();
                Main.get().setupSpawnerItems();
                p.sendMessage("§bPlugin Reloaded!");
            }
        }
        return true;
    }
}
