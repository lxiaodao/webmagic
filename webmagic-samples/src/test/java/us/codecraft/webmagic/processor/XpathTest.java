/**
 * 
 */
package us.codecraft.webmagic.processor;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import us.codecraft.webmagic.selector.Html;

/**
 * @author yang
 *
 */
public class XpathTest {
	 
	@Test
	 public void xpath() {
	    	String content="<div class=\"page-break no-gaps\"> \r\n" + 
	    			" <img id=\"image-0\" src=\"			\r\n" + 
	    			"			http://www.piggymanga.com/wp-content/uploads/WP-manga/data/manga_5ee90fc236232/523be46a43c50a4aee9ba9a4d4c0d2af/ia_10001.jpg\" class=\"wp-manga-chapter-img\"> \r\n" + 
	    			"</div>";
	    	String id=new Html(content).xpath("//img/@id").toString();
			String src=new Html(content).xpath("//img/@src").toString();
			System.out.println("---id and src---"+id+"         "+src);
			assertTrue(id.equals("image-0"));
	    }
	
	public void test_get_index() {
		
	}
	

}
