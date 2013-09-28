package me.insom.optp.core;

import me.insom.optp.permissions.PermissionsHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class OWHOfflinePlayerTeleport extends JavaPlugin{
	
	PermissionsHandler permissions = null;
	FileConfiguration myConfig = null;
	
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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(label.equalsIgnoreCase("offlinetp"))
		{
			if(args.length > 0)
			{
				
			}
			return true;
		}
		return false;
	}

}
