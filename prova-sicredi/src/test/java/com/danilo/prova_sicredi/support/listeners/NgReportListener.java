package com.danilo.prova_sicredi.support.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.danilo.prova_sicredi.support.TestClass;
import com.danilo.prova_sicredi.support.TestContext;
import com.danilo.prova_sicredi.support.factories.ContextFactory;
import com.danilo.prova_sicredi.support.report.ParallelReport;
import com.danilo.prova_sicredi.support.report.ReportHelper;

/**
 * This class handles the automatic reporting for the TestNG invocations.
 * 
 * @author danilo
 *
 */
public class NgReportListener implements IInvokedMethodListener, ITestListener {

	/**
	 * The context for each test has to be created at this method, as it has access
	 * to the invoked test method what allow us to capture the annotation
	 * information and add automatically to the report. This way we can create the
	 * test and add the proper name as well.
	 */
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult result) {
		TestContext context;
		String testName, testDescription, testClassname;
		Test testAnnotation;
		TestClass classAnnotation;

		// Get the method annotation
		testAnnotation = method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
		classAnnotation = method.getTestMethod().getConstructorOrMethod().getDeclaringClass().getAnnotation(TestClass.class);

		// Get the properties from the annotation
		testClassname = classAnnotation.value();
		testName = testAnnotation.testName();
		testDescription = testAnnotation.description();

		// Create the context
		context = ContextFactory.getContext( testClassname + "#" + testName);

		// Add the first step with the test description
		// Yeah, that doesn't look really good, but works nicely :)
		context.report.info("<strong>Description:</strong><br/>" + testDescription);
	}

	/**
	 * Makes sure we flush the report after each test. We can't finalize Selenium
	 * here because we may need to use at the OnTestFailure, OnFinish and other
	 * methods that are executed after this one.
	 */
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		ParallelReport.flush();
	}

	/**
	 * Capture the exception and add to the report.
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		TestContext context;

		// Get the context and add information in the report (and finalize Selenium)
		context = ContextFactory.getContext();
		takeScreenshot(context, result.getThrowable());
		context.report.fail("Errors happened during the test execution.");
		context.report.fail(result.getThrowable());
		context.finalizeSelenium();

		// Flush the report
		ParallelReport.flush();
		
		// Finalize the context
		ContextFactory.finalizeContext();

	}

	/**
	 * Capture the exception and add to the report.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		TestContext context;

		// Get the context and add information in the report (and finalize Selenium)
		context = ContextFactory.getContext();
		takeScreenshot(context, result.getThrowable());
		context.report.fail("Errors happened during the test execution.");
		context.report.fail(result.getThrowable());
		context.finalizeSelenium();

		// Flush the report
		ParallelReport.flush();
		
		// Finalize the context
		ContextFactory.finalizeContext();
	}

	/**
	 * Add a skip to the report.
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		TestContext context;

		// Get the context and add information in the report (and finalize Selenium)
		context = ContextFactory.getContext();
		context.report.skip("Test skipped.");
		context.finalizeSelenium();

		// Flush the report
		ParallelReport.flush();
		
		// Finalize the context
		ContextFactory.finalizeContext();
	}

	/**
	 * Finalize selenium and flush the report.
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		TestContext context;

		// Get the context and finalize Selenium
		context = ContextFactory.getContext();
		context.finalizeSelenium();

		// Flush the report
		ParallelReport.flush();
		
		// Finalize the context
		ContextFactory.finalizeContext();
	}

	/**
	 * Initialize the report.
	 */
	@Override
	public void onStart(ITestContext arg0) {
		ParallelReport.initialize();
	}
	
	/**
	 * Helps taking screenshots in case of errors.
	 */
	private void takeScreenshot(TestContext context, Throwable exception) {
		if (context.driver != null && exception != null) {

			// We can't take a screenshot in this case
			if (exception.getMessage() !=null && exception.getMessage().contains("UnhandledAlertException")) {
				context.report.info("Cannot take a screenshot, an alert is blocking it.");
			} else {
				// Add the screenshot
				ReportHelper.addScreenshot(context);
			}
		}
	}

	// Methods below aren't used.
	@Override
	public void onTestStart(ITestResult arg0) {
	}

	@Override
	public void onFinish(ITestContext arg0) {
	}

}
