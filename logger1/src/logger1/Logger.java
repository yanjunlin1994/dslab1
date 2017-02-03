package logger1;
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
	}
	public void print(){
		while (!this.messageQueue.isEmpty()){
			
			messageList.add(this.messageQueue.poll());
		}
		if (messageList.size() > 0) {
		    Collections.sort(messageList, new CompareByTimeStamp());
		}
	}
}
