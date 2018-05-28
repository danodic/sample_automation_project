package com.danilo.prova_sicredi.support;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The class holds the settings for the "framework". This class will load the
 * properties at the config.properties as any regular java property file, but it
 * will also parse command line input and override any parameter parsed from the
 * file. The override is useful to provide customizable builds in CI tools, like
 * Jenkins.
 * 
 * @author danilo
 *
 */
public abstract class Settings {

	// This is the path for the config file
	private static String CONFIG_PROPERTIES_PATH = "config.properties";

	// Holds the initialization status
	private static boolean initialized = false;

	// Holds the properties
	private static Properties properties;

	/**
	 * Will load the settings from the properties file and then will also parse the
	 * command line arguments.
	 */
	private static void initialize() {
		parseConfigFile();
		parseOverrides();

		// Mark initialized
		initialized = true;
	}

	/**
	 * Go over a list of overrides and replace check if the override is needed.
	 */
	private static void parseOverrides() {

		// List of possible command-line overrides
		String[] overrides = new String[] { "browser", "timeout" };

		// Check for the overrides
		for (String entry : overrides) {
			doOverride(entry);
		}

	}

	/**
	 * Check if a Parameter exists in the system properties. If so, add it to the
	 * properties contained in this file.
	 * 
	 * @param entry
	 *            Name of the entry to be overridden.
	 */
	private static void doOverride(String entry) {

		String value;

		// Check if the value has been passed as a VM argument in the command line.
		if (System.getProperties().containsKey("framework." + entry)) {

			// Get the value
			value = System.getProperty("framework." + entry);

			// Replace or add the value
			if (properties.containsKey(entry)) {
				properties.replace(entry, value);
			} else {
				properties.put(entry, value);
			}

			// Add some log
			System.out.println(String.format("Overriding '%s' with '%s'.", entry, value));
		}
	}

	/**
	 * Parse the config.properties and load the properties.
	 */
	private static void parseConfigFile() {

		// Instantiate the properties object
		properties = new Properties();

		try {
			// Try to load the settings file
			properties.load(new FileInputStream(CONFIG_PROPERTIES_PATH));

		} catch (FileNotFoundException e) {
			// Handle the error
			System.err.println(String.format("Config.properties not found at '%s.'.",
					Paths.get(CONFIG_PROPERTIES_PATH).toAbsolutePath().toString()));

			// Abort the execution with error status
			System.exit(1);

		} catch (IOException e) {
			// Handle the error
			System.err.println(String.format("Unable to read config.properties at '%s.'.",
					Paths.get(CONFIG_PROPERTIES_PATH).toAbsolutePath().toString()));

			// Abort the execution with error status
			System.exit(1);
		}
	}

	/**
	 * Will return a property loaded either from the command line or from the
	 * property file.
	 */
	public static String getProperty(String propertyName) {
		// In case the settings have not been initialize, do it
		if (!initialized)
			initialize();

		// Check if the value exists
		if (properties.containsKey(propertyName)) {
			return properties.getProperty(propertyName);
		}

		// Throw an exception in case it doesn't exists
		throw new RuntimeException(String.format("Property '%s' has not been find in the file '%s'.", propertyName,
				CONFIG_PROPERTIES_PATH));

	}

}
