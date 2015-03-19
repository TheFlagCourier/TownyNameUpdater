package backcab.TownyNameUpdater;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;


public class TownyNameUpdater extends JavaPlugin implements Listener{

	private PluginFile map;
	
	public void onEnable(){
		final Logger logger = this.getLogger();
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable(){
			public void run(){
				if(!Bukkit.getPluginManager().isPluginEnabled("Towny")){
					logger.log(Level.SEVERE, "Towny is not enabled. Disabling this plugin.");
					Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("TownyNameUpdater"));
					return;
				}
				
				String minVersion = "0.89.2.2";
				
				Plugin towny = Bukkit.getPluginManager().getPlugin("Towny");
				
				if(minVersion.compareTo(towny.getDescription().getVersion().replaceAll("v", "")) > 0){
					logger.log(Level.SEVERE, "Towny version " + minVersion + " or higher required. Disabling this plugin");
				}
			}
		}, 0);
		
		map = new PluginFile(this, "playermap", false);
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onLogin(PlayerJoinEvent event){
		String currName = event.getPlayer().getName();
		UUID id = event.getPlayer().getUniqueId();
		String oldName = map.getFile().getString(id.toString());
		
		if(oldName == null){
			map.getFile().set(id.toString(), currName);
			try {
				map.save();
			} catch (Exception e) {e.printStackTrace();}
			return;
		}
		
		if(!oldName.equals(currName)){
			try {
				Resident r = TownyUniverse.getDataSource().getResident(oldName);
				TownyUniverse.getDataSource().renamePlayer(r, currName);
				map.getFile().set(id.toString(), currName);
				map.save();
			} catch (Exception e) {e.printStackTrace();}
			return;
		}
	}
}
