/*
    WatekUzytkownika (eng. thread of uesr) - it is a class which handles 
    connection beetween host and client, and receives messages from client
*/
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
    /*
        Creating a new WatekUzytkownika
    */
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
    /*
        This method responds on commends, and depending on first
    */
    public void ObslugaZdarzen(String komenda) throws IOException
    {
        System.out.println("Wiadomosc od użytkownika: " + komenda);
        String []wiadomosc = komenda.toLowerCase().split(" ");
        String komend = wiadomosc[0];
        String []parametry = Arrays.copyOfRange(wiadomosc, 1, wiadomosc.length);
        switch(komend)
        {
            case "/newroom": //make a new room
            {
                terminalSerweraTCP.utworzPokoj(this);
                break;
            } 
            case "/message": //send a message
            {
                terminalSerweraTCP.message(parametry, this);
                break;
            }
            case "/connect": //join to the room
            {
                terminalSerweraTCP.connect(parametry,this);
                break;
            }
            case "/exit": // exit session
            {
                strumienOdbioru.close();
                strumienWysylu.close();
                
                gniazdoUzytkownika.close();
                
                terminalSerweraTCP.close(this);
                break;
            }
        }
        
    }
    /*
        Send message
    */
    public void napisz(String wiadomosc) throws IOException
    {
        strumienWysylu.writeObject(wiadomosc);
    }
    
    /*
        Thread where we waiting for new messages
    */
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
