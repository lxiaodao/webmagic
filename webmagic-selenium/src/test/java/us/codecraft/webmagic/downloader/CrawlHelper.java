/**
 * 
 */
package us.codecraft.webmagic.downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author yang
 *
 */
public class CrawlHelper {
	 protected static Logger log = LoggerFactory.getLogger(CrawlHelper.class);
	
	 public static String loadJs(String scriptName) throws IOException{
			
	     InputStream is = CrawlHelper.class.getClassLoader().getResourceAsStream(scriptName);
	     
	     
	     BufferedReader br = new BufferedReader(new InputStreamReader(is));
	     StringBuilder build=new StringBuilder();
	     for (String line = br.readLine(); line != null; line = br.readLine()) {
	    	 build.append(line);           
        }
	     return build.toString().trim();   
	 
	}
	 
	 public static String makeFileStorePath(String url, String chapter) {
	    	String theStorePath="D:/webmagic/fitness/";
	    	 int indexof=url.lastIndexOf("/");
	     	
	    	 if(indexof!=-1) {
	    		 theStorePath+=chapter+"/";
	    		 theStorePath+=chapter+"-"+url.substring(indexof+1, url.length());
	    	 }else {
	    		 log.error("Can not fine / char in url {}",url);
	    	 }
	    	 
			return theStorePath;
		}
	    
	    public static void downloadAndStore(String id,String url,String filestorepath){
	    	
	    	
	    	
	    	 CloseableHttpClient httpclient = HttpClients.createDefault();  
	    	 CloseableHttpResponse response=null;
	         try {  
	             // 创建httpget.    
	             HttpGet httpget = new HttpGet(url);  
	             RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000)
	            		 .setConnectTimeout(6000).build();//设置请求和传输超时时间
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
	    
	    public static File createDictonaryAndfile(String path) {
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
		public static void loopcreate(File file) {
			
			if(file.getParentFile()!=null) {
				File parent=file.getParentFile();
				if(!parent.exists()) {
					parent.mkdir();
					loopcreate(parent);
				}
			}
			
		}
	    
	    public static String fileStore(String id,String theStorePath,InputStream ins) {
			
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
