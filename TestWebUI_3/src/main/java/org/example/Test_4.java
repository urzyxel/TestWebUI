package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

public class Test_4 {
    public static void main(String[] args) throws InterruptedException {
        String error = "";
        Integer existenceElement = 0;
        Map<String, Integer> productsBasket = new HashMap<String, Integer>();//карта с товарами, которые нужно добавить в корзину
        productsBasket.put("#02802", 1);

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://greenwaystart.com/");

        Authorization authorization = new Authorization();
        if (authorization.authorizationSate(driver, "20101432", "program") == 0) {
            driver.navigate().to("https://greenwaystart.com/products/Sharme-Essential/");
            СlearBasket сlearBasket = new СlearBasket();
            driver.findElement(By.xpath("//button[@class='btn btn-cart']/span")).click();
            WebElement webElement = (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='basket']//div/p")));
            System.out.println(webElement.getText());
            if (webElement.getText().equals("")) {
                сlearBasket.clear(driver, 1);
            }

            for (Map.Entry<String, Integer> entry : productsBasket.entrySet()) {
                webElement = new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[text()='"+entry.getKey()+"']/..//a[@class='binfo  basketcnt baction btn-gift bskt-btn-p']")));
                webElement.click();
                try {
                    new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@class='alert alert-danger alert-dismissible'][text()='У Вас нет средств на подарочном счёте для выбора подарка']")));
                }catch (Exception e){
                    error = "Сообщение об ошибке: У Вас нет средств на подарочном счёте для выбора подарка, не выводилось!\n";
                }
            }
            webElement = (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='basket']//div/p")));
            if (webElement.getText().equals("ничего нет")) {
                error = error + "Продукт, за подарочную валюту добавлен, но средств на подарочном счету нет\n";
            }
            System.out.println(error);
        } else
            System.err.println("Ошибка проверки авторизации, выполнить тест не возможно!");

        do {
            existenceElement = driver.findElements(By.xpath(".//div[@class='alert alert-danger alert-dismissible'][text()='У Вас нет средств на подарочном счёте для выбора подарка']")).size();
        } while (existenceElement > 0);

        Logout logout = new Logout();
        if (logout.logoutSate(driver) == 1) {
            System.out.println("Ошибка Logout на сайте");
        }

        driver.quit();
    }
}
