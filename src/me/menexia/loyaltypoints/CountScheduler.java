package me.menexia.loyaltypoints;

import org.bukkit.Bukkit;

public class CountScheduler implements Runnable {
	private LoyaltyPoints _p;
	private String player;
	public CountScheduler(LoyaltyPoints isCool, String isAwesome) {
		_p = isCool;
		player = isAwesome;
	}
	
	public void run() {
		if (Bukkit.getPlayer(player) != null) {
			if (_p.loyaltyMap.containsKey(player)) {
				_p.loyaltyMap.put(player, _p.loyaltyMap.get(player) + _p.increment);
			} else {
				_p.loyaltyMap.put(player, _p.startingPoints);
			}
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(_p, new CountScheduler(_p, player), (long)_p.cycleNumber);
	}

}
