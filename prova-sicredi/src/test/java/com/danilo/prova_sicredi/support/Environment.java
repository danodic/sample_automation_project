package com.danilo.prova_sicredi.support;

/**
 * This is a simple environment variable class. The reason to use a class instead of a config.properties file
 * is that we can have autocomplete in the IDE and can also add custom logic to define environment-specific
 * values. (not done here, though) 
 * @author danilo
 *
 */
public abstract class Environment {

	// Grocery Application URL
	public static String GROCERY_CRUD_URL = "https://www.grocerycrud.com/demo/bootstrap_theme";
	
}
