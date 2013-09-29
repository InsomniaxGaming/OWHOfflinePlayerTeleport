package me.insom.optp.listeners;

import me.insom.optp.core.OWHOfflinePlayerTeleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener{
	
	OWHOfflinePlayerTeleport myPlugin = null;
	
	public JoinListener(OWHOfflinePlayerTeleport instance)
	{
		myPlugin = instance;
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Location location = myPlugin.getLocation(event.getPlayer().getName());
		
		if(location != null)
		{
			String lastTeleporter = myPlugin.getLastTP(event.getPlayer().getName());
			if(lastTeleporter != null)
			{
				event.getPlayer().sendMessage(ChatColor.GOLD + "You were stolen by " + lastTeleporter);	
			}
			event.getPlayer().teleport(location); // If location exists, send player there
		}
	}

}
