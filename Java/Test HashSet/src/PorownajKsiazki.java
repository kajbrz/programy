/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kajetan Kacper Brzuszczak
 * Index 209869 
 * W-4 INF Języki Programowania z P. Rogalińskim
 */
public class PorownajKsiazki extends JFrame implements ActionListener {
    ///
    ///Utworzenie zmiennych kolekcji;
    ///
    Vector<Ksiazka> vektor = new Vector();
    ArrayList<Ksiazka> arraylist = new ArrayList();
    LinkedList<Ksiazka> linkedlist = new LinkedList();
    HashSet<Ksiazka> hashset = new HashSet();
    TreeSet<Ksiazka> treeset = new TreeSet();
    
    /*
        Wygląd okna
    */
    WidokKolekcjiK widokVector = new WidokKolekcjiK(vektor, 150, 200, "Vector List");
    WidokKolekcjiK widokArray = new WidokKolekcjiK(arraylist, 150, 200, "Array List");
    WidokKolekcjiK widokList = new WidokKolekcjiK(linkedlist, 150, 200, "Linked List");
    
    WidokSetK widokHash = new WidokSetK(hashset, 150, 200, "HashSet");
    WidokSetK widokTree = new WidokSetK(treeset, 150, 200, "TreeSet");
    
    JLabel etykieta_autor = new JLabel("Autor");
    JLabel etykieta_tytul  = new JLabel("Tytul:");
    JTextField pole_autor    = new JTextField(10);
    JTextField pole_tytul  = new JTextField(10);
    
    JButton przycisk_dodaj   = new JButton("Dodaj");
    JButton przycisk_usun    = new JButton("Usuń");
    JButton przycisk_wyczysc = new JButton("Wyczyść");
    JButton przycisk_sortuj = new JButton("Sortuj");
    JButton przycisk_autor   = new JButton("Autor");
    
    public PorownajKsiazki()
    {
        super("Program Porownaj Książki");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        
        panel.add(etykieta_autor);
        panel.add(pole_autor);
        
        panel.add(etykieta_tytul);
        panel.add(pole_tytul);
        
        panel.add(przycisk_dodaj);
        panel.add(przycisk_usun);
        panel.add(przycisk_sortuj);
        panel.add(przycisk_wyczysc);
        panel.add(przycisk_autor);
        //////////////////       
        panel.add(widokArray);
        panel.add(widokList);
        panel.add(widokVector);
        panel.add(widokHash);
        panel.add(widokTree);
        ////////////////////

        this.add(panel);
        this.setSize(new Dimension(640,480));
        
        
        przycisk_dodaj.addActionListener(this);
        przycisk_usun.addActionListener(this);
        przycisk_sortuj.addActionListener(this);
        przycisk_wyczysc.addActionListener(this);
        przycisk_autor.addActionListener(this);
        
        
        
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object obiekt = ae.getSource();
        
        if (obiekt == przycisk_dodaj)
        {
            Ksiazka ksiazka = new Ksiazka(pole_autor.getText(), pole_tytul.getText());
            vektor.add(ksiazka);
            arraylist.add(ksiazka);
            linkedlist.add(ksiazka);
            
            hashset.add(ksiazka);
            treeset.add(ksiazka);
            
            refresh();
        }
        else if (obiekt == przycisk_wyczysc)
        {
            vektor.clear();
            arraylist.clear();
            linkedlist.clear();
            
            hashset.clear();
            treeset.clear();
            
            refresh();
        }  
        else if (obiekt == przycisk_autor)
        {
            JOptionPane.showMessageDialog(this, "Kajetan Kacper Brzuszczak"
                    + " 2014 \nJęzyki Programowania\nINF W-4\nRok 2 Semestr 3", "Autor", 3);
        }   
        else if (obiekt == przycisk_sortuj)
        {
            vektor.sort(Ksiazka::compareTo);
            arraylist.sort(Ksiazka::compareTo);
            linkedlist.sort(Ksiazka::compareTo);
            
            refresh();
        }        
        else if (obiekt == przycisk_usun)
        {
            Ksiazka ksiazka = new Ksiazka(pole_autor.getText(), pole_tytul.getText());
            vektor.remove(ksiazka);
            arraylist.remove(ksiazka);
            linkedlist.remove(ksiazka);
            
            treeset.remove(ksiazka);
            hashset.remove(ksiazka);
            
            refresh();
        }
                
    }
    
    private void refresh()
    {
        widokArray.refresh();
        widokList.refresh();
        widokVector.refresh();
        widokHash.refresh();
        widokTree.refresh();
    }
    
    public static void main(String[] args)
    {
        new PorownajKsiazki();
    }
    
}

class WidokKolekcjiK extends JScrollPane {
		private static final long serialVersionUID = 1L;

		private JTable tabela;
		private DefaultTableModel modelTabeli;
		private AbstractList<Ksiazka> abstractList;
                
		WidokKolekcjiK(AbstractList<Ksiazka> abstractList, int szerokosc, int wysokosc,
				String opis) {
			String[] kolumny = { "Autor:", "Tytul:"};
			modelTabeli = new DefaultTableModel(kolumny, 0);
			tabela = new JTable(modelTabeli);
			tabela.setRowSelectionAllowed(false);
			this.abstractList = abstractList;
			setViewportView(tabela);
			setPreferredSize(new Dimension(szerokosc, wysokosc));
			setBorder(BorderFactory.createTitledBorder(opis));
		}

		void refresh(){
                    modelTabeli.setRowCount(0);
                    for (Ksiazka wartosc : abstractList) {                        
                        String[] wiersz = {wartosc.autor, wartosc.tytul};
                        modelTabeli.addRow(wiersz);
	    	}                
	    }
}
class WidokSetK extends JScrollPane {
		private static final long serialVersionUID = 1L;

		private JTable tabela;
		private DefaultTableModel modelTabeli;
		private AbstractSet<Ksiazka> set;

		WidokSetK(AbstractSet<Ksiazka> set, int szerokosc, int wysokosc,
				String opis) {
			String[] kolumny = { "Autor:", "Tytuł:" };
			modelTabeli = new DefaultTableModel(kolumny, 0);
			tabela = new JTable(modelTabeli);
			tabela.setRowSelectionAllowed(true);
			this.set = set;
			setViewportView(tabela);
			setPreferredSize(new Dimension(szerokosc, wysokosc));
			setBorder(BorderFactory.createTitledBorder(opis));
		}

		void refresh(){
	    	modelTabeli.setRowCount(0);
			for (Ksiazka ksiazka : set) {
				String[] wiersz = { ksiazka.autor, ksiazka.tytul };
				modelTabeli.addRow(wiersz);
	    	}
	    }
}