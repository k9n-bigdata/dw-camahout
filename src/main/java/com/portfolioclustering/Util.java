package com.portfolioclustering;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mahout.math.Vector;



public class Util {

	public static void WriteStringVectorToFile(String fileName, Map<String,Vector> contentMap) {
		 
		File file = new File(fileName);
		
		try {
			
			FileOutputStream fop = new FileOutputStream(file,true);
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			Iterator<Entry<String, Vector>> it = contentMap.entrySet().iterator();
			
		    while (it.hasNext()) {
		        Entry<String, Vector> pairs = it.next();
		        
		        String key = pairs.getKey().toString();
			    String value = ((Vector)pairs.getValue()).asFormatString();
		        String content = key + " : " + value + "\n";
		        
				// get the content in bytes
		        byte[] contentInBytes = content.getBytes();

				fop.write(contentInBytes);
		    }
		
			fop.flush();
			fop.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void WriteStringIntegerToFile(String fileName, Map<String,Integer> contentMap) {
		 
		File file = new File(fileName);
		
		try {
			
			FileOutputStream fop = new FileOutputStream(file,true);
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			Iterator<Entry<String, Integer>> it = contentMap.entrySet().iterator();
			
		    while (it.hasNext()) {
		        Entry<String, Integer> pairs = it.next();
		        
		        String key = pairs.getKey().toString();
			    String value = pairs.getValue().toString();
		        String content = key + " : " + value + "\n";
		        
				// get the content in bytes
		        byte[] contentInBytes = content.getBytes();

				fop.write(contentInBytes);
		    }
		
			fop.flush();
			fop.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void WriteListToFile(String fileName,List<String> content) {
		 
		File file = new File(fileName);
		
		try {
			
			FileOutputStream fop = new FileOutputStream(file,true);
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			Iterator<String> it = content.iterator();
			
		    while (it.hasNext()) {
		    	
		        String data = it.next();
		        data = data + "\n";
		        
				// get the content in bytes
		        byte[] contentInBytes = data.getBytes();

				fop.write(contentInBytes);
		    }
		
			fop.flush();
			fop.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 
}


