package com.palmergames.townynameupdater;

import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.util.FileMgmt;

import java.io.File;

public class TownyNameUpdaterConfiguration {
	private static CommentedConfiguration config, newConfig;

	public static void loadConfig(String filepath) {

		File file = FileMgmt.checkYMLExists(new File(filepath));

		// read the config.yml into memory
		config = new CommentedConfiguration(file);
		if (!config.load())
			System.out.print("Failed to load Config!");

		setDefaults(file);

		config.save();
	}

	private static void addComment(String root, String... comments) {

		newConfig.addComment(root.toLowerCase(), comments);
	}

	/**
	 * Builds a new config reading old config data.
	 */
	private static void setDefaults(File file) {

		newConfig = new CommentedConfiguration(file);
		newConfig.load();

		for (ConfigurationNodes root : ConfigurationNodes.values()) {
			if (root.getComments().length > 0) {
				addComment(root.getRoot(), root.getComments());
			}

			setNewProperty(root.getRoot(), config.get(root.getRoot().toLowerCase()) != null ? config.get(root.getRoot().toLowerCase()) : root.getDefault());
		}

		config = newConfig;
		newConfig = null;
	}

	private static void setProperty(String root, Object value) {

		config.set(root.toLowerCase(), value.toString());
	}

	private static void setNewProperty(String root, Object value) {

		if (value == null) {
			// System.out.print("value is null for " + root.toLowerCase());
			value = "";
		}
		newConfig.set(root.toLowerCase(), value.toString());
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

enum ConfigurationNodes {
	MAIN(
			"uuid",
			"name",
			"# This is where Towny stores all of the uuid to username mappings.",
			"# DO NOT TOUCH THESE UNLESS YOU KNOW WHAT YOU ARE DOING!"
	);
	private final String Root;
	private final String Default;
	private String[] comments;

	ConfigurationNodes(String root, String def, String... comments) {

		this.Root = root;
		this.Default = def;
		this.comments = comments;
	}

	/**
	 * Retrieves the root for a config option
	 *
	 * @return The root for a config option
	 */
	public String getRoot() {

		return Root;
	}

	/**
	 * Retrieves the default value for a config path
	 *
	 * @return The default value for a config path
	 */
	public String getDefault() {

		return Default;
	}

	/**
	 * Retrieves the comment for a config path
	 *
	 * @return The comments for a config path
	 */
	public String[] getComments() {

		if (comments != null) {
			return comments;
		}

		String[] comments = new String[1];
		comments[0] = "";
		return comments;
	}

}
