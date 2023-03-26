package StepDef;

import Helpers.CommonUtils;
import Helpers.Ipage;
import PageObjects.LoginPage;
import PageRepository.LoginPageRepo;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;

import static TestBase.BaseClass.test;

public class SeleniumStepDefinations {

    public static Ipage ipage;
    @Given("User open {string} URL")
    public void userOpenURL(String URL) {
        try {
            test.info("User opening "+URL+" URL");
            CommonUtils.getPageByName("LoginPage").gotoURL(URL);
        }catch (Exception e){
            test.fail(e);
        }
    }

    @Given("User clicks on {string}")
    public void userClicksOn(String arg0) {
    try {
        test.info("User clicking on "+arg0);
        String pageName = arg0.split("\\.")[0];
        String locator = arg0.split("\\.")[1];
        CommonUtils.getPageByName(pageName).clickButton(locator);
    }catch (Exception e) {
        test.fail(e);
        throw new RuntimeException(e);
        }
    }
}
