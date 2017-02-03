package logger1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TestMain {
	public static void main(String args[]){
		String clockType = args[0];
	    Logger log = new Logger("myLog1", clockType);
	    while (true){
	        InputStreamReader isrd = new InputStreamReader(System.in);
	        BufferedReader br = new BufferedReader(isrd);
	        
	        try {
	            String input = br.readLine();
	            if (input.equals("p")) {
	                log.print();
	            } else {
	                continue;
	            }
		    } catch(Exception e) {
	            e.printStackTrace();
	        } 
	        
	    }
	    
	}


	
	
	
}
