import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */

public class Okno extends JFrame implements ActionListener, ChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5338984940511940512L;
	PanelAnimacji panelAnimacji = new PanelAnimacji();
	JPanel panelSuwakow = new JPanel();
	JButton przyciskStartStop = new JButton("Start");
	JButton przyciskDodajKoralik = new JButton("Dodaj");	
	
	Vector<JSlider> suwakiPredkosci = new Vector<JSlider>();
	
	
	Okno()
	{
		super("Laboratorium 5 Zadanie 2");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,300);
		
		setContentPane(panelAnimacji);
		
		panelSuwakow.add(przyciskStartStop);
		przyciskStartStop.addActionListener(this);
		
		panelSuwakow.add(przyciskDodajKoralik);
		przyciskDodajKoralik.addActionListener(this);
		
		///////////////
		panelSuwakow.setBounds(100, 500, 100, 300);
		panelSuwakow.setSize(new Dimension(100,500));
		panelSuwakow.setLocation(0, 300);
		panelSuwakow.setLayout(new java.awt.GridLayout(0,1,4,4));
		this.add(panelSuwakow);
		//panelSuwakow.setBounds(0, 200, 200, 400);
		
		//panelAnimacji.add(panelSuwakow);
		
		this.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT));
		//////////////
		
		repaint();
		
		
		setVisible(true);
	}
	
	public static void main(String agrs[])
	{
		new Okno();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == przyciskStartStop)
		{
			if (przyciskStartStop.getText() == "Stop")
			{
				panelAnimacji.wylaczAnimacje();
				przyciskStartStop.setText("Start");
			}
			else 
			{   panelAnimacji.uruchomAnimacje();
				przyciskStartStop.setText("Stop");
			}
			
		}
		else if(o == przyciskDodajKoralik)			
		{
			panelAnimacji.dodajKoralik();
			
			JSlider jSlider = new JSlider(0,1000,500);
			//jSlider.setLocation(0,200 + 10*suwakiPredkosci.size());
			panelSuwakow.add(jSlider);
			//panelSuwakow.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT));
			//this.add(jSlider);
			jSlider.addChangeListener(this);
			suwakiPredkosci.addElement(jSlider);
		}
		
		
		repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object o = e.getSource();
		int i = 0;
		for(JSlider jSlider : suwakiPredkosci)
		{
			if(o == jSlider)
			{
				//System.out.print("\nWartosc: " + jSlider.getValue());
				panelAnimacji.zmienPredkosc(i, jSlider.getValue()/1000f);
			}
			i++;
		}
		
		repaint();
	}
}


class PanelAnimacji extends JPanel implements Runnable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2130048889372025968L;
	AtomicBoolean animacjaWlaczona = new AtomicBoolean(false);
	ArrayList<Koralik> koraliki = new ArrayList<Koralik>();
	Okrag okrag;
	
	
	PanelAnimacji()
	{
				
		okrag = new Okrag(300,120,80);
		setSize(new Dimension(120,120));
		
		Thread watek = new Thread(this);
		watek.start();	

		Thread watekKolizji = new WatekKolizji();
		watekKolizji.start();
	}
	
	
	public void dodajKoralik()
	{
		Koralik koralik = new Koralik(okrag, animacjaWlaczona);
		koraliki.add(koralik);
		koralik.start();
	}
	
	public void wylaczAnimacje()
	{
		animacjaWlaczona.set(false);

	}
	public void uruchomAnimacje()
	{
		animacjaWlaczona.set(true);
	}
	
	public void zmienPredkosc(int id, float predkoscKatowa)
	{
		koraliki.get(id).zmniejszPredkosc(predkoscKatowa);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		okrag.narysuj(g);
		
		for (Koralik koralik : koraliki)
		{
			koralik.narysuj(g);
		}
	}


	@Override
	public void run() {
		Date czas = new Date();
		Date czas2 = new Date();
		//odœwie¿anie 60klatek na sekundê
		while(true)		
		{
			czas2 = new Date();
			if(animacjaWlaczona.get())
			{ 
				
				if(((czas2.getTime()-czas.getTime())) > 20)
				{
					czas = new Date();
					repaint();
				}
			}
			else
			{
				czas = new Date();
			}
		}
	}
	
	class WatekKolizji extends Thread
	{
		int licznik = 0;
		@Override
		public void run()
		{
			while(true)
			{
				int ilosc = koraliki.size();
				int ileZatrzymanychWatkow = 0;
				for(int i = 0 ; i < ilosc; i++)
				{
					Koralik koralik = koraliki.get(i);
					boolean kolizja = false;

					if (i == ilosc - 1)
					{
						if (Koralik.porownaj(koralik, koraliki.get(0)))
						{
							kolizja = true;
						}
					}
					else if (Koralik.porownaj(koralik, koraliki.get(i+1)))
					{
						kolizja = true;	
					}
					if(kolizja)
					{
						koralik.zatrzymaj();
						ileZatrzymanychWatkow++;
					}
					else
					{
						koralik.wznow();
					}
					if (ileZatrzymanychWatkow == ilosc)
					{
						koraliki.get(0).wznow();
					}
				}
				
				try {
					sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			}
		}

	}
}