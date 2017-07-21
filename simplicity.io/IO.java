//DavidM's Simplicity
package simplicity.io;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IO{
	
	public static String readFrom(String filePath){
		try{
			FileReader reader=new FileReader(filePath);
			BufferedReader textReader=new BufferedReader(reader);
			String x=textReader.readLine();
			textReader.close();
			return x;
		}catch (Exception e){
			System.out.println("Error: "+e);
			return null;
		}
	}
	
	public static boolean writeTo(String content,String filePath){
		try{
			PrintWriter writer=new PrintWriter(filePath,"UTF-8");
		    writer.println(content);
		    writer.close();
		} catch (IOException e) {
		   return false;
		}
		return true;
	}
	
	public static boolean writeTo(String content,String filePath,boolean append){
		try{
			PrintWriter writer=new PrintWriter(new FileWriter(filePath,append));
		    writer.println(content);
		    writer.close();
		} catch (IOException e) {
		   return false;
		}
		return true;
	}
}
