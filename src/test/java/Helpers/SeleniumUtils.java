package Helpers;


import TestBase.BaseClass;
import org.openqa.selenium.By;

public class SeleniumUtils extends BaseClass {

	private SeleniumUtils() {
	}

    public static void click(By locator) {
        driver.findElement(locator).click();
    }
}
