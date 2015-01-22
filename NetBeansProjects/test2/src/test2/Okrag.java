import java.awt.Color;
import java.awt.Graphics;
/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */

class Okrag {

	float r;
	Pozycja pozycja;
	
	Okrag(Pozycja pozycja, float promien)
	{
		this.pozycja = pozycja;
		this.r = promien;
	}
	Okrag(float x, float y, float promien)
	{
		this(new Pozycja(x,y), promien);
	}
	Okrag()
	{
		this(0,0,1);
	}
	
	public float getR()
	{
		return r;
	}
	public Pozycja getPozycja()
	{
		return pozycja;
	}
	

	public void narysuj(Graphics g)
	{
		g.setColor(Color.black);
		
		g.drawOval((int)(pozycja.x - r), (int)(pozycja.y - r), (int)r*2, (int)r*2);
	}
	
}
