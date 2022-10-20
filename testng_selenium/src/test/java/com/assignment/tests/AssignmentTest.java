package com.assignment.tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.assignment.managers.ConfigReader;
import com.assignment.managers.PageManager;
import com.assignment.managers.Utilities;
import com.assignment.managers.WebdriverManager;
import com.assignment.pages.CartPage;
import com.assignment.pages.HomePage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class AssignmentTest{
	
	WebDriver driver;
	PageManager pm;
	ConfigReader cr;
	WebdriverManager wdm;
	HomePage hp;
	CartPage cp;
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest test;
	SoftAssert sa = new SoftAssert();
	
	@BeforeTest
	public void BeforeAllMethods()
	{
		cr = new ConfigReader();
		wdm = new WebdriverManager();
		driver= wdm.getDriver();
        pm = new PageManager(driver);
        hp = pm.getHomePage();
        cp = pm.getCartPage();
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/Reports/testReport"+Utilities.getCurrentDateTime()+".html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Simple Automation Report");
        htmlReporter.config().setReportName("Test Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
	}
	
	@AfterTest
	public void TakeDown()
	{
		extent.flush();
		wdm.closeDriver();
	}
	
	@AfterMethod
    public void getResult(ITestResult result,Method method) throws IOException {
        if(result.getStatus() == ITestResult.FAILURE) {
            //test.log(Status.FAIL,result.getThrowable());
        	String screenShotPath = Utilities.CaptureScreenshot(driver);
        	File file = new File(screenShotPath);
        	byte[] fileContent = FileUtils.readFileToByteArray(file);
        	String screenshotFile="Test_"+(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date()).toString()+".png";
        	String resultantimage = "data:image/png;base64,"+Base64.getEncoder().encodeToString(fileContent);
        	test.fail("Method Failed",MediaEntityBuilder.createScreenCaptureFromPath(resultantimage,screenshotFile).build());
        }
        else if(result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, method.getAnnotation(Test.class).description());
        }
        else {
            test.log(Status.SKIP, method.getAnnotation(Test.class).description());
        }
    }
	
	@Test(description = "Asignment Test Case")
	public void testMethod() throws InterruptedException, IOException
	{
		test = extent.createTest("Assignment Test Scenario");
		hp.navigatetoURL(cr.getApplicationUrl());
		
		//searching for first dress and adding it
		String[] val;
		Map<String,String> skuvalues = new HashMap<String,String>();
		
		val = hp.addToCart("faded short sleeve t-shirt", "blue", "medium");
		sa.assertTrue(val[0].equalsIgnoreCase("Success"), "Medium Blue Faded Short Sleeve T-shirt not added to cart: "+val);
		if(val[0].equalsIgnoreCase("Success"))
			test.log(Status.PASS, "Medium Blue Faded Short Sleeve T-shirt successfully added to cart");
		else
			{
				test.log(Status.FAIL, "Medium Blue Faded Short Sleeve T-shirt not added to cart: "+val);
				
			}
		hp.rtnToHomeBtn.click();
		skuvalues.put("faded short sleeve t-shirt", val[1]);
		
		
		//searching and adding second dress
		val=hp.addToCart("evening dress", "beige","small");
		sa.assertTrue(val[0].equalsIgnoreCase("Success"), "Small Beige Evening dress not added to cart: "+val);
		if(val[0].equalsIgnoreCase("Success"))
			test.log(Status.PASS, "Small Beige Evening dress successfully added to cart");
		else
			{
				test.log(Status.FAIL, "Small Beige Evening dress not added to cart: "+val);
				
			}
		hp.rtnToHomeBtn.click();
		skuvalues.put("evening dress", val[1]);
		
		//searching and adding third dress
		val = hp.addToCart("printed summer dress", "orange","medium");
		sa.assertTrue(val[0].equalsIgnoreCase("Success"), "Medium Orange Printed Summer Dress not added to cart: "+val);
		if(val[0].equalsIgnoreCase("Success"))
			test.log(Status.PASS, "Medium Orange Printed Summer Dress successfully added to cart");
		else
			{
				test.log(Status.FAIL, "Medium Orange Printed Summer Dress not added to cart: "+val);
				
			}
		hp.rtnToHomeBtn.click();
		skuvalues.put("printed summer dress", val[1]);
		
		//navigating to the cart after adding all the products
		cp.navigateToCart();
		
		//removing the evening dress
		Boolean removed = cp.removeItem(skuvalues.get("evening dress"));
		sa.assertTrue(removed,"Unable to remove evening dress from the cart");
		if(removed)
			{
				test.log(Status.PASS, "Evening dress was successfully removed from the cart");
				skuvalues.remove("evening dress");
			}
		else
			test.log(Status.FAIL, "Unable to remove evening dress from the cart because SKU does not exist");
		
		
		//adding another faded short sleeve t-shirt with same details
		Boolean added = cp.addItem(skuvalues.get("faded short sleeve t-shirt"), 1);
		sa.assertTrue(added,"Unable to add faded short sleeve t-shirt to the cart");
		if(added)
			test.log(Status.PASS, "Faded short sleeve t-shirt was successfully added to the cart with additional quanity: 1");
		else
			test.log(Status.FAIL, "Unable to add Faded short sleeve t-shirt to the cart because SKU does not exist");
		
		//asserting individual prices
		for(Map.Entry<String,String> m:skuvalues.entrySet())
		{
			Boolean linetot = cp.assertLineTotal(m.getValue());
			sa.assertTrue(linetot,"The line total for the: "+m.getKey()+" is not correctly displayed");
			if(linetot)
				test.log(Status.PASS, "The line total for the: "+m.getKey()+" is correctly displayed");
			else
				test.log(Status.FAIL, "The line total for the: "+m.getKey()+" is incorrectly displayed");
		}
		
		//validating the cart total price is $63.53 or not
		sa.assertTrue(cp.fetchCartTotal()==65.53, "The cart total of the added items is not $65.53");
		if(cp.fetchCartTotal()==65.53)
			test.log(Status.PASS, "The cart total for the items added is correctly displayed as $65.53");
		else
			test.log(Status.FAIL, "The cart total for the items added is incorrectly displayed as: $"+cp.fetchCartTotal()+ " instead of $65.53");
		sa.assertAll();
		
	}
	

}
