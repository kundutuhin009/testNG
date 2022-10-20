package com.assignment.managers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.MediaEntityBuilder;



public class Utilities {
	
	public static void waitForElementToLoad(WebDriver driver,WebElement ele)
	{
		WebDriverWait wait = new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(ele)));
	}
	
	public static void waitForElementToBeClickable(WebDriver driver,WebElement ele)
	{
		WebDriverWait wait = new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.elementToBeClickable(ele));
	}
	
	public static void waitForElementToBeVisible(WebDriver driver,WebElement ele)
	{
		WebDriverWait wait = new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.visibilityOf(ele));
	}

	public static void hoverOnElement(WebDriver driver,WebElement ele)
	{
		Actions ac = new Actions(driver);
		ac.moveToElement(ele).perform();
	}
	
	
	public static void clickOnElementAction(WebDriver driver,WebElement ele)
	{
		Actions ac = new Actions(driver);
		ac.moveToElement(ele).click(ele).build().perform();
	}
	
	public static void presenceOfEle(WebDriver driver,WebElement ele)
	{
		WebDriverWait wait = new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(ele)));
	}
	
	
	public static WebElement returnIndexedElement(WebDriver driver,String xpath,Integer index)
	{
		WebElement ele = driver.findElement(By.xpath(xpath+"["+index+"]"));
		return ele;
	}
	
	public static void jsScrollDown(WebDriver driver,int value)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,"+value+")", "");
	}
	
	public static void jsClick(WebDriver driver,WebElement ele)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", ele);
	}
	
	public static String CaptureScreenshot(WebDriver driver) throws IOException {
		
		String Extent_report_path = System.getProperty("user.dir") +"/Reports/";
		String ScreenshotPath = Extent_report_path+"screenshots";
		 
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String screenshotName = "screenshot"+Utilities.getCurrentDateTime().toString()+".png";
		String screenshotpath = ScreenshotPath+"/"+screenshotName;
		 
		FileUtils.copyFile(src,new File(screenshotpath));
		return screenshotpath;
		 
		 
	}
	
	public static String getCurrentDateTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");  
	    Date date = new Date();
	    return formatter.format(date);
	}

	public static void waitUntilValueChange(WebDriver driver,final WebElement e,final String before)
	{
		WebDriverWait wait = new WebDriverWait(driver,20);
		wait.until(new ExpectedCondition<Boolean>() {
		    public Boolean apply(WebDriver driver) {
		        String after = e.getAttribute("value");
		        if(!before.equals(after)) 
		            return true;
		        else
		            return false;
		    }
		});
		
	}
}
