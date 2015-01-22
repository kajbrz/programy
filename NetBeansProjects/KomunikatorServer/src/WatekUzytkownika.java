
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */
class WatekUzytkownika extends Thread 
{
    TerminalSerweraTCP terminalSerweraTCP;
    
    ObjectOutputStream strumienWysylu;
    ObjectInputStream strumienOdbioru;
    
    Socket gniazdoUzytkownika;

    public WatekUzytkownika(Socket nowyUzytkownik, TerminalSerweraTCP terminalSerweraTCP, ObjectInputStream input) throws IOException 
    {
        this.terminalSerweraTCP = terminalSerweraTCP;
        this.gniazdoUzytkownika = nowyUzytkownik;
        strumienWysylu = new ObjectOutputStream(nowyUzytkownik.getOutputStream());
        strumienOdbioru = input;
        System.out.print(" Włączono strumień wysyłu. ");
        this.start();
        System.out.print(" Włączono wątek odbioru danych. ");
    }
    
    public void ObslugaZdarzen(String komenda) throws IOException
    {
        System.out.println("Wiadomosc od użytkownika: " + komenda);
        String []wiadomosc = komenda.toLowerCase().split(" ");
        String komend = wiadomosc[0];
        String []parametry = Arrays.copyOfRange(wiadomosc, 1, wiadomosc.length);
        switch(komend)
        {
            case "/newroom":
            {
                terminalSerweraTCP.utworzPokoj(this);
                break;
            } 
            case "/message":
            {
                terminalSerweraTCP.message(parametry, this);
                break;
            }
            case "/connect":
            {
                terminalSerweraTCP.connect(parametry,this);
                break;
            }
            case "/exit":
            {
                strumienOdbioru.close();
                strumienWysylu.close();
                
                gniazdoUzytkownika.close();
                
                terminalSerweraTCP.close(this);
                break;
            }
        }
        
    }
    
    public void napisz(String wiadomosc) throws IOException
    {
        strumienWysylu.writeObject(wiadomosc);
    }
    
    
    @Override
    public void run()
    {
        String wiadomosc = "";
        while(true)
        {
            try {
                wiadomosc = (String)strumienOdbioru.readObject();
                ObslugaZdarzen(wiadomosc);
            } catch (NullPointerException ex) {
                System.out.println("Uzytkownik opuszcza serwer");
                break;
            } catch (IOException ex) {
                //Logger.getLogger(WatekUzytkownika.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                //Logger.getLogger(WatekUzytkownika.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
        }        
    }

}
