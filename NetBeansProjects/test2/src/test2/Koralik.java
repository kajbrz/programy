import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */
class Koralik extends Thread implements Comparator<Koralik>{
	Pozycja pozycja = new Pozycja();
	AtomicBoolean zatrzymany = new AtomicBoolean(false);
	static Integer rgbKolorGlobal = 0;
	int rgbKolorLocal;
	Okrag okrag;	
	AtomicBoolean animacjaWlaczona;
	float predkosc = 2.5f;
	float promien = 15;
	Koralik(Okrag okrag, AtomicBoolean animacjaWlaczona)
	{
		rgbKolorLocal = rgbKolorGlobal.intValue();
		rgbKolorGlobal+=0b10001;
		this.okrag = okrag;
		this.animacjaWlaczona = animacjaWlaczona;
		pozycja.ustaw(okrag.getPozycja().x + okrag.getR(), okrag.getPozycja().y);
		
		
	}
	
	public void narysuj(Graphics g)
	{
		g.setColor(new Color(rgbKolorLocal));
		g.fillOval((int)(pozycja.x - promien), (int)(pozycja.y - promien), (int)(promien*2), (int)(promien*2));
	}
	
	public void przesun(float kat) //w radianach
	{
		synchronized(okrag)
		{
			float r = okrag.getR();
			float w1 = (pozycja.y - okrag.getPozycja().y)/r;
			float w2 = (pozycja.x - okrag.getPozycja().x)/r;
			float katn = 0f;
			if (w1 >= 0 && w2 >= 0)
			{
				katn = (float)(Math.acos(w2));		
				//System.out.println("Æwiartka : " + 1);
			}
			else if (w1 >= 0 && w2 < 0)
			{
				katn = (float)(Math.acos(w2));// + 1.74f;
				//System.out.println("Æwiartka : " + 2);					
			}
			else if (w1 < 0 && w2 < 0)
			{
				katn =(float) Math.PI*2.0f - (float)(Math.acos(w2));// + 3.14f;				
				//System.out.println("Æwiartka : " + 3);
			}
			else if (w1 < 0 && w2 >= 0)
			{
				katn =(float) Math.PI*2.0f - (float)(Math.acos(w2));// + 4.88f;
				//System.out.println("Æwiartka : " + 4);						
			}
			//float acos =  (float)Math.acos((pozycja.x - okrag.getPozycja().x)/r);

			
			//if (acos<0)
			//	asin = - asin;
			float katBeta = (kat + katn);
			//System.out.println("\nKat: " + (r*Math.cos(katBeta) + okrag.getPozycja().x) + "  " + rgbKolorLocal);
			//System.out.println("Katn: " + Math.toDegrees(katn) + "  " + rgbKolorLocal);
			//System.out.println("KatB: " + Math.toDegrees(katBeta) + "  " + rgbKolorLocal);
				
			pozycja.ustaw((float)(r*Math.cos(katBeta) + okrag.getPozycja().x),
					(float)(r*Math.sin(katBeta) + okrag.getPozycja().y));			
		}			
	}
	synchronized Boolean getAnimacjaWlaczona()
	{		
		return animacjaWlaczona.get();		
	}

	public void zmniejszPredkosc(float predkoscKatowa)
	{
		if(predkoscKatowa >= 0 && predkoscKatowa <= 1)
		{
			predkosc = predkoscKatowa*5f;
		}
		else
		{
			predkosc = 2.5f;
		}
	}
	
	@Override
	public int compare(Koralik o1, Koralik o2) { // nachodz¹ na siebie
		float odlegloscX = o1.pozycja.x - o2.pozycja.x;
		float odlegloscY = o1.pozycja.y - o2.pozycja.y;
		
		float odleglosc =(float)Math.sqrt((odlegloscX*odlegloscX + odlegloscY*odlegloscY));
		
		if (odleglosc < (o1.promien + o2.promien))
		{
			return 1;
		}

		return 0;
	}
	
	public static boolean porownaj(Koralik o1, Koralik o2) { // nachodz¹ na siebie

		float odlegloscX = o1.pozycja.x - o2.pozycja.x;
		float odlegloscY = o1.pozycja.y - o2.pozycja.y;
		
		float odleglosc =(float)Math.sqrt((odlegloscX*odlegloscX + odlegloscY*odlegloscY));

		if (odleglosc < (o1.promien + o2.promien))
		{
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run()
	{
		Date czas = new Date();
		Date czas2 = new Date();
		
		while(true)		
		{
			czas2 = new Date();
			if(getAnimacjaWlaczona())
			{
				if (!zatrzymany.get())
				przesun(predkosc*(((float)(czas2.getTime()-czas.getTime())/1000)));
				czas = new Date();
			}
			else
			{
				czas = new Date();
			}
			
			try {
				this.sleep(20);	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	}
	
	public void zatrzymaj()
	{
		zatrzymany.set(true);
	}
	
	public void wznow()
	{
	zatrzymany.set(false);
	}
	
	public void getZatrzymany()
	{
		
	}
	public int getKolor()
	{
		return rgbKolorLocal;
	}

	
	
}
