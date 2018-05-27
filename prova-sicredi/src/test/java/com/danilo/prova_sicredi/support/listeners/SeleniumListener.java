package com.danilo.prova_sicredi.support.listeners;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.danilo.prova_sicredi.support.TestContext;
import com.danilo.prova_sicredi.support.report.ReportHelper;

/**
 * This class handles the log of Selenium events in the ExtentTest instance.
 * This listener is connected to the webdriver by the time it is instantiated by
 * the DriverFactory class.
 * 
 * @author danilo
 *
 */
public class SeleniumListener implements WebDriverEventListener {

	// The test context
	private TestContext context;

	// The report for the current thread
	private ExtentTest report;

	// Stores the last step executed, used for error handling.
	private String lastStep;
	private String script;

	/**
	 * Initializes the Selenium Listener using the Test Context, so it has access to
	 * the report.
	 * 
	 * @param context
	 *            Context used by the thread.
	 */
	public SeleniumListener(TestContext context) {
		this.context = context;
		report = context.report;
		lastStep = null;
		script = null;
	}

	@Override
	public void afterAlertAccept(WebDriver arg0) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterAlertDismiss(WebDriver arg0) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> arg0, X arg1) {
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterNavigateRefresh(WebDriver arg0) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		report.pass(lastStep);
		report.pass(MarkupHelper.createCodeBlock(script));
		lastStep = null;
		this.script = null;
	}

	@Override
	public void afterSwitchToWindow(String arg0, WebDriver arg1) {
		report.pass(lastStep);
		lastStep = null;
	}

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		lastStep = "Accept alert.";
	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		lastStep = "Dismiss alert.";
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] value) {
		lastStep = "Change value of element with tag <" + element.getTagName() + "> to '" + value.toString() + "'.";
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		lastStep = "Click on element with tag <" + element.getTagName() + ">.";
	}

	@Override
	public void beforeFindBy(By selector, WebElement element, WebDriver driver) {
		lastStep = "Find element selected '" + selector.toString() + "'";
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> arg0) {

	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		lastStep = "Navigate back.";
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		lastStep = "Navigate forward.";
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		lastStep = "Refresh the page.";
	}

	@Override
	public void beforeNavigateTo(String path, WebDriver driver) {
		lastStep = "Navigate to '" + path + "'.";
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		lastStep = "Inject script.";
		this.script = script;
	}

	@Override
	public void beforeSwitchToWindow(String handle, WebDriver driver) {
		lastStep = "Switch to window with handle '" + handle + "'.";
	}

	/**
	 * This method is very useful but has some issues:
	 * 
	 * - when using the method not()along with ExpectedConditions, Selenium will
	 * trigger an internal Exception as a mean of handling the inversion of the
	 * condition. The problem is that this internal exception will trigger this
	 * method anyway and, if you are logging errors, closing drivers and etc, that
	 * will become a mess.
	 * 
	 * - when trying to work around issues, some times we may want to generate and
	 * handle exceptions in our code. This method won't be aware that the exceptions
	 * are being handled and will run before the code can reach your catch block.
	 * 
	 * For those reasons, there has to be a way to temporarily deactivate the
	 * exception handling, and this is done by using the context, by checking the
	 * flag handleExceptions.
	 */
	@Override
	public void onException(Throwable exception, WebDriver driver) {

		// Skip the handling of exceptions in case it is needed.
		if (!context.isHandleExceptions())
			return;

		// In case we have existing steps, fail them
		if (lastStep != null)
			report.fail(lastStep);

		// Add the script as well in case we have it
		if (script != null)
			report.fail(MarkupHelper.createCodeBlock(script));

		// Add the error to the report
		report.fail("Something wrong has happened while running the test.");
		report.fail(exception);

		/*
		 * Check if we still have the driver available to take a screenshot. Also, check
		 * if this is not a blocking exception that would trigger an exception loop
		 * here.
		 */
		if (context.driver != null) {

			// We can't take a screenshot in this case
			if (exception instanceof UnhandledAlertException) {
				report.info("Cannot take a screenshot, a alert is blocking it.");
			} else {

				// Add the screenshot
				ReportHelper.addScreenshot(context);
			}

		}

		// Raise the exception as a runtime exception so we don't need to add throws
		// declaration
		throw new RuntimeException(exception);
	}

}
