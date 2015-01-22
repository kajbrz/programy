import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;



public class Test extends JFrame implements ActionListener
{
	private Vector<Integer> vectorList = new Vector<Integer>();
	private Vector<MojaKlasa> mojejKlasy = new Vector<MojaKlasa>();
	
	ZapisIOdczytKolekcjiDoPliku vectorZapOdcz = new ZapisIOdczytKolekcjiDoPliku(vectorList);
	ZapisIOdczytMojejKlasyDoPliku mojejKlasyZapOdcz = new ZapisIOdczytMojejKlasyDoPliku(mojejKlasy);
	
	

	private TabelkaKolekcja tabelkaKolekcja = new TabelkaKolekcja(vectorList);
	private TabelkaMojaKlasa tabelkaMojaKlasa= new TabelkaMojaKlasa(mojejKlasy);

	JButton b = new JButton("sort(I)");
	JButton wybierzMiejsceDoceloweDlaPliku = new JButton("Zapisz do(I)...");
	JButton wybierzMiejsceDoceloweDlaPliku2 = new JButton("Odczyt z(I)...");
	
	JButton b1 = new JButton("sort(MK)");
	JButton wybierzMiejsceDoceloweDlaPlikuMojaKlasa = new JButton("Zapisz do(MK)...");
	JButton wybierzMiejsceDoceloweDlaPlikuMojaKlasa2 = new JButton("Odczyt z(MK)...");
	
	public Test()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(640,480));
		setVisible(true);
		
		
		JPanel jpanel = (JPanel)getContentPane();
		jpanel.setLayout(new FlowLayout());
		jpanel.add(wybierzMiejsceDoceloweDlaPliku);
		jpanel.add(wybierzMiejsceDoceloweDlaPliku2);
		jpanel.add(b);
		
		jpanel.add(wybierzMiejsceDoceloweDlaPlikuMojaKlasa);
		jpanel.add(wybierzMiejsceDoceloweDlaPlikuMojaKlasa2);
		jpanel.add(b1);

		b.addActionListener(this);
		
		wybierzMiejsceDoceloweDlaPliku2.addActionListener(this);
		
		wybierzMiejsceDoceloweDlaPliku.addActionListener(this);
		
		
		b1.addActionListener(this);
		
		wybierzMiejsceDoceloweDlaPlikuMojaKlasa.addActionListener(this);
		
		wybierzMiejsceDoceloweDlaPlikuMojaKlasa2.addActionListener(this);
		
		int n = ((int)(Math.random()*20));
		for(int i = 0 ; i < n; i++)
		{
			vectorList.addElement(new Integer(((int)(Math.random()*100))));
		}
		
		n = ((int)(Math.random()*20));
		for(int i = 0 ; i < n; i++)
		{
			mojejKlasy.addElement(new MojaKlasa());
		}
		
		
		jpanel.add(tabelkaKolekcja, new FlowLayout().LEFT);
		jpanel.add(tabelkaMojaKlasa, new FlowLayout().LEFT);
		
		tabelkaKolekcja.refresh();
		tabelkaMojaKlasa.refresh();
	}
	
	
	public static void main(String agrv[])
	{

		new Test();
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == wybierzMiejsceDoceloweDlaPliku)
		{
			JFileChooser wybieraniePliku  = new JFileChooser();
			
			int wartosc = wybieraniePliku.showSaveDialog(wybieraniePliku);
			if (wartosc != 0)
				return;
			String sciezka = wybieraniePliku.getSelectedFile().toString();
			
			vectorZapOdcz.ustawSciezke(sciezka);
			
			boolean zapisano = vectorZapOdcz.zapisTekstowy();
			
			if(zapisano == false)
			{
				System.out.print("B³¹d zapisu");
			}
			else
				System.out.print("Poprawny zapis");
				
		}
		else if(o == wybierzMiejsceDoceloweDlaPliku2)
		{
			JFileChooser wybieraniePliku  = new JFileChooser();
			
			int wartosc = wybieraniePliku.showOpenDialog(wybieraniePliku);
			if (wartosc != 0)
				return;
			String sciezka = wybieraniePliku.getSelectedFile().toString();
			
			vectorZapOdcz.ustawSciezke(sciezka);
			
			boolean odczytano = vectorZapOdcz.odczyt();
			
			if(odczytano == false)
			{
				System.out.print("B³¹d odczytu");
			}
			else
				System.out.print("Poprawny odczyt");
		}
		else if(o == b)
		{
			Collections.sort(vectorList);

		}
		else if(o == wybierzMiejsceDoceloweDlaPlikuMojaKlasa)
		{
			JFileChooser wybieraniePliku  = new JFileChooser();
			
			int wartosc = wybieraniePliku.showSaveDialog(wybieraniePliku);
			if (wartosc != 0)
				return;
			String sciezka = wybieraniePliku.getSelectedFile().toString();
			
			mojejKlasyZapOdcz.ustawSciezke(sciezka);
			
			boolean zapisano = mojejKlasyZapOdcz.zapisTekstowy();
			
			if(zapisano == false)
			{
				System.out.print("B³¹d zapisu");
			}
			else
				System.out.print("Poprawny zapis");
				
		}
		else if(o == wybierzMiejsceDoceloweDlaPlikuMojaKlasa2)
		{
			JFileChooser wybieraniePliku  = new JFileChooser();
			
			int wartosc = wybieraniePliku.showOpenDialog(wybieraniePliku);
			if (wartosc != 0)
				return;
			String sciezka = wybieraniePliku.getSelectedFile().toString();
			
			mojejKlasyZapOdcz.ustawSciezke(sciezka);
			
			boolean odczytano = mojejKlasyZapOdcz.odczyt();
			
			if(odczytano == false)
			{
				System.out.print("B³¹d odczytu");
			}
			else
				System.out.print("Poprawny odczyt");
		}
		else if(o == b1)
		{
			mojejKlasy.sort(MojaKlasa.porownywanieWszystkiego);

		}
		repaint();
		tabelkaMojaKlasa.refresh();
		tabelkaKolekcja.refresh();
	}
}

