package com.utility.library;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.common.io.Files;
import com.properties.mapping.LoadProp;

public class Util {
	static java.util.Date date;
	static BufferedWriter logger = null;
	static InputStream tests;
	static Workbook wb;
	static Iterator<Row> testsRowIt;
	static int webTestCount = 0;
	static int androidTestCount = 0;
	static int androidBrowserTestCount = 0;
	static int iosTestCount = 0;
	static int iosBrowserTestCount = 0;
	static int apiTestCount = 0;
	static DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
	static SoftAssert assertion = new SoftAssert();
	static SoftAssert sAssert = new SoftAssert();

	private Util() {
	}

	/**
	 * Method to map functions from xls file.
	 * 
	 * @param testfile Name of the test file
	 * @throws IOException exception
	 */
	public static void getXlsTestsMapping(String testfile) throws IOException {
		// Read test input file
		tests = new FileInputStream(Global.PROJECT_LOCATION + testfile);
		wb = new HSSFWorkbook(tests);
		// Get tests sheet
		Global.testSheet = wb.getSheetAt(0);
	}

	/**
	 * Method to populate the test cases start and end rows in dictionary for each
	 * platform from tests input file.
	 * 
	 * @return Return total test count
	 */
	public static int populatePlatformTests() {
		int testcount = 0;
		int rowCount = 0;
		String currentPlatform = "";
		String previousPlatform = "";
		testsRowIt = Global.testSheet.iterator();
		while (testsRowIt.hasNext()) {
			Row row = testsRowIt.next();
			if (rowCount >= 1) {

				if (row.getCell(0) == null || !row.getCell(0).toString().equalsIgnoreCase(""))
					previousPlatform = currentPlatform;
				if (row.getCell(0) != null && row.getCell(8) != null) {
					testcount++;
					currentPlatform = row.getCell(9).toString();
					// populate PlatformTests based current currentPlatform value
					if (currentPlatform.equalsIgnoreCase(Global.WEB)) {
						Global.totalWebPlatformTests++;
						// populate start row number
						Global.webPlatformTestCases.put("S" + Integer.toString(webTestCount),
								Integer.toString(row.getRowNum()));
						// populate test end row number
						if (!previousPlatform.equalsIgnoreCase("")) {
							populatePlatformTestEndRowNumber(previousPlatform, row);
						}
						webTestCount++;
					} else if (currentPlatform.equalsIgnoreCase(Global.ANDROID_BROWSER)) {
						Global.totalAndroidBrowserPlatformTests++;
						// populate start row number
						Global.androidBrowserPlatformTestCases.put("S" + Integer.toString(androidBrowserTestCount),
								Integer.toString(row.getRowNum()));
						// populate test end row number
						if (!previousPlatform.equalsIgnoreCase("")) {
							populatePlatformTestEndRowNumber(previousPlatform, row);
						}
						androidBrowserTestCount++;
					} else if (currentPlatform.equalsIgnoreCase(Global.ANDROID)) {
						Global.totalAndroidPlatformTests++;
						// populate start row number
						Global.androidPlatformTestCases.put("S" + Integer.toString(androidTestCount),
								Integer.toString(row.getRowNum()));
						// populate test end row number
						if (!previousPlatform.equalsIgnoreCase("")) {
							populatePlatformTestEndRowNumber(previousPlatform, row);
						}
						androidTestCount++;
					} else if (currentPlatform.equalsIgnoreCase(Global.IOS)) {
						Global.totalIOSPlatformTests++;
						// populate start row number
						Global.iosPlatformTestCases.put("S" + Integer.toString(iosTestCount),
								Integer.toString(row.getRowNum()));
						// populate test end row number
						if (!previousPlatform.equalsIgnoreCase("")) {
							populatePlatformTestEndRowNumber(previousPlatform, row);
						}
						iosTestCount++;
					} else if (currentPlatform.equalsIgnoreCase("API")) {
						Global.totalAPIPlatformTests++;
						// populate start row number
						Global.apiPlatformTestCases.put("S" + Integer.toString(apiTestCount),
								Integer.toString(row.getRowNum()));
						// populate test end row number
						if (!previousPlatform.equalsIgnoreCase("")) {
							populatePlatformTestEndRowNumber(previousPlatform, row);
						}
						apiTestCount++;
					} else if (currentPlatform.equalsIgnoreCase(Global.IOS_BROWSER)) {
						Global.totalIOSBrowserPlatformTests++;
						// populate start row number
						Global.iosBrowserPlatformTestCases.put("S" + Integer.toString(iosBrowserTestCount),
								Integer.toString(row.getRowNum()));
						// populate test end row number
						if (!previousPlatform.equalsIgnoreCase("")) {
							populatePlatformTestEndRowNumber(previousPlatform, row);
						}
						iosBrowserTestCount++;
					}
				}
				// populate test end row when it is end of test suite and break out of the loop
				else if (row.getCell(0) == null && row.getCell(8) == null) {
					if (previousPlatform.equalsIgnoreCase(Global.WEB)) {
						Global.webPlatformTestCases.put("E" + Integer.toString(webTestCount - 1),
								Integer.toString(row.getRowNum() - 1));
					} else if (previousPlatform.equalsIgnoreCase(Global.ANDROID_BROWSER)) {
						Global.androidBrowserPlatformTestCases.put("E" + Integer.toString(androidBrowserTestCount - 1),
								Integer.toString(row.getRowNum() - 1));
					} else if (previousPlatform.equalsIgnoreCase(Global.ANDROID)) {
						Global.androidPlatformTestCases.put("E" + Integer.toString(androidTestCount - 1),
								Integer.toString(row.getRowNum() - 1));
					} else if (previousPlatform.equalsIgnoreCase(Global.IOS)) {
						Global.iosPlatformTestCases.put("E" + Integer.toString(iosTestCount - 1),
								Integer.toString(row.getRowNum() - 1));
					} else if (previousPlatform.equalsIgnoreCase("API")) {
						Global.apiPlatformTestCases.put("E" + Integer.toString(apiTestCount - 1),
								Integer.toString(row.getRowNum() - 1));
					} else if (previousPlatform.equalsIgnoreCase(Global.IOS_BROWSER)) {
						Global.iosBrowserPlatformTestCases.put("E" + Integer.toString(iosBrowserTestCount - 1),
								Integer.toString(row.getRowNum() - 1));
					}
					break;
				}
			} // rowCount if loop..
			rowCount++;
		} // end of while

		// store the total platform wise test count
		Global.totalWebPlatformTests = webTestCount;
		Global.totalIOSPlatformTests = iosTestCount;
		Global.totalIOSBrowserPlatformTests = iosBrowserTestCount;
		Global.totalAndroidPlatformTests = androidTestCount;
		Global.totalAndroidBrowserPlatformTests = androidBrowserTestCount;
		System.out.println(" web : " + (Global.webPlatformTestCases).toString());
		System.out.println(" Android : " + (Global.androidPlatformTestCases).toString());
		System.out.println(" Android Browser : " + (Global.androidBrowserPlatformTestCases).toString());
		System.out.println(" iOS : " + (Global.iosPlatformTestCases).toString());
		System.out.println(" iOS Browser : " + (Global.iosBrowserPlatformTestCases).toString());
		return testcount;
	}

