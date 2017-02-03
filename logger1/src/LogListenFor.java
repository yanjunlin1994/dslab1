
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
                System.out.println("[New log file add from: " + senderName + " ]");
                System.out.println(newMes.toString()); 
//                listenQueue.offer(newMes);
            } catch (IOException | ClassNotFoundException e) {
                
                    e.printStackTrace();
                
            } 
        }
    }

}
