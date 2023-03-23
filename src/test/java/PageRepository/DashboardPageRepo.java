package PageRepository;

import org.openqa.selenium.By;

public interface DashboardPageRepo {

    By elections_menu = By.xpath("//*[@id='mainsidebar']/layout-sidebar/div/layout-default-nav/ul/li/a/span[@title='Elections']");
    By contests_menu = By.xpath("//*[@id='mainsidebar']/layout-sidebar/div/layout-default-nav/ul/li[3]/ul/li[5]/a/span[@title='Contests']");

}
