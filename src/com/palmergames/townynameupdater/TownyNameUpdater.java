package com.palmergames.townynameupdater;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;


public class TownyNameUpdater extends JavaPlugin implements Listener {


	public void onEnable() {
		TownyNameUpdaterConfiguration.loadConfig(getDataFolder() + File.separator + "playermap.yml");

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerLoginEvent event) {
		String currName = event.getPlayer().getName();
		UUID id = event.getPlayer().getUniqueId();
		String oldName = TownyNameUpdaterConfiguration.getString(id.toString());

		if (oldName.isEmpty()) {
			TownyNameUpdaterConfiguration.setString(id.toString(), currName);
			return;
		}

		if (!oldName.equals(currName)) {
			try {
				Resident r = TownyUniverse.getInstance().getDataSource().getResident(oldName);
				TownyUniverse.getInstance().getDataSource().renamePlayer(r, currName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			TownyNameUpdaterConfiguration.setString(id.toString(), currName);
		}
	}
}
