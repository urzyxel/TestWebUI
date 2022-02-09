package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class WorkBasket {
    public static Map addProduct(WebDriver driver, Map<String, Integer> productsBasket) {
        String strPrice = "";
        Map<String, String[]> orderMap = new HashMap<String, String[]>();
        String[] orderInfo;
        WebElement element;
        for (Map.Entry<String, Integer> entry : productsBasket.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            for (int i = 0; i < entry.getValue(); i++) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                try {
                    element = driver.findElement(By.xpath(".//div[text()='" + key + "']/..//a[@class='binfo basketcnt baction btn-to-cart bskt-btn-p']"));
                } catch (Exception e) {
                    element = driver.findElement(By.xpath(".//div[text()='" + key + "']/..//a[@class='binfo basketcnt baction btn-to-cart bskt-btn-p inthecart btn-before']"));
                }
                js.executeScript("arguments[0].scrollIntoView();", element);
                js.executeScript("arguments[0].click();", element);
                strPrice = driver.findElement(By.xpath(".//div[@class='catalog-item-code'][text()='" + key + "']/..//p[2]/span[2]")).getText();
                orderInfo = new String[]{value, strPrice};
                orderMap.put(key, orderInfo);
            }
        }
        return orderMap;
    }
}
