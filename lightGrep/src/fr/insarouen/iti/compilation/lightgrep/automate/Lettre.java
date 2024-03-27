/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.compilation.lightgrep.automate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author delestre
 */
public class Lettre {
    private static Character[] caracteresPossibles = {'!','"','#','$','%','&','\\','(',')','*','+',',','-','.','/',
        ':','<','=','>','?','@','[','\\',']','^','_','`','{','|','}','~', '0','1','2','3','4','5','6','7','8','9', 
        'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    // private static Character[] caracteresPossibles = {'a','b','c'};   
    
    private static Set<Lettre> lettresPossibles = new HashSet<>();
    static {
        for(Character caractere: caracteresPossibles)
            lettresPossibles.add(new Lettre(caractere));
    }
    
    public static Lettre epsilon = new Lettre();
    
    private Character leCaractere;

    public static Set<Lettre> toutesLesLettresPossibles() {
        return lettresPossibles;
    }
    
    public static Set<Lettre> toutesLesLettresComprisesEntre(Character a, Character b) throws LettreException {
        Set<Lettre> resultat = new HashSet<>();
        if(a<=b) {
            boolean aInclure = false;
            for(Character caractere: caracteresPossibles) {
                if(caractere==a)
                    aInclure = true;
                if(aInclure)
                    resultat.add(new Lettre(caractere));
                if(caractere==b)
                    aInclure=false;
            }
        } else
            throw new LettreException("Fin d'intervalle invalide");
        return resultat;        
    }

    public static Set<Lettre> toutesLesLettresComprisesEntre(Lettre a, Lettre b) throws LettreException {
        return toutesLesLettresComprisesEntre(a.leCaractere, b.leCaractere);
    }
        
    public static Set<Lettre> lettresComplementaires(Set<Lettre> lettresANePasRetenir) {
        Set<Lettre> resultat = new HashSet<>();
        for (Lettre lettre: toutesLesLettresPossibles())
            if (!lettresANePasRetenir.contains(lettre))
                resultat.add(lettre);
        return resultat;
    }
    
    private Lettre() {
        leCaractere = null;
    }

    public Lettre(Character lettre) {
        this.leCaractere = lettre;
    }
    
    public boolean estEpsilon() {
        return leCaractere == null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Lettre)) {
            return false;
        }
        Lettre autre = (Lettre)obj;
        if (leCaractere == null) {
            if (autre.leCaractere == null)
                return true;
            else
                return false;
        } else {
            if (autre.leCaractere == null)
                return false;
            else
                return leCaractere.equals(autre.leCaractere);            
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash;
        if (leCaractere != null)
            hash = hash + leCaractere.hashCode();
        return hash;
    }  
    
    @Override
    public String toString() {
        if(leCaractere == null)
            return "€";
        else
            return leCaractere.toString();
    }
}
