import java.util.*;
public class VectorClock implements ClockService {
	private int[] times;
	private int id;
	VectorClock(int size, int i){
		times = new int[size];
		id = i;
	}
	@Override
	public void increment() {
		// TODO Auto-generated method stub
		times[id]++;
	}

	@Override
	public Integer getTimeStamp() {
		// TODO Auto-generated method stub
		return times[id];
		
	}



	@Override
	public void Synchronize(TimeStampedMessage msg) {
		// TODO Auto-generated method stub
		for (int i = 0; i<times.length;i++){
			times[i] = Math.max(msg.getTimeStamp(i),times[i]);
			
		}
		times[id]++;
	}
	@Override
	public Integer getTimeStamp(int i) {
		// TODO Auto-generated method stub
		return times[i];
		
	}
	@Override
	/*vector clock doesn't need this method.*/
	public int compare(int m1, int m2) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int compare(int[] m1, int[] m2) {
		// TODO Auto-generated method stub
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
}
