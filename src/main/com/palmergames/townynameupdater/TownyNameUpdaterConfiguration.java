package com.palmergames.townynameupdater;

import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.util.FileMgmt;

import java.io.File;

public class TownyNameUpdaterConfiguration {
	private static CommentedConfiguration config;

	public static void loadConfig(String filepath) {
		if (!FileMgmt.checkOrCreateFile(filepath)) {
			System.out.println("Failed to create playermap.yml!");
			System.out.println("Will not continue to load TownyNameUopdater!");
			return;
		}
		File file = new File(filepath);

		// read the config.yml into memory
		config = new CommentedConfiguration(file);
		if (!config.load()) {
			System.out.print("Failed to load Config!");
			System.out.println("Will not continue to load TownyNameUopdater!");
			return;
		}

		CommentedConfiguration newConfig = new CommentedConfiguration(file);
		newConfig.load();

		config = newConfig;

		config.save();
	}

	private static void setProperty(String root, Object value) {
		config.set(root.toLowerCase(), value.toString());
	}


	public static String getString(String root) {
		String data = config.getString(root.toLowerCase());
		if (data == null) {
			return "";
		}
		return data;
	}

	public static void setString(String root, Object value) {
		setProperty(root, value);
		config.save();
	}
}
