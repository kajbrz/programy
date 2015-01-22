
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
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


/*
    Tę klasę trzeba uruchomić w konsoli, 
    ponieważ nie zawiera graficznego interfejsu,
    jest to terminal do obsługi komunikacji pomiędzy użytkownikami
*/
class Main
{
    public static void main(String args[]) throws IOException
    {
        TerminalSerweraTCP terminalSerweraTCP = new TerminalSerweraTCP();
        
    }
}

public class TerminalSerweraTCP extends Thread{
    private Integer indeksOfPokoje = 0;
    private ConcurrentHashMap<Integer, ArrayList<User>> listaPokoi;
    private ConcurrentHashMap<User, WatekUzytkownika> gniazdaUzytkownikow;
    private ServerSocket gniazdoSerwera;
    

    public TerminalSerweraTCP() throws IOException
    {
        try {
            gniazdoSerwera = new ServerSocket(3310);
        } catch (IOException ex) {
            //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR : nie mozna bylo utworzyc serwera");
            System.exit(-1);
        }
        //inicjalizacja zmiennych;
        listaPokoi = new ConcurrentHashMap<>();
        gniazdaUzytkownikow = new ConcurrentHashMap<>();
        
        System.out.println("Zainicjowano serwer...");
        this.start();
        odswierzaniePokoi();
        
        System.out.println(" Enter, aby zakończyć ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        zamknij();
        System.exit(0);
    }
    
    public void zamknij() throws IOException
    {
        gniazdoSerwera.close();
    }
    
    
    public void dodajDoPokoju(Integer numerPokoju, User user)
    {
        ArrayList<User> uzytkownicy = listaPokoi.get(numerPokoju);
        
        uzytkownicy.add(user);
    }
    
    public void dodajDoPokoju(Integer numerPokoju, WatekUzytkownika watekUzytkownika)
    {
        for(Entry<User, WatekUzytkownika> wejscie : gniazdaUzytkownikow.entrySet())
        {
            if(watekUzytkownika.equals(wejscie.getValue()))
            {
                dodajDoPokoju(numerPokoju, wejscie.getKey());
                break;
            }
        }
    } 
    
    public void usunZPokoju(Integer numerPokoju, User user)
    {
        ArrayList<User> uzytkownicy = listaPokoi.get(numerPokoju);
        uzytkownicy.remove(user);
    }

    public void usunZPokoju(Integer numerPokoju, WatekUzytkownika watekUzytkownika)
    {
        for(Entry<User, WatekUzytkownika> wejscie : gniazdaUzytkownikow.entrySet())
        {
            if(watekUzytkownika.equals(wejscie.getValue()))
            {
                usunZPokoju(numerPokoju, wejscie.getKey());
                break;
            }
        }
    }
    
    public void utworzPokoj(User user) throws IOException
    {
        System.out.println("\nPróba utworzenia pokoju");
        ArrayList<User> uzytkownicy = new ArrayList<User>();
        listaPokoi.put(++indeksOfPokoje, uzytkownicy);
        uzytkownicy.add(user);
        napiszDoUzytkownia(user, "/createroom " + indeksOfPokoje);
    }
    
    public void utworzPokoj(WatekUzytkownika watekUzytkownika) throws IOException
    { 
        for(Entry<User, WatekUzytkownika> wejscie : gniazdaUzytkownikow.entrySet())
        {
            if(watekUzytkownika.equals(wejscie.getValue()))
            {
                utworzPokoj(wejscie.getKey());
                break;
            }
        }
    }
    public void napiszDoUzytkownia(User user, String wiadomosc) throws IOException
    {
        
        gniazdaUzytkownikow.get(user).napisz(wiadomosc);
    }
    public void napiszDoUzytkownia(WatekUzytkownika watekUzytkownika, String wiadomosc) throws IOException
    {
        for(Entry<User, WatekUzytkownika> wejscie : gniazdaUzytkownikow.entrySet())
        {
            if(watekUzytkownika.equals(wejscie.getValue()))
            {
                napiszDoUzytkownia(wejscie.getKey(), wiadomosc);
                break;
            }
        }
    }
    
    public void napiszDoPokoju(Integer numerPokoju, String wiadomosc) throws IOException
    {
        napiszDoUzytkownikow(listaPokoi.get(numerPokoju), wiadomosc);
    }
    
    public void napiszDoUzytkownikow(ArrayList<User> uzytkownicy, String wiadomosc) throws IOException
    {
        for(User user : uzytkownicy)
        {
            gniazdaUzytkownikow.get(user).napisz(wiadomosc);
        }
    }
    
    @Override
    public void run()
    {
       
       ObjectInputStream strumienUzytkownika = null;
       while(true)
       {
           try {
               Socket noweGniazdko = gniazdoSerwera.accept(); //oczekiwanie na nowego klienta
               System.out.print("Zalogowal się nowy użytkownik...  ");
               
               //odczytanie informacji
               strumienUzytkownika = new ObjectInputStream(noweGniazdko.getInputStream());
               String komenda = (String)strumienUzytkownika.readObject();
               String []komendy = komenda.split(" ");
               
               
               int port = 0;
               try
               {
                   port = Integer.parseInt(komendy[3]);
               
               }
               catch(NumberFormatException ex)
               {
                   
               }
               System.out.println(komendy[1]);
               User user = new User(
                       komendy[1], komendy[2], Integer.parseInt(komendy[3]));
               WatekUzytkownika watekUzytkownika = new WatekUzytkownika(noweGniazdko, this, strumienUzytkownika);
               gniazdaUzytkownikow.put(user,watekUzytkownika);
               //strumienUzytkownika.close();
           } catch (IOException ex) {
               //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ClassNotFoundException ex) {
               //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }

    private void odswierzaniePokoi() {
        //Funkcja tworzy
         //Wątek który będzie co 10 sekund wysyłał aktualną listę pokoi 
        Thread watek = new Thread(new Runnable() {
           @Override
           public void run() {
               Iterator iterator6 = (Iterator) gniazdaUzytkownikow.keys();
               while(iterator6.hasNext())
               {
                   User user = (User)iterator6.next();
                   System.out.print(user.getNazwa() + " " );
               }
               System.out.println("");
               
               while(true)
               {
                    //System.out.printf("\nWysylanie pokoi ");
                    if (gniazdaUzytkownikow.size() > 0)
                    {
                         Iterator iterator = (Iterator) gniazdaUzytkownikow.keys();
                         String pokoje = "";
                         while(iterator.hasNext())
                         {
                            pokoje = "/room";
                            Iterator numeryPokoi = (Iterator)listaPokoi.keys();
                            while(numeryPokoi.hasNext())
                            {
                                pokoje += " " + numeryPokoi.next();
                            }
                         
                            try {
                                gniazdaUzytkownikow.get(iterator.next()).napisz(pokoje);
                            } catch (IOException ex) {
                                //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
                            }
                         }
                     }
                     try {
                         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                         Thread.sleep(1000L);
                     } catch (InterruptedException ex) {
                         //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
                     }
               }
           }
        });
        watek.start();    
    }
    
    //Obsluga rządzań od klientów
    void message(String[] parametry, WatekUzytkownika watekUzytkownika) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        User user = null;
        for(Entry<User, WatekUzytkownika> wejscie : gniazdaUzytkownikow.entrySet())
        {
            if(watekUzytkownika.equals(wejscie.getValue()))
            {
                user = wejscie.getKey();
                break;
            }
        }
        
        String wiadomosc = "";
        for(int i = 1; i < parametry.length; i++) 
        {
            wiadomosc += parametry[i] + " ";
        }
        try
        {
            napiszDoPokoju(Integer.parseInt(parametry[0]), 
                    "/message " + parametry[0] + " " + user.getNazwa() + ": " + wiadomosc);
            
        }catch(NumberFormatException ex){
            try {
                napiszDoUzytkownia(watekUzytkownika, "/error Nie udało się dostarczyć wiadomości");
            } catch (IOException ex1) {
                //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            try {
                //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
                napiszDoUzytkownia(watekUzytkownika, "/error Nie udało się dostarczyć wiadomości");
            } catch (IOException ex1) {
                //Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    void connect(String[] parametry, WatekUzytkownika watekUzytkownika) {
        try {
            int n = Integer.parseInt(parametry[0]);
            dodajDoPokoju(n, watekUzytkownika);
            
            napiszDoUzytkownia(watekUzytkownika, "/createroom " + n);
        } catch (IOException ex) {
            Logger.getLogger(TerminalSerweraTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void close(WatekUzytkownika watekUzytkownika) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
        User user = null;
        for(Entry<User, WatekUzytkownika> wejscie : gniazdaUzytkownikow.entrySet())
        {
            if(watekUzytkownika.equals(wejscie.getValue()))
            {
                user = wejscie.getKey();
                break;
            }
        }
        close(user);
    }

    private void close(User user) {
        for(ArrayList<User> uzytkownicyPokoi : listaPokoi.values())
        {
            uzytkownicyPokoi.remove(user);
            if (uzytkownicyPokoi.size() == 0)
            {
                for(Entry<Integer, ArrayList<User>> wejscie : listaPokoi.entrySet())
                {
                    if(uzytkownicyPokoi.equals(wejscie.getValue()))
                    {
                        listaPokoi.remove(wejscie.getValue());
                        
                    }
                }
            }
        }
        gniazdaUzytkownikow.remove(user);
        
        System.out.println("Usunięto użytkowników");
    }
}
/*
    @Struktura uzytkownika
*/
class User
{
    private String nazwa, ip;
    private int port;
    
    public User(String nazwa, String ip, int port)
    {
        this.nazwa = nazwa;
        this.ip = ip;
        this.port = port;
    }    
    public String getNazwa()
    {
        return nazwa;
    }
    public String getIP()
    {
        return ip;
    }
    public int getPort()
    {
        return port;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.nazwa);
        hash = 89 * hash + Objects.hashCode(this.ip);
        hash = 89 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.nazwa, other.nazwa)) {
            return false;
        }
        if (!Objects.equals(this.ip, other.ip)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }
}