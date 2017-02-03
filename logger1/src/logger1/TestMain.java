package logger1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Queue;

public class TestMain {
	public static void main(String args[]){
	    Logger log = new Logger();
	    while (true){
	        InputStreamReader isrd = new InputStreamReader(System.in);
	        BufferedReader br = new BufferedReader(isrd);
	        String input = br.readLine();
	        try {
	        if (input.equals("p")){
	        	log.print();
	        }
	    } catch(Exception e) {
            e.printStackTrace();
        } 
	        
	    }
	    
	}
	
	
	
}
