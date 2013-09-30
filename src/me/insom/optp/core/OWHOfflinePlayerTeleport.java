package me.insom.optp.core;

import me.insom.optp.listeners.JoinListener;
import me.insom.optp.listeners.QuitListener;
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
	
	String tpToggleNode = "OWHOPTP.admin.tpdisable";
	String tpBypassNode = "OWHOPTP.admin.tpbypass";
	
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
		
		myConfig = this.getConfig();
		
		//Enable listeners
		getServer().getPluginManager().registerEvents(new JoinListener(this), this);
		getServer().getPluginManager().registerEvents(new QuitListener(this), this);
	}
	
	public void onDisable()
	{
		this.saveAllLocations(); // Save the location of every online player
		this.saveConfig();
	}
	
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
							// Check if the player has tp disabled, and if the sender has the bypass node
							if(this.getDisabledTP(Bukkit.getPlayer(args[0]).getName()))
							{
								if(permissions.has(sender, tpBypassNode) == false)
								{
									// No perms to bypass, tell player to go f himself
									sender.sendMessage(ChatColor.RED + "" + Bukkit.getPlayer(args[0]).getName() + " has teleporting disabled.");
									return true;
								}						
							}
							
							((Player) sender).teleport(Bukkit.getPlayer(args[0])); // If player is online, just teleport like normal
							sender.sendMessage(ChatColor.GOLD + "*Please stand by while a portal to the 7th dimension is formed for you...*");
							
							//TODO add functionality similar to /tpo(?)
							
						} else
						{
							// Check if the player has tp disabled, and if the sender has the bypass node
							if(this.getDisabledTP(Bukkit.getOfflinePlayer(args[0]).getName()))
							{
								if(permissions.has(sender, tpBypassNode) == false)
								{
									// No perms to bypass, tell player to go f himself
									sender.sendMessage(ChatColor.RED + "" + Bukkit.getPlayer(args[0]).getName() + " has teleporting disabled.");
									return true;
								}						
							}
							
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
						// Check if the player has tp disabled, and if the sender has the bypass node
						if(this.getDisabledTP(Bukkit.getPlayer(args[0]).getName()))
						{
							if(permissions.has(sender, tpBypassNode) == false)
							{
								// No perms to bypass, tell player to go f himself
								sender.sendMessage(ChatColor.RED + "" + Bukkit.getPlayer(args[0]).getName() + " has teleporting disabled.");
								return true;
							}						
						}
						
						Bukkit.getPlayer(args[0]).teleport(((Player)sender).getLocation()); // If player is online, just teleport like normal
						sender.sendMessage(ChatColor.GOLD + "*Please contain yourself while we hire a bountyhunter to kidnap " + Bukkit.getPlayer(args[0]).getName() + " for you...*");
						
						//TODO add functionality similar to /tpohere(?)
						
					} else
					{
						// Check if the player has tp disabled, and if the sender has the bypass node
						if(this.getDisabledTP(Bukkit.getOfflinePlayer(args[0]).getName()))
						{
							if(permissions.has(sender, tpBypassNode) == false)
							{
								// No perms to bypass, tell player to go f himself
								sender.sendMessage(ChatColor.RED + "" + Bukkit.getOfflinePlayer(args[0]).getName() + " has teleporting disabled.");
								return true;
							}						
						}
						
						//Player's not online. Replace their current location in config with command sender's
						this.setLocation(args[0], sender.getName(),((Player)sender).getLocation());
						sender.sendMessage(ChatColor.GOLD + "*Please renounce the urge to scream bloody murder while we set " + Bukkit.getOfflinePlayer(args[0]).getName() + "'s location to yours...*");
						
					}
				}
			}
			return true;
		}
		
		if(label.equalsIgnoreCase("toggleofflinetp") || label.equalsIgnoreCase("totp"))
		{
			if(sender instanceof Player)
			{
				if(permissions.has(sender, tpToggleNode))
				{
					
					if(this.getDisabledTP(sender.getName())) // Check if player currently has TP disabled
					{
						sender.sendMessage("Teleporting enabled. People may now teleport to you / teleport you to them.");
						this.disableTP(sender.getName(), false);
						
					}else
					{
						sender.sendMessage("Teleporting disabled. People may no longer teleport to you / teleport you to them.");
						this.disableTP(sender.getName(), true);
					}
					
				}
			}
			return true;
		}
		
		return false;
	}
	
	//Retrieve location from config. Null if not found
	public Location getLocation(String playerName)
	{
		
		playerName = Bukkit.getOfflinePlayer(playerName).getName(); // Change name to most accurate name
		
		Vector vector = myConfig.getVector("locations." + playerName + ".vector");
		String worldName = myConfig.getString("locations."+ playerName + ".world");
		if((vector != null) && (worldName != null))
		{
			return vector.toLocation(getServer().getWorld(worldName));
		}
		return null;
	}
	
	//Set specified player's location in config.
	public void setLocation(String playerName, String teleporterName, Location location)
	{
		
		playerName = Bukkit.getOfflinePlayer(playerName).getName(); // Change name to most accurate name
		
		myConfig.set("locations." + playerName + ".vector", location.toVector());
		myConfig.set("locations." + playerName + ".world", location.getWorld().getName());
		myConfig.set("locations." + playerName + ".lastTeleporter", teleporterName); // Add the player who teleported did the location setting.
	}
	
	// Retrieve the last person to teleport the specified player
	public String getLastTP(String playerName)
	{
		return myConfig.getString("locations." + playerName + ".lastTeleporter");
	}
	
	// Set last teleporter for specified player
	public void setLastTP(String playerName, String lastTeleporter)
	{
		myConfig.set("locations." + playerName + ".lastTeleporter", lastTeleporter);
		this.saveConfig();
	}
	
	public void disableTP(String playerName, boolean toggle)
	{
		myConfig.set("locations." + playerName + ".disabled", toggle);
		this.saveConfig();
	}
	
	// Returns whether player has TP disabled or not
	public boolean getDisabledTP(String playerName)
	{
		return myConfig.getBoolean("locations." + playerName + ".disabled", false);
	}
	
	public void saveAllLocations()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			this.setLocation(p.getName(), null, p.getLocation());
		}
	}
	
}
