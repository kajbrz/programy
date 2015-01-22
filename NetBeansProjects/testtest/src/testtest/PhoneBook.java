/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtest;

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
            return "ERROR LOAD: bląd wejścia/wyjścia";
        }
        ksiazkaTelefoniczna.clear();
        ksiazkaTelefoniczna.putAll(nowaKsiazkaTelefoniczna);
        return "OK LOAD: Pomyślnie załadowano listę";
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
            return "ERROR SAVE: bląd wejścia/wyjścia";
        }
        
        return "OK SAVE: pomyślnie zapisano kontakty";
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
