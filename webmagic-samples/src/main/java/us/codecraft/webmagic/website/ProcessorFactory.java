/**
 * 
 */
package us.codecraft.webmagic.website;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.website.domain.WebSiteType;

/**
 * @author yang
 *
 */
public class ProcessorFactory {
	
	
	private static final Map<String, Class> WEBSITE_PAGE_PROCESSOR=new ConcurrentHashMap<String, Class>();
	static {
		WEBSITE_PAGE_PROCESSOR.put(WebSiteType.MANGA18fFX_COM, Manga18fxProcessor.class);
		WEBSITE_PAGE_PROCESSOR.put(WebSiteType.MANGA1ST_ONLINE, MangaFirstPageProcessor.class);
	}
	
	
	public static PageProcessor buildProcessor(String websiteType,String contentUrl2, String theStorePath2, String chapter2) {
		
//		if("manga18fx.com".equals(processorType)) {
//			return new Manga18fxProcessor(contentUrl2,theStorePath2,chapter2);
//		}
		if(!WEBSITE_PAGE_PROCESSOR.containsKey(websiteType)) {
			throw new RuntimeException("---can not find website type---"+websiteType);
		}
		Class<PageProcessor> processor_class=WEBSITE_PAGE_PROCESSOR.get(websiteType);
		try {
			Constructor<PageProcessor> cons=(Constructor<PageProcessor>) processor_class.getConstructors()[0];//only one 
			return cons.newInstance(contentUrl2,theStorePath2,chapter2);
			//(contentUrl2,theStorePath2,chapter2);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new RuntimeException("---Could not initialize website processor---"+websiteType);
		}
		
	}

}
