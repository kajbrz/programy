/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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