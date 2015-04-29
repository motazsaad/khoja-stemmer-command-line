/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myfileutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;

/**
 *
 * @author msaad
 */
public class myFileHandler {
    public String myReadFileUtf8(String fileName){
        String str = null;
	try {
		File fileDir = new File(fileName);
 
		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(fileDir), "UTF8"));
 
		
 
		while ((str = in.readLine()) != null) {
		    System.out.println(str);
		}
 
                in.close();
	    } 
	    catch (UnsupportedEncodingException e) 
	    {
			System.out.println(e.getMessage());
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
	    }
        
        return str;
	}
    
    
    public String readEntilerFile(String fn) throws FileNotFoundException{
        String content = new Scanner(new File(fn), "UTF-8").useDelimiter("\\A").next();
        return content;
    }
    
    
    public void writeFileUTF(String sring, String fn) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), "UTF-8"));
        try {
            out.write(sring);
        } finally {
            out.close();
        }
    }

}
