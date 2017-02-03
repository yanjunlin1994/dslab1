package logger1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TestMain {
	public static void main(String args[]){
		String type = args[0];
	    Logger log = new Logger(type);
	    while (true){
	        InputStreamReader isrd = new InputStreamReader(System.in);
	        BufferedReader br = new BufferedReader(isrd);
	        String input = null;
			try {
				input = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (input.equals("p")){
	        	log.print();
	        }
	    }
	}
	private class LogListen implements Runnable{
		Queue<TimeStampedMessage> queue = new LinkedList<TimeStampedMessage>();
		
	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	
	
}
