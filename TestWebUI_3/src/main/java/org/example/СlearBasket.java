package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class СlearBasket {
    void clear(WebDriver driver, Integer fl) {
        if (fl == 0) {
            driver.findElement(By.xpath("//button[@class='btn btn-cart']/span")).click();
        }
        WebElement webElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='basket']/li[2]/a")));
        webElement.click();
        driver.switchTo().alert().accept();
    }
}
