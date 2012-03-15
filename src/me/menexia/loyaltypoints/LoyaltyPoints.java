package me.menexia.loyaltypoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * 
 * @author Xavier Luis Ablaza - MeneXia
 *
 */
public class LoyaltyPoints extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public int increment = 1;
	public int cycleNumber = 600; // 10 minutes
	public int startingPoints = 0;
	public String pluginTag = "&6[LoyaltyPoints]";
	public String selfcheckMessage = "%TAG% &3You have &b%POINTS% &3Loyalty Points.";
	public String checkotherMessage = "%TAG% &3%PLAYERNAME% has &b%POINTS% &3Loyalty Points.";
	
	public Map<String, Integer> loyaltyMap = new HashMap<String, Integer>();
	public FileConfiguration config;
	public File mapFile;
	public FileConfiguration mapFileConfig;
	public LPFileManager lcFM = new LPFileManager(this);
	
	public double currentVersion = 0.1;
	// http://forums.bukkit.org/threads/loyalty-plugin.62813/
	
	public void onDisable() {
		LPFileManager.save();
		loyaltyMap.clear();
		info(this.getDescription(), "disabled");
	}
	
	public void onEnable() {
		mapFile = new File(this.getDataFolder(), "points.yml");
		mapFileConfig = YamlConfiguration.loadConfiguration(mapFile);

		/*Player[] onlinePlayers = getServer().getOnlinePlayers();
		for (Player p : onlinePlayers) {
			kickStart(p);
		}*/
		for (String s : this.mapFileConfig.getKeys(false)) {
				kickStart(s);
		}
		checkConfig();
		loadVariables();
		getCommand("lp").setExecutor(new LPCommand(this));
		this.getServer().getPluginManager().registerEvents(new LCListener(this), this);
		info(this.getDescription(), "enabled");
	}
	
	public void info(PluginDescriptionFile pdf, String status) {
		this.logger.info("[LoyaltyPoints] version " + pdf.getVersion() + " by MeneXia is now " + status + "!");
	}
	
	public void loadVariables() {
		config = this.getConfig();
		increment = config.getInt("increment-per-cycle");
		cycleNumber = config.getInt("cycle-time-in-seconds")*20;
		startingPoints = config.getInt("starting-points");
		pluginTag = colorize(config.getString("plugin-tag"));
		selfcheckMessage = colorize(config.getString("self-check-message")).replaceAll("%TAG%", pluginTag);
		checkotherMessage = colorize(config.getString("check-otherplayer-message")).replaceAll("%TAG%", pluginTag);
	}
	
	public String colorize(String message) {
		return message.replaceAll("&([a-f0-9])", ChatColor.COLOR_CHAR + "$1");
	}
	
	public void kickStart(String player) {
		if (!LPFileManager.load(player)) {
			this.loyaltyMap.put(player, this.startingPoints);
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new CountScheduler(this, player), (long)this.cycleNumber);
	}
	
	private void checkConfig() {
		String name = "config.yml";
		File actual = new File(getDataFolder(), name);
		if (!actual.exists()) {
			getDataFolder().mkdir();
			InputStream input = this.getClass().getResourceAsStream("/defaults/config.yml");
			if (input != null) {
				FileOutputStream output = null;
				
				try {
					output = new FileOutputStream(actual);
					byte[] buf = new byte[4096]; //[8192]?
					int length = 0;
					while ((length = input.read(buf)) > 0) {
						output.write(buf, 0, length);
					}
					this.logger.info("[LoyaltyPoints] Default configuration file written: " + name);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (IOException e) {}
					
					try {
						if (output != null)
							output.close();
					} catch (IOException e) {}
				}
			}
		}
	}
	
}
