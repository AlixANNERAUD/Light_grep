/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.compilation.lightgrep.automate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author delestre
 */
class Transitions {
    private Map<Lettre, Set<Etat>> transitions;

    Transitions() {
        this.transitions = new HashMap();
    }

    Transitions(Lettre lettre, Etat etatDestination) {
        this();
        Set<Etat> destinations = new HashSet();
        destinations.add(etatDestination);
        transitions.put(lettre, destinations);
    }
    
    void addTransition(Lettre lettre, Etat etatDestination) {
        if (!transitions.containsKey(lettre))
             transitions.put(lettre, new HashSet<Etat>());
        transitions.get(lettre).add(etatDestination);
    }
        
    Set<Etat> etatsDestination(Lettre lettre) {
        if(transitions.containsKey(lettre))
            return transitions.get(lettre);
        else
            return new HashSet<>();
    }
    
    Set<Lettre> lettres() {
        return transitions.keySet();
    }
    
    boolean estDeterministe() {
        for(Lettre lettre : transitions.keySet())
            if (transitions.get(lettre).size()>1)
                return false;
        return true;
    }
    
    boolean estSansEpsilonTransition() {
        for(Lettre lettre : transitions.keySet())
            if(lettre.estEpsilon())
                return false;
        return true;
    }
    
    void supprimerEpsilonTransition() {
        transitions.remove(Lettre.epsilon);
    }
}
