package com.assignment.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.assignment.managers.Utilities;

public class HomePage {
	
	public WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//a[text()='Women']/../following-sibling::li//a[text()='Dresses']")
	public WebElement dressTab;
	
	@FindBy(xpath="//a[text()='Women']/../following-sibling::li//ul//a[text()='Evening Dresses']")
	public WebElement eveningDressTab;
	
	@FindBy(xpath="//a[text()='Women']/../following-sibling::li//ul//a[text()='Summer Dresses']")
	public WebElement summerDressTab;
	
	@FindBy(xpath="//a[text()='Women']/../following-sibling::li//a[text()='T-shirts']")
	public WebElement tshirtTab;
	
	@FindBy(xpath="//ul[@class='product_list grid row']")
	public WebElement productResults;
	
	@FindBy(xpath="//ul[@class='product_list grid row']/li//div[@class='left-block']//a[@itemprop]")
	public List<WebElement> listOfProducts;
	
	@FindBy(xpath="//select[@name='group_1']")
	public WebElement sizeSelect;
	
	@FindBy(xpath="//ul[@id='color_to_pick_list']/li/a")
	public List<WebElement> selectColor;
	
	@FindBy(xpath="//button[@name='Submit']")
	public WebElement addToCart;
	
	@FindBy(xpath="//span[@title='Continue shopping']")
	public WebElement conShoppingBtn;
	
	@FindBy(xpath="//span[text()='Quick view']/..")
	public List<WebElement> qckViewBtn;
	
	@FindBy(xpath="//a[@title='Return to Home']")
	public WebElement rtnToHomeBtn;
	
	@FindBy(xpath="//ul[@class='product_list grid row']/li")
	public List<WebElement> productListItems;
	
	@FindBy(xpath="//a[@title='Close']")
	public WebElement shopCloseBtn;
	
	@FindBy(xpath="//div/h1[@itemprop='name']")
	public WebElement productTitle;
	
	@FindBy(xpath="//div[@id='short_description_content']/p")
	public WebElement productDescription;
	
	@FindBy(xpath="//p[@id='product_reference']/span[@itemprop='sku']")
	public WebElement productSku;
	
	public void navigatetoURL(String url)
	{
		driver.get(url);
	}
	
	public boolean selectSize(String size)
	{
		
		Select se = new Select(sizeSelect);
		if(size.equalsIgnoreCase("Small"))
			se.selectByIndex(0);
		else if(size.equalsIgnoreCase("Medium"))
			se.selectByIndex(1);
		else if(size.equalsIgnoreCase("Large"))
			se.selectByIndex(2);
		else
			return false;
		return true;
	}
	
	public boolean selectColor(String color)
	{
		if(selectColor.size()==0)
		{
			return false;
		}
	else
	{
		for(WebElement c:selectColor)
		{
			if(c.getAttribute("name").equalsIgnoreCase(color))
			{
				c.click();
				return true;
			}
		}
				
	}
		return false;
		
	}
	public String[] addToCart(String dress,String color,String size) throws InterruptedException
	{
		String[] arr = new String[2];
		try
		{
			if(dress.toUpperCase().contains("DRESS"))
				{
					Utilities.waitForElementToBeClickable(driver, dressTab);
					Utilities.hoverOnElement(driver, dressTab);
					if(dress.toUpperCase().contains("SUMMER"))
						Utilities.clickOnElementAction(driver, summerDressTab);
					if(dress.toUpperCase().contains("EVENING"))
						Utilities.clickOnElementAction(driver, eveningDressTab);
				}
			else if(dress.toUpperCase().contains("T-SHIRT"))
				{
					Utilities.waitForElementToBeClickable(driver, tshirtTab);
					Utilities.clickOnElementAction(driver, tshirtTab);
				}
			else
				{
					arr[0] = "Category should be Dress or T-shirt";
					arr[1] = "";
					return arr;
				}
			
		}
		catch(Exception e)
		{
			//System.out.println(e);
			arr[0] = "Dress with category does not exist";
			arr[1] = "";
			return arr;
		}
		try
		{
			Utilities.waitForElementToLoad(driver, productResults);
		}
		catch(Exception e)
		{
			arr[0] = "No dress exists under the category";
			arr[1] = "";
			return arr;
		}
		Boolean added = false;
		String sku="";
		int count=1;
		for(WebElement e:productListItems)
		{
			Utilities.hoverOnElement(driver, e);
			Utilities.waitForElementToBeClickable(driver, driver.findElement(By.xpath("(//span[text()='Quick view'])["+count+"]")));
			Utilities.clickOnElementAction(driver, driver.findElement(By.xpath("(//span[text()='Quick view'])["+count+"]")));
			Utilities.waitForElementToBeClickable(driver, shopCloseBtn);
			driver.switchTo().frame(0);
			if(productTitle.getText().toUpperCase().contains(dress.toUpperCase())||productDescription.getText().toUpperCase().contains(dress.toUpperCase()))
				added=true;
			else
				{
					
					driver.switchTo().parentFrame();
					shopCloseBtn.click();
					count++;
					continue;
				}
			
			added = selectSize(size);
			if(added)
				{
					added=selectColor(color);
					if(added)
					{
						addToCart.click();
						sku= productSku.getText();
						Utilities.waitForElementToBeClickable(driver, conShoppingBtn);
						conShoppingBtn.click();
					}
				}
			if(added)
				break;
			else
				{
					driver.switchTo().parentFrame();
					shopCloseBtn.click();
					count++;
				}
				
		}
		if(added)
			{
				arr[0] = "Success";
				arr[1] = sku;
				return arr;
			}
		else
			{
				arr[0] = "Dress specifications does not exist or the dress you are looking for does not exist";
				arr[1] = sku;
				return 	arr;
			}
		
	}

}