	/**
	 * Method to populate test's end row number
	 * 
	 * @param previousPlatform platform name
	 * @param row              row instance
	 */
	public static void populatePlatformTestEndRowNumber(String previousPlatform, Row row) {

		if (previousPlatform.equalsIgnoreCase(Global.WEB)) {
			Global.webPlatformTestCases.put("E" + Integer.toString(webTestCount - 1),
					Integer.toString(row.getRowNum() - 1));
		} else if (previousPlatform.equalsIgnoreCase(Global.ANDROID_BROWSER)) {
			Global.androidBrowserPlatformTestCases.put("E" + Integer.toString(androidBrowserTestCount - 1),
					Integer.toString(row.getRowNum() - 1));
		} else if (previousPlatform.equalsIgnoreCase(Global.ANDROID)) {
			Global.androidPlatformTestCases.put("E" + Integer.toString(androidTestCount - 1),
					Integer.toString(row.getRowNum() - 1));
		} else if (previousPlatform.equalsIgnoreCase(Global.IOS)) {
			Global.iosPlatformTestCases.put("E" + Integer.toString(iosTestCount - 1),
					Integer.toString(row.getRowNum() - 1));
		} else if (previousPlatform.equalsIgnoreCase(Global.API)) {
			Global.apiPlatformTestCases.put("E" + Integer.toString(apiTestCount - 1),
					Integer.toString(row.getRowNum() - 1));
		} else if (previousPlatform.equalsIgnoreCase(Global.IOS_BROWSER)) {
			Global.iosBrowserPlatformTestCases.put("E" + Integer.toString(iosBrowserTestCount - 1),
					Integer.toString(row.getRowNum() - 1));
		}
	}

	/**
	 * Method to get count of test cases to be executed on current instance
	 * 
	 * @param instance     instance number
	 * @param platformtype platform type name
	 * @return count of test cases to be executed on the current instance
	 */
	public static int getCurrentInstanctTestCount(int instanceNo, String platformtype) {

		int totalTestsOnThisInstance = 0;
		int totalPlatformInstance = 1;
		int totalPlatformTestCases = 0;
		int startTestAt = 0;
		int endTestAt = 0;
		int testExecutionAt = 0;

		// get total Platform Instances and total Platform TestCases
		if (platformtype.equalsIgnoreCase(Global.ANDROID)) {
			totalPlatformInstance = Integer.parseInt(LoadProp.getProperty("total_android_instance"));
			totalPlatformTestCases = Global.totalAndroidPlatformTests;
		} else if (platformtype.equalsIgnoreCase(Global.WEB)) {
			totalPlatformInstance = Integer.parseInt(LoadProp.getProperty("total_web_instance"));
			totalPlatformTestCases = Global.totalWebPlatformTests;
		} else if (platformtype.equalsIgnoreCase(Global.IOS)) {
			totalPlatformInstance = Integer.parseInt(LoadProp.getProperty("total_ios_instance"));
			totalPlatformTestCases = Global.totalIOSPlatformTests;
		} else if (platformtype.equalsIgnoreCase(Global.ANDROID_BROWSER)) {
			totalPlatformInstance = Integer.parseInt(LoadProp.getProperty("total_android_browser_instance"));
			totalPlatformTestCases = Global.totalAndroidBrowserPlatformTests;
		} else if (platformtype.equalsIgnoreCase(Global.API)) {
			totalPlatformInstance = Integer.parseInt(LoadProp.getProperty("total_api_instance"));
			totalPlatformTestCases = Global.totalAPIPlatformTests;
		} else if (platformtype.equalsIgnoreCase(Global.IOS_BROWSER)) {
			totalPlatformInstance = Integer.parseInt(LoadProp.getProperty("total_ios_browser_nodes"));
			totalPlatformTestCases = Global.totalIOSBrowserPlatformTests;
		}

		String split = LoadProp.getProperty("split_execution_across_platform_instances");
		if (split.equalsIgnoreCase("Y")) {
			if (instanceNo < totalPlatformInstance) {
				totalTestsOnThisInstance = totalPlatformTestCases / totalPlatformInstance;
				startTestAt = instanceNo * totalTestsOnThisInstance - totalTestsOnThisInstance;
				endTestAt = startTestAt + totalTestsOnThisInstance - 1;
			} else if (instanceNo == totalPlatformInstance) {
				totalTestsOnThisInstance = totalPlatformTestCases
						- (totalPlatformTestCases / totalPlatformInstance) * totalPlatformInstance
						+ totalPlatformTestCases / totalPlatformInstance;
				startTestAt = totalPlatformTestCases - totalTestsOnThisInstance;
				endTestAt = totalPlatformTestCases - 1;
			}
		} else {
			totalTestsOnThisInstance = totalPlatformTestCases;
			startTestAt = 0;
			endTestAt = startTestAt + totalTestsOnThisInstance - 1;
		}
		testExecutionAt = startTestAt;

		System.out.println("----------------");
		System.out.println(platformtype + "_" + instanceNo + "  :  startTestAt : " + startTestAt);
		System.out.println(platformtype + "_" + instanceNo + "  :  endTestAt : " + endTestAt);
		System.out.println(platformtype + "_" + instanceNo + "  :  testExecutionAt : " + testExecutionAt);
		System.out.println("================");

		// populate the current instance start , end and test execution At row values.
		Global.platformTestBag.put("startAt" + platformtype + instanceNo, Integer.toString(startTestAt));
		Global.platformTestBag.put("endTestAt" + platformtype + instanceNo, Integer.toString(endTestAt));
		Global.platformTestBag.put("testExecutionAt" + platformtype + instanceNo, Integer.toString(testExecutionAt));

		return totalTestsOnThisInstance;
	}

