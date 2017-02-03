package logger1;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
public class Logger {
    String myname;
	List<TimeStampedMessage> messageList;
	Queue<TimeStampedMessage> messageQueue;
	String clockType;
	public Logger(String name, String ct){
	    this.myname = name;
		this.messageList = new ArrayList<TimeStampedMessage>();
		this.messageQueue = new ArrayDeque<TimeStampedMessage>();
		this.clockType = ct;
		Thread listner = new Thread(new LogListener(myname, messageQueue));
		listner.start();
	}
	public void runNow() {
	    while (true){
            InputStreamReader isrd = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isrd);
            try {
                String input = br.readLine();
                if (input.equals("p")) {
                    this.print();
                } else {
                    continue;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }  
        }  
	}
	public synchronized void print(){
		while (!this.messageQueue.isEmpty()){	
			messageList.add(this.messageQueue.poll());
		}
		if (messageList.size() > 0) {
		    Collections.sort(messageList, new CompareByTimeStamp());
		}
	}
}
