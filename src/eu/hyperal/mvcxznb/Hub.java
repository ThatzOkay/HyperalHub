package eu.hyperal.mvcxznb;


import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;




public class Hub extends JavaPlugin {

	@Override
	public void onEnable() {
		
		Bukkit.getServer().getPluginManager().registerEvents(new HubItems(this), this);
	        this.getConfig().options().copyDefaults(true);
	        if (!this.getConfig().contains("Options.onWorldChange")) {
	            this.getConfig().set("Options.onWorldChange", (Object)"true");
	            this.saveConfig();
	            return;
	        }
	        if (!this.getConfig().contains("Options.SpawnCommand")) {
	            this.getConfig().set("Options.SpawnCommand", (Object)"false");
	            this.saveConfig();
	            return;
	        }
	        this.saveConfig();
	}
	
	
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players can use this command!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("spawn") && this.getConfig().getString("Options.SpawnCommand").equalsIgnoreCase("true")) {
            Player p = (Player)sender;
            if (this.getConfig().getString("Worlds." + p.getWorld().getName()) == null) {
                Optional newWorld = this.getConfig().getConfigurationSection("Worlds").getKeys(false).stream().findFirst();
                if (newWorld.isPresent()) {
                    String spawn1 = this.getConfig().getString("Worlds." + (String)newWorld.get());
                    Location ploc = this.stringToLocation(spawn1);
                    p.teleport(ploc);
                }
                return true;
            }
            String spawn = this.getConfig().getString("Worlds." + p.getWorld().getName());
            Location ploc = this.stringToLocation(spawn);
            p.teleport(ploc);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("Hyperal")) {
            Player p = (Player)sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage((Object)ChatColor.AQUA + "---==Hyperal help==---");
                    p.sendMessage((Object)ChatColor.GREEN + "Set spawn:  /Hyperal setspawn");
                    p.sendMessage((Object)ChatColor.AQUA + "---=================---");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload") && p.hasPermission("hyperal.admin")) {
                    this.reloadConfig();
                    p.sendMessage((Object)ChatColor.GREEN + "Reloaded config");
                    return true;
                }
                if (args[0].equalsIgnoreCase("setspawn")) {
                    if (p.hasPermission("hyperal.admin")) {
                        Location loc = p.getLocation();
                        String name = "Worlds." + p.getWorld().getName();
                        String key = String.valueOf(loc.getWorld().getName()) + " , " + loc.getX() + " , " + loc.getY() + " , " + loc.getZ() + " , " + loc.getPitch() + " , " + loc.getYaw();
                        this.getConfig().set(name, (Object)key);
                        this.saveConfig();
                        p.sendMessage((Object)ChatColor.GREEN + "Spawn loction has been set!");
                        return true;
                    }
                    p.sendMessage((Object)ChatColor.RED + "You do not have the right permission to perform that command!");
                }
            }
        }
        return true;
    }

    public Location stringToLocation(String key) {
        String[] split = key.split(" , ");
        if (split.length == 6) {
            Location loc = new Location(Bukkit.getWorld((String)split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float.parseFloat(split[4]));
            return loc;
        }
        return null;
    }
	
}
