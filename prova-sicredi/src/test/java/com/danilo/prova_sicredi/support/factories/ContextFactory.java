package com.danilo.prova_sicredi.support.factories;

import java.util.HashMap;

import com.danilo.prova_sicredi.support.TestContext;

/**
 * This class takes care of instantiating the Text Context for each thread in
 * the test. It checks for the Thread ID in a map and, in case it is not found,
 * it will create a new instance of it.
 * 
 * In TestNG, an entire test will always run in the same thread, even the
 * listener methods. That makes it possible to handle the webdriver and report
 * by using the Thread ID.
 * 
 * @author danilo
 *
 */
public abstract class ContextFactory {

	// This variable will hold all instances of the test context
	private static HashMap<Long, TestContext> instances = new HashMap<Long, TestContext>();

	/**
	 * Get a new context for the current Thread. Adds a test name to the context.
	 * 
	 * @return a new Test context initialized with the test name.
	 */
	public static TestContext getContext() {
		return getContext("No name defined.");
	}

	/**
	 * Returns the TestContext for the current Thread. Create a new one in case it
	 * doesn't exists.
	 * 
	 * @return
	 */
	public static TestContext getContext(String testName) {

		long threadId;
		TestContext newContext;

		// Get the thread id
		threadId = Thread.currentThread().getId();

		// Check if it exists and return it
		if (instances.containsKey(threadId)) {
			return instances.get(threadId);
		}

		// Create a new one and add to the list of instances
		newContext = new TestContext(testName);
		instances.put(threadId, newContext);

		// Return it
		return newContext;

	}

}
