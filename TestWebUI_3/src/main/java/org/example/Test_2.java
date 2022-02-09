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


public class Test_2 {
    public static void main(String[] args) throws InterruptedException {
        String errorBasket = "";
        String article = ""; //артикул товара
        Integer quantity = 0; //количество в заказе
        Integer price = 0; //цена товара
        Integer cost = 0; //стоимость
        Integer orderAmount = 0; //сумма заказа

        Map<String, Integer> productsBasket = new HashMap<String, Integer>();//карта с товарами, которые нужно добавить в корзину
        productsBasket.put("#02109", 1);
        productsBasket.put("#02117", 2);
        productsBasket.put("#02152", 1);

        Map<String, String[]> orderMap = new HashMap<String, String[]>();//карта, которая содержит товары, количество и цену за единицу

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://greenwaystart.com/");

        Authorization authorization = new Authorization();
        if (authorization.authorizationSate(driver, "20101432", "program") == 0) {
            driver.navigate().to("https://greenwaystart.com/products/Aquamatic/");
            //проверка предусловия, что корзина на начало теста пуста, если нет, то чистим
            СlearBasket сlearBasket = new СlearBasket();
            driver.findElement(By.xpath("//button[@class='btn btn-cart']/span")).click();
            WebElement webElement = (new WebDriverWait(driver, 20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='basket']//div/p")));
            if (webElement.getText().equals("")) {
                сlearBasket.clear(driver, 1);
            }

            WorkBasket workBasket = new WorkBasket();
            orderMap = workBasket.addProduct(driver, productsBasket);

            Thread.sleep(5000);

            WebElement element;
            element = driver.findElement(By.xpath("//button[@class='btn btn-cart']/span"));
            JavascriptExecutor js1 = (JavascriptExecutor) driver;
            js1.executeScript("arguments[0].scrollIntoView();", element);
            js1.executeScript("arguments[0].click();", element);

            element = driver.findElement(By.xpath("//a[@class='alert-info']"));
            js1.executeScript("arguments[0].scrollIntoView();", element);
            js1.executeScript("arguments[0].click();", element);
            //проверка состава корзины и стоимости
            for (Map.Entry<String, String[]> entry : orderMap.entrySet()) {
                article = entry.getKey();
                quantity = Integer.parseInt(orderMap.get(entry.getKey())[0]);
                price = Integer.parseInt(orderMap.get(entry.getKey())[1].replaceAll("\\D+", ""));
                try {
                    driver.findElement(By.xpath("//td[@class='name'][text()='" + article + " ']"));
                } catch (Exception e) {
                    System.out.println("Товар с артиклом " + article + ", проверка с валидацией корзины не пройдена");
                }

                if (!orderMap.get(entry.getKey())[0].equals(driver.findElement(By.xpath("//td[@class='name'][text()='" + article + " ']/../td[@class='control']/b[@class='badge basketcnt box']")).getText())) {
                    errorBasket = errorBasket + "Не совпадает количество товара в корзине, для : " + article + "\n";
                }
                if (!orderMap.get(entry.getKey())[1].replaceAll("\\D+", "").equals(driver.findElement(By.xpath("//td[@class='name'][text()='" + article + " ']/../td[@class='price']")).getText())) {
                    errorBasket = errorBasket + "Не совпадает цена товара в корзине, для : " + article + "\n";
                }
                cost = price * quantity;
                if (cost != Integer.parseInt(driver.findElement(By.xpath("//td[@class='name'][text()='" + article + " ']/../td[@class='basketcnt price'][1]")).getText())) {
                    errorBasket = errorBasket + "Не совпадает итого товара в корзине, для : " + article + "\n";
                }
                orderAmount += cost;

            }
            if (orderAmount != Integer.parseInt(driver.findElement(By.xpath("//th[@class='price basketcost']")).getText())) {
                errorBasket = errorBasket + "Не совпадает итоговая сумма заказа!";
            }

            if (errorBasket.equals("")) {
                System.out.println("Проверка корзины на содержимое пройдено успешно");
            }
            System.out.println(errorBasket);
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
