package testcase;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SendThanksMessage {
	static WebDriver driver;
	static String baseURL;
	static int countSent =0;
	static int countNotSent =0;
	static JavascriptExecutor js ;
	static List<WebElement> messages ;
	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);  
		System.out.print("Enter your LinkedIn email ID ");
		System.out.print( "\n" );
		String userEmail= sc.nextLine();  
		System.out.print("Enter your LinkedIn password ");  
		System.out.print( "\n" );
		String  password= sc.nextLine(); 
		initializeDriver();
		sendThanksToCongratulationsMessage(userEmail,password);
	}
	public static void initializeDriver() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		baseURL = "https://www.linkedin.com/";
		driver.manage().window().maximize();
		//driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get(baseURL);
		System.out.println("driver initialised");
	}
	public static void sendThanksToCongratulationsMessage(String userEmail, String password) {
		driver.findElement(By.xpath("//input[@autocomplete='username']")).sendKeys(userEmail);
		driver.findElement(By.xpath("//input[@autocomplete='current-password']")).sendKeys(password);
		driver.findElement(By.xpath("//button[@class='sign-in-form__submit-button']")).click();
		driver.findElement(By.xpath("//a[@href='/messaging/']")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		messages = driver.findElements(By.xpath("//span[contains(@class,'msg-conversation-card__message-snippet-body')]"));
		System.out.println(messages.size());
		for(int i=0;i<messages.size();i++) {
			if(messages.get(i).getText().contains("Congrats") && !messages.get(i).getText().contains("You:")) {
				countSent++;
				messages.get(i).findElement(By.xpath("./ancestor::div[@class='msg-conversation-card__content--selectable']")).click();
				driver.findElement(By.xpath("//div[@role='textbox']")).sendKeys("Thank you!!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				driver.findElement(By.xpath("//button[@type='submit']")).click();
				//
				if(countSent==5) {
					countSent=0;
					 js = (JavascriptExecutor) driver;
					 js.executeScript("arguments[0].scrollBy(0,1000);",driver.findElement(By.xpath("//ul[@class='msg-conversations-container__conversations-list list-style-none ember-view']")));
					 messages = driver.findElements(By.xpath("//span[contains(@class,'msg-conversation-card__message-snippet-body')]"));
				}
					
				
			}else {
				countNotSent++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				js = (JavascriptExecutor) driver;
				 js.executeScript("arguments[0].scrollBy(0,1000);",driver.findElement(By.xpath("//ul[@class='msg-conversations-container__conversations-list list-style-none ember-view']")));
				 messages = driver.findElements(By.xpath("//span[contains(@class,'msg-conversation-card__message-snippet-body')]"));
			}
			if(countNotSent>=50) {
				return;
			}
		};
		
		}
}
