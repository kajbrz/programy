import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 * @laboratorium 5 
 */
public class Timer extends Thread {
	Date czasStart;
	Date czasStop;
	boolean koniec = false;
	
	Timer()
	{
		czasStart = new Date();
		czasStop = new Date();
		
		this.start();
	}
	
	public void startTimer()
	{
		czasStart = new Date();
	}
	
	public void stopTimer()
	{
		czasStop = new Date();
	}
	
	public long ileSekundMinelo()
	{
		return (czasStop.getTime() - czasStart.getTime()) * 1000;
	}
	
	public long ileSekundMineloTeraz()
	{
		return ((new Date()).getTime() - czasStart.getTime()) * 1000;
	}
	
	public long ileMiliSekundMinelo()
	{
		return (czasStop.getTime() - czasStart.getTime());
	}
	
	public long ileMiliSekundMineloTeraz()
	{
		return ((new Date()).getTime() - czasStart.getTime());
	}
	
	
	
	void zakoncz()
	{
		koniec = true;
	}
	
	@Override
	public void run()
	{
		while(!koniec)
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
                    }
	}
}
