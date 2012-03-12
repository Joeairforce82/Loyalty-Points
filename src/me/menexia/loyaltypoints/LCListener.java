package me.menexia.loyaltypoints;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LCListener implements Listener {
	private final LoyaltyPoints plugin;
	public LCListener(LoyaltyPoints isCool) {
		plugin = isCool;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void Ck4d1sxIfxD13vk(final PlayerJoinEvent event) {
		plugin.kickStart(event.getPlayer().getName());
	}

}
