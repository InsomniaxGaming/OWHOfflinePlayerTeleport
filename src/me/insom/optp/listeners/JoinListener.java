package me.insom.optp.listeners;

import me.insom.optp.core.OWHOfflinePlayerTeleport;

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
			event.getPlayer().teleport(location); // If location exists, send player there
	}

}
