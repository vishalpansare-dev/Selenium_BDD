package Helpers;

public interface Ipage {
public void gotoURL(String URL);
public void clickButton(String buttonName);
public void enterText(String textBoxName,String text);
public void selectRadioButton(String radioButtonName,String value);
public void selectDropDown(String dropDownName,String value);
public void navigate(String navigateAction);
public void readWebTable(String webTableName);


}
