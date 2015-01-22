import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */
class KonsumentWlasna extends Thread
{
	boolean koniec = false;
	Bufor bufor;
	
	float predkosc;
	boolean dowoz = true;
	
	boolean kierunek = false;
	

	Color kolor = Color.black;
	Point pozycja = new Point(50,260);
	
	public KonsumentWlasna(Bufor referencjaBufor) 
	{
		bufor = referencjaBufor;

		predkosc = (float)Math.random() + 1f;
		//predkosc = 1f;
	}
	
	@Override
	public void run()
	{
		Timer timer = new Timer();
		while(!koniec)
		{
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(KonsumentWlasna.class.getName()).log(Level.SEVERE, null, ex);
                    }
			float dystans = (predkosc * timer.ileMiliSekundMineloTeraz()) / 10;
			if (dystans < 1 )
				continue;
			timer.startTimer();
			//System.out.println("D: " + dystans);
			if(kierunek)
			{
				pozycja.x += -dystans;
			}
			else
			{

				pozycja.x += dystans;
			//	System.out.println(pozycja.x);
			}

			
			if (pozycja.x  > bufor.pozycja.x)
			{
				try {
					zabierz();
					pozycja.x -= 1f;
					kierunek = !kierunek;
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			else if(pozycja.x < 20)
			{
				kierunek = !kierunek;
				pozycja.x += 1f;
				
			}
		}
	}
	
	void zabierz() throws InterruptedException
	{
		bufor.pobierz();
	}
	
	public void zakoncz()
	{
		koniec = true;
	}
	
	public void przesun(Point przesuniecie)
	{
		//S = V*t
	}
	
	
	public void narysuj(Graphics g)
	{
		g.setColor(kolor); 
		g.drawRect(pozycja.x - 20, pozycja.y - 20, 20, 20);
	}
}
