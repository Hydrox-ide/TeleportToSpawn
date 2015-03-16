package me.Hydroxide.TeleportToSpawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		getConfig().addDefault("spawnSetMessage", "&8[&aTeleportToSpawn&8] &eSpawn succesfully set!");
		getConfig().addDefault("prefix", "&8[&aTeleportToSpawn&8]");
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("tpts")) {
			if(args.length == 0) {
				player.sendMessage(getConfig().getString("prefix").replaceAll("(&([a-f0-9]))", "\u00A7$2") + " §c§oYou may use /tpts set or /tpts test.");
				return false;
			} else {
				if(args[0].equalsIgnoreCase("set") && player.hasPermission("tpts.admin")) {
					Location playerLoc = player.getLocation();
					double x = playerLoc.getX();
					double y = playerLoc.getY();
					double z = playerLoc.getZ();
					String playerWorld = player.getWorld().getName();
					
					getConfig().set("spawn.X", x);
					getConfig().set("spawn.Y", y);
					getConfig().set("spawn.Z", z);
					getConfig().set("spawn.world", playerWorld);
					saveConfig();
					player.sendMessage(getConfig().getString("spawnSetMessage").replaceAll("(&([a-f0-9]))", "\u00A7$2"));
					return true;
				} else if(args[0].equalsIgnoreCase("test") && player.hasPermission("tpts.admin")) {
					double x = getConfig().getDouble("spawn.X");
					double y = getConfig().getDouble("spawn.Y");
					double z = getConfig().getDouble("spawn.Z");
					World world = Bukkit.getWorld(getConfig().getString("spawn.world"));
					Location destination = new Location(world, x, y, z);
					if(getConfig().getString("spawn.world") == null || getConfig().getDouble("spawn.X") == 0 || getConfig().getDouble("spawn.Y") == 0 || getConfig().getDouble("spawn.Z") == 0) {
						player.sendMessage(getConfig().getString("prefix").replaceAll("(&([a-f0-9]))", "\u00A7$2") + " §c§oUse /tpts set first.");
						return false;
					}
					player.teleport(destination);
					return true;
				} else if(!(player.hasPermission("tpts.admin"))) {
					return false;
				} else if(args[0].equalsIgnoreCase("reload")) {
					player.sendMessage(getConfig().getString("prefix").replaceAll("(&([a-f0-9]))", "\u00A7$2") + " §aReloaded");
					reloadConfig();
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		double x = getConfig().getDouble("spawn.X");
		double y = getConfig().getDouble("spawn.Y");
		double z = getConfig().getDouble("spawn.Z");
		World world = Bukkit.getWorld(getConfig().getString("spawn.world"));
		Location destination = new Location(world, x, y, z);
		event.getPlayer().teleport(destination);
	}
}
