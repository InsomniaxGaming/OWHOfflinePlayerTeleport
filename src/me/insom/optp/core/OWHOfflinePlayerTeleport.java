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
				if(args.length > 0) // Make sure sender told us who to tp them to
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
							Location location = this.getLocationFromConfig(args[0]);
							
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
			
		}
		
		return false;
	}
	
	public Location getLocationFromConfig(String playerName)
	{
		Vector vector = myConfig.getVector("locations." + playerName + ".vector");
		String worldName = myConfig.getString("locations."+ playerName + ".world");
		if((vector != null) && (worldName != null))
		{
			return vector.toLocation(getServer().getWorld(worldName));
		}
		return null;
	}

}
