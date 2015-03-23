/*
    This is the terminal where all data are receiving and 
    sending to correct clients.

    Terminal answering to the text commands from communicator.

    All is working on string commands.
    Simple Command has slash ('/') and the name of the command, the 
    rest of the string it is params of the command e.g. "/room 2", this mean
    call method "room", with param "2".
*/
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


/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */


class Main
{
    public static void main(String args[]) throws IOException
    {
        //Creating a new thread with terminal host;
        TerminalSerweraTCP terminalSerweraTCP = new TerminalSerweraTCP();
    }
}
/*
    This general class has to responding on commands from communicator, and keep
    information about rooms and clients in himself.
*/

public class TerminalSerweraTCP extends Thread{
    private Integer indeksOfPokoje = 0;  //index of rooms
    
    /*
        Every room has unique ID, and has many Users
    */
    private ConcurrentHashMap<Integer, ArrayList<User>> listaPokoi; //list of rooms
    
    /*
        Every WatekUzytkownika - (eng. UserThread) has unique User,
        and this HashMap keep connection beetween Clients and Host
    */    
    private ConcurrentHashMap<User, WatekUzytkownika> gniazdaUzytkownikow; //useres' sockets
    private ServerSocket gniazdoSerwera; //server socket
    
    
    public TerminalSerweraTCP() throws IOException
    {
        try {
            //Server has default socket on port 3310;
            gniazdoSerwera = new ServerSocket(3310);
        } catch (IOException ex) {
            System.out.println("ERROR : nie mozna bylo utworzyc serwera"); 
                //ERROR : connnot create server
            System.exit(-1);
        }
        //initializing variables;
        listaPokoi = new ConcurrentHashMap<>();
        gniazdaUzytkownikow = new ConcurrentHashMap<>();
        
        System.out.println("Zainicjowano serwer...");
            //Serwer is ready..
        
        this.start(); //start a Thread
        
        odswierzaniePokoi();
        
        System.out.println(" Enter, aby zakończyć ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        zamknij(); //close a serwer
        System.exit(0);
    }
    
    /*
        Closing a server
    */
    public void zamknij() throws IOException
    {
        gniazdoSerwera.close();
    }
    ///////////////////////////////////////////////
    /*
        From now to the "END", every methods has two forms of parameters:
        a) with refrence User - it is classic finding users
        b) with refrence WatekUzytkownika - it is another way to find users
    
        WatekUzytkownika will be descripted in WatekUzytkownika.java
    */
    
    /*
        This method adds a users to the room.
    */    
    public void dodajDoPokoju(Integer numerPokoju, User user)
            //public void addToRoom(Integer roomNumber, User user);
    {
        ArrayList<User> uzytkownicy = listaPokoi.get(numerPokoju);
        
        uzytkownicy.add(user);
    }
    /*
        This method is equivalent to the previous one, but hi is looking user
        after WatekUzytkownika
    */
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
    
    /*
        This method removes a users to the room.
    */
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
    
    /*
        This method creating a newRoom.
    */    
    public void utworzPokoj(User user) throws IOException
    {
        System.out.println("\nPróba utworzenia pokoju"); //Try to create room
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
    
    /*
        This method sends message to the client
    */
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
    
    //////////////////  END METHODS WITH TWO FORMS /////////////////////////////
    
    /*
        This method sends messages to the all people connected to the room
    */
    public void napiszDoPokoju(Integer numerPokoju, String wiadomosc) throws IOException
    {
        napiszDoUzytkownikow(listaPokoi.get(numerPokoju), wiadomosc);
    }
    
    /*
        This method sends messages to the list of users
    */
      public void napiszDoUzytkownikow(ArrayList<User> uzytkownicy, String wiadomosc) throws IOException
    {
        for(User user : uzytkownicy)
        {
            gniazdaUzytkownikow.get(user).napisz(wiadomosc);
        }
    }
    
    /*
      Main Thread, here we are waiting for new user,
    */
    @Override
    public void run()
    {
       ObjectInputStream strumienUzytkownika = null;
       while(true)
       {
           try {
               Socket noweGniazdko = gniazdoSerwera.accept(); //waiting for the client
               System.out.print("Zalogowal się nowy użytkownik...  "); //User has logged in..."
               
               //reading basic information about user -> name
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
               
               //Creating a new Handle with client
               WatekUzytkownika watekUzytkownika = new WatekUzytkownika(noweGniazdko, this, strumienUzytkownika);
               //Add this client to the list
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
        //Thread which sends every 10 seconds actual list of rooms to the all people
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
    
    /*
        This method services clients requests 
    */
    void message(String[] parametry, WatekUzytkownika watekUzytkownika) {
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
                
            }
        } catch (IOException ex) {
            try {
                napiszDoUzytkownia(watekUzytkownika, "/error Nie udało się dostarczyć wiadomości");
            } catch (IOException ex1) {
                
            }
        }
    }
    /*
        
    */   
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
    @Structure of user
*/
class User
{
    private String nazwa, ip; //name, and IP
    private int port;
    /*
        Creating a new User with name, ip, and port
    */
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