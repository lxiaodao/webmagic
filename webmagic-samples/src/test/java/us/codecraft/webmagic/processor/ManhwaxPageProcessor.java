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


public class ManhwaxPageProcessor implements PageProcessor {
	
	 protected Logger log = LoggerFactory.getLogger(ManhwaxPageProcessor.class);

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
    		.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6,ko;q=0.5")
            .setCharset("UTF-8");
    
   String contentUrl;
   String theStorePath;
   int current_chapter=1;
    public ManhwaxPageProcessor(int chapter, String contentUrl2, String theStorePath2) {
		this.contentUrl=contentUrl2;
		this.theStorePath=theStorePath2;
		this.current_chapter=chapter;
	}



	@Override
    public void process(Page page) {
    	
        
        //System.out.println("this is html of the-----"+page.getHtml());
    	
    	List<String> imges= page.getHtml().xpath("//div[@id='readerarea']").xpath("//img").all();
    	for(String content:imges) {
    		String index=new Html(content).xpath("@data-index").toString();
    		String src=new Html(content).xpath("@src").toString();
    		//System.out.println("---id and src---"+id+"         "+src);
    		
    		String filepath=makeFileStorePath(index,this.current_chapter+"");
    		downloadAndStore(index,src.trim(),filepath);
    	}
    	
    	System.out.println("---Load the page sucess-----"+this.current_chapter);
        
    }

   

	@Override
    public Site getSite() {
        return site;
    }
    
   
	
    public static void main(String[] args) {
    	String theStorePath="D:/webmagic/boarding-diary/";
    	String contentUrl="https://manhwax.com/boarding-diary-chapter-39-english";
    	
    	int loop_number=0;
    	int loop_begin=0;// start number
    	if(args!=null&&args.length>0) {
    		contentUrl=args[0];
    		theStorePath=args[1];
    		
    	}
    	
		String part1="https://manhwax.com/boarding-diary-chapter-";
    	if(args!=null&&args.length>2) {
    		loop_begin=Integer.valueOf(args[2]);
    		loop_number=Integer.valueOf(args[3]);
    		
    	}
    	//at least loop 1
    	
    	for(int i=0;i<loop_number+1;i++) {
    		
    		
       
    	Spider mainspider=Spider.create(new ManhwaxPageProcessor(loop_begin,contentUrl,theStorePath)).
                addUrl(contentUrl).
                //addPipeline(new FilePipeline("D:\\webmagic\\")).
                thread(5);
    	       mainspider.run();
    	       
    	       if(loop_number!=0) {
       			loop_begin=loop_begin+1;
       			contentUrl=part1+loop_begin+"-english";
       		}
    	}
    	System.out.println("---complete---");
    	
    	
    }
    
    private String makeFileStorePath(String index, String chapter) {
    	
    	
    	 
    	 String fullstorpath=new String(theStorePath);
    	 fullstorpath+=fullstorpath+chapter+"-"+index;
  
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
             httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
             // .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6,ko;q=0.5")
             httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6,ko;q=0.5");
        
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
                 httpclient.close();  
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
