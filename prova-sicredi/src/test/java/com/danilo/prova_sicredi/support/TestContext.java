package com.danilo.prova_sicredi.support;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.danilo.prova_sicredi.support.factories.DriverFactory;
import com.danilo.prova_sicredi.support.report.ParallelReport;

/**
 * The test context makes it easier to handle multi-threaded test execution. We
 * create a new context for each test using the ContextFactory class and then
 * inside the context we do a few things like managing the report and the
 * driver.
 * 
 * The report contained inside the TestContext is a sub-report that will later
 * be consolidated in the main report.
 * 
 * @author danilo
 *
 */
public class TestContext {

	// The webdriver we'll use for the test
	public WebDriver driver;

	// The report instance
	public ExtentTest report;

	// This flag will be checked by the Selenium listener
	// It defines wheter it has or not to handle exceptions in the exception capture
	// method.
	private boolean handleExceptions;
	
	// Mark if Selenium has been initialized already
	private boolean seleniumInitialized = false;

	/**
	 * This is the default constructor. It has to take no parameters so this class
	 * becomes compatible with any dependency injection system we might want to use
	 * or with factories.
	 * 
	 * We won't start Selenium over here, in case the test don't need Selenium that
	 * would be a waste of time. For that reason, selenium is initialized manually
	 * by the test.
	 */
	public TestContext() {
		this("No test name provided");
	}

	/**
	 * This is an alternative constructor that will be used by the NgReportListener
	 * to instantiate the report. Test name will be retrieved from the TestNG
	 * interfaces.
	 * 
	 * @param testName
	 */
	public TestContext(String testName) {

		// Get the report instance
		report = ParallelReport.getExtentTest(testName);
		
		// Handle exceptions is true
		handleExceptions = true;

	}

	/**
	 * Will initialize selenium accordingly to the browser defined at
	 * config.properties.
	 */
	public void initializeSelenium() {

		String browser;
		
		// Do not initialize twice
		if(seleniumInitialized)
			return;

		// Get the browser name
		browser = Settings.getProperty("browser");

		// Get the driver
		driver = DriverFactory.getDriver(this, browser);
		
		// Change the flag
		seleniumInitialized = true;

	}

	/**
	 * Finalizes Selenium and avoid hanging sessions.
	 */
	public void finalizeSelenium() {
		// In case the driver is not null, close it
		if (driver != null) {
			driver.quit();
		}

		// Set the driver to null again
		driver = null;
		seleniumInitialized = false;
	}

	/**
	 * Defines if the Selenium Listener connected to the driver must handle
	 * exceptions or not.
	 * 
	 * @return true or false.
	 */
	public boolean isHandleExceptions() {
		return handleExceptions;
	}

	/**
	 * Will define if the Selenium Listener instance will handle exceptions for this
	 * test.
	 * 
	 * @param handleExceptions
	 *            True to handle exceptions, false to not handle.
	 */
	public void setHandleExceptions(boolean handleExceptions) {
		this.handleExceptions = handleExceptions;
	}

}
