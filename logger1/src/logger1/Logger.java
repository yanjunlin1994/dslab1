package logger1;
import java.util.*;
public class Logger {
	List messageList;
	String clockType;
	public Logger(String ct){
		messageList = new ArrayList<TimeStampedMessage>();
		clockType = ct;
	}
	public void print(Queue q){
		while (!q.isEmpty()){
			
			messageList.add(q.poll());
		}
		Collections.sort(messageList);
	
		Collections.sort(messageList,new Comparator<TimeStampedMessage>() {
			public int compare(TimeStampedMessage msg1, TimeStampedMessage msg2){
				if (!(msg1.getType().equals(msg2.getType()))) {
				    throw new RuntimeException("two message type different");
				}
				if (msg1.getType().equals("vector")) {
					int[] m1 = msg1.getTimeStamps();
					int[] m2 = msg2.getTimeStamps();
					int flag = 0;
					for (int i = 0; i < m1.length; i++){
						if(flag == 1 && m1[i] < m2[i]) {
						    return 0;
						}
						if(flag == -1 && m1[i] > m2[i]) {
						    return 0;
						}
						
						if(flag == 0 && m1[i] < m2[i]) {
							flag = -1;
						} else if (flag == 0 && m1[i] > m2[i]){
							flag = 1;
						}
					}
					return flag;
				}
				else {
					int m1 = msg1.getTimeStamp();
					int m2 = msg2.getTimeStamp();
					return m1 - m2;
				}
				
			}
		});
	
		for (int i = 0; i<)
	}
}
