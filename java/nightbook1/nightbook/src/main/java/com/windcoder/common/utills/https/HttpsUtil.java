package com.windcoder.common.utills.https;


import org.json.JSONObject;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

public class HttpsUtil {

	/**
	 * GET/POST方式请求服务器(https协议)
	 * 
	 * @param requestUrl
	 * 				请求地址
	 * @param requestMethod
	 * 				请求类型（GET/POST）
	 * @param outputStr
	 * 			参数
	 * @return JSONObject
	 * 			
	 * */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr)
	  {
	    JSONObject jsonObject = new JSONObject();
	    
	    StringBuffer buffer = new StringBuffer();
	    try
	    {
	      TrustManager[] tm = { new MyX509TrustManager() };
	      
	      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	      
	      sslContext.init(null, tm, new SecureRandom());
	      
	   //   SSLSocketFactory ssf = sslContext.getSocketFactory();
	      
	      URL url = new URL(requestUrl);
	      
	      HttpsURLConnectionImpl httpUrlConn = (HttpsURLConnectionImpl)url
	        .openConnection();
	      
	      httpUrlConn.setDoOutput(true);
	      
	      httpUrlConn.setDoInput(true);
	      
	      httpUrlConn.setUseCaches(false);
	      
	      httpUrlConn.setRequestMethod(requestMethod);
	      if ("GET".equalsIgnoreCase(requestMethod)) {
	        httpUrlConn.connect();
	      }
	      if (outputStr != null)
	      {
	        OutputStream outputStream = httpUrlConn.getOutputStream();
	        
	        outputStream.write(outputStr.getBytes("UTF-8"));
	        
	        outputStream.close();
	      }
	      InputStream inputStream = httpUrlConn.getInputStream();
	      
	      InputStreamReader inputStreamReader = new InputStreamReader(
	        inputStream, "utf-8");
	      
	      BufferedReader bufferedReader = new BufferedReader(
	        inputStreamReader);
	      
	      String str = null;
	      while ((str = bufferedReader.readLine()) != null) {
	        buffer.append(str);
	      }
	      bufferedReader.close();
	      
	      inputStreamReader.close();
	      
	      inputStream.close();
	      
	      inputStream = null;
	      
	      httpUrlConn.disconnect();
	      
	      System.out.println("buffer:"+buffer);
	      jsonObject.put("data",buffer.toString());
	      System.out.println("jsonObject:"+jsonObject);
	    }
	    catch (ConnectException ce)
	    {
	      ce.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return jsonObject;
	  }
}
