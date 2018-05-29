package com.danilo.prova_sicredi.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import com.danilo.prova_sicredi.pageobjects.PageAddCustomer;
import com.danilo.prova_sicredi.pageobjects.PageCustomers;
import com.danilo.prova_sicredi.support.TestClass;
import com.danilo.prova_sicredi.support.TestContext;
import com.danilo.prova_sicredi.support.factories.ContextFactory;
import com.danilo.prova_sicredi.support.listeners.NgReportListener;

@TestClass("Grocery Crud Example Tests")
@Listeners(NgReportListener.class)
public class GroceryCrudTest {

	/**
	 * Challenge 1:
	 * 1. Go to https://www.grocerycrud.com/demo/bootstrap_theme
	 * 2. Change the combo box "Select Version" to "Bootstrap V4 Theme"
	 * 3. Click on the button "Add Customer"
	 * 4. Fill out the form fields with the following information:
	 *    - Name: Teste Sicredi
	 *    - Last name: Teste
	 *    - ContactFirstName: your name
	 *    - Phone: 51 9999-9999
	 *    - AddressLine1: Av Assis Brasil, 3970
	 *    - AddressLine2: Torre D
	 *    - City: Porto Alegre
	 *    - State: RS
	 *    - PostalCode: 91000-000
	 *    - Country: Brasil
	 *    - from Employeer: Fixter
	 *    - CreditLimit: 200
	 * 5. Click on the button "Save"
	 * 6. Validate the message "Your data has been successfully stored into the database." using an assertion.
	 * 7. Close the web browser.
	 * 
	 * @param data
	 *            Data loaded from YAML file.
	 */
	@Test(testName = "001 - Add a customer", description = "Will add a customer and validate the system displays a success message;", dataProvider = "customerDataProvider")
	public static void addCustomer(Map<String, String> data) {

		// Get the context from the factory and initialize Selenium
		TestContext context = ContextFactory.getContext();
		context.initializeSelenium();

		// Instantiate the pages
		PageCustomers pCust = PageFactory.initElements(context.driver, PageCustomers.class);
		PageAddCustomer pAddCust = PageFactory.initElements(context.driver, PageAddCustomer.class);

		// 1. Navigate to the crud page
		pCust.navigateToCustomers();

		// 2. Change the value of the drop down to Bootstrap V4
		pCust.doSelectVersion(PageCustomers.SelectVersionOptions.BOOTSTRAP_V4);
		pCust.validatePageLoaded();

		// 3. Click on the Add Customer button
		pCust.doClickAddCustomer();
		pAddCust.validatePageLoaded();

		// 4. Fill out the form
		pAddCust.doFillOutForm(data.get("name"), data.get("lastName"), data.get("contactFirstName"), data.get("phone"),
				data.get("addressLine1"), data.get("addressLine2"), data.get("city"), data.get("state"),
				data.get("postalCode"), data.get("country"), data.get("fromEmployer"), data.get("creditLimit"));

		// 5. Click on the save button
		pAddCust.doClickSave();

		// 6. Validate the success message is displayed on screen
		pAddCust.validateDataHasBeenInserted();

		// 7. Close the browser
		// (the listeners are going to take care of it)

	}

	/**
	 * Challenge 2: Pre-Conditions: Execute all steps from Challenge 1.
	 * 
	 * 1. Click on "Go Back to List"
	 * 2. Click on the looking glass icon (search) and type the content of Name
	 *    (Teste Sicredi)
	 * 3. Click on the checkbox under the word "Actions"
	 * 4. Validate the text Are you sure that you want to delete this 1 item?" in
	 *    the pop-up that will be presented, by using an assertion
	 * 5. Click on "Delete" inside the pop-up
	 * 6. A message inside a green box in the upper-right corner of the screen will
	 * show up. Add an assert for the message "Your data has been successfully
	 * deleted from the database."
	 * 7. Validate the message "Your data has been successfully stored into the
	 * database." using an assertion.
	 * 8. Close the web driver.
	 * 
	 * @param data
	 *            Data loaded from YAML file.
	 */
	@Test(testName = "002 - Delete a customer", description = "Will add a customer and then delete it", dataProvider = "customerDataProvider")
	public static void deleteCustomer(Map<String, String> data) {

		// Get the context from the factory and initialize Selenium
		TestContext context = ContextFactory.getContext();
		context.initializeSelenium();

		// Instantiate the pages
		PageCustomers pCust = PageFactory.initElements(context.driver, PageCustomers.class);
		PageAddCustomer pAddCust = PageFactory.initElements(context.driver, PageAddCustomer.class);

		// 0. Add a customer
		/*
		 * As the context is managed at a Thread ID level, we can just run the very same
		 * steps from the previous test within this one. When requesting a new context
		 * from the ContextFactory, it will find an existing context and will return it
		 * instead of creating a new one. As a result, we can call directly the method
		 * from the last test within this one and assure both will run within the same
		 * context, using the same driver and the same report instance.
		 */
		addCustomer(data);

		// 1. Click on 'Go Back to List'
		pAddCust.doClickGoBackToList();
		pCust.validatePageLoaded();

		// 2. Search for the customer that has been added on pre-conditions
		pCust.doSearch(data.get("name"));
		pCust.validateSearchResultsDisplayed(data.get("name"));

		// 3. Click on the 'Actions' checkbox for our customer
		pCust.doSelectSearchResult(data.get("name"));

		// 4. Click on 'Delete'
		pCust.doClickDelete();

		// 5. Validate the text displayed in the modal
		pCust.validateDeletePopupIsDisplayed();

		// 6. Click on 'Delete' in the modal
		pCust.doConfirmDeleteModal();

		// 7. Validate the delete confirmation alert
		pCust.validateDeleteConfirmation();

		// 7. Close the browser
		// (the listeners are going to take care of it)

	}

	/**
	 * Data will be parsed from a YAML file using SnakeYAML.
	 * 
	 * @return An array of Maps containing the data parsed from the file.
	 * @throws FileNotFoundException
	 *             In case the file path is wrong.
	 */
	@DataProvider()
	public static Object[] customerDataProvider() throws FileNotFoundException {

		ArrayList<Map<String, String>> data;
		String dataPath;
		Yaml yaml;

		// Set the data input path
		dataPath = "data\\grocery_crud\\001_add_customer.yaml";

		// Instantiate the YAML parser and load the data
		yaml = new Yaml();
		data = yaml.load(new FileInputStream(dataPath));

		// Parse the data and return it
		return data.toArray();

	}

}
