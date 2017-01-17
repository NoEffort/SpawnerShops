package me.noeffort.spawnershops;

//Imports
import java.util.Arrays;
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
  public void openSpawnerGUI(Player player)
  {
	// Creating the inventory varible, inv
	// Bukkit.createInventory (InventoryHolder owner, int size, String title);
    Inventory inv = Bukkit.createInventory(null, 9, "SpawnerShops - Spawners");
    
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
    ItemStack next = new ItemStack(Material.PAPER);
    ItemMeta nextMeta = next.getItemMeta();
    ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
    ItemMeta cancelMeta = cancel.getItemMeta();
    
    //Turning Integers into Strings to use in the lores, will tell the price of the object
    //String x = Integer.toString(int i);
    String spiderInt = Integer.toString(getConfig().getInt("Spider"));
    String skeletonInt = Integer.toString(getConfig().getInt("Skeleton"));
    String creeperInt = Integer.toString(getConfig().getInt("Creeper"));
    String zombieInt =  Integer.toString(getConfig().getInt("Zombie"));
    String ironGolemInt = Integer.toString(getConfig().getInt("IronGolem"));
    String blazeInt = Integer.toString(getConfig().getInt("Blaze"));
    String witchInt = Integer.toString(getConfig().getInt("Witch"));
    
    // Creating the ItemMeta for each item
    // x.setDisplayName (String s);
    // x.setLore (List<String> arg0);
    // y.setItemMeta (ItemMeta itemMeta);
    spiderMeta.setDisplayName("§8Spider");
    spiderMeta.setLore(Arrays.asList("§eCost: " + spiderInt));
    spider.setItemMeta(spiderMeta);
    skeletonMeta.setDisplayName("§7Skeleton");
    skeletonMeta.setLore(Arrays.asList("§eCost: " + skeletonInt));
    skeleton.setItemMeta(skeletonMeta);
    creeperMeta.setDisplayName("§aCreeper");
    creeperMeta.setLore(Arrays.asList("§eCost: " + creeperInt));
    creeper.setItemMeta(creeperMeta);
    zombieMeta.setDisplayName("§2Zombie");
    zombieMeta.setLore(Arrays.asList("§eCost: " + zombieInt));
    zombie.setItemMeta(zombieMeta);
    ironGolemMeta.setDisplayName("§fIron§6Golem");
    ironGolemMeta.setLore(Arrays.asList("§eCost: " + ironGolemInt));
    ironGolem.setItemMeta(ironGolemMeta);
    blazeMeta.setDisplayName("§eBlaze");
    blazeMeta.setLore(Arrays.asList("§eCost: " + blazeInt));
    blaze.setItemMeta(blazeMeta);
    witchMeta.setDisplayName("§dWitch");
    witchMeta.setLore(Arrays.asList("§eCost: " + witchInt));
    witch.setItemMeta(witchMeta);
    nextMeta.setDisplayName("§eNext Page");
    next.setItemMeta(nextMeta);
    cancelMeta.setDisplayName("§cCancel");
    cancel.setItemMeta(cancelMeta);
    
    // Setting the placement for each item in the inventory <Starts at 0 - 8, not 1 - 9>)
    // x.setItem(int i, ItemStack itemStack);
    inv.setItem(0, spider);
    inv.setItem(1, skeleton);
    inv.setItem(2, creeper);
    inv.setItem(3, zombie);
    inv.setItem(4, ironGolem);
    inv.setItem(5, blaze);
    inv.setItem(6, witch);
    inv.setItem(7, next);
    inv.setItem(8, cancel);
    
    // Determining which inventory the player will open
    player.openInventory(inv);
  }
  
  public void openEggGUI(Player player) {
	  Inventory inv = Bukkit.createInventory(null, 9, "SpawnerShops - Eggs");
	  
	  ItemStack back = new ItemStack(Material.PAPER);
	  ItemMeta backMeta = back.getItemMeta();
	  ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
	  ItemMeta cancelMeta = cancel.getItemMeta();
	  
	  backMeta.setDisplayName("§ePrevious Page");
	  back.setItemMeta(backMeta);
	  cancelMeta.setDisplayName("§cCancel");
	  cancel.setItemMeta(cancelMeta);
	  
	  inv.setItem(7, back);
	  inv.setItem(8, cancel);
	  
	  player.openInventory(inv);
  }
  
  /*
  The next many lines are all the same
  They create the InventoryClickEvents for each item made beforehand
  I will not explain how to use the InventoryClickEvent
   */
  @EventHandler
  public void onInventoryClickSpider(InventoryClickEvent spiderEvent)
  {
    if (!ChatColor.stripColor(spiderEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
      if (econ.getBalance(p) < getConfig().getInt("Spider")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  spiderEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Spider"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { spider });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }

@EventHandler
  public void onInventoryClickSkeleton(InventoryClickEvent skeletonEvent)
  {
    if (!ChatColor.stripColor(skeletonEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
    
    skeletonMeta.setDisplayName("§7Skeleton");
    skeleton.setItemMeta(skeletonMeta);
    if (skeletonEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§7Skeleton"))
    {
      if (econ.getBalance(p) < getConfig().getInt("Skeleton")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  skeletonEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Skeleton"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { skeleton });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickCreeper(InventoryClickEvent creeperEvent)
  {
    if (!ChatColor.stripColor(creeperEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
      if (econ.getBalance(p) < getConfig().getInt("Creeper")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  creeperEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Creeper"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { creeper });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickZombie(InventoryClickEvent zombieEvent)
  {
    if (!ChatColor.stripColor(zombieEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
      if (econ.getBalance(p) < getConfig().getInt("Zombie")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  zombieEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Zombie"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { zombie });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickIronGolem(InventoryClickEvent ironGolemEvent)
  {
    if (!ChatColor.stripColor(ironGolemEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
      if (econ.getBalance(p) < getConfig().getInt("IronGolem")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  ironGolemEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("IronGolem"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { ironGolem });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickBlaze(InventoryClickEvent blazeEvent)
  {
    if (!ChatColor.stripColor(blazeEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
      if (econ.getBalance(p) < getConfig().getInt("Blaze")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  blazeEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Blaze"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { blaze });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickWitch(InventoryClickEvent witchEvent)
  {
    if (!ChatColor.stripColor(witchEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
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
      if (econ.getBalance(p) < getConfig().getInt("Witch")) {
    	  p.sendMessage("§cYou cannot afford this item!");
    	  witchEvent.setCancelled(true);
    	  return;
      }
      EconomyResponse r = econ.withdrawPlayer(p, getConfig().getInt("Witch"));
      if (r.transactionSuccess()) {
          p.getInventory().addItem(new ItemStack[] { witch });
          p.closeInventory();
      }
      else
    	  p.sendMessage("§4ERROR: Unexpected Results");
    }
  }
  
  @EventHandler
  public void onInventoryClickNext(InventoryClickEvent nextEvent)
  {
    if (!ChatColor.stripColor(nextEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
      return;
    }
    Player p = (Player)nextEvent.getWhoClicked();
    nextEvent.setCancelled(true);
    if ((nextEvent.getCurrentItem() == null) || (nextEvent.getCurrentItem().getType() == Material.AIR) || (!nextEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      nextEvent.setCancelled(true);
      return;
    }
    ItemStack next = new ItemStack(Material.PAPER);
    ItemMeta nextMeta = next.getItemMeta();
    
    nextMeta.setDisplayName("§eNext Page");
    next.setItemMeta(nextMeta);
    if (nextEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§eNext Page")) {
    	openEggGUI(p);
    }
  }
  
  @EventHandler
  public void onInventoryClickBack(InventoryClickEvent backEvent)
  {
    if (!ChatColor.stripColor(backEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Eggs")) {
      return;
    }
    Player p = (Player)backEvent.getWhoClicked();
    backEvent.setCancelled(true);
    if ((backEvent.getCurrentItem() == null) || (backEvent.getCurrentItem().getType() == Material.AIR) || (!backEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      backEvent.setCancelled(true);
      return;
    }
    ItemStack back = new ItemStack(Material.PAPER);
    ItemMeta backMeta = back.getItemMeta();
    
    backMeta.setDisplayName("§ePrevious Page");
    back.setItemMeta(backMeta);
    if (backEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§ePrevious Page")) {
    	openSpawnerGUI(p);
    }
  }
  
  @EventHandler
  public void onInventoryClickCancelSpawner(InventoryClickEvent cancelEvent)
  {
    if (!ChatColor.stripColor(cancelEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Spawners")) {
      return;
    }
    Player p = (Player)cancelEvent.getWhoClicked();
    cancelEvent.setCancelled(true);
    if ((cancelEvent.getCurrentItem() == null) || (cancelEvent.getCurrentItem().getType() == Material.AIR) || (!cancelEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      cancelEvent.setCancelled(true);
      return;
    }
    ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
    ItemMeta cancelMeta = cancel.getItemMeta();
    
    cancelMeta.setDisplayName("§cCancel");
    cancel.setItemMeta(cancelMeta);
    if (cancelEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§cCancel")) {
    	p.closeInventory();
    }
  }
  
  @EventHandler
  public void onInventoryClickCancelEgg(InventoryClickEvent cancelEvent)
  {
    if (!ChatColor.stripColor(cancelEvent.getInventory().getName()).equalsIgnoreCase("SpawnerShops - Eggs")) {
      return;
    }
    Player p = (Player)cancelEvent.getWhoClicked();
    cancelEvent.setCancelled(true);
    if ((cancelEvent.getCurrentItem() == null) || (cancelEvent.getCurrentItem().getType() == Material.AIR) || (!cancelEvent.getCurrentItem().hasItemMeta()))
    {
      p.closeInventory();
      cancelEvent.setCancelled(true);
      return;
    }
    ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
    ItemMeta cancelMeta = cancel.getItemMeta();
    
    cancelMeta.setDisplayName("§cCancel");
    cancel.setItemMeta(cancelMeta);
    if (cancelEvent.getCurrentItem().getItemMeta().getDisplayName().equals("§cCancel")) {
    	p.closeInventory();
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
        openSpawnerGUI(p);
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
