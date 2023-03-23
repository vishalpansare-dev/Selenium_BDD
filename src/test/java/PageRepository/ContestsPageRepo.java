package PageRepository;

import org.openqa.selenium.By;

public interface ContestsPageRepo {

    By reset_btn = By.xpath("//*[@id='btnReset']");
    By search_btn = By.xpath("//*[@id='btnSearch']");
    By expand_btn = By.xpath("//*[@id='btnExpand']");
    By contests_search = By.xpath("//*[@id='ddlSearchContests']/nz-select-top-control/nz-select-search/input");
    By first_contestName = By.xpath("//nz-option-item[1]/div[text()='XXXX']");
}
