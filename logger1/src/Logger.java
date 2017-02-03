
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
            //TODO:CLOCK TYPE
            int i = 0;
            int j = 1;
            boolean ifC = false;
            while (i < messageList.size() - 1) {
                while (ifConcurrVec(messageList.get(i), messageList.get(j))) {
                    ifC = true;
                    if (j - i == 1) {
                        System.out.println(">>>>>>>>concurrent>>>>>>>>>");
                        System.out.println(messageList.get(i));
                    }
                    System.out.println(messageList.get(j));
                    j++;
                }
                if (ifC = true) {
                    i = j;
                    j++;
                    System.out.println("<<<<<<<<concurrent<<<<<<<<");
                    ifC = false;
                } else {
                    System.out.println(messageList.get(i));
                    i++;
                    j++;
                }
                
            }
            
//            Iterator<TimeStampedMessage> itr = messageList.iterator();
//            System.out.println("----------log's sorted file list---------");
//            while (itr.hasNext()) {
//                System.out.println(itr.next());
//            }
//            System.out.println("---------log's sorted file list end----------");
        }
        
    }
    private boolean ifConcurrVec(TimeStampedMessage t1, TimeStampedMessage t2) {
        int[] m1 = t1.getTimeStamps();
        int[] m2 = t2.getTimeStamps();
        int flag = 0;
        for (int i = 0; i < m1.length; i++) {
            if(flag == 1 && m1[i] < m2[i]) {
                return true;
            }
            if(flag == -1 && m1[i] > m2[i]) {
                return true;
            }
            if(flag == 0 && m1[i] < m2[i]) {
                flag = -1;
            } else if (flag == 0 && m1[i] > m2[i]){
                flag = 1;
            }
        }
        return false;
    }
}
