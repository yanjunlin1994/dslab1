import java.util.Queue;
/**
 * Receive class to receive message from receive queue.
 * @author Team 3
 *
 */
public class Receive implements Runnable{
    private Queue<Message> receiveQueue;
    private ClockService clockservice;
    
    public Receive(Queue<Message> receiveQ,ClockService cs) {
        this.receiveQueue = receiveQ;
        this.clockservice = cs;
    }
    @SuppressWarnings("resource")
    @Override
    public void run(){
        try {
            while (true) {
                try {
                    Message receMes = receive();
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        } catch(Exception e) {
            e.printStackTrace();
        } 
    }  
    public synchronized Message receive(){
        Message msg = null;
        if (!receiveQueue.isEmpty()){
            System.out.println("[Receive] polling something from receive queue");
            msg = receiveQueue.poll();
            clockservice.Synchronize((TimeStampedMessage)msg);
            System.out.println("[Receive] receive from queue" + msg);
        }
        return msg;
    }
}
