package com.danilo.prova_sicredi.pageobjects;

import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.danilo.prova_sicredi.support.Environment;
import com.danilo.prova_sicredi.support.PageObject;
import com.danilo.prova_sicredi.support.report.ReportHelper;

public class PageCustomers extends PageObject {

	// "Add Customer" header
	private By lblCustomersHeader = By.xpath("//div[contains(text(), 'Customers') and @class='floatL l5']");

	// Select Version drop down
	private By dpwSelectVersion = By.xpath("//select[@id='switch-version-select']");

	// Add Customer button
	private By btnAddCustomer = By
			.xpath("//a[contains(text()[2], 'Add Customer') and contains(@class, 'btn btn-default')]");

	// Search elements
	private By btnSearch = By.xpath("//a[contains(@class, 'search-button')]");
	private By txtSearch = By.xpath("//input[@name='search']");

	// Search results elements
	// Those selectors are parameterized, so we use string for those ones
	private String lblNameSearchResults = "//td[contains(text(), '?')]";
	private String chkActions = "//td[contains(text(), '?')]//ancestor::tr//input[@type='checkbox']";

	// Delete button
	private By btnDelete = By.xpath("//tr[contains(@class, 'filter-row')]//a[@title='Delete']");

	// Modal dialog element
	private By lblDeleteModalHeader = By
			.xpath("//div[contains(@class, 'delete-multiple-confirmation modal')]//h5[text()='Delete']");
	private By lblDeleteModalTextOne = By.xpath(
			"//div[contains(@class, 'delete-multiple-confirmation modal')]//p[@class='alert-delete-multiple-one']");
	private By btnDeleteModalDeleteButton = By.xpath(
			"//div[contains(@class, 'delete-multiple-confirmation modal')]//button[normalize-space(text())='Delete']");

	// Delete confirmation alert
	private By lblDeleteConfirmationAlert = By.xpath("//span[@data-growl='message']/p");

	// Drop down options enum
	public static enum SelectVersionOptions {
		BOOTSTRAP_V3, BOOTSTRAP_V4
	}

	@Override
	public void validatePageLoaded() {
		report.info("Validate the 'Customers' page has loaded.");

		// Wait for the element to be on screen
		new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(lblCustomersHeader));

		// Add a screenshot and report entry
		report.pass("Page was displayed sucessfully.");
		ReportHelper.addScreenshot(context);
	}

	/**
	 * Will navigate to the Customers page and validate it has loaded.
	 */
	public void navigateToCustomers() {
		driver.get(Environment.GROCERY_CRUD_URL);

		// Validate that the page has loaded
		validatePageLoaded();
	}

	/**
	 * Select an option in the "Select Version" drop down.
	 * 
	 * @param option
	 *            An option from the enum SelectVersionOptions
	 */
	public void doSelectVersion(SelectVersionOptions option) {
		Select dropDown = new Select(driver.findElement(dpwSelectVersion));

		switch (option) {
		case BOOTSTRAP_V3:
			dropDown.selectByVisibleText("Bootstrap V3 Theme");
			break;
		case BOOTSTRAP_V4:
			dropDown.selectByVisibleText("Bootstrap V4 Theme");
			break;
		}
	}

	/**
	 * Click on the addCustomer button.
	 */
	public void doClickAddCustomer() {
		driver.findElement(btnAddCustomer).click();
	}

	/**
	 * Click on the search button, type the search term and press enter.
	 * 
	 * @param value
	 *            Value to look for.
	 */
	public void doSearch(String value) {
		// Click on the button
		driver.findElement(btnSearch).click();

		// Wait for the input to be visible
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(txtSearch));

		// Send keys to the text field
		driver.findElement(txtSearch).sendKeys(value);

		// Do the search
		driver.findElement(txtSearch).sendKeys(Keys.ENTER);

	}

	/**
	 * Validate that the search results have been displayed properly.
	 * 
	 * @param value
	 *            value to look for.
	 */

	/**
	 * Validate that the search results are displayed for the name provided.
	 */
	public void validateSearchResultsDisplayed(String value) {

		By selector;

		// Setup the selector parameters
		selector = By.xpath(lblNameSearchResults.replaceAll("\\?", value));

		// Assert the value is on screen
		new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(selector));

		// Add a screenshot
		ReportHelper.addScreenshot(context);

	}

	/**
	 * Click on the 'Action' checkbox for the result with the name provided.
	 */
	public void doSelectSearchResult(String name) {

		Calendar cal;
		Date endTime;
		By selector;

		// Setup the selector parameters
		selector = By.xpath(chkActions.replaceAll("\\?", name));

		// Make sure it is clickable
		new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(selector));

		// When there are too many search results, we'll get stale reference
		// exceptions.. Needs to be insistent here.
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, timeout);
		endTime = cal.getTime();

		// Deactivate Selenium's exception handling for a moment
		context.setHandleExceptions(false);

		// Try for some time
		while (Calendar.getInstance().getTime().before(endTime)) {

			try {

				// Click on the checkbox
				driver.findElement(selector).click();

				// Reactivate exception handling
				context.setHandleExceptions(true);

				// Leave the method
				return;

			} catch (Exception e) {
				continue;
			}

		}

		// Throw an exception in case we reach this point
		throw new RuntimeException("Unable to select result.");

	}

	/**
	 * Will clicl on the button 'Delete' after selecting a search result.
	 */
	public void doClickDelete() {
		driver.findElement(btnDelete).click();
	}

	/**
	 * Makes sure the delete modal is displayed and validate the message displayed
	 * in it.
	 */
	public void validateDeletePopupIsDisplayed() {
		String message;

		// Setup message for validation
		message = "Are you sure that you want to delete this 1 item?".trim().toUpperCase();

		// Add some reporting
		report.info("Validate that the following message shows up on screen: '" + message + "'.");

		// Wait for the modal to show up
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(lblDeleteModalHeader));

		// Validate the message
		assert (driver.findElement(lblDeleteModalTextOne).getText().trim().toUpperCase().equals(message));

		// Add a screenshot and some reporting
		report.pass("Message has been found on screen.");
		ReportHelper.addScreenshot(context);

	}

	/**
	 * Click on 'Delete' in the delete confirmation modal.
	 */
	public void doConfirmDeleteModal() {
		driver.findElement(btnDeleteModalDeleteButton).click();
	}

	/**
	 * Validates the alert that is displayed after deleting the entry.
	 */
	public void validateDeleteConfirmation() {
		String message;

		// Setup message for validation
		message = "Your data has been successfully deleted from the database.".trim().toUpperCase();

		// Add some reporting
		report.info("Validate that the following message shows up on screen: '" + message + "'.");

		// Wait for the alert to be on screen and capture it
		new WebDriverWait(driver, timeout)
				.until(ExpectedConditions.presenceOfElementLocated(lblDeleteConfirmationAlert));

		new WebDriverWait(driver, timeout)
				.until(ExpectedConditions.visibilityOfElementLocated(lblDeleteConfirmationAlert));

		// Do the assert
		assert (driver.findElement(lblDeleteConfirmationAlert).getText().trim().toUpperCase().equals(message));

		// Add a screenshot and some reporting
		report.pass("Message has been found on screen.");
		ReportHelper.addScreenshot(context);

	}

}
