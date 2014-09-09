package com.mymos.Util;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class PostImage {
	 public void postImg(String addr,String imgUrl) {
		  String targetURL = null; //-- 指定URL
		  File targetFile = null; //-- 指定上传文件
		  targetFile = new File(imgUrl);
		  targetURL = RequestAndResponse.serverAdd + addr;
		  PostMethod filePost = new PostMethod(targetURL);
		  try {
		   // 通过以下方法可以模拟页面参数提交
		   // filePost.setParameter("name", "中文");
		   // filePost.setParameter("pass", "1234");
		   Part[] parts = { new FilePart(targetFile.getName(), targetFile) };
		   filePost.setRequestEntity(new MultipartRequestEntity(parts,
		     filePost.getParams()));
		   HttpClient client = new HttpClient();
		   client.getHttpConnectionManager().getParams().setConnectionTimeout(
		     5000);
		   int status = client.executeMethod(filePost);
		   if (status == HttpStatus.SC_OK) {
		    System.out.println("上传成功");
		    // 上传成功
		   } else {
		    System.out.println("上传失败");
		    // 上传失败
		   }
		  } catch (Exception ex) {
		   ex.printStackTrace();
		  } finally {
		   filePost.releaseConnection();
		  }
		 }
}
