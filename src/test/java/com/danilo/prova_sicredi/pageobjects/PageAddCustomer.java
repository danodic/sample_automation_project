package com.danilo.prova_sicredi.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.danilo.prova_sicredi.support.PageObject;
import com.danilo.prova_sicredi.support.report.ReportHelper;

public class PageAddCustomer extends PageObject {

	// "Add Customer" header
	private By lblAddCustomerHeader = By.xpath("//div[contains(text(), 'A') and @class='floatL l5']");

	// Form fields
	private By txtName = By.xpath("//input[@id='field-customerName']");
	private By txtLastName = By.xpath("//input[@id='field-contactLastName']");
	private By txtContactFirstName = By.xpath("//input[@id='field-contactFirstName']");
	private By txtPhone = By.xpath("//input[@id='field-phone']");
	private By txtAddressLine1 = By.xpath("//input[@id='field-addressLine1']");
	private By txtAddressLine2 = By.xpath("//input[@id='field-addressLine2']");
	private By txtCity = By.xpath("//input[@id='field-city']");
	private By txtState = By.xpath("//input[@id='field-state']");
	private By txtPostalCode = By.xpath("//input[@id='field-postalCode']");
	private By txtCountry = By.xpath("//input[@id='field-country']");
	private By txtFromEmployeer = By.xpath("//div[@class='chosen-search']/input");
	private By txtCreditLimit = By.xpath("//input[@id='field-creditLimit']");

	// From employer needs special handling
	private By lblSelectFromEmployer = By.xpath("//span[text()='Select from Employeer']");

	// Buttons
	private By btnSave = By.xpath("//button[@id='form-button-save']");
	
	// Links
	private By lnkGoBackToList = By.xpath("//a[text()='Go back to list']");

	// Success message
	private By lblSuccessMessage = By.xpath("//div[@id='report-success']");

	@Override
	public void validatePageLoaded() {
		report.info("Validate the 'Add Customer' page has loaded.");

		// Validate the page is displayed
		new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(lblAddCustomerHeader));

		// Add a screenshot and report entry
		report.pass("Page was displayed sucessfully.");
		ReportHelper.addScreenshot(context);
	}

	/**
	 * Fill out the Add Customer form.
	 */
	public void doFillOutForm(String name, String lastName, String contactFirstName, String phone, String address1,
			String address2, String city, String state, String postalCode, String country, String fromEmployer,
			String creditLimit) {

		// Send keys to different fields
		driver.findElement(txtName).sendKeys(name);
		driver.findElement(txtLastName).sendKeys(lastName);
		driver.findElement(txtContactFirstName).sendKeys(contactFirstName);
		driver.findElement(txtPhone).sendKeys(phone);
		driver.findElement(txtAddressLine1).sendKeys(address1);
		driver.findElement(txtAddressLine2).sendKeys(address2);
		driver.findElement(txtCity).sendKeys(city);
		driver.findElement(txtState).sendKeys(state);
		driver.findElement(txtPostalCode).sendKeys(postalCode);
		driver.findElement(txtCountry).sendKeys(country);
		
		// "From Employer" needs some special handling
		driver.findElement(txtCountry).sendKeys(Keys.TAB);
		driver.findElement(lblSelectFromEmployer).click();
		
		// Wait for the field to be on screen and then send the keys
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(txtFromEmployeer));
		driver.findElement(txtFromEmployeer).sendKeys(fromEmployer);
		driver.findElement(txtFromEmployeer).sendKeys(Keys.TAB);

		driver.findElement(txtCreditLimit).sendKeys(creditLimit);

	}

	/**
	 * Click on the save button.
	 */
	public void doClickSave() {
		driver.findElement(btnSave).click();
	}

	/**
	 * Click on "Go Back to list" at the green panel.
	 */
	public void doClickGoBackToList() {
		driver.findElement(lnkGoBackToList).click();
	}

	/**
	 * Validates data has been inserted successfully after clicking on Save button.
	 */
	public void validateDataHasBeenInserted() {

		String message;

		// Setup the message and add some verbose
		message = "Your data has been successfully stored into the database.".trim().toUpperCase();
		report.info("Validate that the following message shows up on screen: '" + message + "'.");

		// Check if it is being displayed
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(lblSuccessMessage));

		// Check if the right message is in place
		assert (driver.findElement(lblSuccessMessage).getText().trim().toUpperCase().contains(message));

		// Add an screenshot and some reporting
		report.pass("Message has been found on screen.");
		ReportHelper.addScreenshot(context, driver.findElement(lblSuccessMessage));
	}

}
