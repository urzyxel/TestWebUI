package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Authorization {
    public static int authorizationSate(WebDriver driver, String user, String password) {
        String validity = "";
        try {
            driver.findElement(By.xpath("//a[@class='user-nav_login']")).click();
            driver.findElement(By.id("nameModal")).sendKeys(user);
            driver.findElement(By.id("passwordModal")).sendKeys(password);
            driver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("user-block")));
            validity = driver.findElement(By.className("user-block")).getText();
        } catch (Exception e) {
            return 1;
        }
        if (!validity.contains(user)) {
            return 1;
        } else
            return 0;
    }
}
