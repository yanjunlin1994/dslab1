package logger1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Queue;

public class LogListenFor implements Runnable{
    /** the Object Input Stream. */
    private ObjectInputStream ois;
    private Queue<TimeStampedMessage> listenQueue;
    private String senderName;
    public LogListenFor(ObjectInputStream oistream, Queue<TimeStampedMessage> listenQ) {
        this.ois = oistream;
        this.listenQueue = listenQ;
    }
    @Override
    public synchronized void run() {
        while(true) {
            try {
                TimeStampedMessage newMes = (TimeStampedMessage)ois.readObject();
                senderName = newMes.get_source();
                String checkResult = checkReceiveRule(newMes,myConfig);
                if (checkResult != null) {
                    if (checkResult.equals("drop")) {
                        continue;
                    } else if (checkResult.equals("delay")) {
                        listenDelayQueue.offer(newMes);   
                    } else if (checkResult.equals("duplicate")) {
                        listenQueue.offer(newMes);
                        listenQueue.offer((TimeStampedMessage)newMes.clone());
                        while (!listenDelayQueue.isEmpty()){
                            TimeStampedMessage msg = listenDelayQueue.poll();
                            listenQueue.offer(msg);
                        }
                        
                    } 
                    else {
                        System.out.println("[ATTENTION] abnormal checkResult" + checkResult); 
                    }
                }
                else {
                    listenQueue.offer(newMes);
                    while (!listenDelayQueue.isEmpty()){
                        TimeStampedMessage msg = listenDelayQueue.poll();
                        listenQueue.offer(msg);
                    }
               
                }
            } catch (IOException | ClassNotFoundException e) {
                if (ois != null) {
                    try {
                        System.out.println("close the object input stream and the socket");
                        ois.close(); 
                        myConfig.OSMap.remove(senderName);
                        return;
                    } catch (Exception nestedE) {
                        nestedE.printStackTrace();   
                    }
                } else {
                    e.printStackTrace();
                } 
            } 
        }
    }

}
