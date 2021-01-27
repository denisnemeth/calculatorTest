package sk.itsovy.nemethd;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Main {

    @Test
    public static void main(String[] args) throws Exception {

        String url = "http://itsovy.sk/testing";

        System.setProperty("webdriver.chrome.driver", "D:\\SCHOOL\\Other\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get(url);

        WebElement errorMessage = driver.findElement(By.id("error"));
        WebElement amount = driver.findElement(By.id("amount"));
        WebElement interest = driver.findElement(By.id("interest"));
        WebElement periodRng = driver.findElement(By.id("period"));
        WebElement periodLbl = driver.findElement(By.id("lblPeriod"));
        WebElement taxYes = driver.findElement(By.cssSelector("input[value=\"y\"]"));
        WebElement taxNo = driver.findElement(By.cssSelector("input[value=\"n\"]"));
        WebElement agreement = driver.findElement(By.id("confirm"));
        WebElement resetBtn = driver.findElement(By.id("btnreset"));
        WebElement calculateBtn = driver.findElement(By.id("btnsubmit"));
        WebElement result = driver.findElement(By.id("result"));

        Random rnd = new Random();
        String amountErrorMsg = "Amount must be a number between 0 and 1000000 !";
        String interestErrorMsg = "Interest must be a number between 0 and 100 !";
        String agreementErrorMsg = "You must agree to the processing !";
        String periodString;
        String red = "rgba(255, 0, 0, 1)";
        String darkGreen = "rgba(0, 100, 0, 1)";

        //default state
        Assert.assertFalse(errorMessage.isDisplayed());
        Assert.assertEquals(amount.getText(), "");
        Assert.assertEquals(interest.getText(), "");
        Assert.assertEquals(periodRng.getAttribute("value"), "1");
        periodString = periodLbl.getText();
        Assert.assertEquals(String.valueOf(periodString.charAt(periodString.length()-1)), "1");
        Assert.assertTrue(taxYes.isSelected());
        Assert.assertFalse(taxNo.isSelected());
        Assert.assertFalse(agreement.isSelected());
        Assert.assertFalse(result.isDisplayed());

        //nic sa nedoplní
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + interestErrorMsg + "\n" + agreementErrorMsg);

        //nedoplní sa amount a interest
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + interestErrorMsg);
        resetBtn.click();

        //nedoplní sa interest a nesúhlasí sa
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg + "\n" + agreementErrorMsg);
        resetBtn.click();

        //nesúhlasí sa a nedoplní sa amount
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg + "\n" + agreementErrorMsg);
        resetBtn.click();

        //nedoplní sa amount
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        resetBtn.click();

        //nedoplní sa interest
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        resetBtn.click();

        //nesúhlasí sa
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), agreementErrorMsg);
        resetBtn.click();

        //záporný amount
        amount.sendKeys("-" + rnd.nextInt(1000000));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        resetBtn.click();

        //amount nad milión
        amount.sendKeys(String.valueOf(rnd.nextInt(9000000)+1000000));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), amountErrorMsg);
        resetBtn.click();

        //záporný interest
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys("-" + rnd.nextInt(100));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        resetBtn.click();

        //interest nad sto
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(900)+100));
        agreement.click();
        calculateBtn.click();
        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage.getCssValue("color"), red);
        Assert.assertEquals(errorMessage.getText(), interestErrorMsg);
        resetBtn.click();

        //text pod sliderom sa rovná vybranej hodnote
        for (int i = 1; i < 6; i++) {
            Assert.assertEquals(periodRng.getAttribute("value"), String.valueOf(i));
            periodString = periodLbl.getText();
            Assert.assertEquals(String.valueOf(periodString.charAt(periodString.length()-1)), String.valueOf(i));
            periodRng.sendKeys(Keys.RIGHT);
        }
        for (int i = 5; i > 0; i--) {
            Assert.assertEquals(periodRng.getAttribute("value"), String.valueOf(i));
            periodString = periodLbl.getText();
            Assert.assertEquals(String.valueOf(periodString.charAt(periodString.length()-1)), String.valueOf(i));
            periodRng.sendKeys(Keys.LEFT);
        }
        resetBtn.click();

        //daň radio button
        Assert.assertTrue(taxYes.isSelected());
        Assert.assertFalse(taxNo.isSelected());
        taxNo.click();
        Assert.assertFalse(taxYes.isSelected());
        Assert.assertTrue(taxNo.isSelected());
        resetBtn.click();

        //reset nastavý default hodnoty
        amount.sendKeys(String.valueOf(rnd.nextInt(1000000)));
        interest.sendKeys(String.valueOf(rnd.nextInt(100)));
        agreement.click();
        calculateBtn.click();
        resetBtn.click();
        Assert.assertFalse(errorMessage.isDisplayed());
        Assert.assertEquals(amount.getText(), "");
        Assert.assertEquals(interest.getText(), "");
        Assert.assertEquals(periodRng.getAttribute("value"), "1");
        periodString = periodLbl.getText();
        Assert.assertEquals(String.valueOf(periodString.charAt(periodString.length()-1)), "1");
        Assert.assertTrue(taxYes.isSelected());
        Assert.assertFalse(taxNo.isSelected());
        Assert.assertFalse(agreement.isSelected());
        Assert.assertFalse(result.isDisplayed());

        //po výpočte sa vypíše správny výsledok
        int tax = 1;
        for (int i = 10; i > 0; i--) {
            double amountDbl = rnd.nextInt(1000000), interestDbl = rnd.nextInt(100), t = 0, expectedAmount = amountDbl, expectedProfit = 0;
            int periodInt = rnd.nextInt(4)+1;
            amount.sendKeys(String.valueOf(amountDbl));
            interest.sendKeys(String.valueOf(interestDbl));
            for (int x = 1; x < periodInt; x++) periodRng.sendKeys(Keys.RIGHT);
            if (tax == 1) {
                taxYes.click();
                tax = 0;
                t = 0.2;
            } else {
                taxNo.click();
                tax = 1;
                t = 0;
            }
            agreement.click();
            calculateBtn.click();
            for (int x = 0; x < periodInt; x++) expectedAmount *= (1 + (1 - t) * interestDbl / 100);
            expectedProfit = expectedAmount - amountDbl;
            BigDecimal roundA = new BigDecimal(expectedAmount);
            BigDecimal roundP = new BigDecimal(expectedProfit);
            roundA = roundA.setScale(2, RoundingMode.HALF_UP);
            roundP = roundP.setScale(2, RoundingMode.HALF_UP);
            Assert.assertTrue(result.isDisplayed());
            Assert.assertEquals(result.getCssValue("color"), darkGreen);
            //Assert.assertEquals(result.getText(), "Total amount : " + roundA + " , net profit : " + roundP);
            resetBtn.click();
        }

        System.out.println("All test were successful !");

        driver.quit();
    }
}
