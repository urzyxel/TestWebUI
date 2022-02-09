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

public class Test_3 {
    public static void main(String[] args) throws InterruptedException {
        String errorDelivery = "";
        Map<String, Integer> productsBasket = new HashMap<String, Integer>();//карта с товарами, которые нужно добавить в корзину
        productsBasket.put("#01210", 1);

        Map<String, String> serviceDelivery = new HashMap<String, String>();//id служб доставки
        serviceDelivery.put("ozon_pvz", "Доставка до пункта выдачи заказов OZON ROCKET");
        serviceDelivery.put("pochta_pvz", "Доставка до пункта выдачи заказов Почта России");
        serviceDelivery.put("sdek_pvz", "Доставка до пункта выдачи заказов СДЭК");
        serviceDelivery.put("sdek", "Доставка до дверей, курьерской службой СДЭК");
        serviceDelivery.put("pickpoint", "Доставка до пункта выдачи заказов PickPoint");
        serviceDelivery.put("bb_pvz", "Доставка до пункта выдачи заказов BoxBerry");
        serviceDelivery.put("pochta", "Доставка курьерской службой Почта России");
        serviceDelivery.put("dpd_pvz", "Доставка до пункта выдачи заказов DPD");
        serviceDelivery.put("ozon", "Доставка до дверей, курьерской службой OZON ROCKET");
        serviceDelivery.put("dpd_ru", "Доставка до дверей, курьерской службой DPD");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://greenwaystart.com/");

        Authorization authorization = new Authorization();
        if (authorization.authorizationSate(driver, "20101432", "program") == 0) {
            driver.navigate().to("https://greenwaystart.com/products/Balancer/");
            //проверка предусловия, что корзина на начало теста пуста, если нет, то чистим
            СlearBasket сlearBasket = new СlearBasket();
            driver.findElement(By.xpath("//button[@class='btn btn-cart']/span")).click();
            WebElement webElement = (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='basket']//div/p")));
            if (webElement.getText().equals("")) {
                сlearBasket.clear(driver, 1);
            }

            WorkBasket workBasket = new WorkBasket();
            workBasket.addProduct(driver, productsBasket);

            WebElement element;
            element = driver.findElement(By.xpath("//button[@class='btn btn-cart']/span"));
            JavascriptExecutor js1 = (JavascriptExecutor) driver;
            js1.executeScript("arguments[0].scrollIntoView();", element);
            js1.executeScript("arguments[0].click();", element);

            element = driver.findElement(By.xpath("//a[@class='alert-info']"));
            js1.executeScript("arguments[0].scrollIntoView();", element);
            js1.executeScript("arguments[0].click();", element);
            //проверка служб доставки
            driver.findElement(By.xpath("//a[@id='goToCityChoose']")).click();
            element = driver.findElement(By.xpath("//a[@id='goToDeliveryWay']"));
            js1.executeScript("arguments[0].scrollIntoView();", element);
            js1.executeScript("arguments[0].click();", element);
            webElement = new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='collapseDelivery']/div[@class='panel-body']/descendant::ul/li[2]/a[@class='bg-info']")));
            webElement.click();

            for (Map.Entry<String, String> entry : serviceDelivery.entrySet()) {
                try {
                    driver.findElement(By.xpath("//input[@id='" + entry.getKey() + "']"));
                } catch (Exception e) {
                    errorDelivery = errorDelivery + "Не найдена служба доставки: " + entry.getValue() + "\n";
                }
            }
            if (errorDelivery.equals("")) {
                System.out.println("Проверка служб доставки пройдено успешно!");
            }
            System.out.println(errorDelivery);
            сlearBasket.clear(driver, 0);
        } else
            System.err.println("Ошибка проверки авторизации, выполнить тест не возможно!");

        Logout logout = new Logout();
        if (logout.logoutSate(driver) == 1) {
            System.out.println("Ошибка Logout на сайте");
        }

        driver.quit();
    }
}
