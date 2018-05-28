package com.danilo.prova_sicredi.support.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.danilo.prova_sicredi.support.Settings;
import com.danilo.prova_sicredi.support.TestContext;

/**
 * This is a simple helper class to provide assistance with some common tasks
 * like adding/saving screenshots.
 * 
 * @author danilo
 *
 */
public abstract class ReportHelper {

	// Make sure screenshots are going to have unique names.
	// This one is volatile to make sure the threads are always going to pick the
	// value from the memory instead of getting from the cache.
	private volatile static int screenshotCount = 0;

	/**
	 * Will take a screenshot, save to the disk and add to the ExtentTest instance.
	 */
	public static void addScreenshot(TestContext context) {

		String screenshotPath;
		MediaEntityModelProvider screenshot;

		// Save the screenshot
		screenshotPath = saveScreenshot(context);

		// Return in case couldn't take screenshot
		if (screenshotPath == null) {
			context.report.info("Could not take screenshot.");
			return;
		}
		
		// Remove the report folder from the beginning of the path
		screenshotPath = screenshotPath.replaceAll(Settings.getProperty("report_path"), "");
		
		// Remove / from the beginning
		if(screenshotPath.startsWith(File.separator)) {
			screenshotPath = screenshotPath.substring(1, screenshotPath.length());
		}

		// Create the screenshot
		try {
			screenshot = MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build();
		} catch (IOException e) {
			context.report.info("Could not add screenshot to the report.");
			return;
		}

		// Add to the report
		context.report.info("", screenshot);
	}
	
	/**
	 * Will bring an element into view and take the screenshot.
	 */
	public static void addScreenshot(TestContext context, WebElement element) {
		showElement(context, element);
		addScreenshot(context);
	}

	/**
	 * Will take a screenshot, save to the disk and return the filename.
	 * 
	 * @return Path to the screenshot.
	 */
	public static String saveScreenshot(TestContext context) {

		WebDriver driver;
		String screenshotName, screenshotPath, fullPath;
		byte[] screenshotData;
		long threadId;

		// Initialize some stuff
		driver = context.driver;

		// Increment the screenshot count
		screenshotCount++;

		// Get the Thread ID
		threadId = Thread.currentThread().getId();

		// Set the filename and build the path
		screenshotName = String.format("scr_%d_%d.png", threadId, screenshotCount);
		screenshotPath = Settings.getProperty("report_path") + File.separator + "screenshots";
		fullPath = screenshotPath + File.separator + screenshotName;

		// Get the bytes from the screenshot
		screenshotData = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

		try {
			// Create the folders
			Files.createDirectories(Paths.get(screenshotPath));

			// Create the file
			Files.write(Paths.get(fullPath), screenshotData);

		} catch (IOException e) {
			// Return empty in case of issue.
			return null;

		}

		return fullPath;

	}

	/**
	 * Bring an element into view.
	 * 
	 * @param context
	 *            Test context containing the driver.
	 * @param element
	 *            Element to be displayed.
	 */
	public static void showElement(TestContext context, WebElement element) {

		JavascriptExecutor js;
		String script;

		// Get the driver
		js = (JavascriptExecutor) context.driver;

		// Setup the script
		script = "arguments[0].scrollIntoView(true);";

		// Run the script
		js.executeScript(script, element);

	}

}
