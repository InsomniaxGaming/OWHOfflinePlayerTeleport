package me.insom.optp.listeners;

import me.insom.optp.core.OWHOfflinePlayerTeleport;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener{
	
	OWHOfflinePlayerTeleport myPlugin = null;
	
	public QuitListener(OWHOfflinePlayerTeleport instance)
	{
		myPlugin = instance;
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		// Put player's location into config and save
		myPlugin.setLocation(event.getPlayer().getName(), event.getPlayer().getLocation());
		myPlugin.saveConfig();
	}

}
