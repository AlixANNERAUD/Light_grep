/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.compilation.lightgrep.automate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author delestre
 */
public class Automate implements Cloneable {
    
    
    
    private Etat etatInitial;
    private Set<Etat> etats;
    private Map<Etat, Transitions> transitions;
    private Set<Etat> etatsFinaux;

    public Automate() {
        this.etatInitial = new Etat();
        etats = new HashSet();
        transitions = new HashMap();
        etatsFinaux = new HashSet();
        addEtat(etatInitial);
    }
    
    public Automate(Lettre lettre) {
        this();
        Etat etatFinal = new Etat();
        addEtat(etatFinal);
        transitions.put(etatInitial, new Transitions(lettre, etatFinal));
        etatsFinaux.add(etatFinal);
    }
    
    public Automate(Set<Lettre> lettres) {
        this();
        if (!lettres.isEmpty()) {
            Etat etatFinal = new Etat();
            addEtat(etatFinal);
            Transitions lesTransitions = new Transitions();
            for (Lettre lettre: lettres) {
                lesTransitions.addTransition(lettre, etatFinal);
            }
            transitions.put(etatInitial, lesTransitions);
            etatsFinaux.add(etatFinal);                
        }
    }
    
    public boolean estSansEpsilonTransition() {
        for(Etat etat: getEtats())
            if(!getTransitions(etat).estSansEpsilonTransition())
                return false;
        return true;
    }
    
    public boolean estDeterministe() {
        if(!estSansEpsilonTransition())
            return false;
        for(Etat etat: getEtats())
            if(!getTransitions(etat).estDeterministe())
                return false;
        return true;
    }
    
    public final void addEtat(Etat etat) {
        etats.add(etat);
        if (!transitions.containsKey(etat))
            transitions.put(etat, new Transitions());
    }
    
    public final void addAutomate(Automate automate) {
        etats.addAll(automate.etats);
        transitions.putAll(automate.transitions);
    }
    
    public final void delEtat(Etat etat) {
        etatsFinaux.remove(etat);
        etats.remove(etat);
        transitions.remove(etat);          
    }
    
    public final void addEtatFinal(Etat etat) {
        etats.add(etat);
        etatsFinaux.add(etat);
        if (!transitions.containsKey(etat))
            transitions.put(etat, new Transitions());
    }    
    
    Transitions getTransitions(Etat etat) {
        return transitions.get(etat);
    }
    
    public void addTransition(Etat source, Lettre lettre, Etat destination) {
        addEtat(source);
        addEtat(destination);
        transitions.get(source).addTransition(lettre, destination);
    }
    
    
    public final Etat getEtatInitial() {
        return etatInitial;
    }
    
    public Set<Etat> getEtats() {
        return etats;
    }

    public Set<Etat> getEtatsFinaux() {
        return etatsFinaux;
    }
    
    public final boolean estUnEtatFinal(Etat etat){
        return etatsFinaux.contains(etat);
    }
    
    public final Set<Etat> suivreTransition(Etat etat, Lettre lettre) {
        if (etats.contains(etat))
            return transitions.get(etat).etatsDestination(lettre);
        return new HashSet<>();
    }
    
    public Set<Lettre> lettresDepuis(Etat etat) {
        if (etats.contains(etat))
            return transitions.get(etat).lettres();
        return new HashSet<Lettre>();
    }
    
    public final Set<Etat> etatsAccessiblesDirectement(Etat etat) {
        Set<Etat> resultat = new HashSet<>();
        if (etats.contains(etat))
            for(Lettre lettre: transitions.get(etat).lettres())
                resultat.addAll(transitions.get(etat).etatsDestination(lettre));
        return resultat;
    }
    
    public void concatener(Automate automateAConcatener) {
        Automate copie = automateAConcatener.copie();
        for(Etat etatFinal : getEtatsFinaux()){
            addTransition(etatFinal, Lettre.epsilon, copie.getEtatInitial());
        }
        etats.addAll(copie.etats);
        transitions.putAll(copie.transitions);
        etatsFinaux = copie.etatsFinaux;
    }
    
    public void kleene() {
        for(Etat etatFinal: getEtatsFinaux())
            addTransition(etatFinal, Lettre.epsilon, etatInitial);
        addEtatFinal(getEtatInitial());
    }

    public Set<Lettre> lettres(){
        Set<Lettre> resultat = new HashSet<>();
        for(Etat etat:getEtats())
            resultat.addAll(getTransitions(etat).lettres());
        return resultat;
    }
    
    public Automate copie() {
        Automate laCopie = new Automate();
        Map<Etat, Etat> correspondanceSourceCopie = new HashMap();
        correspondanceSourceCopie.put(getEtatInitial(), laCopie.getEtatInitial());
        Etat nelEtat;
        for(Etat etat: getEtats()){
            if(etat != getEtatInitial()) {
                nelEtat = new Etat();
                laCopie.addEtat(nelEtat);
                correspondanceSourceCopie.put(etat, nelEtat);
            }
            if(etatsFinaux.contains(etat)) {
                laCopie.etatsFinaux.add(correspondanceSourceCopie.get(etat));
            }            
        }
        for(Etat source: getEtats()){
            for(Lettre lettre: transitions.get(source).lettres()) {
                for(Etat destination: transitions.get(source).etatsDestination(lettre)) {
                    laCopie.addTransition(correspondanceSourceCopie.get(source), lettre, correspondanceSourceCopie.get(destination));
                }
            }
        }        
        return laCopie;
    }
    
    private Map<Etat, String> etatEnString() {
        Map<Etat, String> res = new HashMap();
        int i=1;
        String representation;
        for(Etat etat: getEtats()){
            if (etat == getEtatInitial()) {
                representation = "->[";
                if (getEtatsFinaux().contains(etat))
                    representation = representation + "{";
                representation = representation + Integer.toString(i);
                if (getEtatsFinaux().contains(etat))
                    representation = representation + "}";
                representation = representation +"]";
            } else {
                if (!getEtatsFinaux().contains(etat))
                    representation = "("+ Integer.toString(i) + ")";
                else
                    representation = "{"+Integer.toString(i)+"}";
            }
            res.put(etat, representation);
            i=i+1;
        }
        return res;
    }
    
    @Override
    public String toString() {
        Map<Etat, String> eES = etatEnString();
        StringBuilder resultat = new StringBuilder();
        for(Etat source: getEtats())
            for(Lettre lettre: transitions.get(source).lettres())
                for(Etat dest: transitions.get(source).etatsDestination(lettre))
                    resultat.append(eES.get(source)+"-"+lettre.toString()+"->"+eES.get(dest)+" ");
        
        return resultat.toString();
    }
}

