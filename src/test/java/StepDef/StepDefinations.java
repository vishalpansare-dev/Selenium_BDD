package StepDef;

import PageObjects.LoginPage;
import TestBase.BaseClass;
import io.cucumber.java.en.Given;

public class StepDefinations  {

    LoginPage loginPage;
    @Given("User open {string} URL")
    public void userOpenURL(String URL) {
        System.out.println("User open URL"+URL);
        loginPage = new LoginPage();
        loginPage.gotoU(URL);
    }
}
