

public class LogicalClock implements ClockService {
	private int time = 1;
	@Override
	public void increment() {
		// TODO Auto-generated method stub
		time++;
	}

	@Override
	public Integer getTimeStamp() {
		// TODO Auto-generated method stub
		return time;
	}



	@Override
	public void Synchronize(TimeStampedMessage msg) {
		// TODO Auto-generated method stub
		time = Math.max(time, msg.getTimeStamp());
		time++;
	}
	public String toString(){
		return "[TimeStamp ]: "+ time;
	}

	@Override
	public Integer getTimeStamp(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compare(int m1, int m2) {
		// TODO Auto-generated method stub
		return m1 - m2;
	}

	@Override
	/*Logical Clock doesn't need this method. */
	public int compare(int[] m1, int[] m2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
