package PageObjects;

import Helpers.CommonUtils;
import Helpers.Ipage;
import Helpers.SeleniumUtils;
import PageRepository.LoginPageRepo;
import TestBase.BaseClass;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;

public class LoginPage extends BaseClass implements LoginPageRepo, Ipage {



    @Override
    public void gotoURL(String URL) {
        driver.get(URL);
        test.pass("User opens "+ URL);
    }

    @Override
    public void clickButton(String buttonName) {
        Class<LoginPageRepo> loginPageRepoClass = LoginPageRepo.class;
        By locator = CommonUtils.getLocatorByName(loginPageRepoClass,buttonName);
        SeleniumUtils.click(locator);
        test.pass("User clicked on " + buttonName);
    }

    @Override
    public void enterText(String textBoxName, String text) {

    }

    @Override
    public void selectRadioButton(String radioButtonName, String value) {

    }

    @Override
    public void selectDropDown(String dropDownName, String value) {

    }

    @Override
    public void navigate(String navigateAction) {

    }

    @Override
    public void readWebTable(String webTableName) {

    }
}
