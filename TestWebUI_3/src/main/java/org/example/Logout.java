package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Logout {
    public static int logoutSate(WebDriver driver) {
        try {
            driver.findElement(By.xpath("//button[@class='btn btn-logout']/span")).click();
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }
}