	/**
	 * Method to return list of all class names in the folder
	 * 
	 * @param directoryName Directory name in which the class names are to be listed
	 * @return List of class names in the given directory
	 */
	public static List<String> listFilesAndFolders(String directoryName) {
		List<String> actionClassNameLst = new ArrayList<>();
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			String currentTestName = file.getName().replace(".java", "");
			actionClassNameLst.add(currentTestName);
		}
		return actionClassNameLst;
	}

	/**
	 * Method to Explicit Wait.
	 * 
	 * @param expectedCondition Expected condition
	 * @param driver            Driver instance
	 * @param time              Time to wait
	 */
	public static void waitUntil(ExpectedCondition<WebElement> expectedCondition, RemoteWebDriver driver, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(expectedCondition);
	}

	/**
	 * Method to get Election date
	 * 
	 * @throws ParseException
	 */
	public static String getElectiontDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 20); // Adding 5 days
		String output = sdf.format(c.getTime());
		System.out.println(output);

		return output;

	}

	/**
	 * Method to get Election Held Between Start date
	 * 
	 * @throws ParseException
	 */
	public static String getElectiontStartDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, -700); // Adding 5 days
		String output = sdf.format(c.getTime());
		System.out.println(output);

		return output;

	}

//	 * Method to get Election Held Between End date 
//	 * @throws ParseException

	public static String getElectiontEndDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 1460); // Adding 5 days
		String output = sdf.format(c.getTime());
		System.out.println(output);

		return output;

	}