class MojaKlasa implements Comparable, Serializable
{
	String string;
	Integer integer;
	
	MojaKlasa()
	{
		String[] arr = {"Ania",
				"Bo¿ena",
				"Michasia",
				"Asia",
				"Bernadeta"
		};
		
		string = arr[(int)(Math.random()*5)];
		
		integer = (int)(Math.random()*1000);
	}

	@Override
	public int compareTo(Object arg0) {
		
		return 0;
	}
	
	public static Comparator<MojaKlasa> porownywanieWszystkiego 
		= new Comparator<MojaKlasa>(){

			@Override
			public int compare(MojaKlasa arg0, MojaKlasa arg1) {
				int nazwa = arg0.string.compareTo(arg1.string);
				int liczba = arg0.integer - arg1.integer;
				
				return nazwa*1000 + liczba;
			}
	
	};
}
class ZapisIOdczytMojejKlasyDoPliku
{
	private String sciezka = null;
	private Collection<MojaKlasa> collection = null;
	
	ZapisIOdczytMojejKlasyDoPliku()
	{
		
	}
	ZapisIOdczytMojejKlasyDoPliku(Collection<MojaKlasa> collection, String sciezka)
	{
		this.sciezka = sciezka;
		this.collection = collection;
	}
	ZapisIOdczytMojejKlasyDoPliku(Collection<MojaKlasa> collection)
	{
		this.collection = collection;
	}
	
