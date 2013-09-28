package me.insom.optp.permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.insom.optp.core.OWHOfflinePlayerTeleport;

public class PermissionsHandler {
	
	OWHOfflinePlayerTeleport myPlugin;
	private static Permission permission = null;
	
	public PermissionsHandler(OWHOfflinePlayerTeleport instance)
	{
		myPlugin = instance;
	}
	
	public boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = myPlugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	
	public boolean has(Player player, String node)
	{
		return permission.has(player, node);
	}
}
