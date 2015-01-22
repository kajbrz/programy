import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Okno extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6604416051931733087L;
	
	PlanszaDoRysowania planszaDoRysowania = new PlanszaDoRysowania();
	JPanel panelNaPrzyciski = new JPanel();

	Okno()
	{
		super("Laboratorium 5 zad 3");
		setSize(new Dimension(300,300));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//panelNaPrzyciski.setSize(new Dimension(300,300));
		//panelNaPrzyciski.setVisible(true);
		//panelNaPrzyciski.add(new JButton("DUPA"));
		
		setContentPane(planszaDoRysowania);		
		Thread watek = new Thread(planszaDoRysowania);
		watek.start();
		
		//panelNaPrzyciski.add(planszaDoRysowania);
		
		//Thread watek = new Thread(planszaDoRysowania);
		//watek.start();
		
		setVisible(true);
	}
	
	
	public static void main(String agrv[])
	{
		new Okno();
	}
}

class PlanszaDoRysowania extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4091471096385204171L;
	PlanszaLiscia planszaLiscia;
	
	PlanszaDoRysowania()
	{
		planszaLiscia = new PlanszaLiscia(50,25, 20, 11*1000);
		Thread watek = new Thread(planszaLiscia);
		watek.start();
		setSize(planszaLiscia.getWymiarX()*10,planszaLiscia.getWymiarY()*10);
		setVisible(true);
		repaint();
	}
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		planszaLiscia.narysuj(g);		
	}	
	
	@Override
	public void run() {		
		while(true)
		{
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}		
	}
}


class PlanszaLiscia extends Thread {
	
	private AtomicInteger wymiarX = new AtomicInteger();
	private AtomicInteger wymiarY = new AtomicInteger();
	private AtomicLong czasOdrostuLisci = new AtomicLong(); //w milisekundach
	private Vector<Lisc> wartosci = new Vector<Lisc>();
	private Vector<Slimak> slimaki = new Vector<Slimak>();
	private Lisc emptyCell = new Lisc();
	
	private Color kolory[] = new Color[]
			{
			//odcienie zieleni
				new Color(0b000000000),
				new Color(0b1011100000000),
				new Color(0b1111100000000),
				new Color(0b10111100000000),
				new Color(0b11111100000000),
				new Color(0b101111100000000),
				new Color(0b111111100000000),
				new Color(0b1011111100000000),
				new Color(0b1111111100000000),
				new Color(0b101111111100000000),
				new Color(0b1111111111100000000)
			};
	
	
	PlanszaLiscia(int x, int y, int iloscSlimakow, long czasOdrostuLisci)
	{
		emptyCell.setWartosc(0);
		wymiarX.set(x);
		wymiarY.set(y);		
		this.czasOdrostuLisci.set(czasOdrostuLisci);
		
		for(int i = 0; i < x*y; i++)
		{			
			wartosci.addElement(new Lisc());			
		}
		
		for (int i = 0; i < iloscSlimakow; i++)
		{
			Slimak slimak = new Slimak(this);
			slimaki.addElement(slimak);
			slimak.start();
		}
	}
	
	PlanszaLiscia()
	{
		this(1,1,1,200);
	}
	
	public void zmienWymiary(int x, int y)
	{
		wymiarX.set(x);
		wymiarY.set(y);
	}
	
	public int getWymiarX()
	{
		return wymiarX.get();
	}
	
	public int getWymiarY()
	{
		return wymiarY.get();
	}
	
	public Lisc getWartoscLisci(int pozycja)
	{
		return wartosci.get(pozycja);
	}
	
	public Lisc getWartoscLisci(int x, int y)
	{
		if(x < 0 || x >= getWymiarX())
			return emptyCell;
		if(y < 0 || y >= getWymiarY())
			return emptyCell;
		return wartosci.get(y*getWymiarX()+x);
	}
	public Vector<Lisc> getVectorLisci()
	{
		return wartosci;
	}
	
	public void narysuj(Graphics g)
	{
		int x = this.getWymiarX();
		int y = this.getWymiarY();
		
		for (int i = 0; i < x; i++)
		{
			for (int j = 0; j < y; j++)
			{
				//wartoscLisci.get((x+1)*(y+1)-1).getWartosc()
				Color kolor = kolory[getWartoscLisci(i,j).getWartosc()];
				g.setColor(kolor);				
				g.fillRect(21*i, 21*j, 20, 20);
			}
		}
		
		for (Slimak slimak : slimaki)
		{
			g.setColor(Color.RED);
			
			g.fillOval((int)slimak.getPoleX()*21 + 4, (int)slimak.getPoleY()*21 + 4, 10, 10);
			//System.out.println(slimak.getPoleX() + " : " + slimak.getPoleY());
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Lisc lisc: wartosci)
			{
				Date czasTeraz = new Date();
				if(czasTeraz.getTime() - lisc.getCzas() > czasOdrostuLisci.get())
				{
					lisc.odrosnij();
				}
			}
		}
	}
}

