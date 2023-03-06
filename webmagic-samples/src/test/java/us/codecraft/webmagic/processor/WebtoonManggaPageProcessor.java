package us.codecraft.webmagic.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.selector.Html;


public class WebtoonManggaPageProcessor implements PageProcessor {
	
	 protected Logger log = LoggerFactory.getLogger(WebtoonManggaPageProcessor.class);

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
    		.addHeader(":authority", "www.webtoon.xyz")
    		.addHeader(":method", "GET")
    		.addHeader(":scheme", "https")
    		.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
            .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .addHeader("accept-encoding", "gzip, deflate, br")
            .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6,ko;q=0.5")
            .addHeader("cache-control", "no-cache")
            .addHeader("referer", "https://www.webtoon.xyz/")
            .addHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\"")
            .addHeader("sec-ch-ua-mobile", "?0")
            .addHeader("sec-ch-ua-platform", "\"Windows\"")
            .addHeader("sec-fetch-dest", "document")
            .addHeader("sec-fetch-mode", "navigate")
            .addHeader("sec-fetch-site", "same-origin")
            .addHeader("sec-fetch-user", "?1")
            .addHeader("upgrade-insecure-requests", "1")
            .setCharset("UTF-8");
    
   String contentUrl;
   String theStorePath;
    public WebtoonManggaPageProcessor(String contentUrl2, String theStorePath2) {
		this.contentUrl=contentUrl2;
		this.theStorePath=theStorePath2;
	}



	@Override
    public void process(Page page) {
    	
        
        //System.out.println("this is html of the-----"+page.getHtml());
    	String chapter=page.getHtml().xpath("//div[@class='reading-content']//input[@id='wp-manga-current-chap']/@value").toString();
    	List<String> divs= page.getHtml().xpath("//div[@class='page-break no-gaps']").all();
    	for(String content:divs) {
    		String id=new Html(content).xpath("//img/@id").toString();
    		String src=new Html(content).xpath("//img/@src").toString();
    		//System.out.println("---id and src---"+id+"         "+src);
    		
    		String filepath=makeFileStorePath(src.trim(),chapter);
    		downloadAndStore(id,src.trim(),filepath);
    	}
    	
    	System.out.println("---Load the site sucess-----");
        
    }

   

	@Override
    public Site getSite() {
        return site;
    }
    
   
	
    public static void main(String[] args) {
    	String theStorePath="D:/webmagic/boarding-diary/";
    	String contentUrl="https://www.webtoon.xyz/read/boarding-diary/chapter-41";
    	if(args!=null&&args.length>0) {
    		contentUrl=args[0];
    		theStorePath=args[1];
    		
    	}
    	Spider mainspider=Spider.create(new WebtoonManggaPageProcessor(contentUrl,theStorePath)).
                addUrl(contentUrl).
                //addPipeline(new FilePipeline("D:\\webmagic\\")).
                thread(5);
    	
    	mainspider.run();
    }
    
    private String makeFileStorePath(String url, String chapter) {
    	
    	 int indexof=url.lastIndexOf("/");
    	 
    	 String fullstorpath=new String(theStorePath);
     	
    	 if(indexof!=-1) {
    		 //theStorePath+=chapter+"/";
    		 String index_chapter=chapter;
			
			  if(chapter.indexOf("-")!=-1) {
			  index_chapter=chapter.substring(chapter.indexOf("-")+1,chapter.length());
			  }
			 
			  fullstorpath+=index_chapter+"-"+url.substring(indexof+1, url.length());
    	 }else {
    		 log.error("Can not fine / char in url {}",url);
    	 }
    	 
		return fullstorpath;
	}
    
    public void downloadAndStore(String id,String url,String filestorepath){
    	
    	
    	
    	 CloseableHttpClient httpclient = HttpClients.createDefault();  
    	 CloseableHttpResponse response=null;
         try {  
             // 创建httpget.    
             HttpGet httpget = new HttpGet(url);  
             RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000)
            		 .setConnectTimeout(30000).build();//设置请求和传输超时时间
             httpget.setConfig(requestConfig);
             httpget.addHeader(":authority", "cdn-manga.com");
             httpget.addHeader(":method", "GET");
             httpget.addHeader(":scheme", "https");
             httpget.addHeader("accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
             httpget.addHeader("accept-encoding", "gzip, deflate, br");
             httpget.addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6,ko;q=0.5");
             httpget.addHeader("referer", "https://manga1st.online/");
             httpget.addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"");
             httpget.addHeader("sec-ch-ua-mobile", "?0");
             httpget.addHeader("sec-ch-ua-platform", "\"Windows\"");
             httpget.addHeader("sec-fetch-dest", "image");
             httpget.addHeader("sec-fetch-mode", "no-cors");
             httpget.addHeader("sec-fetch-site", "cross-site");
             
             httpget.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36");
             // .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6,ko;q=0.5")
        
             System.out.println("executing request " + httpget.getURI());  
             // 执行get请求.    
             response = httpclient.execute(httpget);  
            
                 // 获取响应实体    
                 //HttpEntity entity = response.getEntity();  
                 // 打印响应状态    
                 System.out.println(response.getStatusLine());  
            	 InputStream ins=response.getEntity().getContent();
            	 
            	 fileStore(id,filestorepath,ins);
            	 //
            	 Thread.sleep(3000);
         } catch (ClientProtocolException e) {  
             e.printStackTrace();  
         } catch (Exception e) {  
             e.printStackTrace();  
         } finally {  
             // 关闭连接,释放资源    
             try {  
            	 
            	 response.close();  
                 //httpclient.close();  
             } catch (IOException e) {  
                 e.printStackTrace();  
             }  
         }  
    	
    	
    }
    
    public File createDictonaryAndfile(String path) {
		File thefile=new File(path);
		if(!thefile.exists()) {
			try {
				loopcreate(thefile);
				thefile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return thefile;
	}
	public void loopcreate(File file) {
		
		if(file.getParentFile()!=null) {
			File parent=file.getParentFile();
			if(!parent.exists()) {
				parent.mkdir();
				loopcreate(parent);
			}
		}
		
	}
    
    private String fileStore(String id,String theStorePath,InputStream ins) {
		
		FileOutputStream outstream=null;
		FileChannel filechannel=null;
		
		try {
			
			      
								
			
			
			System.out.println("---storethe file---"+theStorePath);
			File thefile=createDictonaryAndfile(theStorePath);
			outstream = new FileOutputStream(thefile);
					//new FileOutputStream(theStorePath);

							filechannel = outstream.getChannel();
							int index = 0;
							for (;;) {
								byte[] content = new byte[8192];
								int res = ins.read(content);// 从流中读取固定长度的byte[]
								if (res == -1) {
									break;
								}

								ByteBuffer contentBuffer = ByteBuffer.wrap(content);							
								filechannel.write(contentBuffer, index);// 一段一段写入
								index = index + res;

							}

							
					
						
			
		} catch (IOException e) {
		   e.printStackTrace();
		}finally{
			
			try {
				if (null != outstream) {
					outstream.close();
				}
				if (null != filechannel) {
					filechannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return theStorePath;
	}
}
