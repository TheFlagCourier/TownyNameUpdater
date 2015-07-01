package backcab.TownyNameUpdater;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;


public class TownyNameUpdater extends JavaPlugin implements Listener{

	private PluginFile map;
	
	public void onEnable(){

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
			} catch (Exception e) {e.printStackTrace();}
			
			try {
				map.getFile().set(id.toString(), currName);
				map.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}
