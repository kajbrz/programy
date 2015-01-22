/*
 * @author Kajetan Kacper Brzuszczak
 * @indeks 209869
 * @lab laboratorium 5
 * @zad 1
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ProducentKonsumentWlasna{

	/**
	 * @param argv
	 */
	public static void main(String argv[])
	{
		new Okno();		
	}
	
	
}


 
class Okno extends JFrame implements Runnable, ActionListener
{
	/**
	 * 
	 */
	PanelAnimacji panelAnimacji;
	JButton przyciskDodajProducenta = new JButton("Dodaj producenta");
	JButton przyciskDodajKonsumenta = new JButton("Dodaj konsumenta");
	JButton przyciskZatrzymajWznow = new JButton("Stop");
	JLabel iloscKonsumentow = new JLabel("      ");
	JLabel iloscProducentow = new JLabel("      ");
	JLabel iloscBufora = new JLabel("      ");
	JPanel pane = new JPanel();
	private static final long serialVersionUID = -5542107354408136360L;
	
	
	Okno()
	{
		super("Lab 1");

		try {
			panelAnimacji = new PanelAnimacji();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
                        System.exit(1);
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(300,400));
		setVisible(true);	
		setContentPane(panelAnimacji);
		
		przyciskDodajKonsumenta.addActionListener(this);
		przyciskDodajProducenta.addActionListener(this);
		przyciskZatrzymajWznow.addActionListener(this);

		pane = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -1728724766489705329L;

			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				iloscKonsumentow.setText(String.valueOf(panelAnimacji.pobierzIloscKonsumentow()));
				iloscBufora.setText(String.valueOf(panelAnimacji.pobierzIloscBufora()));
				iloscProducentow.setText(String.valueOf(panelAnimacji.pobierzIloscProducentow()));
			}
		};
		
		pane.add(iloscKonsumentow);
		pane.add(iloscBufora);		
		pane.add(iloscProducentow);
		add(przyciskDodajKonsumenta);
		add(przyciskDodajProducenta);
		add(pane);
		add(przyciskZatrzymajWznow);
	
                Thread watek = new Thread(this);
		watek.start();
		
	}
	
	@Override
	public void run()
	{
		
		while(true)
		{
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object o = arg0.getSource();
		
		if (o == przyciskDodajKonsumenta)
		{
			panelAnimacji.dodajKonsumenta();
		}
		else if (o == przyciskDodajProducenta)
		{
			panelAnimacji.dodajProducenta();
			
		}
		else if (o == przyciskZatrzymajWznow)
		{
			if (przyciskZatrzymajWznow.getText().equals("Stop"))
			{
				try {
					panelAnimacji.zatrzymajWszystkie();
					przyciskZatrzymajWznow.setText("Start");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
			{
				przyciskZatrzymajWznow.setText("Stop");
				panelAnimacji.wznowWszystkie();
			}
		}
	}
	
	
}






















