package com.mymos.Util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import android.util.Log;

public class RequestAndResponse {
	public static String serverAdd = "http://192.168.23.1:8080/MYMOS_SERVER/";
	private HttpClient httpClient;
	
	public String access(String addr,String sendInfor) throws HttpException, IOException{
		httpClient = new HttpClient();
		
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); 
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY); 
		  
		  
		String address = serverAdd + addr;
		Log.i("address",address);
		PostMethod post = new PostMethod(address);
		post.setParameter("sendInfor", sendInfor);
		
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		
		
		int status = httpClient.executeMethod(post);		
		if(status != HttpStatus.SC_OK){
			Log.i("httpStatus","wrong");
			return "wrong";
		}

		String result = post.getResponseBodyAsString();
		//String result = post.getResponseBodyAsString();
		
		Log.i("result==---",result);
		
		post.releaseConnection();
		
		return result;
	}

}
