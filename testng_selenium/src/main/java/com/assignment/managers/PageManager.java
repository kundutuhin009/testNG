package com.assignment.managers;

import org.openqa.selenium.WebDriver;

import com.assignment.pages.CartPage;
import com.assignment.pages.HomePage;

public class PageManager {

	WebDriver driver;
	HomePage homePage;
	CartPage cartPage;
	
	public PageManager(WebDriver driver) {

		this.driver = driver;

	}
	
	public HomePage getHomePage(){

		return (homePage == null) ? homePage = new HomePage(driver) : homePage;

	}
	
	public CartPage getCartPage(){

		return (cartPage == null) ? cartPage = new CartPage(driver) : cartPage;

	}
}
