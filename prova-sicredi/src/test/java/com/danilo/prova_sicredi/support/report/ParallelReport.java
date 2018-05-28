package com.danilo.prova_sicredi.support.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.danilo.prova_sicredi.support.Settings;

/**
 * This class manages the main context for the Extent Report instance. This will
 * provide ExtentTest instances for whoever requests it. This is meant to be
 * used by the TextContext so it can get the ExtentTest instance for each thread
 * that will run.
 * 
 * @author danilo
 *
 */
public abstract class ParallelReport {

	// This is the main report instance
	public static ExtentReports report;

	// Store the initialization status
	private static boolean initialized = false;

	/**
	 * Will initialize the ExtentReports instance.
	 */
	public static synchronized void initialize() {

		ExtentHtmlReporter htmlReporter;
		String reportPath;

		// Get the html report path
		reportPath = Settings.getProperty("report_path") + File.separator + "report.html";

		// Initialize the HTML reporter
		htmlReporter = new ExtentHtmlReporter(reportPath);

		// Instantiate the ExtentReports main class
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		
		// Clean the screenshots folder
		cleanFolders();

		// Mark initialized
		initialized = true;

	}
	
	/**
	 * Clean the report folder and delete old screenshots.
	 */
	private static synchronized void cleanFolders() {
		
		String screenshotFolder;
		
		// Create the screenshots folder
		screenshotFolder = Settings.getProperty("report_path") + File.separator + "screenshots";
		
		// Make sure the screenshots folder exists
		try {
			Files.createDirectories(Paths.get(screenshotFolder));
		} catch (IOException e) {
			System.err.println("Could not clean screenshots folder. Old screenshots may remain in the folder.");
			return;
		}
		
		// Clean the folder
		for(File file : Paths.get(screenshotFolder).toFile().listFiles()) {
			file.delete();
		}
		
	}

	/**
	 * Create a new test node inside the main report.
	 * 
	 * @param testName
	 *            Name of the test to be displayed in the report.
	 * @return An instance of ExtentTest.
	 */
	public static synchronized ExtentTest getExtentTest(String testName) {
		if (!initialized)
			initialize();

		return report.createTest(testName);
	}

	/**
	 * Will flush the existing contents to the html in the disk.
	 */
	public static synchronized void flush() {
		report.flush();
	}

}
