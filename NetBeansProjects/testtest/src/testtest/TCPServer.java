/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */
class TCPServer extends Thread
{    
    ServerGUI serverGUI;
    
    PhoneBook ksiazkaTelefoniczna = new PhoneBook();
    
    boolean zakoncz = false;
    Boolean LOCK = false;
    
    ServerSocket GniazdoSerwera;
    
    Vector<WatekKlienta> listaPolaczonychKlientow  = new Vector<>();
    
    TCPServer(int numerPortu, ServerGUI serverGUI) throws IOException 
    {
        this.serverGUI = serverGUI;
        GniazdoSerwera = new ServerSocket(numerPortu);
        GniazdoSerwera.setSoTimeout(1000);
        this.start();
    }
    
    TCPServer(ServerGUI serverGUI) throws IOException
    {
        this(3310, serverGUI);
    }
   
    public void zakoncz()
    {
        zakoncz = true;
    }
    
    public void napisz(int numerKlienta, String wiadomosc) throws IOException
    {
        if(numerKlienta < 0 || numerKlienta >= listaPolaczonychKlientow.size())
        {
            return;
        }
        
        listaPolaczonychKlientow.get(numerKlienta).napisz(wiadomosc);
    }
    
    @Override
    public void run()
    {
        while(!zakoncz)
        {
            try {
                Socket gniazdko = GniazdoSerwera.accept();
                
                if(gniazdko != null)
                {
                    synchronized(LOCK)
                    {
                        listaPolaczonychKlientow.add(new WatekKlienta(gniazdko, serverGUI));
                        listaPolaczonychKlientow.lastElement().setReferencjaKsiazkiTelefonicznej(ksiazkaTelefoniczna);
                    }
                   // System.out.print("Nawiązano nowe połączenie");
                }
            } 
            catch (SocketException ex)
            {
                continue;
            } 
            catch (IOException ex) {
                //Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void zamknijWszystkieGniazda() throws IOException 
    {
        for (WatekKlienta watekKlienta : listaPolaczonychKlientow)
        {
            watekKlienta.napisz("exit");
        }
        for (WatekKlienta watekKlienta : listaPolaczonychKlientow)
        {
            listaPolaczonychKlientow.remove(watekKlienta);
        }
    }
}