import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * @author Kajetan Brzuszczak
 * @indeks 209869
 * @kierunek INF W-4
 * @laboratorium 5 
 */
public class PanelAnimacji extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4368533596252786272L;
	
	private Bufor bufor = new Bufor(10);

	ArrayList<ProducentWlasna> producenci = new ArrayList<ProducentWlasna>();
	ArrayList<KonsumentWlasna> konsumenci = new ArrayList<KonsumentWlasna>();
	
	
	PanelAnimacji() throws InterruptedException
	{
		//bufor.doloz();	
	}
	
	public void dodajProducenta()
	{
		ProducentWlasna producentWlasna = new ProducentWlasna(bufor);
		producenci.add(producentWlasna);
		producentWlasna.start();	
	}
	
	public void dodajKonsumenta()
	{
		KonsumentWlasna konsumentWlasna = new KonsumentWlasna(bufor);
		konsumenci.add(konsumentWlasna);
		konsumentWlasna.start();
	}
	
	public int pobierzIloscKonsumentow()
	{
		return konsumenci.size();
	}
	public int pobierzIloscProducentow()
	{
		return producenci.size();
	}
	public int pobierzIloscBufora()
	{
		return bufor.pobierzIlosc();
	}
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		bufor.narysuj(g);
		for(ProducentWlasna producent : producenci)
			producent.narysuj(g);
		for(KonsumentWlasna konsument : konsumenci)
			konsument.narysuj(g);
	}
	
	@SuppressWarnings("deprecation")
	public void zatrzymajWszystkie() throws InterruptedException
	{
		for(ProducentWlasna producent : producenci)
			producent.suspend();
		for(KonsumentWlasna konsument : konsumenci)
			konsument.suspend();
		
	}
	
	@SuppressWarnings("deprecation")
	public void wznowWszystkie()
	{
		for(ProducentWlasna producent : producenci)
			producent.resume();
		for(KonsumentWlasna konsument : konsumenci)
			konsument.resume();
	}
}
