/* 
 *  Komunikator sieciowy
 *   - program serwera
 *
 *  Autor: Pawel Rogalinski
 *   Data: 1 stycznia 2010 r.
 */
 
import java.net.*;
import java.io.*;   

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


class Serwer extends JFrame implements ActionListener, Runnable {
	private JLabel e_odbiorca = new JLabel("Odbiorca:");
	private JLabel e_mess = new JLabel("Napisz:");
	private JLabel e_text = new JLabel("Dialog:");
	private JComboBox menuKlient = new JComboBox();
	private JTextField message = new JTextField(20);
	private JTextArea  textArea  = new JTextArea(15,18);
        
	private JScrollPane scroll = new JScrollPane(textArea,
	  				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	
	static final int SERVER_PORT = 15000;
	private String host;
	private ServerSocket serwer;

	
	Serwer(){ 
		super("SERWER");
	  	setSize(300,340);
	  	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  	JPanel panel = new JPanel();
	  	panel.add(e_odbiorca);
	  	menuKlient.setPrototypeDisplayValue("#########################");
	  	panel.add(menuKlient);
	 	panel.add(e_mess);
	  	panel.add(message);
	  	message.addActionListener(this);
	  	textArea.setLineWrap(true);
	  	textArea.setWrapStyleWord(true);
	  	panel.add(e_text);
	  	textArea.setEditable(false);
	  	panel.add(scroll);
	  	setContentPane(panel);
	  	Thread t = new Thread(this); // Uruchomienie dodatkowego watka
	  	t.start();                   // czekajacego na nowych klientow
	  	setVisible(true);
	}
	
	synchronized public void wypiszOdebrane(WatekKlienta k, String s){
		String pom = textArea.getText();
		textArea.setText(k.getNazwa() + " >>> " + s + "\n" + pom);
	}
	
	synchronized public void wypiszWyslane(WatekKlienta k, String s){
	  	String pom = textArea.getText();
	  	textArea.setText(k.getNazwa() + " <<< " + s + "\n" + pom);
	}
	
	synchronized void dodajKlienta(WatekKlienta k){
	  	menuKlient.addItem(k);
	}  
		
	synchronized void usunKlienta(WatekKlienta k){
                menuKlient.removeItem(k);
	}
	
	public void actionPerformed(ActionEvent evt){
		String m;
		Object src = evt.getSource();
	  	if (src == message){
	  		WatekKlienta k = (WatekKlienta)menuKlient.getSelectedItem();
	  		if (k!=null){
	  			try{ m = message.getText();
	  				 k.getOutput().writeObject(m);
	  				 wypiszWyslane(k, m);
	  				 if (m.equals("exit")){
						usunKlienta(k);
	  				 }
	  			} catch(IOException e){ 
	  				System.out.println("Wyjatek serwera "+e);
	  			}
	  		}
	  	}
	  	repaint();
	}
	
	
	public void run(){
		Socket s;
	  	WatekKlienta klient;
		
	  	// inicjalizacja po��cze� sieciowych
	  	try{host = InetAddress.getLocalHost().getHostName();
	  	   	serwer = new ServerSocket(SERVER_PORT);
		} catch(IOException e){ 	
		 	System.out.println(e);
		   	JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie mo�e by� utworzone"); 
		   	System.exit(0);
		}
		System.out.println("Serwer zosta� uruchomiony na hoscie " + host);
	  	// koniec inicjalizacji po��cze� sieciowych
	  
	  	while(true){
			try { 
				s = serwer.accept();
				if (s!=null){
		  			klient = new WatekKlienta(this, s);
		  		}
			} catch(IOException e){ 
				System.out.println("BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
			}
		}
	}
	
	
	public static void main(String [] args){
		new Serwer();
	}
	
}



class WatekKlienta implements Runnable {
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private String nazwa;
	private Serwer okno;
	
	WatekKlienta(Serwer os, Socket s) throws IOException{ 
		okno = os;
	  	socket = s;
	  	Thread t = new Thread(this);  // Utworzenie dodatkowego watka
	  	t.start();                    // do obslugi komunikacji sieciowej
	}
	
	public String getNazwa(){ return nazwa; }
	
	public ObjectOutputStream getOutput(){ return output; }
	
	public String toString(){ return nazwa; }
	
	public void run(){  
		String m, pom;
	   	try{
	   		output = new ObjectOutputStream(socket.getOutputStream());
	  		input = new ObjectInputStream(socket.getInputStream());
	   		nazwa = (String)input.readObject();
	   		okno.dodajKlienta(this);
			while(true){
				m = (String)input.readObject();
                                System.out.print("Coś czyta");
				okno.wypiszOdebrane(this,m);
				if (m.equals("exit")){
					okno.usunKlienta(this);
					break;		
				}
			}
			input.close();
			output.close();
			socket.close();
			socket = null;
	   	} catch(Exception e) {}
	}
}



