package com.danilo.prova_sicredi.support.factories;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.danilo.prova_sicredi.support.Settings;
import com.danilo.prova_sicredi.support.TestContext;
import com.danilo.prova_sicredi.support.listeners.SeleniumListener;

/**
 * This class takes care of instantiating and setting up the web drivers. The
 * drivers are requested by the TestContext class, that will use the Settings
 * class to know what browser to request. The selection of the browser is
 * delegated to the TestContext so that the class can be called manually if
 * needed.
 * 
 * @author danilo
 *
 */
public abstract class DriverFactory {

	/**
	 * Will return an instance of the WebDriver accordingly to the browser passed as
	 * parameter.
	 * 
	 * @param context
	 *            TestContext for the current Thread.
	 * @param browser
	 *            The browser name.
	 * @return a WebDriver instance connected to a SeleniumListener instance.
	 */
	public static WebDriver getDriver(TestContext context, String browser) {
		// Call the right method to get the browser
		switch (browser.trim().toUpperCase()) {
		case "CHROME":
			return DriverFactory.getChromeDriver(context);
		default:
			// Throw an exception in case of wrong browser
			throw new RuntimeException(String.format("Browser '%s' is not supported.", browser));
		}

	}

	/**
	 * Will instantiate and setup a ChromeDriver.
	 * 
	 * @param context
	 *            TestContext for the current Thread.
	 * @return A ChromeDriver.
	 */
	private static WebDriver getChromeDriver(TestContext context) {

		WebDriver driver;
		ChromeOptions options;
		SeleniumListener listener;

		// Add the property for the chromedriver path
		System.getProperty("webdriver.chrome.driver",
				Settings.getProperty("driver_path") + File.separator + "chromedriver.exe");

		// Setup desired capabilities
		options = new ChromeOptions();

		// Avoid annoying (and sometimes blocking) password pop-ups
		options.setCapability("credentials_enable_service", false);
		options.setCapability("password_manager_enabled", false);

		// Instantiate the listener
		listener = new SeleniumListener(context);

		// Instantiate the driver
		driver = new ChromeDriver();

		// Connect the listener to the driver
		((EventFiringWebDriver) driver).register(listener);

		// Maximize the window
		driver.manage().window().maximize();

		// Return it
		return driver;
	}

}
