package me.noeffort.spawnershops;

//Imports
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  private static final Logger log = Logger.getLogger("Minecraft");
  public static Economy econ = null;
  
  /*
  Initializing Vault Eco for this plugin
  */
  public void onEnable()
  {
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    saveDefaultConfig();
    if (!setupEconomy())
    {
      log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[] { getDescription().getName() }));
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
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
  
  /*
  Creating the GUI for the player, with the valid items
 */
  public void openGUI(Player player)
  {
	// Creating the inventory varible, inv
	// Bukkit.createInventory (InventoryHolder owner, int size, String title);
    Inventory inv = Bukkit.createInventory(null, 9, "SpawnerShops");
    
    // Creating the ItemStack for each item
    // ItemStack x = new ItemStack (org.bukkit.inventory <Basically, Material.item/block>);
    // ItemMeta y = x.getItemMeta();
    ItemStack spider = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta spiderMeta = spider.getItemMeta();
    ItemStack skeleton = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta skeletonMeta = skeleton.getItemMeta();
    ItemStack creeper = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta creeperMeta = creeper.getItemMeta();
    ItemStack zombie = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta zombieMeta = zombie.getItemMeta();
    ItemStack ironGolem = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta ironGolemMeta = ironGolem.getItemMeta();
    ItemStack blaze = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta blazeMeta = blaze.getItemMeta();
    ItemStack witch = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta witchMeta = witch.getItemMeta();
    
    // Creating the ItemMeta for each item
    // x.setDisplayName (String s);
    // y.setItemMeta (ItemMeta itemMeta);
    spiderMeta.setDisplayName("§8Spider");
    spider.setItemMeta(spiderMeta);
    skeletonMeta.setDisplayName("§7skeletonEventleton");
    skeleton.setItemMeta(skeletonMeta);
    creeperMeta.setDisplayName("§aCreeper");
    creeper.setItemMeta(creeperMeta);
    zombieMeta.setDisplayName("§2Zombie");
    zombie.setItemMeta(zombieMeta);
    ironGolemMeta.setDisplayName("§fIron§6Golem");
    ironGolem.setItemMeta(ironGolemMeta);
    blazeMeta.setDisplayName("§eBlaze");
    blaze.setItemMeta(blazeMeta);
    witchMeta.setDisplayName("§dWitch");
    witch.setItemMeta(witchMeta);
    
    // Setting the placement for each item in the inventory <Starts at 0 - 8, not 1 - 9>)
    // x.setItem(int i, ItemStack itemStack);
    inv.setItem(0, spider);
    inv.setItem(1, skeleton);
    inv.setItem(2, creeper);
    inv.setItem(4, zombie);
    inv.setItem(6, ironGolem);
    inv.setItem(7, blaze);
    inv.setItem(8, witch);
    
    // Determining which inventory the player will open
    player.openInventory(inv);
  }
  
  /*
  The next many lines are all the same
  They create the InventoryClickEvents for each item made beforehand
  I will not explain how to use the InventoryClickEvent
   */
  
  /*
  HELP! I attempted to check if the user's balance is enough to buy each spawner
  However, it doesn't seem to work. Look for:
  
        else if (econ.getBalance(p) < getConfig().getInt("x")) {
    	  creeperEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      
  and submit any possible solutions you can, it will be greatly appreciated!
   */
  @EventHandler
  public void onInventoryClickSpider(InventoryClickEvent spiderEvent)
  {
    if (!ChatColor.stripColor(spiderEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)spiderEvent.getWhoClicked();
    spiderEvent.setCancelled(true);
    if ((spiderEvent.getCurrentItem() == null) || (spiderEvent.getCurrentItem().getType() == Material.AIR) || (!spiderEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      return;
    }
    ItemStack spider = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta spiderMeta = spider.getItemMeta();
    
    spiderMeta.setDisplayName("§8Spider");
    spider.setItemMeta(spiderMeta);
    
    if (spiderEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§8Spider"))
    {
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Spider"));
      if (r.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { spider });
        p.closeInventory();
      }
      if (econ.getBalance(p) < getConfig().getInt("Spider")) {
    	  spiderEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }

@EventHandler
  public void onInventoryClickSkeleton(InventoryClickEvent skeletonEvent)
  {
    if (!ChatColor.stripColor(skeletonEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)skeletonEvent.getWhoClicked();
    skeletonEvent.setCancelled(true);
    if ((skeletonEvent.getCurrentItem() == null) || (skeletonEvent.getCurrentItem().getType() == Material.AIR) || (!skeletonEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      return;
    }
    ItemStack skeleton = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta skeletonMeta = skeleton.getItemMeta();
    
    skeletonMeta.setDisplayName("§7skeletonEventleton");
    skeleton.setItemMeta(skeletonMeta);
    if (skeletonEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§7skeletonEventleton"))
    {
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("skeletonEventleton"));
      if (r.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { skeleton });
        p.closeInventory();
      }
      else if (econ.getBalance(p) < getConfig().getInt("skeletonEventleton")) {
    	  skeletonEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickCreeper(InventoryClickEvent creeperEvent)
  {
    if (!ChatColor.stripColor(creeperEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)creeperEvent.getWhoClicked();
    creeperEvent.setCancelled(true);
    if ((creeperEvent.getCurrentItem() == null) || (creeperEvent.getCurrentItem().getType() == Material.AIR) || (!creeperEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      return;
    }
    ItemStack creeper = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta creeperMeta = creeper.getItemMeta();
    
    creeperMeta.setDisplayName("§aCreeper");
    creeper.setItemMeta(creeperMeta);
    if (creeperEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§aCreeper"))
    {
      EconomyResponse ecr = econ.withdrawPlayer(p, getConfig().getInt("Creeper"));
      if (ecr.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { creeper });
        p.closeInventory();
      }
      else if (econ.getBalance(p) < getConfig().getInt("Creeper")) {
    	  creeperEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickZombie(InventoryClickEvent zombieEvent)
  {
    if (!ChatColor.stripColor(zombieEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)zombieEvent.getWhoClicked();
    zombieEvent.setCancelled(true);
    if ((zombieEvent.getCurrentItem() == null) || (zombieEvent.getCurrentItem().getType() == Material.AIR) || (!zombieEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      return;
    }
    ItemStack zombie = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta zombieMeta = zombie.getItemMeta();
    
    zombieMeta.setDisplayName("§2Zombie");
    zombie.setItemMeta(zombieMeta);
    if (zombieEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§2Zombie"))
    {
      EconomyResponse ezo = econ.withdrawPlayer(p, getConfig().getInt("Zombie"));
      if (ezo.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { zombie });
        p.closeInventory();
      }
      else if (econ.getBalance(p) < getConfig().getInt("Zombie")) {
    	  zombieEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickIronGolem(InventoryClickEvent ironGolemEvent)
  {
    if (!ChatColor.stripColor(ironGolemEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)ironGolemEvent.getWhoClicked();
    ironGolemEvent.setCancelled(true);
    if ((ironGolemEvent.getCurrentItem() == null) || (ironGolemEvent.getCurrentItem().getType() == Material.AIR) || (!ironGolemEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      return;
    }
    ItemStack ironGolem = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta ironGolemMeta = ironGolem.getItemMeta();
    
    ironGolemMeta.setDisplayName("§fIron§6Golem");
    ironGolem.setItemMeta(ironGolemMeta);
    if (ironGolemEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§fIron§6Golem"))
    {
      EconomyResponse eig = econ.withdrawPlayer(p, getConfig().getInt("IronGolem"));
      if (eig.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { ironGolem });
        p.closeInventory();
      }
      else if (econ.getBalance(p) < getConfig().getInt("IronGolem")) {
    	  ironGolemEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickBlaze(InventoryClickEvent blazeEvent)
  {
    if (!ChatColor.stripColor(blazeEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)blazeEvent.getWhoClicked();
    blazeEvent.setCancelled(true);
    if ((blazeEvent.getCurrentItem() == null) || (blazeEvent.getCurrentItem().getType() == Material.AIR) || (!blazeEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      return;
    }
    ItemStack blaze = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta blazeMeta = blaze.getItemMeta();
    
    blazeMeta.setDisplayName("§eBlaze");
    blaze.setItemMeta(blazeMeta);
    if (blazeEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§eBlaze"))
    {
      EconomyResponse ebl = econ.withdrawPlayer(p, getConfig().getInt("Blaze"));
      if (ebl.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { blaze });
        p.closeInventory();
      }
      else if (econ.getBalance(p) < getConfig().getInt("Blaze")) {
    	  blazeEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickWitch(InventoryClickEvent witchEvent)
  {
    if (!ChatColor.stripColor(witchEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player p = (Player)witchEvent.getWhoClicked();
    witchEvent.setCancelled(true);
    if ((witchEvent.getCurrentItem() == null) || (witchEvent.getCurrentItem().getType() == Material.AIR) || (!witchEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      witchEvent.setCancelled(true);
      return;
    }
    ItemStack witch = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta witchMeta = witch.getItemMeta();
    
    witchMeta.setDisplayName("§dWitch");
    witch.setItemMeta(witchMeta);
    if (witchEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§dWitch"))
    {
      EconomyResponse ewi = econ.withdrawPlayer(p, getConfig().getInt("Witch"));
      if (ewi.transactionSuccess())
      {
        p.getInventory().addItem(new ItemStack[] { witch });
        p.closeInventory();
      }
      else if (econ.getBalance(p) < getConfig().getInt("Witch")) {
    	  witchEvent.setCancelled(true);
    	  p.sendMessage("§cYou cannot afford this item!");
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  /*
  Creating and Initializing the command that will be used to open the inventory
   */
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
	// Only players can do this command, if they are not a player, tell the log <Console> that it cannot be completed through it)
    if (!(sender instanceof Player))
    {
      Log.info(new Object[] {"§cSpawnerShops can only be used by players!" });
      return true;
    }
    // Creating variable p as the Player. Command cannot be used in Creative Mode as well
    Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("spawnershops")) {
      if ((!p.hasPermission("ss.use")) || (p.getGameMode() == GameMode.CREATIVE)) {
        p.sendMessage("§cYou cannot use SpawnerShops!");
      } else {
        openGUI(p);
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
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SpawnerShops");
        plugin.reloadConfig();
        p.sendMessage("§bPlugin Reloaded!");
      }
    }
    return true;
  }
  
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
  @SuppressWarnings("deprecation")
  @EventHandler
  public void onSpawnerPlace(BlockPlaceEvent e)
  {
    Block b = e.getBlockPlaced();
    ItemStack inh = e.getPlayer().getItemInHand();
    if ((b != null) && (inh != null) && 
      (b.getType() == Material.MOB_SPAWNER) && (inh.getType() == Material.MOB_SPAWNER))
    {
      ItemMeta im = inh.getItemMeta();
      if (im.getDisplayName().equals("§8Spider"))
      {
        setSpawner(b, EntityType.SPIDER);
        return;
      }
      if (im.getDisplayName().equals("§7Skeleton"))
      {
        setSpawner(b, EntityType.SKELETON);
        return;
      }
      if (im.getDisplayName().equals("§aCreeper"))
      {
        setSpawner(b, EntityType.CREEPER);
        return;
      }
      if (im.getDisplayName().equals("§2Zombie"))
      {
        setSpawner(b, EntityType.ZOMBIE);
        return;
      }
      if (im.getDisplayName().equals("§fIron§6Golem"))
      {
        setSpawner(b, EntityType.IRON_GOLEM);
        return;
      }
      if (im.getDisplayName().equals("§eBlaze"))
      {
        setSpawner(b, EntityType.BLAZE);
        return;
      }
      if (im.getDisplayName().equals("§dWitch"))
      {
        setSpawner(b, EntityType.WITCH);
        return;
      }
      if (EntityType.UNKNOWN != null)
      {
        e.getPlayer().sendMessage("§cInvalid MobSpawner Type!");
        return;
      }
    }
  }
}
