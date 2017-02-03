import java.util.Arrays;

/**
 * 
 * @author Team 3
 *
 */
public class TimeStampedMessage extends Message{
    /** logical timeStamp. */
	private int timeStamp;
	/** vector timeStamp. */
	private int[] timeStamps;
	/** the clock's type is either vector or logical.*/
	private String clock_type;
	/** size of vector array.*/
	private int size;
	/** my index in array */
	private int id;
	
	/**
	 * Constructor using Message class's contructor.
	 */
	public TimeStampedMessage(String s,String d,String k,Object data) {
		super(s, d, k, data);
	}
	/**
	 * set the type either vector or logical.
	 * @param clock the clock type
	 */
	public void setType(String clock){
	    if ((clock.equals("vector")) || (clock.equals("logical"))) {
	        this.clock_type = clock;
	    } else {
	        throw new RuntimeException("error in TimeStampedMessage's setType");
	    }
	}
	public String getType(){
	    if (this.clock_type == null) {
	        throw new RuntimeException("TimeStampedMessage's type was not set");
	    }
		return this.clock_type;
	}
	public void setSize(int s){
		this.size = s;
		this.timeStamps = new int[s];
		return;
	}
	public void setId(int i){
		this.id = i;
		return;
	}
	/**
	 * vector timestamp setter.
	 * Sets the time t in corresponding position i.
	 * @param t timestamp
	 * @param i index
	 */
	public void setTimeStamp(int t, int i){
		if(clock_type.equals("vector")){
			this.timeStamps[i] = t;
		} else {
		    throw new RuntimeException("error in TimeStampedMessage's setTimeStamp");
		}
	}
	/**
     * vector timestamp getter by specific index.
     */
	public int getTimeStamp(int i){
	    if(clock_type.equals("vector")){
	        return timeStamps[i];
        } else {
            throw new RuntimeException("error in TimeStampedMessage's getTimeStamp");
        } 
    }
	/**
	 * logical timestamp setter.
	 */
	public void setTimeStamp(int t){
	    if(clock_type.equals("logical")){
	        this.timeStamp = t;
        } else {
            throw new RuntimeException("error in TimeStampedMessage's setTimeStamp class");
        }
	}
	/**
     * logical timestamp getter.
     */
	public int getTimeStamp(){
	    if(clock_type.equals("logical")){
	        return this.timeStamp;
        } else {
            throw new RuntimeException("error in TimeStampedMessage's setTimeStamp class");
        }
	}
	/**
	 * get the whole array of time stamp.
	 * @return
	 */
	public int[] getTimeStamps(){
		return timeStamps;
	}
	/**
	 * compare method.
	 */
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
	public String toString() { 
        return  "[clock type:" +  this.clock_type + "]" 
                + "[time stamp:"
                + (this.clock_type.equals("logical") ? this.timeStamp : Arrays.toString(timeStamps))
                + "]"
                + "[NO." + this.get_seqNum() + "]" + "[source]"+ this.get_source() + " [dest]"+ this.get_dest() 
                + " [kind]"+ this.get_kind() + " [content]" + this.get_payload();
    }

}
