package us.codecraft.webmagic.downloader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午12:27 <br>
 */
public class ScriptSeleniumWindowsChromeTest {

    //@Ignore("need chrome driver")
    @Test
    public void testExcuteScript() {
      
    	System.getProperties().setProperty("webdriver.chrome.driver", "D:\\software\\chrome\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        
        options.setCapability("chrome.switches", Arrays.asList("--user-data-dir=D:\\software\\chrome\\temp\\chrome"));
    
        ChromeDriver webDriver = new ChromeDriver(options);
        
        webDriver.get("http://www.piggymanga.com/manga/fitness/fitness/chapter-20");
		
		  String script=""; 
		  try { 
			  script = CrawlHelper.loadJs("js/manga.js"); 
			  } catch(IOException e) { 
			  // TODO Auto-generated catch block 
				  e.printStackTrace(); 
		}
		  
		 
		 //String func="(function(){ return document.getElementsByClassName('reading-content'); });";
        //return document.domain;
		  //Boolean
		  //Long
		 // String
		 // List
		 // WebElement.
        //Object content=webDriver.executeScript("return document.getElementsByClassName('reading-content');");
		  System.out.println("------the script should be excute-------------"+script);
        Object content=webDriver.executeScript(script);
        
        System.out.println("------return content-------------"+content.toString());
       
        
        
        webDriver.close();
    }
}
