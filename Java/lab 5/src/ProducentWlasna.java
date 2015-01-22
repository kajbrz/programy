import java.awt.Color;
import java.awt.Graphics;
/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */

class ProducentWlasna extends Thread
{
	boolean koniec = false;
	boolean kierunek = true;
	Bufor bufor;
	
	
	float predkosc; 
	boolean dowoz = true;
	

	Color kolor = Color.black;
	Graphics wyglad;
	
	Pozycja pozycja = new Pozycja(250,260);  
	
	ProducentWlasna(Bufor referencjaBufor)
	{
		bufor = referencjaBufor;
		
		predkosc = (float)Math.random() + 1;
	}
	
	@Override
	public void run()
	{
		Timer timer = new Timer();
		while(!koniec)
		{
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

			
			if (pozycja.x  < bufor.pozycja.x + 20)
			{
				try {
					//System.out.print("Producent probuje dowieŸæ towar...");
					wyprodukuj();
					pozycja.x += 1;
					kierunek = !kierunek;
					//System.out.print(". wyprodukowa³");
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			else if(pozycja.x > 280)
			{
				kierunek = !kierunek;
			}
		}
	}
	
	void wyprodukuj() throws InterruptedException
	{
		bufor.doloz();
	}
	
	public void zakoncz()
	{
		koniec = true;
	}
	
	public void narysuj(Graphics g)
	{
		g.setColor(kolor);		
		g.fillOval((int)pozycja.x - 20, (int)pozycja.y - 20, 20, 20);
		//g.fillOval(200, 150 - 5, 5, 5);
		
	}
}
