package me.noeffort.spawnershops;

//Imports
import java.util.*;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  @Getter
  private static Economy econ = null;
  @Getter
  private static HashSet<SpawnShopItem> items;
  private static Main instance;
  @Getter
  private Inventory spawnerInventory;

  static {
    items = new HashSet<>();
  }
  
  /*
  Initializing Vault Eco for this plugin
  */

  @Override
  public void onEnable()
  {
      instance = this;
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    saveDefaultConfig();
    if (!setupEconomy())
    {
      getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    setupSpawnerItems();
    getCommand("spawnershops").setExecutor(new SpawnerShopCommand());
    getCommand("reloadshop").setExecutor(new SpawnerShopCommand());
    getServer().getPluginManager().registerEvents(new ItemListeners(), this);
  }
  
  /*
  Setting up the economy for Vault Eco
   */
  private boolean setupEconomy()
  {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }
    econ = (Economy)rsp.getProvider();
    return econ != null;
  }

  // Setting up the items to go into the spawnerInventory
  public void setupSpawnerItems() {
      /*
      Adding each item in the inventory
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format(List<String> lore, String path, Sting displayName, material), String type, int cost, EntityType eType));
       */
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("Spider"))), "§8Spider", Material.MOB_SPAWNER), "Spider", getConfig().getInt("Spider"), EntityType.SPIDER));
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("Skeleton"))), "§7Skeleton", Material.MOB_SPAWNER), "Skeleton", getConfig().getInt("Skeleton"), EntityType.SKELETON));
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("Creeper"))), "§aCreeper", Material.MOB_SPAWNER), "Creeper", getConfig().getInt("Creeper"), EntityType.CREEPER));
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("Zombie"))), "§2Zombie", Material.MOB_SPAWNER), "Zombie", getConfig().getInt("Zombie"), EntityType.ZOMBIE));
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("IronGolem"))), "§7Iron§6Golem", Material.MOB_SPAWNER), "IronGolem", getConfig().getInt("IronGolem"), EntityType.IRON_GOLEM));
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("Blaze"))), "§6Blaze", Material.MOB_SPAWNER), "Blaze", getConfig().getInt("Blaze"), EntityType.BLAZE));
      items.add(new SpawnShopItem(ItemUtil.addItem(Collections.singletonList(String.format("§eCost: %s", getConfig().getInt("Witch"))), "§dWitch", Material.MOB_SPAWNER), "Witch", getConfig().getInt("Witch"), EntityType.WITCH));
      items.add(new SpawnShopItem(ItemUtil.addItem(new ArrayList<>(), "§eNext Page", Material.PAPER), "NEXT", 0, EntityType.UNKNOWN));
      items.add(new SpawnShopItem(ItemUtil.addItem(new ArrayList<>(), "§cCancel", Material.REDSTONE_BLOCK), "CANCEL", 0, EntityType.UNKNOWN));
      spawnerInventory = Bukkit.createInventory(null, 9, "SpawnerShops - Spawners");
      items.forEach(i -> {
          spawnerInventory.addItem(i.getItem());
      });
  }

  /*
  Creating the GUI for the player, with the valid items
 */
  public void openSpawnerGUI(Player player)
  {
    player.openInventory(this.spawnerInventory);
  }


  public void openEggGUI(Player player) { openEggGUI(player); }
  
  /*
  Sets the spawnerBlock to be the proper spawner when placed
   */
  public void setSpawner(Block block, EntityType ent)
  {
    BlockState blockState = block.getState();
    CreatureSpawner spawner = (CreatureSpawner)blockState;
    spawner.setSpawnedType(ent);
    blockState.update();
  }
  
  // Creates each mob already in the SpawnerShops plugin to be recognized and registered
  @EventHandler
  public void onSpawnerPlace(BlockPlaceEvent e)
  {
    Block b = e.getBlockPlaced();
    ItemStack inh = e.getItemInHand();
    if ((b != null) && (inh != null) && 
      (b.getType() == Material.MOB_SPAWNER) && (inh.getType() == Material.MOB_SPAWNER))
    {
        SpawnShopItem item;
        if((item = ItemUtil.getReturnedItem(inh)) != null) {
            try {
                setSpawner(b, EntityType.valueOf(item.getEntityType().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                e.getPlayer().sendMessage(ChatColor.RED + "Invalid Entity Type");
                setSpawner(b, EntityType.PIG);
            }

        }
    }
  }

  public static Main get() {
      return instance;
  }

}