	public void ustawSciezke(String sciezka)
	{
		this.sciezka = sciezka;
	}
	
	
	public boolean zapisTekstowy()
	{
		if(sciezka == null || collection == null)
		{
			return false;
		}

		File file = new File(sciezka);
		

		System.out.println("\nTEST\n");
		file.setWritable(true);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream p = null;
		try {
			p = new ObjectOutputStream(fileOutputStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (MojaKlasa mojaKlasa : collection)
		{
			try {
				//fileOutputStream.write(integer.byteValue());
				p.writeObject(mojaKlasa);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	public boolean odczyt()
	{
		if(sciezka == null || collection == null)
		{
			return false;
		}

		File file = new File(sciezka);
		if(!file.exists())
			return false;
		
		System.out.println("\nTEST\n");
		file.setWritable(true);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		collection.clear();
		
		ObjectInputStream p = null;
		try {
			p = new ObjectInputStream(fileInputStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			while(fileInputStream.available() != 0)
			{
				MojaKlasa mojaKlasa = null;
				try {
					mojaKlasa = (MojaKlasa)p.readObject();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				collection.add(mojaKlasa);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			fileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
}
class ZapisIOdczytKolekcjiDoPliku
{
	private String sciezka = null;
	private Collection<Integer> collection = null;
	
	ZapisIOdczytKolekcjiDoPliku()
	{
		
	}
	ZapisIOdczytKolekcjiDoPliku(Collection<Integer> collection, String sciezka)
	{
		this.sciezka = sciezka;
		this.collection = collection;
	}
	ZapisIOdczytKolekcjiDoPliku(Collection<Integer> collection)
	{
		this.collection = collection;
	}
	
	public void ustawSciezke(String sciezka)
	{
		this.sciezka = sciezka;
	}
	
	
	public boolean zapisTekstowy()
	{
		if(sciezka == null || collection == null)
		{
			return false;
		}

		File file = new File(sciezka);
		

		System.out.println("\nTEST\n");
		file.setWritable(true);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream p = null;
		try {
			p = new ObjectOutputStream(fileOutputStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (Integer integer : collection)
		{
			try {
				//fileOutputStream.write(integer.byteValue());
				p.writeObject(integer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	public boolean odczyt()
	{
		if(sciezka == null || collection == null)
		{
			return false;
		}

		File file = new File(sciezka);
		if(!file.exists())
			return false;
		
		System.out.println("\nTEST\n");
		file.setWritable(true);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		collection.clear();
		
		ObjectInputStream p = null;
		try {
			p = new ObjectInputStream(fileInputStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			while(fileInputStream.available() != 0)
			{
				Integer integer = null;
				try {
					integer = (Integer)p.readObject();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				collection.add(integer);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			fileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	
	
}


@SuppressWarnings("serial")
class TabelkaMojaKlasa extends JScrollPane
{
	private JTable tabelaV;
	private DefaultTableModel modelTabeli;
	private Collection<MojaKlasa> collection;
	public TabelkaMojaKlasa(Collection<MojaKlasa> collection)
	{
		this.collection = collection;
		String[] kolumny = {"LP", "Wartoœæ", "Nazwa"};
		modelTabeli = new DefaultTableModel(kolumny, 0);
		
		tabelaV = new JTable(modelTabeli);
		tabelaV.setRowSelectionAllowed(false);
		
		setViewportView(tabelaV);
		setPreferredSize(new Dimension(120,200));
		
		setBorder(BorderFactory.createTitledBorder("TabelaMojaKlasa"));
		
	}
	
	public void refresh()
	{
		modelTabeli.setRowCount(0);
		Integer i = new Integer(0);
		for(MojaKlasa mojaKlasa : collection)
		{
			String[] wiersz = { i.toString(), new Integer(mojaKlasa.integer).toString(), mojaKlasa.string};
			modelTabeli.addRow(wiersz);
			i++;
		}
	}
	
}






@SuppressWarnings("serial")
class TabelkaKolekcja extends JScrollPane
{
	private JTable tabelaV;
	private DefaultTableModel modelTabeli;
	private Collection<Integer> collection;
	public TabelkaKolekcja(Collection<Integer> collection)
	{
		this.collection = collection;
		String[] kolumny = {"LP", "Wartoœæ"};
		modelTabeli = new DefaultTableModel(kolumny, 0);
		
		tabelaV = new JTable(modelTabeli);
		tabelaV.setRowSelectionAllowed(false);
		
		setViewportView(tabelaV);
		setPreferredSize(new Dimension(120,200));
		
		setBorder(BorderFactory.createTitledBorder("Tabela Integer"));
		
	}
	
	public void refresh()
	{
		modelTabeli.setRowCount(0);
		Integer i = new Integer(0);
		for(Integer integer : collection)
		{
			String[] wiersz = { i.toString(), integer.toString()};
			modelTabeli.addRow(wiersz);
			i++;
		}
	}
	
}




























