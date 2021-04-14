package us.codecraft.webmagic.script.selenium;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午12:27 <br>
 */
public class SeleniumWindowsChromeTest {

    //@Ignore("need chrome driver")
	  @Test
	    public void testExcuteScript() {
	      
	    	System.getProperties().setProperty("webdriver.chrome.driver", "D:\\software\\chrome\\chromedriver.exe");
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments("--headless");
	        
	        options.setCapability("chrome.switches", Arrays.asList("--user-data-dir=D:\\software\\chrome\\temp\\chrome"));
	        		 //options.addExtensions(new File("/path/to/extension.crx"))
	        //options.setBinary("D:\\software\\chrome\\chromedriver.exe");
	        ChromeDriver webDriver = new ChromeDriver(options);
	        
	        webDriver.get("http://www.piggymanga.com/manga/fitness/fitness/chapter-14");
			/*
			 * String script=""; try { script = CrawlHelper.loadJs("js/manga.js"); } catch
			 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			 //String func="(function(){ return document.getElementsByClassName('reading-content'); });";
	        //Object content=webDriver.executeScript("setTimeout(function() { return document.getElementsByClassName('reading-content'); }, 10000);", 3);
	        
	    
	        //
	      
	        //List<WebElement> elist = webDriver.findElementsByClassName("page-break no-gaps");
	        List<WebElement> elist = webDriver.findElements(By.xpath("//div[@class='page-break no-gaps']//img"));
	        for(WebElement we:elist) {
	        
	        	 System.out.println("---the content is---"+we.getAttribute("src"));
	        }
	       
	        
	        
	        webDriver.close();
	    }
}
