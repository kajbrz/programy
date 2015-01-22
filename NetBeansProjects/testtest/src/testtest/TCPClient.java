/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author "Kajetan Brzuszczak 209869"
 */


class TCPClient extends Thread
{
    ClientGUI clientGUI;
    
    String nazwaKlienta = "";
    Socket mojeGniazdko;
    
    String ostatnioWyslane = "", ostatnioOdebrane = "";
    
    ObjectOutputStream strumienWysylanych;
    ObjectInputStream strumienOdbieranych;
    
    boolean zakoncz = false;
    TCPClient(String ip , int port, ClientGUI clientGUI, String nazwaKlienta) throws Exception
    {
        this.nazwaKlienta = nazwaKlienta;
        
        mojeGniazdko = new Socket();        
        mojeGniazdko.connect(new InetSocketAddress(ip, port),1000);
        
        this.clientGUI = clientGUI;
        
        strumienWysylanych = new ObjectOutputStream(mojeGniazdko.getOutputStream());
        strumienOdbieranych = new ObjectInputStream(mojeGniazdko.getInputStream());
        napisz(nazwaKlienta);
        this.start();        
    }
    
    public void napisz(String wiadomosc) throws IOException
    {
        strumienWysylanych.writeObject(wiadomosc);
    }
    
    
    public void zakoncz() throws IOException
    {
        zakoncz = true;
        
        mojeGniazdko.close();
        strumienWysylanych.close();
        strumienOdbieranych.close();
    }
    
    @Override
    public void run()
    {   
        while(!zakoncz)
        {
            try {
                ostatnioOdebrane = (String)strumienOdbieranych.readObject();
                
                
                if(ostatnioOdebrane.equals("exit"))
                {
                    zakoncz();
                }
                //System.out.printf(Thread.currentThread().getName() + "#Serwer: " + ostatnioOdebrane + "\n");
                clientGUI.wyswietl(ostatnioOdebrane);
            } catch (IOException ex) {
                //Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            } catch (ClassNotFoundException ex) {
               // Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }                
        }
        System.out.print("Koniec");
    }
} 

