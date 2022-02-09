package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Test_1 {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://greenwaystart.com/");

        Authorization authorization = new Authorization();
        if (authorization.authorizationSate(driver, "20101432", "program") == 0) {
            System.out.println("Проверка успешна, авторизация пройдена");
        } else
            System.out.println("Ошибка проверки авторизации");

        Logout logout = new Logout();
        if (logout.logoutSate(driver) == 1) {
            System.out.println("Ошибка Logout на сайте");
        }

        driver.quit();
    }
}
