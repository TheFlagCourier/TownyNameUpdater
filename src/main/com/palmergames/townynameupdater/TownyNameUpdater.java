package com.palmergames.townynameupdater;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;


public class TownyNameUpdater extends JavaPlugin implements Listener {


	public void onEnable() {
		TownyNameUpdaterConfiguration.loadConfig(getDataFolder() + File.separator + "playermap.yml");

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(AsyncPlayerPreLoginEvent event) {
		String currName = event.getName();
		UUID id = event.getUniqueId();
		String oldName = TownyNameUpdaterConfiguration.getString(id.toString());

		if (oldName.isEmpty()) {
			TownyNameUpdaterConfiguration.setString(id.toString(), currName);
			return;
		}

		if (!oldName.equals(currName)) {
			System.out.println("[TownyNameUpdater] Player name change detected! " + oldName + " has changed their name to " + currName + ". Updating database.");

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
