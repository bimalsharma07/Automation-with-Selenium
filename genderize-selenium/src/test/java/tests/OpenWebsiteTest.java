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

        
        Map<String, String> testData = new LinkedHashMap<>();
        testData.put("Bimal", "male");
        testData.put("Sophia", "female");
        testData.put("Jack", "male");
        testData.put("Emily", "female");
        testData.put("Liam", "male");
        testData.put("Emma", "female");
        testData.put("23234", "Uh oh. 23234 is unknown to us");  

       
        By inputLocator = By.id("trial-input");
        By buttonLocator = By.xpath("//*[@id='trial-input-form']/button");
        By resultLocator = By.xpath("//*[@id='genderize']/main/div[1]/div[1]/div/p");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (Map.Entry<String, String> entry : testData.entrySet()) {
            String input = entry.getKey();
            String expectedOutput = entry.getValue();

            
            WebElement inputBox = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));

           
            js.executeScript("arguments[0].value = '';", inputBox);
            inputBox.sendKeys(input);
            
            searchButton.click();

            
            wait.until(ExpectedConditions.textToBePresentInElementLocated(resultLocator, input));
            
            WebElement resultElement = driver.findElement(resultLocator);
            String actualResult = resultElement.getText().trim();

            
            if (input.equals("23234")) {
                // Verify full error message for digits
                assertTrue(actualResult.equals(expectedOutput),
                        "❌ For input: " + input + ", expected: '" + expectedOutput +
                        "', but got: '" + actualResult + "'");
            } else {
                // Standard gender verification for names
                assertTrue(actualResult.toLowerCase().contains(expectedOutput.toLowerCase()),
                        "❌ For name: " + input + ", expected gender: " + expectedOutput +
                        ", but got: " + actualResult);
            }

            System.out.printf("✅ %s → '%s' verified%n", input, expectedOutput);
        }

        driver.quit();
    }
}
