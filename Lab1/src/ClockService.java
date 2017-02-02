
public interface ClockService {
	public void increment();
	public Integer getTimeStamp();
	public Integer getTimeStamp(int i);
	public void Synchronize(TimeStampedMessage msg);
	public String toString();
	public int compare(int m1, int m2);
	public int compare(int[] m1, int[] m2);
}
