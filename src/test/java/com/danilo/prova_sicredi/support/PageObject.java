package com.danilo.prova_sicredi.support;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.danilo.prova_sicredi.support.factories.ContextFactory;

/**
 * This class represents a basic PageObject. It enforces the usage of a
 * constructor that gets the TestContext as parameter as well as the
 * implementation of the method validatePageLoaded().
 * 
 * @author danilo
 *
 */
public abstract class PageObject {

	// Useful things to have in the PageObject
	public TestContext context;
	public WebDriver driver;
	public ExtentTest report;

	// Default timeout
	public int timeout;

	/**
	 * Default constructor gets the TestContext as parameter so that the driver,
	 * report and context are integrated in the PageObject, removing the need to
	 * qualify the access to its components every time we want to interact with the
	 * report or with Selenium.
	 */
	public PageObject() {
		// Get the context for the current thread
		this.context = ContextFactory.getContext();
		
		// Copies the data
		this.driver = context.driver;
		this.report = context.report;

		// Stores the default timeout
		timeout = Integer.parseInt(Settings.getProperty("default_timeout"));
	}

	/**
	 * This method validates that the page has loaded properly. It is a good
	 * practice to always call it before moving on to make sure we are in the right
	 * place.
	 */
	public abstract void validatePageLoaded();

}
