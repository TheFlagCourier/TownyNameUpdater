package com.palmergames.townynameupdater;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.util.FileMgmt;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;


public class TownyNameUpdater extends JavaPlugin implements Listener {


	public void onEnable() {
		TownyNameUpdaterConfiguration.loadConfig(getDataFolder() + FileMgmt.fileSeparator() + "playermap.yml");

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event) {
		String currName = event.getPlayer().getName();
		UUID id = event.getPlayer().getUniqueId();
		String oldName = TownyNameUpdaterConfiguration.getString(id.toString());

		if (oldName == null || oldName.isEmpty()) {
			TownyNameUpdaterConfiguration.setString(id.toString(), currName);
			return;
		}

		if (!oldName.equals(currName)) {
			try {
				Resident r = TownyUniverse.getDataSource().getResident(oldName);
				TownyUniverse.getDataSource().renamePlayer(r, currName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			TownyNameUpdaterConfiguration.setString(id.toString(), currName);
		}
	}
}
