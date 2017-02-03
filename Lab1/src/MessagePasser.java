import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
/**
 * MessagePasser who in charge of sending and receiving message.
 * @author Team 3
 */
public class MessagePasser {
	/** MessagePasser's configuration object. */
	private Configuration myConfig;
	/** MessagePasser's local name. */
	private String myName;
	/** MessagePasser's send queue. */
	private Queue<TimeStampedMessage> sendDelayQueue;
	/** MessagePasser's receive queue. */
	private Queue<TimeStampedMessage> receiveQueue;
	/** MessagePasser's receive delay queue. */
    private Queue<TimeStampedMessage> receiveDelayQueue;
    /*Clock type for this process can be logical or vector*/
    private String myClock;
    /*Clock service for this process*/
    private ClockService clockservice;
    /*number of nodes in the system*/
    private int size;
    private int id;
	/**
	 * MessagePasser constructor.
	 * initialize local name,
	 * send queue, receive queue， receive delay queue and configuration file.
	 * start listening on a new thread.
	 */
	public MessagePasser(String configuration_filename, String local_name, String clock_name) {
	    this.myName = local_name;
	    this.myClock = clock_name;
	    this.sendDelayQueue = new ArrayDeque<TimeStampedMessage>(10);
	    this.receiveQueue = new LinkedList<TimeStampedMessage>();
	    this.receiveDelayQueue = new ArrayDeque<TimeStampedMessage>(10);
		this.myConfig = new Configuration(configuration_filename);
		this.size = myConfig.get_NodeMap().keySet().size();
		int counter = 0;
		//TODO:
		for (String name : myConfig.get_NodeMap().keySet()) {
			if (name.equals(local_name)){
				this.id = counter;
				System.out.println("MY ID: " + this.id);
				break;
			}
			counter++;
		}
		/* Use the clock factory to generate clock service. */
        ClockFactory factory = new ClockFactory(this);
        this.clockservice = factory.getClockService();
        
		Thread listen = new Thread(new Listener(myConfig, myName, receiveQueue, receiveDelayQueue));
		listen.start(); 
		Thread receive = new Thread(new Receive(receiveQueue, clockservice));
		receive.start(); 
	}
	public void runNow(){
	    while(true) {	    	
	        TimeStampedMessage newMes = this.enterParameter(myName);
	        if (newMes == null) {
	            continue;
	        }
	        newMes.set_seqNum(myConfig.getNode(newMes.get_dest()).get_seqN());
	        myConfig.getNode(newMes.get_dest()).incre_seqN();
	        /*send a timestamped message*/
	        TimeStampedMessage Tmes = (TimeStampedMessage)newMes;
	        if (this.myClock.equals("vector")){
	        	Tmes.setId(this.id);
	        	Tmes.setSize(this.size);
	        	Tmes.setType(myClock);
	        	for (int i = 0; i< this.size;i++) {
	        		Tmes.setTimeStamp(clockservice.getTimeStamp(i),i);
	        	}
	        }
	        else if (this.myClock.equals("logical")){
	        	Tmes.setType(myClock);
	        	Tmes.setTimeStamp(clockservice.getTimeStamp());
	        }
	        System.out.println("[runNow:new timeStampedmessage]" + Tmes);
	        clockservice.increment();
	        if (Tmes.get_log()){
	        	sendToLog(Tmes);
	        }
//	        System.out.println("[runNow:node sequence number]" + myConfig.getNode(Tmes.get_dest()).get_seqN());
		    String checkResult = check(Tmes); 
		    if (checkResult != null) {
		        if (checkResult.equals("drop")) {
		            continue;
		        } else if (checkResult.equals("duplicate")) {
		            TimeStampedMessage clone = (TimeStampedMessage)Tmes.clone();
		            send(Tmes);
		            send(clone);
		            while (!sendDelayQueue.isEmpty()){
		                TimeStampedMessage msg = sendDelayQueue.poll();
		            	send(msg);
		            }
		        } else if(checkResult.equals("delay")){
		            sendDelayQueue.offer(Tmes);
		            
		        }else {
		            System.out.println("[ATTENTION]abnormal checkResult" + checkResult); 
		        }
		    }
		    else {
		    	send(Tmes);
	            while (!sendDelayQueue.isEmpty()){
	            	TimeStampedMessage msg = (TimeStampedMessage)sendDelayQueue.poll();
	            	send(msg);
	            }
		    }
	    }
	}
	/**
     * Construct the message from input parameters.
     * @return the message constructed from input parameters.
     */
	private TimeStampedMessage enterParameter(String localName) {
        System.out.println("Enter destination, "
                + "message kind and the message content, seperate them with slash :)");
        InputStreamReader isrd = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isrd);
        String[] inputParam = null;
        try {
            String temp = br.readLine();
            inputParam = temp.split("/");
            if (inputParam.length < 4) {
                //wrong input
                System.out.println("oops, illegal input.");
                return null;
            }
//            System.out.println("Okay, so your message to be send --");
//            System.out.println("destination:" + inputParam[0] + "  kind:" + 
//                    inputParam[1] + "  content:" + inputParam[2]);
        } catch(Exception e) {
            e.printStackTrace();
        }   
        try {
            TimeStampedMessage newM = new TimeStampedMessage(localName, inputParam[0], inputParam[1], inputParam[2]);
            newM.set_log(inputParam[3]);
            return newM;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	/**
	 * Send message to a particular destination.
	 * @param dest destination
	 * @param kind message kind
	 * @param data the data in message
	 */
	private void send(TimeStampedMessage newMes) {
	    System.out.println("[MessagePasser class: send function]");
	    if (newMes == null) {
	        System.out.println("Message is empty, can't send it");
	        return;
	    }
        ObjectOutputStream os = null;
        os = myConfig.get_OSMap(newMes.get_dest());
        if (os != null) {
            try {
                System.out.println("[MessagePasser class: send function: using exsiting output stream.]");
                System.out.println("message to be send is:" + newMes);
                os.writeObject(newMes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[MessagePasser class: send function: create new output stream...]");
            Node me = myConfig.getNode(myName);
            Node he = myConfig.getNode(newMes.get_dest());
            Socket sck = null;
            try {
//                sck = new Socket(he.get_ip(), he.get_port());
                sck = new Socket("localhost", he.get_port());
                System.out.println("succeed");
                os = new ObjectOutputStream(sck.getOutputStream());
                myConfig.add_OSMap(newMes.get_dest(), os);
                System.out.println("message to be send is:" + newMes);
                os.writeObject(newMes);
            } catch (IOException e) {
                if (sck != null) {
                    try {
                        sck.close();
                    } catch (Exception nestedE) {
                        nestedE.printStackTrace();   
                    }
                } else {
                    e.printStackTrace();
                }
                
            }   
        }   
    }
	private void sendToLog(TimeStampedMessage newMes) {
	    ;
	    if (newMes == null) {
	        System.out.println("Message is empty, can't send it");
	        return;
	    }
        ObjectOutputStream os = null;
        os = myConfig.get_LoggerOS();
        if (os != null) {
            try {
            //    System.out.println("[MessagePasser class: send function: using exsiting output stream.]");
            //    System.out.println("message to be send is:" + newMes);
                os.writeObject(newMes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[MessagePasser class: send function: create new LOG output stream...]");
            //Node me = myConfig.getNode(myName);
            //Node he = myConfig.getNode(newMes.get_dest());
            Socket sck = null;
            try {
                sck = new Socket("localhost",16820);
//                sck = new Socket("localhost", he.get_port());
                //System.out.println("succeed");
                os = new ObjectOutputStream(sck.getOutputStream());
                myConfig.set_LoggerOS(os);
                //myConfig.add_OSMap(newMes.get_dest(), os);
                //System.out.println("message to be send is:" + newMes);
                os.writeObject(newMes);
                System.out.println("Ok");
            } catch (IOException e) {
                if (sck != null) {
                    try {
                        System.out.println("CLOSE");
                        sck.close();
                    } catch (Exception nestedE) {
                        nestedE.printStackTrace();   
                    }
                } else {
                    e.printStackTrace();
                }
                
            }   
        }   
    }
	/**
	 * check input message against rules in rule list.
	 * @return actions should be taken.
	 */
	private String check(TimeStampedMessage newMes) {
	    System.out.println("[check send message]");
	    for (Rule r : myConfig.sendRules) {
	        int result = r.match(newMes);
	        if (result == 1) {
	        	if (r.get_action().equals("dropAfter")){
	        		return null;
	        	}
	            return r.get_action();
	        }
	        if (result == 2){
	            //due to drop after, the after message will be dropped.
	        	return "drop";
	        }
	    }
	    return null;
	}
	public String getClock(){
		return this.myClock;
	}
	public int getSize(){
		return this.size;
	}
	public int getId(){
		return this.id;
	}
}
