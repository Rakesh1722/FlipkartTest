package Rakesh;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Test.AbstractComponents;

public class StandAloneTest extends AbstractComponents {

	public StandAloneTest(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException {
		String productName = "DELL Intel Core i3 12th Gen 1215U - (8 GB/512 GB SSD/Windows 11 Home) New Inspiron 15 Laptop Thin and ...";

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Step 1: Open Flipkart Website
		driver.get("https://www.flipkart.com");
		waitForPageLoad(driver, Duration.ofSeconds(30)); // Verify whether homepage loads successfully

		// Step 2: Search and Add to Cart
		driver.findElement(By.cssSelector("input[name$='q']")).sendKeys("laptop");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type$='submit']")));
		driver.findElement(By.cssSelector("button[type$='submit']")).click();

		// Click on One Product
		List<WebElement> products = driver.findElements(By.cssSelector("._2kHMtA"));
		WebElement prod = products.stream()
				.filter(product -> product.findElement(By.cssSelector("._4rR01T")).getText().equals(productName))
				.findFirst().orElse(null);
		prod.click();
		Thread.sleep(1000);

		// Passing driver object instance to newly opened WebPage
		String winHandleBefore = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		driver.findElement(By.xpath("//button[normalize-space()='Add to cart']")).click(); // Add to Cart

		// Step 3: Proceed to Checkout:

		// Add to cart Button
		driver.findElement(By.cssSelector("div[class='YUhWwv'] a")).click();

		// Verify that the correct item is in the cart.
		String OriginalProduct = productName.substring(0, 100);

		List<WebElement> cartProducts = driver.findElements(By.cssSelector("a[class='_2Kn22P gBNbID']"));
		for (WebElement item : cartProducts) {
			Boolean match = item.getText().substring(0, 100).equalsIgnoreCase(OriginalProduct);
			Assert.assertTrue(match);
			break;
		}

		// Proceed to Checkout
		driver.findElement(
				By.xpath("//button[contains(@class, '_2KpZ6l _2ObVJD _3AWRsL')]//*[contains(., 'Place Order')]"))
				.click();

		// Step 4: User Authentication
		driver.findElement(By.cssSelector(".IiD88i.GtCYSF")).click();
		driver.findElement(By.cssSelector("input[class='_2IX_2- _17N0em']")).sendKeys("6303294300");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		driver.findElement(By.cssSelector("input[class='_2IX_2- _3mctLh _17N0em']")).sendKeys("");

		// close Functionality
		// driver.close();
	}

}
