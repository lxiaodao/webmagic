package us.codecraft.webmagic.website;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class WebSiteProcessor {

	public static void main(String[] args) {
		
		String theStorePath="D:/download/Should-I-Study-at-Noryangjin/";
    	String contentUrl="https://manga18fx.com/manga/should-i-study-at-noryangjin-01/chapter-65";
    	
		int number_begin=65;
    	int loop_number=10;
    	String websiteType="manga18fx.com";

		int indexof=contentUrl.lastIndexOf("/");
		String frontPart=contentUrl.substring(0, indexof+1);//content url front part
		
    	
    	
    	
    	
    	if(args!=null&&args.length>0) {
    		contentUrl=args[0];
    		theStorePath=args[1];
    		indexof=contentUrl.lastIndexOf("/");
    		frontPart=contentUrl.substring(0, indexof+1);//content url front part
    	}  	
		
		
    	if(args!=null&&args.length>2) {
    		number_begin=Integer.valueOf(args[2]);//begin chapter number    		
    		loop_number=Integer.valueOf(args[3]);
    		websiteType=args[4];
    	}
    	
    	
    	//at least loop 1
    	
    	for(int i=0;i<loop_number+1;i++) {
    		
    		
        PageProcessor processor=ProcessorFactory.buildProcessor(websiteType, contentUrl, theStorePath, number_begin+"");
    	Spider mainspider=Spider.create(processor).
                addUrl(contentUrl).               
                thread(5);
    	       mainspider.run();
    	       
    	       if(loop_number!=0) {
    	    	  number_begin=number_begin+1;
       			  contentUrl=frontPart+"chapter-"+number_begin;
       			System.out.println("---will scraw url---"+contentUrl);
       		}
    	}
    	System.out.println("---complete---");

	}

}
