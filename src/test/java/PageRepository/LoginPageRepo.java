package PageRepository;

import org.openqa.selenium.By;

public interface LoginPageRepo {

    By username = By.xpath("//*[@id='txtUserName']");
    By password = By.xpath("//*[@id='txtPassword']");
    By login_btn = By.xpath("//*[]");
}
