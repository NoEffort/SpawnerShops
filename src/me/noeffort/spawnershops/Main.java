package me.noeffort.spawnershops;

// Imports
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
  
  public Main() {}
  
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
    ItemStack sp = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta spm = sp.getItemMeta();
    ItemStack sk = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta skm = sk.getItemMeta();
    ItemStack cr = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta crm = cr.getItemMeta();
    ItemStack zo = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta zom = zo.getItemMeta();
    ItemStack ig = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta igm = ig.getItemMeta();
    ItemStack bl = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta blm = bl.getItemMeta();
    ItemStack wi = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta wim = wi.getItemMeta();
    
    // Creating the ItemMeta for each item
    // x.setDisplayName (String s);
    // y.setItemMeta (ItemMeta itemMeta);
    spm.setDisplayName("§8Spider");
    sp.setItemMeta(spm);
    skm.setDisplayName("§7Skeleton");
    sk.setItemMeta(skm);
    crm.setDisplayName("§aCreeper");
    cr.setItemMeta(crm);
    zom.setDisplayName("§2Zombie");
    zo.setItemMeta(zom);
    igm.setDisplayName("§fIron§6Golem");
    ig.setItemMeta(igm);
    blm.setDisplayName("§eBlaze");
    bl.setItemMeta(blm);
    wim.setDisplayName("§dWitch");
    wi.setItemMeta(wim);
    
    // Setting the placement for each item in the inventory <Starts at 0 - 8, not 1 - 9>)
    // x.setItem(int i, ItemStack itemStack);
    inv.setItem(0, sp);
    inv.setItem(1, sk);
    inv.setItem(2, cr);
    inv.setItem(4, zo);
    inv.setItem(6, ig);
    inv.setItem(7, bl);
    inv.setItem(8, wi);
    
    // Determining which inventory the player will open
    player.openInventory(inv);
  }
  
  /*
    The next many lines are all the same
    They create the InventoryClickEvents for each item made beforehand
    I will not explain how to use the InventoryClickEvent
  */
  @EventHandler
  public void onInventoryClickSp(InventoryClickEvent spe)
  {
    if (!ChatColor.stripColor(spe.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player spp = (Player)spe.getWhoClicked();
    spe.setCancelled(true);
    if ((spe.getCurrentItem() == null) || (spe.getCurrentItem().getType() == Material.AIR) || (!spe.getCurrentItem().hasItemMeta()))
    {
      spp.closeInventory();
      return;
    }
    ItemStack spl = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta splm = spl.getItemMeta();
    
    splm.setDisplayName("§8Spider");
    spl.setItemMeta(splm);
    if (spe.getCurrentItem().getItemMeta().getDisplayName().equals("§8Spider"))
    {
      EconomyResponse esp = econ.withdrawPlayer(spp, getConfig().getInt("Spider"));
      if (esp.transactionSuccess())
      {
        spp.getInventory().addItem(new ItemStack[] { spl });
        spp.closeInventory();
      }
      else
      {
        spe.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
    }
  }
  
  @EventHandler
  public void onInventoryClickSk(InventoryClickEvent ske)
  {
    if (!ChatColor.stripColor(ske.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player skp = (Player)ske.getWhoClicked();
    ske.setCancelled(true);
    if ((ske.getCurrentItem() == null) || (ske.getCurrentItem().getType() == Material.AIR) || (!ske.getCurrentItem().hasItemMeta()))
    {
      skp.closeInventory();
      return;
    }
    ItemStack skl = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta sklm = skl.getItemMeta();
    
    sklm.setDisplayName("§7Skeleton");
    skl.setItemMeta(sklm);
    if (ske.getCurrentItem().getItemMeta().getDisplayName().equals("§7Skeleton"))
    {
      EconomyResponse esk = econ.withdrawPlayer(skp, getConfig().getInt("Skeleton"));
      if (esk.transactionSuccess())
      {
        skp.getInventory().addItem(new ItemStack[] { skl });
        skp.closeInventory();
      }
      else
      {
        ske.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
    }
  }
  
  @EventHandler
  public void onInventoryClickCr(InventoryClickEvent cre)
  {
    if (!ChatColor.stripColor(cre.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player crp = (Player)cre.getWhoClicked();
    cre.setCancelled(true);
    if ((cre.getCurrentItem() == null) || (cre.getCurrentItem().getType() == Material.AIR) || (!cre.getCurrentItem().hasItemMeta()))
    {
      crp.closeInventory();
      return;
    }
    ItemStack crl = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta crlm = crl.getItemMeta();
    
    crlm.setDisplayName("§aCreeper");
    crl.setItemMeta(crlm);
    if (cre.getCurrentItem().getItemMeta().getDisplayName().equals("§aCreeper"))
    {
      EconomyResponse ecr = econ.withdrawPlayer(crp, getConfig().getInt("Creeper"));
      if (ecr.transactionSuccess())
      {
        crp.getInventory().addItem(new ItemStack[] { crl });
        crp.closeInventory();
      }
      else
      {
        cre.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
    }
  }
  
  @EventHandler
  public void onInventoryClickZo(InventoryClickEvent zoe)
  {
    if (!ChatColor.stripColor(zoe.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player zop = (Player)zoe.getWhoClicked();
    zoe.setCancelled(true);
    if ((zoe.getCurrentItem() == null) || (zoe.getCurrentItem().getType() == Material.AIR) || (!zoe.getCurrentItem().hasItemMeta()))
    {
      zop.closeInventory();
      return;
    }
    ItemStack zol = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta zolm = zol.getItemMeta();
    
    zolm.setDisplayName("§2Zombie");
    zol.setItemMeta(zolm);
    if (zoe.getCurrentItem().getItemMeta().getDisplayName().equals("§2Zombie"))
    {
      EconomyResponse ezo = econ.withdrawPlayer(zop, getConfig().getInt("Zombie"));
      if (ezo.transactionSuccess())
      {
        zop.getInventory().addItem(new ItemStack[] { zol });
        zop.closeInventory();
      }
      else
      {
        zoe.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
    }
  }
  
  @EventHandler
  public void onInventoryClickIg(InventoryClickEvent ige)
  {
    if (!ChatColor.stripColor(ige.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player igp = (Player)ige.getWhoClicked();
    ige.setCancelled(true);
    if ((ige.getCurrentItem() == null) || (ige.getCurrentItem().getType() == Material.AIR) || (!ige.getCurrentItem().hasItemMeta()))
    {
      igp.closeInventory();
      return;
    }
    ItemStack igl = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta iglm = igl.getItemMeta();
    
    iglm.setDisplayName("§fIron§6Golem");
    igl.setItemMeta(iglm);
    if (ige.getCurrentItem().getItemMeta().getDisplayName().equals("§fIron§6Golem"))
    {
      EconomyResponse eig = econ.withdrawPlayer(igp, getConfig().getInt("IronGolem"));
      if (eig.transactionSuccess())
      {
        igp.getInventory().addItem(new ItemStack[] { igl });
        igp.closeInventory();
      }
      else
      {
        ige.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
    }
  }
  
  @EventHandler
  public void onInventoryClickBl(InventoryClickEvent ble)
  {
    if (!ChatColor.stripColor(ble.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player blp = (Player)ble.getWhoClicked();
    ble.setCancelled(true);
    if ((ble.getCurrentItem() == null) || (ble.getCurrentItem().getType() == Material.AIR) || (!ble.getCurrentItem().hasItemMeta()))
    {
      blp.closeInventory();
      return;
    }
    ItemStack bll = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta bllm = bll.getItemMeta();
    
    bllm.setDisplayName("§eBlaze");
    bll.setItemMeta(bllm);
    if (ble.getCurrentItem().getItemMeta().getDisplayName().equals("§eBlaze"))
    {
      EconomyResponse ebl = econ.withdrawPlayer(blp, getConfig().getInt("Blaze"));
      if (ebl.transactionSuccess())
      {
        blp.getInventory().addItem(new ItemStack[] { bll });
        blp.closeInventory();
      }
      else
      {
        ble.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
    }
  }
  
  @EventHandler
  public void onInventoryClickWi(InventoryClickEvent wie)
  {
    if (!ChatColor.stripColor(wie.getInventory().getName()).equalsIgnoreCase("SpawnerShops")) {
      return;
    }
    Player wip = (Player)wie.getWhoClicked();
    wie.setCancelled(true);
    if ((wie.getCurrentItem() == null) || (wie.getCurrentItem().getType() == Material.AIR) || (!wie.getCurrentItem().hasItemMeta()))
    {
      wip.closeInventory();
      wie.setCancelled(true);
      return;
    }
    ItemStack wil = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta wilm = wil.getItemMeta();
    
    wilm.setDisplayName("§dWitch");
    wil.setItemMeta(wilm);
    if (wie.getCurrentItem().getItemMeta().getDisplayName().equals("§dWitch"))
    {
      EconomyResponse ewi = econ.withdrawPlayer(wip, getConfig().getInt("Witch"));
      if (ewi.transactionSuccess())
      {
        wip.getInventory().addItem(new ItemStack[] { wil });
        wip.closeInventory();
      }
      else
      {
        wie.getWhoClicked().sendMessage("§cYou cannot afford this item!");
      }
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
    Player p = (Player) sender;
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
  
  // Creates each mob already in the SpawnerShops plugin to be recognied and registered
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
