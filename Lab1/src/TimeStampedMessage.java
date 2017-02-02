/**
 * 
 * @author Team 3
 *
 */
public class TimeStampedMessage extends Message{
	Integer timeStamp;
	int[] timeStamps;
	String clock_type;
	int size;
	int id;
	public TimeStampedMessage(String s,String d,String k,Object data) {
		super(s, d, k, data);
		// TODO Auto-generated constructor stub
	}
	public void setType(String clock){
		this.clock_type = clock;
	}
	public String getType(){
		return this.clock_type;
	}
	public void setSize(int s){
		this.size = s;
		timeStamps = new int[s];
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	public void setTimeStamp(int t,int i){
		if(clock_type.equals("vector")){
			timeStamps[i] = t;
		} else {
		    throw new RuntimeException("")
		}
	}
	public void setTimeStamp(int t){
		this.timeStamp = t;
	}
	public Integer getTimeStamp(){
		return this.timeStamp;
	}
	public Integer getTimeStamp(int i){
		return timeStamps[i];
	}
	public int[] getTimeStamps(){
		return timeStamps;
	}
	public int compare(TimeStampedMessage msg1, TimeStampedMessage msg2){
		
		if (msg1.getType().equals("vector")){
			int[] m1 = msg1.getTimeStamps();
			int[] m2 = msg2.getTimeStamps();
			int flag = 0;
			for (int i = 0;i<m1.length;i++){
				if(flag==1 && m1[i]<m2[i]) return 0;
				if(flag == -1 && m1[i]>m2[i]) return 0;
				if(flag == 0 && m1[i]<m2[i]){
					flag = -1;
				}
				else if (flag == 0 && m1[i]>m2[i]){
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

}
