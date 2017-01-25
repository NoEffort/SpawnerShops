package me.noeffort.spawnershops;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemListeners implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if (!e.getInventory().equals(Main.get().getSpawnerInventory())) {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        e.setCancelled(true);

        SpawnShopItem item;

        if ((item = ItemUtil.getSpawnerItem(e.getCurrentItem())) != null)
        {
            switch (item.getType()) {
                case "CANCEL":
                    p.closeInventory();
                    break;
                case "NEXT":
                    Main.get().openEggGUI(p);
                    break;
                default:
                    if (Main.getEcon().getBalance(p) < item.getCost()) {
                        p.sendMessage("§cYou cannot afford this item!");
                        e.setCancelled(true);
                        break;
                    }
                    EconomyResponse r = Main.getEcon().withdrawPlayer(p, item.getCost());
                    if (r.transactionSuccess()) {
                        p.getInventory().addItem(item.getReturnedItem());
                        p.closeInventory();
                    }
                    else
                        p.sendMessage("§4ERROR: Unexpected Results");

                    break;
            }

        }
    }

}
