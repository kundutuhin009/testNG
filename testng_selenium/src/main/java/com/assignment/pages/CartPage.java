package com.assignment.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.assignment.managers.Utilities;

public class CartPage {
	
	public WebDriver driver;
	
	public CartPage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath="//a[@title='View my shopping cart']")
	public WebElement viewMyCartBtn;
	
	@FindBy(xpath="//td[@class='cart_description']//small[contains(text(),'SKU')]")
	public List<WebElement> cartSkuVal;
	
	@FindBy(xpath="//h1[@id='cart_title']")
	public WebElement cartPageTitle;
	
	@FindBy(xpath="//a[@title='Check out']")
	public WebElement checkOutBtn;
	
	@FindBy(xpath="//span[@id='total_price']")
	public WebElement cartTotalPrice;
	
	public void navigateToCart()
	{
		Utilities.jsClick(driver, viewMyCartBtn);
		Utilities.waitForElementToBeVisible(driver, cartPageTitle);
		
	}
	public boolean removeItem(String sku)
	{
		
		Boolean removed = false;
		for(WebElement e:cartSkuVal)
		{
			if(e.getText().contains(sku))
			{
				driver.findElement(By.xpath("//td[@class='cart_description']//small[contains(text(),'"+sku+"')]/../../td[@data-title='Delete']//a")).click();
				removed = true;
			}
		}
		return removed;
	}
	
	public boolean addItem(String sku,int quantity)
	{
		Boolean added=false;
		for(WebElement e:cartSkuVal)
		{
			if(e.getText().contains(sku))
			{
				
				for(int i=1;i<=quantity;i++)
				{
					WebElement before = driver.findElement(By.xpath("//td[@class='cart_description']//small[contains(text(),'"+sku+"')]/../../td[@class='cart_quantity text-center']//input[@type='hidden']"));
					driver.findElement(By.xpath("//td[@class='cart_description']//small[contains(text(),'"+sku+"')]/../../td[@class='cart_quantity text-center']//a[@title='Add']")).click();
					Utilities.waitUntilValueChange(driver, before, before.getAttribute("value"));
				}
				//navigateToCart();
				added=true;
				break;
			}
		}
		return added;
	}
	
	public boolean assertLineTotal(String sku)
	{
		Boolean linetot = false;
		
		for(WebElement e:cartSkuVal)
		{
			if(e.getText().contains(sku))
			{
				String quantity = driver.findElement(By.xpath("//td[@class='cart_description']//small[contains(text(),'"+sku+"')]/../../td[@class='cart_quantity text-center']//input[@type='hidden']")).getAttribute("value");
				String unitprice = driver.findElement(By.xpath("//td[@class='cart_description']//small[contains(text(),'"+sku+"')]/../../td[@class='cart_unit']/span/span[@class='price']")).getText();
				String line = driver.findElement(By.xpath("//td[@class='cart_description']//small[contains(text(),'"+sku+"')]/../../td[@class='cart_total']/span")).getText();
				
				if(Double.parseDouble(unitprice.substring(1))*Integer.parseInt(quantity) == Double.parseDouble(line.substring(1)))
					linetot=true;
				else
					linetot = false;
			}
		}
		return linetot;
	}
	
	public double fetchCartTotal()
	{
		Utilities.jsScrollDown(driver, 250);
		return Double.parseDouble(cartTotalPrice.getText().substring(1));
		
		
	}
}
