package com.utility.library;

import java.awt.Color;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.properties.mapping.LoadProp;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/**
 * Class containing all assert library functions
 * 
 * @author Vikask
 *
 */
public class AssertUtil {

	private AssertUtil() {

	}

	/**
	 * Method to perform soft assert : AssertEqual
	 * 
	 * @param expectedText   expected text
	 * @param actualText     actual text
	 * @param failureMessage fail message
	 * @param driver         driver instance
	 * @param test           ExtentTest instance
	 * @param sAssert        SoftAssert instance
	 */
	public static void performSoftAssertEquals(String expectedText, String actualText, String failureMessage,
			RemoteWebDriver driver, ExtentTest test, SoftAssert sAssert) {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Assert.assertEquals(actualText.toUpperCase(), expectedText.toUpperCase());
			if (LoadProp.getProperty("capture_screenshot_on_assertion_pass").equalsIgnoreCase("Y")) {
				Util.takeScreenshot(driver, test);
			}
			test.log(Status.PASS, MarkupHelper.createLabel(
					"Expected - \"" + expectedText + "\" - Actual - \"" + actualText + "\"", ExtentColor.GREEN));
			js.executeScript("window.scrollBy(0,-2450)", "");
		} catch (Exception | AssertionError e) {
			if (LoadProp.getProperty("capture_screenshot_on_assertion_fail").equalsIgnoreCase("Y")) {
				Util.takeScreenshot(driver, test);
			}
			test.log(Status.FAIL, MarkupHelper.createLabel(failureMessage, ExtentColor.RED));
			sAssert.fail(failureMessage + " Expected - \"" + expectedText + "\" -- Actual - \"" + actualText + "\"");
		}
	}

	public static void performSoftAssertEqualsWithoutScreenshot(String expectedText, String actualText,
			String failureMessage, ExtentTest test, SoftAssert sAssert) {
		try {
			Assert.assertEquals(actualText, expectedText);
			test.log(Status.PASS, MarkupHelper.createLabel(
					"Expected - \"" + expectedText + "\" - Actual - \"" + actualText + "\"", ExtentColor.GREEN));
		} catch (Exception | AssertionError e) {
			test.log(Status.FAIL, MarkupHelper.createLabel(failureMessage, ExtentColor.RED));
			sAssert.fail(failureMessage + " Expected - \"" + expectedText + "\" ---- Actual - \"" + actualText + "\"");
		}
	}

	public static void performSoftAssertContainsStringWithoutScreenshot(String fullString, String actualText,
			String failureMessage, ExtentTest test, SoftAssert sAssert) {
		try {
			Assert.assertTrue(fullString.contains(actualText));
			test.log(Status.PASS, MarkupHelper.createLabel(
					"Expected - \"" + fullString + "\" - contains - \"" + actualText + "\"", ExtentColor.GREEN));
		} catch (Exception | AssertionError e) {
			test.log(Status.FAIL, MarkupHelper.createLabel(failureMessage, ExtentColor.RED));
			sAssert.fail(failureMessage + " Expected - \"" + fullString + "\" ---- not cotains - \"" + actualText + "\"");
		}
	}
}