//	 Get Signed User Name

	public static String getSignedUserName(Map<String, String> propertiesMap, String locatorString, ExtentTest test,
			RemoteWebDriver driver) throws ParseException {
		String getUserName = Util.getText(propertiesMap, locatorString, test, driver);
		getUserName = getUserName.replace("Signed in as ", "");
		return getUserName;

	}

	/**
	 * Method to get current date
	 * 
	 * @throws ParseException
	 */
	public static String getCurrentDate() throws ParseException {

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = dateFormat.format(date);
		System.out.println("Converted String: " + strDate);

		return strDate;

	}

	/**
	 * Method to get Election Registration Date
	 * 
	 * @throws ParseException
	 */
	public static String getRegistrationCloseDate() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 15); // Adding 5 days
		String output = sdf.format(c.getTime());
		System.out.println(output);

		return output;

	}

	/**
	 * Method to Explicit Wait.
	 * 
	 * @param expectedCondition Expected condition
	 * @param driver            Driver instance
	 * @param time              Time to wait
	 */
	public static void waitUntilElementVisibility(RemoteWebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 90);
		@SuppressWarnings("unused")
		WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		ele = wait.until(ExpectedConditions.elementToBeClickable(locator));
		ele = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * Method to Explicit Wait.
	 * 
	 * @param expectedCondition Expected condition
	 * @param driver            Driver instance
	 * @param time              Time to wait
	 */
	public static void elementNotExist(String stepDetails, Map<String, String> propertiesMap, By locator,
			ExtentTest test, RemoteWebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 90);
		@SuppressWarnings("unused")
		Boolean ele = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
		if (ele == true) {
			test.log(Status.PASS, MarkupHelper.createLabel(stepDetails, ExtentColor.GREEN));
		} else {

			test.log(Status.FAIL, MarkupHelper.createLabel(stepDetails, ExtentColor.RED));
		}
	}

	/**
	 * Method to Explicit Wait.
	 * 
	 * @param expectedCondition Expected condition
	 * @param driver            Driver instance
	 * @param time              Time to wait
	 */
	public static void waitUntilElementsVisibility(RemoteWebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 120);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * 
	 * @param driver
	 * @param propertiesMap
	 * @param locator
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static void waitUntilTextToBePresent(RemoteWebDriver driver, ExtentTest test,
			Map<String, String> propertiesMap, String locator, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 240);
			wait.until(ExpectedConditions.textToBe(locateElementBy(propertiesMap, locator), text));
		} catch (Exception e) {
			Global.testResult = false;
			reportCriticalFailure(driver, test, " Page not loaded in given time.");
		}
	}

	/**
	 * 
	 * @param driver
	 * @param propertiesMap
	 * @param locator
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static void waitUntilElementToBePresent(String expectedText, Map<String, String> propertiesMap,
			String locatorString, RemoteWebDriver driver, ExtentTest test) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 240);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locateElementBy(propertiesMap, locatorString)));
		} catch (Exception e) {
			Global.testResult = false;
			reportCriticalFailure(driver, test, " Element Not Exist");
			test.log(Status.FAIL, "Element is not Exist");
		}
	}

	/**
	 * 
	 * @param driver
	 * @param propertiesMap
	 * @param locator
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static void waitUntilElementToBeAbsent(String expectedText, Map<String, String> propertiesMap,
			String locatorString, RemoteWebDriver driver, ExtentTest test) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 240);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locateElementBy(propertiesMap, locatorString)));
		} catch (Exception e) {
			Global.testResult = false;
			reportCriticalFailure(driver, test, " Page not loaded in given time.");
		}
	}

	/**
	 * Method to Invisibility of element.
	 * 
	 * @param expectedCondition Expected condition
	 * @param driver            Driver instance
	 * @param time              Time to wait
	 */
	public static void invisibilityofElement(ExpectedCondition<Boolean> expectedCondition, RemoteWebDriver driver,
			int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(expectedCondition);
	}

	/**
	 * Method to wait until an element get invisible.
	 * 
	 * @param invisibilityOfElementLocated Expected condition
	 * @param driver                       Driver instance
	 * @param time                         Time to wait
	 */
	public static void waitUntilInvisibleElement(ExpectedCondition<Boolean> invisibilityOfElementLocated,
			RemoteWebDriver driver, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(invisibilityOfElementLocated);
	}

	/**
	 * Method to wait until an element get visible.
	 * 
	 * @param invisibilityOfElementLocated Expected condition
	 * @param driver                       Driver instance
	 * @param time                         Time to wait
	 */
	public static void waitUntilvisibleElement(ExpectedCondition<Boolean> visibilityOfElementLocated,
			RemoteWebDriver driver, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);

	}

	/**
	 * Method to click event.
	 * 
	 * @param driver Driver instance
	 * @param by     By locator
	 * @throws IOException          exception
	 * @throws InterruptedException
	 */
	public static void click(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) throws InterruptedException {
		try {
			Thread.sleep(2000);			String t = null;
			// Thread.sleep(2000);
			WebElement element = findElement(propertiesMap, locatorString, test, driver);

			// ((JavascriptExecutor)
			// driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			// ((JavascriptExecutor)
			// ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoViewIfNeeded(true)",
			// element);

			WebDriverWait wait = new WebDriverWait(driver, 4);

			if (element != null) {
				wait.until(ExpectedConditions.elementToBeClickable(element));
//				driver.executeScript("arguments[0].click();", element);
				// Util.takeScreenshot(driver,test);
				element.click();
				// assertion.assertTrue(true, stepDetails);
				test.log(Status.PASS, stepDetails);
				// test.log(Status.INFO, MarkupHelper.createLabel(stepDetails,
				// ExtentColor.WHITE));
			} else {
				System.out.println(locatorString + "not clickable ");
				test.log(Status.FAIL, "User not able to Click on " + locatorString);

			}
			Thread.sleep(1000);
		} catch (Exception | AssertionError e) {
			Assert.fail(e.getMessage());
		}
	}

	public static void isDisplayed(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) throws InterruptedException {
		try {
			String e = null;
			// Thread.sleep(2000);
			WebElement element = findElement(propertiesMap, locatorString, test, driver);

			WebDriverWait wait = new WebDriverWait(driver, 4);

			if (element != null) {
				wait.until(ExpectedConditions.elementToBeClickable(element));
				test.log(Status.PASS, "Element is not displayed " + locatorString);
			} else {
				System.out.println(locatorString + "Element is displayed");
				test.log(Status.FAIL, "Element is not displayed " + locatorString);

			}
			Thread.sleep(1000);
		} catch (Exception | AssertionError e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method to click event.
	 * 
	 * @param driver Driver instance
	 * @param by     By locator
	 * @throws IOException          exception
	 * @throws InterruptedException
	 */
	public static void unchecked(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) throws InterruptedException {
		try {
			String r = null;
			WebDriverWait wait = new WebDriverWait(driver, 4);
			WebElement element = findElement(propertiesMap, locatorString, test, driver);

			if (!element.isSelected()) {
				test.log(Status.PASS, stepDetails + " is unchecked");
			} else {
				test.log(Status.FAIL, stepDetails + " is checked");

			}
			Thread.sleep(1000);
		} catch (Exception | AssertionError e) {
			Assert.fail(e.getMessage());
		}
	}

	// check element attribute value
	public static void checkAttributeValue(String expectedText, Map<String, String> propertiesMap, String locatorString,
			RemoteWebDriver driver, ExtentTest test) throws InterruptedException {
		try {
			String r = null;
			// Thread.sleep(2000);
			WebElement element = findElement(propertiesMap, locatorString, test, driver);
			String actualText = element.getAttribute("value");
			test.log(Status.INFO, "Asserting Text at locator : " + locatorString);

			// String getactualText=actualText.replace("\n", " ");
			AssertUtil.performSoftAssertEquals(expectedText, actualText,
					"Text match failed. Expected : '" + expectedText + "'  :  Actual : '" + actualText + "'", driver,
					test, sAssert);

			Thread.sleep(1000);
		} catch (Exception | AssertionError e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * //Performing the mouse hover action on the target element.
	 * 
	 * @param driver Driver instance
	 * @param by     By locator
	 * @throws IOException          exception
	 * @throws InterruptedException
	 */
	public static void movetoElement(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) throws InterruptedException {
		Actions action = new Actions(driver);
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		if (element != null) {
			action.moveToElement(element).perform();
			action.click().build().perform();
		} else {
			System.out.println(locatorString + "not clickable ");
			test.log(Status.FAIL, "User not able to Click on " + locatorString);
		}
		Thread.sleep(1000);
	}

	public static String[] getVoterName(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String sentence = element.getText();
		String[] words = sentence.split(" ");
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}

		return words;
	}

	public static String getVoterLastName(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String lastName = null;
		String sentence = element.getText();
		String[] words = sentence.split(" ");
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
		lastName = words[1].replace(",", "");
		lastName = lastName.replace(" ", "");

		return lastName;
	}

	public static String getVoterMiddleName(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String middleName = null;
		String sentence = element.getText();
		String[] words = sentence.split(" ");
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
		middleName = words[2].replace(" ", "");

		return middleName;
	}

	public static String getVoterEId(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String voterEMSID = null;
		String sentence = element.getText();
		String[] words = sentence.split(" ");
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
		voterEMSID = words[3].replace("(", "");
		voterEMSID = voterEMSID.replace(")", "");

		return voterEMSID;
	}

	/**
	 * Method to click on the string containing the required text
	 * 
	 * @param driver Driver instance
	 * @param by     By locator
	 * @throws IOException          exception
	 * @throws InterruptedException
	 */
	public static void clickOnContainsText(String stepDetails, String textToFind, ExtentTest test,
			RemoteWebDriver driver) throws InterruptedException {
		String expectedXpath = "//*[contains(text(),'" + textToFind + "')]";

		waitUntilElementVisibility(driver, By.xpath(expectedXpath));
		WebElement element = driver.findElement(By.xpath(expectedXpath));

		// WebElement element = driver.findElement(By.xpath(expectedXpath));
		if (element != null) {
			element.click();
			test.log(Status.INFO, MarkupHelper.createLabel(stepDetails, ExtentColor.WHITE));
		}
		Thread.sleep(2000);
	}

	/**
	 * Method to send key event.
	 * 
	 * @param driver Driver instance
	 * @param by     By locator
	 * @param key    Text to enter
	 * @throws IOException          exception
	 * @throws InterruptedException
	 */
	public static void sendKeys(String stepDetails, Map<String, String> propertiesMap, String locatorString,
			String inputText, ExtentTest test, RemoteWebDriver driver, SoftAssert sAssert) throws InterruptedException {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoViewIfNeeded(true)", element);

		if (element != null) {
			element.clear();
			// ((JavascriptExecutor)driver).executeScript("arguments[0].value='"+inputText+"';",
			// element);
			element.sendKeys(inputText);
			// test.log(Status.PASS,stepDetails);
//			String actualText = Util.getText(propertiesMap, locatorString, test, driver);
//			AssertUtil.performSoftAssertEquals(inputText, actualText,
//					"Text match failed. Expected : '" + inputText + "'  :  Actual '" + actualText + "'", driver, test,
//					sAssert);
			test.log(Status.INFO, MarkupHelper.createLabel(stepDetails + " :  " + inputText, ExtentColor.WHITE));
		}
		Thread.sleep(1000);
	}

	/**
	 * Method to get element text
	 * 
	 * @param driver driver instance
	 * @param by     By locator
	 * @return return element text
	 * @throws IOException
	 */
	public static String getText(Map<String, String> propertiesMap, String locatorString, ExtentTest test,
			RemoteWebDriver driver) {

		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String actualText = null;
		if (element != null) {
			actualText = element.getText();
		}
		return actualText;
	}

	/**
	 * Method to get Ballot Serial number
	 * 
	 * @param driver driver instance
	 * @param by     By locator
	 * @return return element text
	 * @throws IOException
	 */
	public static String getBallotSerialNumber(Map<String, String> propertiesMap, String locatorString, ExtentTest test,
			RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String actualText = null;
		if (element != null) {
			actualText = element.getText();
		}
		return actualText;
	}

	/**
	 * Method to clear text field event.
	 * 
	 * @param driver Driver instance
	 * @param by     By Locator
	 * @throws IOException exception
	 */
	public static void clear(Map<String, String> propertiesMap, String locatorString, ExtentTest test,
			RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		if (element != null) {
			element.clear();
		}
	}

	/**
	 * Method to find element
	 * 
	 * @param propertiesMap Map properties
	 * @param locatorString Locator sting
	 * @param test          ExtentTest Instance
	 * @param driver        RemoteWebDriver instance
	 * @return WebElement
	 * @throws IOException exception
	 */
	public static WebElement findElement(Map<String, String> propertiesMap, String locatorString, ExtentTest test,
			RemoteWebDriver driver) {
		WebElement element = null;
		try {
			By by = locateElementBy(propertiesMap, locatorString);
			waitUntilElementVisibility(driver, by);
			element = driver.findElement(by);
		} catch (Exception e) {
			Global.testResult = false;
			reportCriticalFailure(driver, test, " Unable to locate element located by : " + locatorString);
		}
		return element;
	}

	/**
	 * Method to locate element
	 * 
	 * @param propertiesMap
	 * @param locatorString
	 * @return
	 */
	public static By locateElementBy(Map<String, String> propertiesMap, String locatorString) {
		By by = null;
		if (locatorString.endsWith("_xpath")) {
			by = By.xpath(propertiesMap.get(locatorString));
		} else if (locatorString.endsWith("_id")) {
			by = By.id(propertiesMap.get(locatorString));
		} else if (locatorString.endsWith("_name")) {
			by = By.name(propertiesMap.get(locatorString));
		} else if (locatorString.endsWith("_css")) {
			by = By.cssSelector(propertiesMap.get(locatorString));
		} else if (locatorString.endsWith("_class")) {
			by = By.className(propertiesMap.get(locatorString));
		} else {
			by = By.linkText(locatorString);
		}
		return by;
	}

	/**
	 * Method to find elements.
	 * 
	 * @param driver Driver instance
	 * @param by     By Locator
	 * @return Return list of elements
	 * @throws IOException exception
	 */
	public static List<WebElement> findElements(Map<String, String> propertiesMap, String locatorString,
			ExtentTest test, RemoteWebDriver driver) {
		List<WebElement> elements = null;
		try {
			By by = locateElementBy(propertiesMap, locatorString);
			waitUntilElementsVisibility(driver, by);
			elements = driver.findElements(by);
		} catch (Exception | AssertionError e) {
			Global.testResult = false;
			reportCriticalFailure(driver, test, " Unable to locate element located by : " + locatorString);
		}
		return elements;
	}

	/**
	 * Method to get Time stamp.
	 * 
	 * @return Return time stamp
	 */
	public static Timestamp getTimestamp() {
		date = new java.util.Date();
		return new Timestamp(date.getTime());
	}

	/**
	 * Method to get Current Month in MMMM format
	 * 
	 * @return Return month
	 */
	public static String getCurrentMonth() {
		Date now = new Date();
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM"); // three digit abbreviation
		return simpleDateformat.format(now);
	}

	/**
	 * Method to get Current Day of Month
	 * 
	 * @return Return current day number
	 */
	public static int getCurrentDayOfMonth() {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		return calendar.get(Calendar.DAY_OF_MONTH); // the day of the week in numerical format
	}

	public static String getCurrentYear() {
		Date now = new Date();
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy"); // four digit abbreviation
		return simpleDateformat.format(now);
	}

	public static String generaterandomSixDigitNumber() {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(100000);
		String formatted = String.format("%06d", num);
		return formatted;
	}

	/**
	 * Method to get element's attribute value.
	 * 
	 * @param driver Driver instance
	 * @param by     By Locator
	 * @param value  Attribute name
	 * @return return attribute value
	 * @throws IOException exception
	 */
	public static String getElementAttributeValue(Map<String, String> propertiesMap, String locatorString,
			String attributeName, ExtentTest test, RemoteWebDriver driver) {
		WebElement element = findElement(propertiesMap, locatorString, test, driver);
		String attributeValue = "";
		if (element != null) {
			attributeValue = element.getAttribute(attributeName);
		}
		return attributeValue;
	}

	/**
	 * Method to take screenshot of current screen.
	 * 
	 * @param aDriver Driver instance
	 * @param test    ExtentTest instance
	 */
	public static void takeScreenshot(RemoteWebDriver driver, ExtentTest test) {
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		String screenshotPath = Global.SCREEN_SHOT_FOLDER + screenshotFile;
		try {
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
					.takeScreenshot(driver);
			ImageIO.write(screenshot.getImage(), "PNG", new File(screenshotPath));
			test.log(Status.INFO,
					"Snapshot below :(" + screenshotFile + ")" + test.addScreenCaptureFromPath(screenshotFile));
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	/**
	 * Method to take screenshot of current screen.
	 * 
	 * @param aDriver Driver instance
	 * @param test    ExtentTest instance
	 */
	public static void takeObjectScreenshot(Map<String, String> propertiesMap, RemoteWebDriver driver,
			String locatorString, ExtentTest test) {
		try {
			Date d = new Date();

			WebElement ele = Util.findElement(propertiesMap, locatorString, test, driver);

			// Get entire page screenshot
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage fullImg = ImageIO.read(screenshot);

			// Get the location of element on the page
			Point point = ele.getLocation();

			// Get width and height of the element
			int eleWidth = ele.getSize().getWidth();
			int eleHeight = ele.getSize().getHeight();

			// Crop the entire page screenshot to get only element screenshot
			BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
			ImageIO.write(eleScreenshot, "png", screenshot);

			String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
			String screenshotPath = Global.SCREEN_SHOT_FOLDER + screenshotFile;

			File screenshotLocation = new File(screenshotPath);
			FileUtils.copyFile(screenshot, screenshotLocation);

//			ImageIO.write(screenshot.getImage(), "PNG", new File(screenshotPath));
			test.log(Status.INFO,
					"Snapshot below :(" + screenshotFile + ")" + test.addScreenCaptureFromPath(screenshotFile));
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	/**
	 * Method to report PASS.
	 * 
	 * @param driver  Driver Instance
	 * @param test    Extent test instance
	 * @param message Pass message
	 */
	public static void reportPass(RemoteWebDriver driver, ExtentTest test, String message) {

		if (LoadProp.getProperty("capture_screenshot_on_assertion_pass").equalsIgnoreCase("Y")) {
			takeScreenshot(driver, test);
		}
		test.log(Status.PASS, message);
	}

	/**
	 * Method to report PASS without screenshot.
	 * 
	 * @param test    Extent test instance
	 * @param message Pass message
	 */
	public static void reportPassWithoutScreenshot(ExtentTest test, String message) {
		test.log(Status.PASS, message);
	}

	/**
	 * Method to report Critical FAILURE e.g. element not found
	 * 
	 * @param driver  Driver instance
	 * @param test    Extent Test instance
	 * @param message Failure message
	 */
	public static void reportCriticalFailure(RemoteWebDriver driver, ExtentTest test, String message) {
		test.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
		if (LoadProp.getProperty("capture_screenshot_on_assertion_fail").equalsIgnoreCase("Y")) {
			takeScreenshot(driver, test);
		}
		Assert.fail(message);
	}

	/**
	 * Method to report Critical FAILURE e.g. element not found with out screenshot
	 * 
	 * @param test    Extent Test instance
	 * @param message Failure message
	 */
	public static void reportCriticalFailureWithoutScreenshot(String message, ExtentTest test) {
		test.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
		Assert.fail(message);
	}

	/**
	 * Util method to select element from dropdown based on visible text
	 * 
	 * @param stepDetails
	 * @param driver
	 * @param test
	 * @param propertiesMap
	 * @param filterDropdownXpath
	 * @param value
	 */
	public static void selectElementByVisibleText(String stepDetails, RemoteWebDriver driver, ExtentTest test,
			Map<String, String> propertiesMap, String filterDropdownXpath, String visibleText) {
		test.log(Status.INFO, MarkupHelper.createLabel(stepDetails + " :  " + visibleText, ExtentColor.WHITE));

		Select dropdown = new Select(findElement(propertiesMap, filterDropdownXpath, test, driver));
		dropdown.selectByVisibleText(visibleText);
	}

	/**
	 * Util method to select element from dropdown based on value
	 * 
	 * @param stepDetails
	 * @param driver
	 * @param test
	 * @param propertiesMap
	 * @param filterDropdownXpath
	 * @param value
	 */
	public static void selectElementByValue(String stepDetails, RemoteWebDriver driver, ExtentTest test,
			Map<String, String> propertiesMap, String filterDropdownXpath, String value) {
		test.log(Status.INFO, MarkupHelper.createLabel(stepDetails + " :  " + value, ExtentColor.WHITE));

		Select dropdown = new Select(findElement(propertiesMap, filterDropdownXpath, test, driver));
		dropdown.selectByValue(value);

	}

	/**
	 * Method to read the TestCase log file and import them to xls.
	 * 
	 * @throws IOException
	 */
	public static void populateTestCases() {
		FileOutputStream fileOut = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet firstSheet = workbook.createSheet("FIRST SHEET");
			BufferedReader br = new BufferedReader(new FileReader(Global.PROJECT_LOCATION + "\\log\\TestCase.csv"));
			String st;
			int rowNum = 0;
			while ((st = br.readLine()) != null) {
				Row row = firstSheet.createRow(rowNum);
				String strArray[] = st.split("\\|");
				// print elements of String array
				for (int i = 0; i < strArray.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(strArray[i]);
				}
				rowNum++;
			} // while ends here

			// Resize all columns to fit the content size
			for (int i = 0; i < 6; i++) {
				firstSheet.autoSizeColumn(i);
			}
			fileOut = new FileOutputStream(Global.PROJECT_LOCATION + "\\resources\\manualTestCases\\TestCaseFile.xls");
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();
			br.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Method to fetch Property value
	 * 
	 * @param paramDict     Dictionary
	 * @param testId        Current Test id
	 * @param parameterName parameter name
	 * @param test          ExtentTest instance
	 * @return parameter value
	 */
	public static String getPropertyValue(Map<String, String> propertyBag, String propertyKeyName, ExtentTest test) {
		String propertyValue = null;
		try {
			propertyValue = propertyBag.get(propertyKeyName);
			if (propertyValue == null) {
				throw new NullPointerException("Property '" + propertyKeyName + "' is null");
			}
		} catch (Exception e) {
			test.log(Status.FAIL, e.getMessage());
			Assert.fail("Property '" + propertyKeyName + "' is null");
		}
		return propertyValue;
	}

	/**
	 * Method to fetch parameter Value
	 * 
	 * @param paramDict     Dictionary
	 * @param testId        Current Test id
	 * @param parameterName parameter name
	 * @param test          ExtentTest instance
	 * @return parameter value
	 */
	public static String getParameterValue(Dictionary<String, String> paramDict, String testId, String parameterName,
			ExtentTest test) {
		String parameterValue = null;
		try {
			parameterValue = paramDict.get(testId + "." + parameterName);
			if (parameterValue == null) {
				throw new NullPointerException("Parameter '" + parameterName + "' is null");
			}
		} catch (Exception e) {
			test.log(Status.FAIL, e.getMessage());
			Assert.fail("Parameter '" + parameterName + "' is null");
		}
		return parameterValue;
	}

	/**
	 * Method to return the formatted amount
	 * 
	 * @param amount Amount to be formatted
	 * @return formatted amount
	 */
	public static String formatAmount(String amount) {
		return "$" + decimalFormat.format(Double.parseDouble(amount)) + ".00";
	}

	/**
	 * Method to return instance specific value
	 * 
	 * @param instancePropertyBag Instance Property Bag
	 * @param key                 KeyName
	 * @return value of key for the specified instance
	 */
	public static String getInstanceSpecificParmeterValue(Map<String, String> instancePropertyBag, String key) {

		if (key.equalsIgnoreCase(Global.TEST_ID)) {
			return instancePropertyBag.get(key);
		}
		return LoadProp.getProperty(
				instancePropertyBag.get("platformType") + "_" + instancePropertyBag.get("instanceNo") + "_" + key);
	}

	/**
	 * Method to get text from UI and compare that with expected value
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void getTextAndValidate(String expectedText, Map<String, String> propertiesMap, String locatorString,
			RemoteWebDriver driver, ExtentTest test) {
		test.log(Status.INFO, "Asserting Text at locator : " + locatorString);
		String actualText = Util.getText(propertiesMap, locatorString, test, driver);
		// String getactualText=actualText.replace("\n", " ");
		AssertUtil.performSoftAssertEquals(expectedText, actualText,
				"Text match failed. Expected : '" + expectedText + "'  :  Actual : '" + actualText + "'", driver, test,
				sAssert);
	}

	/**
	 * Compare two text and Validate it
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void compareTextAndValidate(String expectedText, String actualText, RemoteWebDriver driver,
			ExtentTest test) {
		actualText = actualText.toUpperCase();
		expectedText = expectedText.toUpperCase();
		sAssert.assertEquals(actualText, expectedText);
		AssertUtil.performSoftAssertEqualsWithoutScreenshot(expectedText, actualText,
				"" + expectedText + " Text does not match with " + actualText + "", test, sAssert);
	}
	
	public static void stringContains(String fullString, String actualText, RemoteWebDriver driver,
			ExtentTest test) {
	//	actualText = actualText.toUpperCase();
	//	expectedText = expectedText.toUpperCase();
		sAssert.assertTrue(fullString.contains(actualText));
		AssertUtil.performSoftAssertContainsStringWithoutScreenshot(fullString, actualText,
				"" + fullString + " Does not cotains " + actualText + "", test, sAssert);
	}

	public static void getTableDataByPageIndex(String pageIndex, String actualText, RemoteWebDriver driver,
									  ExtentTest test) {

	}

	/**
	 * Method to get text from UI and compare that with expected value
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void fileDataContainsText(String fileData, Map<String, String> propertiesMap, String locatorString,
			RemoteWebDriver driver, ExtentTest test) {
		test.log(Status.INFO, "Asserting Text at locator : " + locatorString);
		String actualText = locatorString;
		if (fileData.contains(actualText.toUpperCase())) {
			sAssert.assertTrue(fileData.contains(actualText), "File Data Does not Contains" + actualText);
			test.log(Status.PASS, "File Data contains  - \"" + actualText + "\"");
		}
	}

	public static void fileContainsVoterData(String fileData, Map<String, String> propertiesMap, String locatorString,
			RemoteWebDriver driver, ExtentTest test) {
		test.log(Status.INFO, "Asserting Text at locator : " + locatorString);
		String actualText = locatorString;
		if (fileData.contains(actualText)) {
			sAssert.assertTrue(fileData.contains(actualText), "File Data Does not Contains" + actualText);
			test.log(Status.PASS, "File Data contains  - \"" + actualText + "\"");
		} else {
			sAssert.fail("File Data Does not contains :-" + locatorString);
		}
	}

	/**
	 * Method to get text from UI and compare that with expected value
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void compareObjectText(String expectedText, Map<String, String> propertiesMap, String locatorString,
			RemoteWebDriver driver, ExtentTest test, SoftAssert sAssert) {
		test.log(Status.INFO, "Asserting Text at locator : " + locatorString);
		String actualText = Util.getText(propertiesMap, locatorString, test, driver);
		if (actualText.contains(expectedText)) {
			sAssert.assertTrue(true, "User Successfully signed in with User" + expectedText);
			// sAssert.assertTrue(true);
			if (LoadProp.getProperty("capture_screenshot_on_assertion_pass").equalsIgnoreCase("Y")) {
				Util.takeScreenshot(driver, test);
			}
			test.log(Status.PASS, "Found  - \"" + actualText + "\"");
		} else {
			sAssert.assertTrue(false);
			test.log(Status.FAIL, "Not Found  - \"" + actualText + "\"");

		}
	}

	/**
	 * Method to get text from UI and compare that with expected value
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void getTextContainsAndValidate(String expectedText, Map<String, String> propertiesMap,
			String locatorString, RemoteWebDriver driver, ExtentTest test, SoftAssert sAssert) {
		test.log(Status.INFO, "Asserting Text at locator : " + locatorString);
		String actualText = Util.getText(propertiesMap, locatorString, test, driver);
		if (actualText.contains(expectedText)) {
			sAssert.assertTrue(true, "User Successfully signed in with User" + expectedText);
			// sAssert.assertTrue(true);
			if (LoadProp.getProperty("capture_screenshot_on_assertion_pass").equalsIgnoreCase("Y")) {
				Util.takeScreenshot(driver, test);
			}
			test.log(Status.PASS, "Found  - \"" + actualText + "\"");
		} else {
			sAssert.assertTrue(false);
			test.log(Status.FAIL, "Not Found  - \"" + actualText + "\"");

		}

//		AssertUtil.performSoftAssertEquals(expectedText, actualText,
//				"Text match failed. Expected : '" + expectedText + "'  :  Actual '" + actualText + "'", driver, test,
//				sAssert);
	}

	/**
	 * Method to validate text on the screen
	 * 
	 * @param propertiesMap
	 * @param stringToLocate
	 * @param test
	 * @param driver
	 * @param sAssert
	 */
	public static void validateTextOnPage(Map<String, String> propertiesMap, String stringToLocate, ExtentTest test,
			RemoteWebDriver driver, SoftAssert sAssert) {
		WebElement element = findElement(propertiesMap, stringToLocate, test, driver);
		String actualText = null;
		if (element != null) {
			actualText = element.getText();
			sAssert.assertTrue(actualText == element.getText());
			if (LoadProp.getProperty("capture_screenshot_on_assertion_pass").equalsIgnoreCase("Y")) {
				Util.takeScreenshot(driver, test);
			}
			test.log(Status.PASS, "Found  - \"" + actualText + "\"");
		} else {
			sAssert.assertTrue(false);
			test.log(Status.FAIL, "Not Found  - \"" + actualText + "\"");

		}
	}

	/**
	 * Method to get parameter to be passed across methods
	 * 
	 * @param instancePropertyBag
	 * @param parameterKey
	 * @return parameter value
	 */
	public static String getParameterValueAcrossActions(Map<String, String> instancePropertyBag, String parameterKey) {

		String testId = instancePropertyBag.get("testId");
		String platform = instancePropertyBag.get("platformType");
		String instanceNo = instancePropertyBag.get("instanceNo");
		String key = testId + "." + platform + "." + instanceNo + "." + parameterKey;
		return Global.parametersAcrossActionsTestBag.get(key);
	}

	/**
	 * Method to set parameter to be passed across other action methods
	 * 
	 * @param instancePropertyBag
	 * @param parameterKey
	 * @param parameterValue
	 */
	public static void setParameterValueAcrossActions(Map<String, String> instancePropertyBag, String parameterKey,
			String parameterValue) {

		String testId = instancePropertyBag.get("testId");
		String platform = instancePropertyBag.get("platformType");
		String instanceNo = instancePropertyBag.get("instanceNo");
		String key = testId + "." + platform + "." + instanceNo + "." + parameterKey;
		Global.parametersAcrossActionsTestBag.put(key, parameterValue);
	}

	/**
	 * Method to get text from UI and compare that with expected value
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void getTextAndValidateByDynamicXpath(String expectedItemName, String locatorString,
			RemoteWebDriver driver, ExtentTest test, SoftAssert sAssert) {
		test.log(Status.INFO, "Asserting Text");
		WebElement element = driver.findElement(By.xpath(locatorString));
		String actualText = element.getText();
		AssertUtil.performSoftAssertEquals(expectedItemName, actualText,
				"Text match failed. Expected : '" + expectedItemName + "'  :  Actual '" + actualText + "'", driver,
				test, sAssert);
	}

	/**
	 * Method to compare two string values
	 * 
	 * @param expectedText expected text
	 * @param actualText   actual Text
	 * @param test         ExtentTest Instance
	 */
	public static void compareStrings(String expectedText, String actualText, ExtentTest test) {
		test.log(Status.INFO, "Asserting Text ");
		test.log(Status.INFO, "Expected Text - " + expectedText + " is same as actual text - " + actualText);
		Assert.assertEquals(expectedText, actualText);
	}

	/**
	 * Method to set the isEnable flag to 'N' if not present in the from the Test
	 * Generator list.
	 * 
	 * @param testCase
	 * @return
	 */
	public static String isTestCasePresent(String testCase, String isEnabled) {
		int s = Global.TestCaseIDsFromTG.size();
		for (int i = 0; i < s; i++) {
			if (testCase.equalsIgnoreCase(Global.TestCaseIDsFromTG.get(i))) {
				isEnabled = "Y";
				break;
			} else {
				isEnabled = "N";
			}
		}
		return isEnabled;
	}

	/**
	 * Method to soft assert
	 * 
	 * @param sAssert Soft assert instance
	 * @param test    Extent Test instance
	 */
	public static void assertAll(SoftAssert sAssert, ExtentTest test) {
		try {
			sAssert.assertAll();
		} catch (Exception | AssertionError e) {
			test.log(Status.FAIL, e.getMessage());
		}
	}

	/**
	 * Method to drag and drop from source lement to destination element.
	 * 
	 * @param driver Driver instance
	 * @param by     By locator
	 * @throws IOException          exception
	 * @throws InterruptedException
	 */
//	public static void draganddrop(String stepDetails, Map<String, String> propertiesMap, String fromLocatorString,
//			String toLocatorString, ExtentTest test, RemoteWebDriver driver) throws InterruptedException {
//		WebElement fromElement = findElement(propertiesMap, fromLocatorString, test, driver);
//		WebElement toElement = findElement(propertiesMap, toLocatorString, test, driver);
//		if (fromElement != null && toElement != null) {
//			new Actions(driver).dragAndDrop(fromElement, toElement).build().perform();
//			test.log(Status.INFO, MarkupHelper.createLabel(stepDetails, ExtentColor.WHITE));
//		}
//		Thread.sleep(5000);
//	}
//
//	public static void dandd(String stepDetails, Map<String, String> propertiesMap, String fromList, String toList,
//			ExtentTest test, RemoteWebDriver driver) throws InterruptedException {
//		WebElement fromElement = driver
//				.findElement(By.xpath("//div[@class='col list-col  enabled']/div[contains(text(), ' " + fromList
//						+ " ')]/../div[2]/app-kanban-list/div/div"));
//		WebElement toElement = driver
//				.findElement(By.xpath("//div[@class='col list-col  enabled']/div[contains(text(), ' " + toList
//						+ " ')]/../div[2]/app-kanban-list/div/div"));
//
//		if (fromElement != null && toElement != null) {
//			new Actions(driver).dragAndDrop(fromElement, toElement).build().perform();
//			test.log(Status.INFO, MarkupHelper.createLabel(stepDetails, ExtentColor.WHITE));
//		}
//		Thread.sleep(5000);
//	}

	// following function is used to clear the content from Download directory
	public static void cleardownloadfileDirectory() {

		try {

			File index = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "DownloadFiles" + File.separator);
			String[] entries = index.list();
			for (String s : entries) {
				File currentFile = new File(index.getPath(), s);
				currentFile.delete();
			}
		}

		catch (Exception | AssertionError e) {

			Assert.fail(e.getMessage());

		}

	}

	// following function is used to clear the content from Download directory
	public static String checkFileDownloaded(String fileFullName, ExtentTest test) {

		String fname = null;
		try {

			File f = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "DownloadFiles"
					+ File.separator + fileFullName + ".docx");
			File filePath = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "DownloadFiles" + File.separator);
			if (fileFullName.contains("Purged Felon Letter")) {
				String contents[] = filePath.list();

				for (int i = 0; i < contents.length; i++) {
					System.out.println(contents[i]);
					fileFullName = contents[i];
					f = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
							+ "DownloadFiles" + File.separator + fileFullName);
//					if (contents[i].contains("Purged")) {
//						f = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
//								+ "DownloadFiles" + File.separator + fileFullName);
//						break;
//					}
				}

			}
			Assert.assertTrue((f.exists()), fileFullName + " is not Downloaded Successfully");
			test.log(Status.PASS, "Found  - \"" + fileFullName + " file Downloaded Successfully" + "\"");
			Assert.assertTrue((f.canRead()), fileFullName + "User can not Read" + fileFullName + " successfully");
			test.log(Status.PASS, "file  - \"" + fileFullName + " can be Read" + "\"");
			fname = f.getName();
		}

		catch (Exception | AssertionError e) {

			Assert.fail(e.getMessage());

		}

		return fname;

	}

	/**
	 * Method to get text from UI and compare that with expected value
	 * 
	 * @param expectedText  expected text
	 * @param propertiesMap Map
	 * @param locatorString locator
	 * @param driver        RemoteWebDriver
	 * @param test          ExtentTest Instance
	 * @param sAssert       SoftAssert instance
	 */
	public static void switchtopopup(String locatorString, RemoteWebDriver driver, ExtentTest test) {
		driver.switchTo().window(locatorString);

	}

	// Read Excel file

	public static String getCellData(String xlFilePath, String sheetName, String colName, int rowNum) {
		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		int col_Num = -1;

		String cellValue = null;

		try {
			fis = new FileInputStream(xlFilePath);
			workbook = new XSSFWorkbook(fis);

			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}

			row = sheet.getRow(rowNum - 1);
			cell = row.getCell(col_Num);

			if (cell.getCellType() == CellType.STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
				cellValue = String.valueOf(cell.getNumericCellValue());

			}

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + col_Num + " does not exist  in Excel";
		}
		return cellValue;

	}

}
