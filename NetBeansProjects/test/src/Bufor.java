import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */
class Bufor 
{
	AtomicInteger wartosc;
	int max = 1;

	Color kolor = Color.black;
	
	Pozycja pozycja = new Pozycja(120,230);  
	
	
	Bufor(int max)
	{
		wartosc = new AtomicInteger(0);
		if (max > 1)
			this.max = max;
		else
			this.max = 1;
	}
	
	Bufor()
	{

	}
	
	public synchronized void pobierz() throws InterruptedException
	{
		while(wartosc.get() < 1 )
		{
			wait();
		}
		
		wartosc.decrementAndGet();
		notifyAll();
		
	}
	
	public synchronized void doloz() throws InterruptedException
	{
		while(wartosc.get() >= max)
		{
			wait();
		}
		
		wartosc.incrementAndGet();	
		notifyAll();
		
	}
	
	public void ustawMax(int wart)
	{
		max = wart;
	}
	
	public int pobierzIlosc()
	{
		return wartosc.get();
	}

	public void narysuj(Graphics g)
	{
		g.setColor(kolor);
		g.drawRect((int)pozycja.x, (int)pozycja.y, 20, max*5);
		
		g.setColor(Color.BLUE);
		for(int i = 0; i < wartosc.get(); i++)
		{
			g.fillRect((int)pozycja.x, (int)pozycja.y + (max-1 -i) * 5, 20, 5);
		}
		
	}
}
