package tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenWebsiteTest {

    @Test
    public void validateGenderPrediction() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://genderize.io/");

        // Use LinkedHashMap to maintain insertion order
        Map<String, String> testData = new LinkedHashMap<>();
        testData.put("Bimal", "male");
        testData.put("Sophia", "female");
        testData.put("Jack", "male");
        testData.put("Emily", "female");
        testData.put("Liam", "male");
        testData.put("Emma", "female");

        By inputLocator = By.id("trial-input");
        By buttonLocator = By.xpath("//*[@id='trial-input-form']/button");
        By resultLocator = By.xpath("//*[@id='genderize']/main/div[1]/div[1]/div/p");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (Map.Entry<String, String> entry : testData.entrySet()) {
            String name = entry.getKey();
            String expectedGender = entry.getValue();

            // Get fresh references
            WebElement inputBox = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));

            // Clear using JavaScript
            js.executeScript("arguments[0].value = '';", inputBox);
            inputBox.sendKeys(name);
            
            searchButton.click();

            // Wait for result to update with current name
            wait.until(ExpectedConditions.textToBePresentInElementLocated(resultLocator, name));
            
            // Get updated result element
            WebElement resultElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(resultLocator)
            );
            String actualResult = resultElement.getText().trim().toLowerCase();

            assertTrue(actualResult.contains(expectedGender.toLowerCase()),
                    "❌ For name: " + name + ", expected gender: " + expectedGender +
                            ", but got: " + actualResult);

            System.out.printf("✅ %s → is '%s' as expected%n", name, expectedGender);
        }

        driver.quit();
    }
}