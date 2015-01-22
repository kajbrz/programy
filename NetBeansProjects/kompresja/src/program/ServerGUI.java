/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 *
 * @author "Kajetan Brzuszczak 209869"
 * @zadanie 1
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
                   // System.out.print("Nawi�zano nowe po��czenie");
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */

class WatekKlienta extends Thread
{
    private PhoneBook ksiazkatelefoniczna = null;
    private ServerGUI serverGUI;
    
    private String nazwaKlienta = "";
    
    private ObjectOutputStream strumienWysylanych;
    private ObjectInputStream strumienOdbieranych;
    
    private Socket gniazdoKlienta;
    
    private String ostatnioWyslane = "", ostatnioOdebrane = "";
    
    boolean zakoncz = false;
    
    WatekKlienta(Socket gniazdoKlienta, ServerGUI serverGUI) throws IOException
    {
        this.serverGUI = serverGUI;
        this.gniazdoKlienta = gniazdoKlienta;
        strumienWysylanych = new ObjectOutputStream(gniazdoKlienta.getOutputStream());
        strumienOdbieranych = new ObjectInputStream(gniazdoKlienta.getInputStream());
        
        this.start();
    }
    
    public void napisz(String wiadomosc) throws IOException
    {
        strumienWysylanych.writeObject(wiadomosc);
    }
    
    void zakoncz() throws IOException
    {
        zakoncz = true;
        gniazdoKlienta.close();
        strumienWysylanych.close();
        strumienOdbieranych.close();
    }
    
    public void setReferencjaKsiazkiTelefonicznej(PhoneBook phoneBook)
    {
        ksiazkatelefoniczna = phoneBook;
    }
    
