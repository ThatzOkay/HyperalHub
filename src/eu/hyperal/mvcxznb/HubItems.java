package eu.hyperal.mvcxznb;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HubItems implements Listener {

	
	public static ItemStack Compass = new ItemStack(Material.COMPASS);
	public static ItemMeta CompassMeta = Compass.getItemMeta();
	
	Hub pl;
	
	public HubItems(Hub hub) {
	
		hub=pl;
	
	}
	
	public void setCompassMeta(){
		CompassMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Server Selector");
		Compass.setItemMeta(CompassMeta);
	}
	
    @EventHandler
    public void onClickSlot(InventoryClickEvent e) {
                e.setCancelled(true);
        }
    
    @EventHandler
    public void onPick(PlayerPickupItemEvent e) {
                e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
                e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
    	 e.setCancelled(true);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
            e.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
            e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
                e.setCancelled(true);
            }
    
	
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
            e.setQuitMessage(null);
    }
	
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
            e.setCancelled(true);
    }
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		
        Player p = e.getPlayer();
        if (pl.getConfig().getString("Worlds." + p.getWorld().getName()) == null) {
            try {
                Optional<String> newWorld = pl.getConfig().getConfigurationSection("Worlds").getKeys(false).stream().findFirst();
                if (newWorld.isPresent()) {
                    String spawn1 = pl.getConfig().getString("Worlds." + (String)newWorld.get());
                    Location ploc = pl.stringToLocation(spawn1);
                    p.teleport(ploc);
                    return;
                }
            }
            catch (Exception e2) {
                p.sendMessage((Object)ChatColor.RED + "There is no spawn set for this world!");
                return;
            }
            return;
        }
		
		e.setJoinMessage(null);
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
		e.getPlayer().getInventory().clear();
		setCompassMeta();
		
		e.getPlayer().getInventory().setItem(5, Compass);
		Bukkit.dispatchCommand(e.getPlayer(), "spawn");
	}
	
	@EventHandler
	public void onPlayerRightClick(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(e.getAction() == Action.PHYSICAL || e.getItem() == null || e.getItem().getType() == Material.AIR)return;
		
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(player.getItemInHand().hasItemMeta() && player.getItemInHand().getType() == Material.BLAZE_ROD
					 && player.getItemInHand().getItemMeta().equals(CompassMeta)){
					openGUI(player);
				}
			
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(!ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Server Selector"))return;
		Player player = (Player) e.getWhoClicked();
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)return;
		
		switch(e.getCurrentItem().getType()){
		
		case DIAMOND_SWORD: 
			Bukkit.dispatchCommand(player, "server Kigndom");
			player.closeInventory();
			player.sendMessage("Sended You to the Kigndom Server");
			break;
			
		case GOLD_SWORD: 
			Bukkit.dispatchCommand(player, "server KitPVP");
			player.closeInventory();
			player.sendMessage("Sended You to the KitPVP Server");
			break;
		
		case GRASS: 
			Bukkit.dispatchCommand(player, "server Skyblock");
			player.closeInventory();
			player.sendMessage("Sended You to the Skyblock Server");
			break;
		default:
			break;
		}
	}
	public void openGUI(Player player){
		Inventory inv = Bukkit.createInventory(null, 9 , ChatColor.DARK_PURPLE + "Server Selector");
		ItemStack Kingdom = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta KingdomMeta = Kingdom.getItemMeta();
		ItemStack KitPVP = new ItemStack(Material.GOLD_SWORD);
		ItemMeta KitPVPMeta = KitPVP.getItemMeta();
		ItemStack Skyblock = new ItemStack(Material.GRASS);
		ItemMeta SkyblockMeta = Skyblock.getItemMeta();
		
		
		KingdomMeta.setDisplayName(ChatColor.DARK_RED + "Kingdom");
		Kingdom.setItemMeta(KingdomMeta);
		
		KitPVPMeta.setDisplayName(ChatColor.GOLD + "KitPVP");
		KitPVP.setItemMeta(KitPVPMeta);
		
		SkyblockMeta.setDisplayName(ChatColor.DARK_GREEN + "Skyblock");
		Skyblock.setItemMeta(SkyblockMeta);
		inv.setItem(1, Skyblock);
		inv.setItem(5, Kingdom);
		inv.setItem(9, KitPVP);	
		player.openInventory(inv);
	}
}