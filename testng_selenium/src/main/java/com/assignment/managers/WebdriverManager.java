package com.assignment.managers;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebdriverManager {
	private WebDriver driver;
	private static DriverType driverType;
	ConfigReader cr = new ConfigReader();
	
	public WebdriverManager() {
		driverType = cr.getBrowser();
	}
	public WebDriver getDriver() {
		if(driver == null) driver = createLocalDriver();
		return driver;
	}
	
	private WebDriver createLocalDriver() {
        switch (driverType) {	    
        case CHROME :
        	WebDriverManager.chromedriver().setup();
        	ChromeOptions options = new ChromeOptions();
        	if(cr.getHeadlessValue().equals("true"))
        		options.addArguments("--headless");
        	driver = new ChromeDriver(options);
        	break;
        case EDGE :
        	WebDriverManager.edgedriver().setup();
        	driver = new EdgeDriver();

        }
        
        if(cr.getBrowserWindowSize().equals("true"))
        	driver.manage().window().maximize();
        if(cr.getHeadlessValue().equals("true"))
        	driver.manage().window().setSize(new Dimension(1920,1080));
        
        	
        driver.manage().timeouts().implicitlyWait(cr.getImplicitlyWait(), TimeUnit.SECONDS);
		return driver;
	}
	
	public void closeDriver() {
		driver.close();
		driver.quit();
	}
	

}
