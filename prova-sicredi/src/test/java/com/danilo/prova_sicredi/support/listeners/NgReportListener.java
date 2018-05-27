package com.danilo.prova_sicredi.support.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.danilo.prova_sicredi.support.TestContext;
import com.danilo.prova_sicredi.support.factories.ContextFactory;
import com.danilo.prova_sicredi.support.report.ParallelReport;

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
		String testName, testDescription;
		Test annotation;

		// Get the method annotation
		annotation = method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);

		// Get the properties from the annotation
		testName = annotation.testName();
		testDescription = annotation.description();

		// Create the context
		context = ContextFactory.getContext(testName);

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
		context.report.fail("Errors happened during the test execution.");
		context.report.fail(result.getThrowable());
		context.finalizeSelenium();

		// Flush the report
		ParallelReport.flush();

	}

	/**
	 * Capture the exception and add to the report.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		TestContext context;

		// Get the context and add information in the report (and finalize Selenium)
		context = ContextFactory.getContext();
		context.report.fail("Errors happened during the test execution.");
		context.report.fail(result.getThrowable());
		context.finalizeSelenium();

		// Flush the report
		ParallelReport.flush();
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
	}
	
	/**
	 * Initialize the report.
	 */
	@Override
	public void onStart(ITestContext arg0) {
		ParallelReport.initialize();
	}

	// Methods below aren't used.
	@Override
	public void onTestStart(ITestResult arg0) {
	}

	@Override
	public void onFinish(ITestContext arg0) {
	}

}
