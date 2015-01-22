/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author Kajetan Kacper Brzuszczak
 * Index 209869 
 * W-4 INF Języki Programowania z P. Rogalińskim
 */
public class Ksiazka implements Comparable<Ksiazka>{
    String autor;
    String tytul;
    
    
    public Ksiazka()
    {
        autor = new String("");
        tytul = new String("");
    }
    
    public Ksiazka(String autor, String tytul)
    {
        this.autor = autor;
        this.tytul = tytul;
    }
    
    @Override
    public int compareTo(Ksiazka t) {
        int wynik = 0;        
        Ksiazka ksiazka = (Ksiazka)t;
        
        if (this == t)
            return 0;
        wynik += this.autor.compareTo(ksiazka.autor)*100;
        System.out.print("\nautor: " + wynik);
        wynik += this.tytul.compareTo(ksiazka.tytul);        
        System.out.print("\ntytul: " + wynik);
        return wynik;
    }
    
    @Override
    public String toString()
    {
        return "Autor: " + autor + ". Tytul: " + tytul;             
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.autor);
        hash = 23 * hash + Objects.hashCode(this.tytul);
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
        final Ksiazka other = (Ksiazka) obj;
        if (!Objects.equals(this.autor, other.autor)) {
            return false;
        }
        if (!Objects.equals(this.tytul, other.tytul)) {
            return false;
        }
        return true;
    }
                  
}