class Lisc 
{
	private int wartosc;
	private Date ostatniCzas = new Date(); //czas od ostatniej zmiany
	
	public boolean czyStoiSlimak = false;
	
	Lisc()
	{
		wartosc = (int)(Math.random()*10);
	}
	
	public int getWartosc()
	{
		return wartosc;
	}
	
	public void setWartosc(int wartosc)
	{
		ostatniCzas = new Date();
		this.wartosc = wartosc;
	}
	
	public boolean urwijLisc() throws InterruptedException
	{
		if (wartosc < 1)
			return false;
		
		this.setWartosc(wartosc - 1);
		return true;
	}
	
	public void odrosnij()
	{
		if (wartosc < 10)
			this.setWartosc(wartosc + 1);
	}
	
	public long getCzas()
	{
		return ostatniCzas.getTime();	
	}
	
}

class Slimak extends Thread
{
	private AtomicInteger poleX = new AtomicInteger();
	private AtomicInteger poleY	= new AtomicInteger();
	
	private AtomicLong czasUspienia = new AtomicLong(1*1000); //milisekundy
	private AtomicLong dlugoscJedzenia = new AtomicLong(5*1000);
	
	PlanszaLiscia planszaLisciaReference;
	
	/*
	 * <h1>Odrod� na pozycji</h1>
	 */
	
	public int getPoleX()
	{
		return poleX.get();
	}
	public int getPoleY()
	{
		return poleY.get();
	}
	Slimak(PlanszaLiscia planszaLiscia)
	{
		planszaLisciaReference = planszaLiscia;
		while(true)
		{
			poleX.set((int)(Math.random()*planszaLisciaReference.getWymiarX()));
			poleY.set((int)(Math.random()*planszaLisciaReference.getWymiarY()));
			if(poleX.get() >= planszaLisciaReference.getWymiarX()
					|| poleY.get() >= planszaLisciaReference.getWymiarY())
				continue;
			if(!planszaLisciaReference
					.getWartoscLisci(poleX.get(), poleY.get())
					.czyStoiSlimak)
			{
				planszaLisciaReference
				.getWartoscLisci(poleX.get(), poleY.get())
				.czyStoiSlimak = true;
				
				break;
			}
		}
	}	
	
	@Override
	public void run()	
	{
		while(true)
		{
			Vector<Point> possible = new Vector<Point>();
			Long toWait = 0L;
			synchronized (planszaLisciaReference.getVectorLisci())
			{
				int poleX = this.poleX.get();
				int poleY = this.poleY.get();
				//System.out.println(poleX + " : " + poleY);
				if(planszaLisciaReference.getWartoscLisci(poleX, poleY-1).getWartosc() != 0
						&& !planszaLisciaReference.getWartoscLisci(poleX, poleY-1).czyStoiSlimak)
					possible.addElement(new Point(poleX, poleY-1));
				if(planszaLisciaReference.getWartoscLisci(poleX+1, poleY).getWartosc() != 0
						&& !planszaLisciaReference.getWartoscLisci(poleX+1, poleY).czyStoiSlimak)
					possible.addElement(new Point(poleX+1, poleY));
				if(planszaLisciaReference.getWartoscLisci(poleX, poleY+1).getWartosc() != 0
						&& !planszaLisciaReference.getWartoscLisci(poleX, poleY+1).czyStoiSlimak)
					possible.addElement(new Point(poleX, poleY+1));
				if(planszaLisciaReference.getWartoscLisci(poleX-1, poleY).getWartosc() != 0
						&& !planszaLisciaReference.getWartoscLisci(poleX-1, poleY).czyStoiSlimak)
					possible.addElement(new Point(poleX-1, poleY));
				
				if(possible.size() == 0)
					toWait = czasUspienia.get();
				else
				{
					Point p = possible.get((int)(Math.random()*100%possible.size()));
					//Point p = possible.get(0);
					planszaLisciaReference.getWartoscLisci(p.x, p.y).czyStoiSlimak = true;
					planszaLisciaReference.getWartoscLisci(poleX, poleY).czyStoiSlimak = false;
					try {
						planszaLisciaReference.getWartoscLisci(p.x, p.y).urwijLisc();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized(this.poleX)
					{
						synchronized(this.poleY)					
						{
							this.poleX.set(p.x);
							this.poleY.set(p.y);
							//this.poleX.set(0);
							//this.poleY.set(5);
						}
					}
					toWait = dlugoscJedzenia.get();
				}
			}
			try {
				sleep(toWait);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}	
	
	
}

class WrongCountException extends Exception
{
	WrongCountException()
	{
		
	}
}




















