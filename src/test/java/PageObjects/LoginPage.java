package PageObjects;

import Helpers.Ipage;
import PageRepository.LoginPageRepo;
import TestBase.BaseClass;

public class LoginPage extends BaseClass implements LoginPageRepo, Ipage {

    public void gotoU(String url) {
        System.out.println("logger = " + logger);
    }

    @Override
    public void gotoURL(String URL) {

    }

    @Override
    public void clickButton(String buttonName) {

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