    private void ObslugaZdarzen() throws IOException
    {
        String ostatnioOdebrane2 = ostatnioOdebrane.toUpperCase();
        boolean blad = true;
        String [] czesci = ostatnioOdebrane2.split(" ");
        switch(czesci[0])
        {
            case "LOAD":
            {
                if(czesci.length != 2)
                {
                    napisz("Zly parametr LOAD");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.LOAD(czesci[1]));
                    blad = false;
                }
                break;
            }
            
            case "SAVE":
            {
                if(czesci.length != 2)
                {
                    napisz("Zly parametr SAVE");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.SAVE(czesci[1]));
                    blad = false;
                }
                break;
            }
            case "PUT":
            {
                if(czesci.length != 3)
                {
                    napisz("Zly parametr PUT");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.PUT(czesci[1], czesci[2]));
                    blad = false;
                }
                break;
            }
            case "GET":
            {
                if(czesci.length != 2)
                {
                    napisz("Zly parametr GET");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.GET(czesci[1]));
                    blad = false;
                }
                break;
            }
            case "REPLACE":
            {
                if(czesci.length != 3)
                {
                    napisz("Zly parametr REPLACE");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.REPLACE(czesci[1], czesci[2]));
                    blad = false;
                }
                break;
            }
            case "LIST":
            {
                if(czesci.length != 1)
                {
                    napisz("Zly parametr LIST");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.LIST());
                    blad = false;
                }
                break;
            } 
            case "DELETE":
            {
                if(czesci.length != 2)
                {
                    napisz("Zly parametr DELETE");
                }
                else
                {
                    napisz(ksiazkatelefoniczna.DELETE(czesci[1]));
                    blad = false;
                }
                break;
            }
            case "CLOSE":
            {
                if(czesci.length != 1)
                {
                    napisz("Zly parametr CLOSE");
                }
                else
                {
                    //napisz(ksiazkatelefoniczna.CLOSE());
                    serverGUI.zakonczNasluch();
                    blad = false;
                }
                break;
            }
            case "BYE":
            {
                if(czesci.length != 1)
                {
                    napisz("Zly parametr BYE");
                }
                else
                {
                    serverGUI.zakonczPrace();
                    blad = false;
                }
                break;
            }
            default :
            {
                napisz("Lista dostępnych opcji\n"
                        + "1.LOAD\n"
                        + "2.SAVE\n"
                        + "3.GET\n"
                        + "4.PUT\n"
                        + "5.REPLACE\n"
                        + "6.DELETE\n"
                        + "7.LIST\n"
                        + "8.CLOSE\n"
                        + "9.BYE");
                break;
            }
        }
    }
    
    public String getNazwaKlienta()
    {
        return nazwaKlienta;
    }
    
    
    @Override
    public void run()
    {
         try {
                nazwaKlienta = (String)strumienOdbieranych.readObject();
                serverGUI.dodajUzytkownika(nazwaKlienta);
            } catch (IOException ex) {
                //Logger.getLogger(WatekKlienta.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                //Logger.getLogger(WatekKlienta.class.getName()).log(Level.SEVERE, null, ex);
            }
        while(!zakoncz)
        {
           
            
            try {
                
                ostatnioOdebrane = (String)strumienOdbieranych.readObject();
                ObslugaZdarzen();
                
                serverGUI.wyswietl(ostatnioOdebrane, this);
                
                System.out.printf(Thread.currentThread().getName() + "#Klient: " + ostatnioOdebrane + "\n");
                
            } catch (IOException ex) {
                //Logger.getLogger(WatekKlienta.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                //Logger.getLogger(WatekKlienta.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }    
        try {
            gniazdoKlienta.close();
        } catch (IOException ex) {
            //Logger.getLogger(WatekKlienta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */
class PhoneBook {
    public  ConcurrentHashMap<String, String> ksiazkaTelefoniczna;
    
    PhoneBook()
    {
        ksiazkaTelefoniczna = new ConcurrentHashMap<>();
    }
    
    public String LOAD(String nazwa_pliku){        
        ConcurrentHashMap<String,String> nowaKsiazkaTelefoniczna = null;
        try {
            FileInputStream plikWejsciowy;
            plikWejsciowy = new FileInputStream(nazwa_pliku);
            
            nowaKsiazkaTelefoniczna 
                    = (ConcurrentHashMap<String, String>)new ObjectInputStream(plikWejsciowy).readObject();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(PhoneBook.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR LOAD: nie znaleziono pliku";
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(PhoneBook.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR LOAD: nie znaleziono podanego obiektu";
        } catch (IOException ex) {
            //Logger.getLogger(PhoneBook.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR LOAD: bl�d wej�cia/wyj�cia";
        }
        ksiazkaTelefoniczna.clear();
        ksiazkaTelefoniczna.putAll(nowaKsiazkaTelefoniczna);
        return "OK LOAD: Pomy�lnie za�adowano list�";
    }
    public String SAVE(String nazwa_pliku)
    {
        try {
            FileOutputStream plikWyjsciowy;
            plikWyjsciowy = new FileOutputStream(nazwa_pliku);
            
            new ObjectOutputStream(plikWyjsciowy).writeObject(ksiazkaTelefoniczna);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(PhoneBook.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR SAVE: nie znaleziono pliku";
        } catch (IOException ex) {
            //Logger.getLogger(PhoneBook.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR SAVE: bl�d wej�cia/wyj�cia";
        }
        
        return "OK SAVE: pomy�lnie zapisano kontakty";
    }
    public String GET(String imie)
    {
        String numer = ksiazkaTelefoniczna.get(imie);
        if(numer == null)
            return "ERROR GET: brak rekordu";
        return "OK :" + numer;
    }
    public String PUT( String imie, String numer)
    {
        ksiazkaTelefoniczna.put(imie,numer);
        return "OK PUT: dodano rekord [" + imie + ";" + numer + "]";
    }
    public String REPLACE(String imie, String numer)
    {
        ksiazkaTelefoniczna.replace(imie, numer);
        return "OK REPLACE:";
    }
    public String DELETE(String imie)
    {
        ksiazkaTelefoniczna.remove(imie);
                
        return "OK DELETE";
    }
    public String LIST()
    {
        String buforNazwisk = "OK : ";
        
        Iterator iterator = (Iterator) ksiazkaTelefoniczna.keys();
        
        while(iterator.hasNext())
        {
            buforNazwisk += iterator.next() + " ";
        }
        
        return buforNazwisk;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */
public class ServerGUI extends javax.swing.JFrame {

   
    TCPServer tcpServer;
    
    PhoneBook ksiazkaTelefoniczna;
    
    /**
     * Creates new form ServerGUI
     */
    public ServerGUI() throws IOException {
        initComponents();
        jTextArea1.setEditable(false);
        jTextArea1.setEnabled(true);
        this.setLocation(500, 100);
        
        tcpServer = new TCPServer(this);
        ksiazkaTelefoniczna = new PhoneBook();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Napisz:");

        jLabel2.setText("Dialog:");

        jTextArea1.setColumns(5);
        jTextArea1.setRows(10);
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Serwer");

        jComboBox1.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        try {
            // TODO add your handling code here:
            tcpServer.napisz(jComboBox1.getSelectedIndex(), jTextField1.getText());
        } catch (IOException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextField1.setText("");
                
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ServerGUI().setVisible(true);
                } catch (IOException ex) {
                    //Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   

    synchronized void wyswietl(String wiadomosc) 
    {
        //throw new UnsupportedOperationException("Not support^ed yet."); //To change body of generated methods, choose Tools | Templates.
        String pom = jTextArea1.getText();
        
        jTextArea1.setText(wiadomosc + "\n" + pom);
        
    }  
    synchronized void wyswietl(String wiadomosc, WatekKlienta watekKlienta) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        wyswietl(watekKlienta.getNazwaKlienta() + "<<" + wiadomosc);
    }

    void dodajUzytkownika(String nazwaKlienta) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        jComboBox1.addItem(nazwaKlienta);
        wyswietl("Zalogowal sie:" + nazwaKlienta);
        repaint();
    }
    
    void zakonczNasluch()
    {
        tcpServer.zakoncz();
    }
    
    void zakonczPrace() throws IOException
    {
        tcpServer.zakoncz();
        
        tcpServer.zamknijWszystkieGniazda();
        System.exit(0);
    }
    
    public void refresh()
    {
        
    }
}
