package com.force.utility;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.force.config.ConfigurationManager;
import com.force.data.dynamic.FakerDataFactory;

public class Registration {

	@Test(invocationCount = 1, threadPoolSize = 5)
	public void createRegistrations() throws InterruptedException {

		RedisManager redisManager = new RedisManager("20.244.5.250", 6379);
		if(redisManager.getRecordCount("registrations") < ConfigurationManager.configuration().medmantraRegCount());{

			for (int i = 0; i < 2; i++) {

				if(redisManager.getRecordCount("registrations") > ConfigurationManager.configuration().medmantraRegCount()) break;
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--remote-allow-origins=*");
				options.addArguments("--headless=new");
				ChromeDriver driver = new ChromeDriver(options);
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				driver.manage().window().maximize();

				try {


					driver.get(ConfigurationManager.configuration().medmantraUrl());

					try {
						driver.findElement(By.id("txtuname")).sendKeys(ConfigurationManager.configuration().medmantraUserName());
						driver.findElement(By.id("txtpwd")).sendKeys(ConfigurationManager.configuration().medmantraPassword());
						driver.findElement(By.id("btnsubmit")).click();
						driver.findElement(By.xpath("//a[text()='Patient Service']"));
						driver.get(ConfigurationManager.configuration().medmantraUrl()+"/Registration/Registration.aspx");
						WebElement title = driver.findElement(By.xpath("//select[contains(@id,'cmbTitle')]"));
						Select sel = new Select(title);
						sel.selectByVisibleText("Mr.");

						driver.findElement(By.xpath("//input[contains(@id,'txtFirstName')]")).sendKeys(FakerDataFactory.getFirstName());
						driver.findElement(By.xpath("//input[contains(@id,'txtLastName')]")).sendKeys(FakerDataFactory.getLastName());

						Thread.sleep(3000);
						WebElement gender = driver.findElement(By.xpath("//select[contains(@id,'cmbMaritalStatus')]"));
						Select sel1 = new Select(gender);
						sel1.selectByVisibleText("Married");

						driver.findElement(By.xpath("//input[contains(@id,'radAge')]")).click();
						driver.findElement(By.xpath("//input[contains(@id,'txtAgeYear')]")).sendKeys("55");
						driver.findElement(By.xpath("//input[contains(@id,'txtFathersName')]")).sendKeys("ss");

						Thread.sleep(2000);

						WebElement City = driver.findElement(By.xpath("//select[contains(@id,'cmbCityCurrent')]"));
						Select sel2 = new Select(City);
						sel2.selectByVisibleText("Chennai");
						Thread.sleep(3000);
						driver.findElement(By.xpath("//textarea[contains(@id,'Current')]")).click();
						Thread.sleep(1000);
						driver.findElement(By.xpath("//textarea[contains(@id,'Current')]")).sendKeys("test");
						driver.findElement(By.xpath("//input[contains(@id,'txtPin')]")).sendKeys("611103");
						driver.findElement(By.xpath("//input[contains(@id,'mobileCurrent_txtMobileNumber')]")).sendKeys(FakerDataFactory.getRandomPhoneNumber());
						Thread.sleep(1000);
						driver.findElement(By.xpath("//input[contains(@id,'btnRegisterMe')]")).click();

						Set<String> allWindows = driver.getWindowHandles();
						List<String> allhandles = new ArrayList<String>(allWindows);
						driver.switchTo().window(allhandles.get(1));

						Thread.sleep(3000);

						WebElement getRegNumber = driver.findElement(By.xpath("//span[text()='Registration no']/following::span"));
						System.out.println(getRegNumber.getText());
						String regNo = getRegNumber.getText();

						driver.findElement(By.xpath("//label[text()='CASH']/preceding::input[1]")).click();
						driver.findElement(By.id("btnAddCash")).click();
						Thread.sleep(1000);
						driver.findElement(By.id("btnSubmit")).click();


						Alert alert = driver.switchTo().alert();
						String text = alert.getText();
						System.out.println(text);
						if(alert.getText().contains("Transaction details  accepted"))
							redisManager.pushToTable("registrations", regNo);

					}catch (UnhandledAlertException e) {
						//driver.quit();
					}
					//driver.quit();


				} 

				catch (Exception e) {//e.printStackTrace();}
					//driver.quit();
				}
				driver.quit();

			}
		}

	}

}

