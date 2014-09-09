package com.mymos.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;
import android.util.Log;

public class FileOperation {
	private String pathName="/sdcard/MYMOS/";
	
	public int buildDir(){
	    String sdStatus = Environment.getExternalStorageState();  
	    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
	        Log.i("TestFile", "SD card is not avaiable/writeable right now.");  
	        return -1;  
	    } 
	    File path = new File(pathName);
	    if(!path.exists()){
	    	path.mkdir();
	    }
	    return 1;
	}
	
	public int buildDir(String folderName){
	    String sdStatus = Environment.getExternalStorageState();  
	    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
	        Log.i("TestFile", "SD card is not avaiable/writeable right now.");  
	        return -1;  
	    } 
	    File path = new File(pathName + folderName + "/");
	    if(!path.exists()){
	    	path.mkdir();
	    	return 0;
	    }
	    return 1;
	}
	
	//获得filePath目录下的所有文件夹名
	public ArrayList<String> getFolderNameListFrom(String filePath){
        ArrayList<String> fileNameList=new ArrayList<String>();
        File path = new File(pathName + filePath);
        //如果是文件夹的话
        if(path.isDirectory()){
        	//什么也不做
        	String[] nameList = path.list();
        	for(String n: nameList){
        		File tempFile = new File(pathName + filePath+n);
        		if(tempFile.isDirectory())
        		{
        			fileNameList.add(n);
        		}
        	}
        	return fileNameList;
        }
        return null;
    }
	
	//获得filePath目录下的所有文件名
	public ArrayList<String> getFileNameListFrom(String filePath){
        ArrayList<String> fileNameList=new ArrayList<String>();
        File path = new File(pathName + filePath);
        //如果是文件夹的话
        if(path.isDirectory()){
        	//什么也不做
        	String[] nameList = path.list();
        	for(String n: nameList){
        		File tempFile = new File(pathName + filePath+n);
        		if(tempFile.isFile())
        		{
        			fileNameList.add(n);
        		}
        	}
        	return fileNameList;
        }
        //如果是文件的话直接加入
        else{
            Log.i("absolute path", path.getAbsolutePath());
            //进行文件的处理
            String fileAbsolutePath = path.getAbsolutePath();
            //文件名
            String fileName = fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf("/")+1);
            //添加
            fileNameList.add(fileName);
        }
        return fileNameList;
    }
    

	//写文件到sd卡
	public int writeFileToSD(String fileName,String write_str) {  
	    String sdStatus = Environment.getExternalStorageState();  
	    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
	        Log.i("TestFile", "SD card is not avaiable/writeable right now.");  
	        return -1;  
	    }  
	    try {    
	        File path = new File(pathName);  
	        File file = new File(pathName + fileName);  
	        if( !path.exists()) {  
	            Log.i("TestFile", "Create the path:" + pathName);  
	            path.mkdir();  
	        }  
	        if( !file.exists()) {  
	            Log.i("TestFile", "Create the file:" + fileName);  
	            file.createNewFile(); 
	        }  
	        FileOutputStream stream = new FileOutputStream(file);  
	        String s = write_str;  
	        byte[] buf = s.getBytes();
	        stream.write(buf);            
	        stream.close(); 
	        return 0;
	          
	    } catch(Exception e) {  
	        Log.i("TestFile", "Error on writeFilToSD.");  
	        e.printStackTrace();  
	        return -2;
	    }  
	}  
	
    //读取SD卡文件
	public String readSDFile(String fileName) {
		try{
		   FileInputStream fileIS = new FileInputStream(pathName+fileName);
           BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
           String fileStr = new String();
           String readString = new String();
            //just reading each line and pass it on the debugger
           while((readString = buf.readLine())!= null){
                Log.d("line: ", readString);
                fileStr += readString;
             }
            fileIS.close();
            return fileStr;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    //删除SD卡文件
    public boolean deleteSDFile(String fileName) { 

        File file = new File(pathName + fileName); 

        if (file == null || !file.exists()) 

            return false; 

        return file.delete(); 

    } 
    
	//重命名文件
	public int renameFile(String oldFileName,String newFileName) {  
	    String sdStatus = Environment.getExternalStorageState();  
	    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {  
	        Log.i("TestFile", "SD card is not avaiable/writeable right now.");  
	        return -1;  
	    }  
	    try {    
	        File path = new File(pathName + oldFileName);  
	        if( !path.exists()) {  
	            return -1;  
	        } 
	        File file = new File(pathName + newFileName);
	        path.renameTo(file);

	        return 0;
	          
	    } catch(Exception e) {  
	        Log.i("TestFile", "Error on writeFilToSD.");  
	        e.printStackTrace();  
	        return -2;
	    }  
	}
	
    //读取SD卡文件
	public byte[] readFile(String fileName) {
		try{
			File file = new File(fileName);
		    FileInputStream fileIS = new FileInputStream(file);
		    int length = fileIS.available();
		    byte[] readInfor = new byte[length];
		    fileIS.read(readInfor);
            fileIS.close();
            return readInfor;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
