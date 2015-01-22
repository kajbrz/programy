package testowanie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;






public class Test extends JFrame implements MouseListener, MouseMotionListener, Runnable
{
	private Pozycja pozycjaMyszki = new Pozycja(200,100);
	private Kropka kropka = new Kropka(200f,100f);
	
	
	public Test()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(640, 480));
		
		setVisible(true);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setFocusable(true);
		
		Thread watek = new Thread(this);
		watek.start();
		
		JPanel jpanel = new JPanel(){	
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				kropka.narysuj(g);
			}
		};
		setContentPane(jpanel);	
	}
	
	public static void main(String agrv[])
	{
		new Test();
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//ustawiasz w³¹czenie kolka bo myszka wchodzi nato

		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//TUtaj siê ustawia to ¿e znika jak wyjedziesz myszk¹ poza ekran
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		synchronized(pozycjaMyszki)
		{
			pozycjaMyszki = new Pozycja(arg0.getX(), arg0.getY());
		}
	}

	@Override
	public void run() {
		Date czas = new Date();
		
		while(true)
		{
			try {
				Thread.sleep(20);
				repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date czas2 = new Date();
			float odleglosc = kropka.getPredkosc() * ((float)(czas2.getTime() - czas.getTime())/1000f);

			System.out.println(odleglosc);
			czas = new Date();
			float odlegoscx = 0;
			float odlegoscy = 0;
			synchronized(pozycjaMyszki)
			{
				float odleglosc_wymiar = (float)Math.sqrt(
						(pozycjaMyszki.x - kropka.getPozycja().x)*(pozycjaMyszki.x - kropka.getPozycja().x) 
						+
						(pozycjaMyszki.y - kropka.getPozycja().y)*(pozycjaMyszki.y - kropka.getPozycja().y) 
						);
				
				
				boolean korzystajZWersora = false;
				if(korzystajZWersora)
				{
					odlegoscx = odleglosc * (pozycjaMyszki.x - kropka.getPozycja().x)/odleglosc_wymiar;
					odlegoscy = odleglosc * (pozycjaMyszki.y - kropka.getPozycja().y)/odleglosc_wymiar;
				}
				else
				{
					odlegoscx = (pozycjaMyszki.x - kropka.getPozycja().x) * odleglosc;
					odlegoscy = (pozycjaMyszki.y - kropka.getPozycja().y) * odleglosc;
		
					//System.out.println(pozycjaMyszki);
				}
			}
			//kropka.ustaw(kropka.getPozycja().x + odlegoscx, kropka.getPozycja().y + odlegoscy);
			//kropka.przesun(odlegoscx, odlegoscy);
			kropka.przesun(odlegoscx, odlegoscy);
			//kropka.przesun(0,-20);
			//kropka.ustaw((float)(Math.cos((float)()));
		}
		
	}
	
}

class Kropka
{
	private Pozycja pozycja = new Pozycja(0,0);
	private float predkosc = 1f;
	private float promien = 10;
	
	Kropka(Pozycja pozycjaMyszki)
	{
		
	}
	Kropka(float x, float y)
	{
		ustaw(new Pozycja(x,y));
	}
	
	
	public void ustaw(Pozycja pozycja)
	{
		//if (this.pozycja.x - pozycja.x > 0.0000001f && this.pozycja.y - pozycja.y > 0.0000001f )
			this.pozycja = new Pozycja(pozycja.x, pozycja.y);
	}
	public void przesun(Pozycja pozycja)
	{
		ustaw(this.pozycja.x + pozycja.x, this.pozycja.y + pozycja.y);
	}
	public void ustaw(float x, float y)
	{
		ustaw(new Pozycja(x,y));
	}
	
	public void przesun(float x, float y)
	{
		przesun(new Pozycja(x,y));
	}
	public void narysuj(Graphics g)
	{
		g.setColor(Color.green);
		g.fillOval((int)(pozycja.x - promien) , (int)(pozycja.y - promien), (int)(2*promien), (int)(2*promien));
	}
	
	public float getPredkosc()
	{
		return predkosc;
	}
	public Pozycja getPozycja()
	{
		return pozycja;
	}
	
}

class Pozycja
{
	float x = 0f, y = 0f;
	
	Pozycja(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	void ustaw(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + " ; " + y + ")";
	}
}