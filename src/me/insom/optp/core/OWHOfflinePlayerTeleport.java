package me.insom.optp.core;

import me.insom.optp.permissions.PermissionsHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class OWHOfflinePlayerTeleport extends JavaPlugin{
	
	PermissionsHandler permissions = null;
	FileConfiguration myConfig = null;
	
	String tpNode = "OWHOPTP.tp";
	String tphereNode = "OWHOPTP.tphere";
	
	public void onEnable()
	{
		getServer().getLogger().info("Enabling OWHOPTP");
		
		// Set up permissions, disable if it fails
		permissions = new PermissionsHandler(this);
		if(permissions.setupPermissions() == false)
		{
			getServer().getLogger().info("Failed to setup permissions! Disabling");
			this.getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	public void onDisable(){}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		
		if(label.equalsIgnoreCase("offlinetp") || label.equalsIgnoreCase("otp"))
		{
			
			if(sender instanceof Player) // Check that sender is a Player. Silly consoles can't tp!
			{
				if(args.length > 0) // Make sure sender told us who to teleport them to
				{
					if(permissions.has(sender, tpNode))
					{
						//Player has permission!
						if(Bukkit.getPlayer(args[0]) != null)
						{
							((Player) sender).teleport(Bukkit.getPlayer(args[0])); // If player is online, just teleport like normal
							sender.sendMessage(ChatColor.GOLD + "*Please stand by while a portal to the 7th dimension is formed for you...*");
							
							//TODO add functionality similar to /tpo(?)
							
						} else
						{
							
							// Player isn't online; check our config for location of player
							Location location = this.getLocation(args[0]);
							
							if(location != null)
							{
								// All location info found! send player to that location
								((Player) sender).teleport(location);
								sender.sendMessage(ChatColor.GOLD + "*Please hold while a team of highly intelligent sea otters resolve your request...*");
								
							} else
							{
								sender.sendMessage("Player location not found.");								
							}
						}
					}
				}
			}
			return true;
		}
		
		if(label.equalsIgnoreCase("offlinetphere") || label.equalsIgnoreCase("otphere"))
		{
			if(args.length > 0) // Make sure sender told us who to teleport to them
			{
				if(permissions.has(sender, tphereNode))
				{
					//Has permission, let's do this thang
					if(Bukkit.getPlayer(args[0]) != null)
					{
						Bukkit.getPlayer(args[0]).teleport(((Player)sender).getLocation()); // If player is online, just teleport like normal
						sender.sendMessage(ChatColor.GOLD + "*Please contain yourself while we hire a bountyhunter to kidnap " + Bukkit.getPlayer(args[0]).getName() + " for you...*");
						
						//TODO add functionality similar to /tpohere(?)
						
					} else
					{
						//Player's not online. Replace their current location in config with command sender's
						this.setLocation(args[0], ((Player)sender).getLocation());
						sender.sendMessage("Offline player successfully teleported to you.");
						
					}
				}
			}
			return true;
		}
		
		return false;
	}
	
	public Location getLocation(String playerName)
	{
		Vector vector = myConfig.getVector("locations." + playerName + ".vector");
		String worldName = myConfig.getString("locations."+ playerName + ".world");
		if((vector != null) && (worldName != null))
		{
			return vector.toLocation(getServer().getWorld(worldName));
		}
		return null;
	}
	
	public void setLocation(String playerName, Location location)
	{
		myConfig.set("locations." + playerName + ".vector", location.toVector());
		myConfig.set("locations." + playerName + ".world", location.getWorld().getName());
	}

}
