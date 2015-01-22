/* 
 *  Komunikator sieciowy
 *   - program klienta
 *
 *  Autor: Pawel Rogalinski
 *   Data: 1 stycznia 2010 r.
 */

import java.net.*;
import java.io.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


class Klient extends JFrame implements ActionListener, Runnable
{
	private JTextField message = new JTextField(20);
	private JTextArea  textArea  = new JTextArea(15,18);
	
	static final int SERVER_PORT = 15000;
	private String nazwa;
	private String host;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	Klient(String n)
	{ super(n);
	  nazwa = n;
	  setSize(300,310);
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  JPanel panel = new JPanel();
	  JLabel e_mess = new JLabel("Napisz:");
	  JLabel e_text = new JLabel("Dialog:");
	  textArea.setLineWrap(true);
	  textArea.setWrapStyleWord(true);
	  panel.add(e_mess);
	  panel.add(message);
	  message.addActionListener(this);
	  panel.add(e_text);
	  JScrollPane scroll_bars = new JScrollPane(
	  		textArea,
	  		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	  panel.add(scroll_bars);
	  setContentPane(panel);
	  Thread t = new Thread(this);
	  t.start();
	  setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evt)
	{ 
           String m;
	  Object src = evt.getSource();
	  if (src==message)
	  {
	  	try{ m = message.getText();
	  		 output.writeObject(m);
	  		 String pom = textArea.getText();
		 	 textArea.setText(pom + "<<< " + m + "\n");
	  		 if (m.equals("exit")){
	  		 	input.close();
	  		 	output.close();
	  		 	socket.close();
	  		 	setVisible(false);
	  		 	dispose();
	  		 	return;
	  		 }
	  	}catch(IOException e)
	  	{ System.out.println("Wyjatek klienta "+e);
	  		}
	  }
	  repaint();
	}
	
	public void run(){
		try{host = InetAddress.getLocalHost().getHostName();
	  		socket = new Socket(host, SERVER_PORT);
	  		input = new ObjectInputStream(socket.getInputStream());
	  		output = new ObjectOutputStream(socket.getOutputStream());
	  		output.writeObject(nazwa);
	  	} catch(IOException e){ 
		 	System.out.println(e);
		   	JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
		   	setVisible(false);
		   	dispose();  // zwolnienie zasob�w graficznych 
		               // okno graficzne nie zostanie utworzone
		    return;
		 }
		 try{
		 	while(true){
		 		String m = (String)input.readObject();
		 		String pom = textArea.getText();
		 		textArea.setText(pom + ">>> " + m + "\n");
		 		if(m.equals("exit")){
		 			input.close();
	  		 		output.close();
	  		 		socket.close();
	  		 		setVisible(false);
	  		 		dispose();
	  		 		break;
		 		}
		 	}
		 } catch(Exception e){
		 	System.out.println(e);
		   	JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
		   	setVisible(false);
		   	dispose();
		 }	
	}
	
	
	public static void main(String [] args){
		String nazwa;
			
		nazwa =JOptionPane.showInputDialog("Podaj nazwe klienta");
		if (nazwa !=null && !nazwa.equals("")){
			new Klient(nazwa);
		}
	}

}
