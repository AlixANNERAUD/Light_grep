/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.compilation.lightgrep.automate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author delestre
 * Bibliothèque proposant des algorithmes de bases sur les automates
 */
public class OutilsPourAutomate { 
    
    /**
     *
     * @param automates
     * @return Automate
     * Créé un nouvel automate qui est la concaténation (en ajoutant des epsilon transitions) des automates
     */
    public static Automate concatenation(List<Automate> automates) {
        if(automates.isEmpty())
            return null;
        Automate resultat = automates.get(0).copie();
        for(int i=1;i<automates.size();i++)
            resultat.concatener(automates.get(i));
        return resultat;
    }
    
    /**
     *
     * @param automates
     * @return Automate
     * Créé un nouvel automate qui est la discjonction (en ajoutant des epsilon transitions) des automates
     */
    public static Automate union(Set<Automate> automates) {
        Automate resultat = new Automate();
        Etat etatFinal = new Etat();
        resultat.addEtatFinal(etatFinal);
        Automate copie;
        for(Automate automate: automates) {
            copie = automate.copie();
            resultat.addTransition(resultat.getEtatInitial(), Lettre.epsilon, copie.getEtatInitial());
            resultat.addAutomate(copie);
            for(Etat ancienEtatFinal: copie.getEtatsFinaux())
                resultat.addTransition(ancienEtatFinal, Lettre.epsilon, etatFinal);
        }    
        return resultat;
    }  
    
    /**
     *
     * @param automate
     * @return Automate
     * Créé un nouvel automate qui est la fermeture de Kleene (en ajoutant des epsilon transitions entre les états finaux et l'état initial) de l'automate passé en paramètre 
     */
    public static Automate fermetureDeKleene(Automate automate) {
        Automate resultat = automate.copie();
        resultat.kleene();
        return resultat;
    }

    /**
     *
     * @param automate
     * @return Automate
     * Créé un nouvel automate qui ajoute le fait que l'état de initial soit aussi final
     */
    public static Automate zeroOuUneFois(Automate automate) {
        Automate resultat = automate.copie();
        resultat.addEtatFinal(resultat.getEtatInitial());
        return resultat;
    }
    
    /**
     *
     * @param automate
     * @return Automate
     * Créé un nouvel automate qui ajoute le fait que l'état de initial soit aussi final
     */
    public static Automate auMoinsUneFois(Automate automate) {
        Automate debut = automate.copie();
        Automate suite = fermetureDeKleene(automate);
        return concatenation(Arrays.asList(debut, suite));
    }    
    
    public static Automate supprimerTransitionEpsilon(Automate automate) {
        if(!automate.estSansEpsilonTransition()) {
            Automate resultat = new Automate();
            Map<Lettre, Set<Etat>> etatsAccessiblesApresFermetureTransitive;            
            Map<Etat,Etat> correspondanceAutomateResultat = new HashMap<>();
            correspondanceAutomateResultat.put(automate.getEtatInitial(), resultat.getEtatInitial());
            Stack<Etat> etatsATraites = new Stack<>();
            Etat etatCourantAutomate, sourceResultat, destinationResultat;
            etatsATraites.push(automate.getEtatInitial());
            while(!etatsATraites.empty()) {
                etatCourantAutomate = etatsATraites.pop();
                sourceResultat = correspondanceAutomateResultat.get(etatCourantAutomate);      
                etatsAccessiblesApresFermetureTransitive = fermetureTransitive(automate, etatCourantAutomate);
                for(Lettre lettre: etatsAccessiblesApresFermetureTransitive.keySet())
                    for(Etat destinationAutomate: etatsAccessiblesApresFermetureTransitive.get(lettre)) {
                        if(correspondanceAutomateResultat.containsKey(destinationAutomate))
                            destinationResultat=correspondanceAutomateResultat.get(destinationAutomate);
                        else {
                            destinationResultat = new Etat();
                            correspondanceAutomateResultat.put(destinationAutomate, destinationResultat);
                            etatsATraites.push(destinationAutomate);
                        }
                        resultat.addTransition(sourceResultat, lettre, destinationResultat);

                    }
            }
            for(Etat etatFinal: automate.getEtatsFinaux())
                resultat.addEtatFinal(correspondanceAutomateResultat.get(etatFinal));
            for(Etat etat: etatsPermmetantDAtteindreUnEtatFinalParEpsilonTransitions(automate))
                resultat.addEtatFinal(correspondanceAutomateResultat.get(etat));

            return resultat;
        } else 
            return automate.copie();
    }
    
    private static Set<Etat> etatsAccessibles(Automate automate, Set<Etat> etats, Lettre lettre) {
        Set<Etat> resultat = new HashSet<>();
        for(Etat etat: etats)
            resultat.addAll(automate.getTransitions(etat).etatsDestination(lettre));
        return resultat;
    }
    
    private static void creerNouveauxEtatsDeterministe(Automate automateOrigine, 
            Automate automateDeterministe,
            Map<Etat, Set<Etat>> correspondanceEtatsDeterminisitesEtatsNonDeterministes
            ) {
        
    }
    
    private static Etat retrouverEtatAutomateDeterministe(Map<Etat, Set<Etat>> correspondanceEtatsDeterminisitesEtatsNonDeterministes,
            Set<Etat> etatsAutomateNonDeterministe) {
        for(Etat etat: correspondanceEtatsDeterminisitesEtatsNonDeterministes.keySet())
            if(correspondanceEtatsDeterminisitesEtatsNonDeterministes.get(etat).equals(etatsAutomateNonDeterministe))
                return etat;
        return null; // Pas possible
    }
    
    public static Automate determiniser(Automate automate){
        if(!automate.estDeterministe()) {
            if(!automate.estSansEpsilonTransition()) 
                automate = supprimerTransitionEpsilon(automate);
            Automate resultat = new Automate();
            Map<Etat, Set<Etat>> correspondanceEtatsDeterminisitesEtatsNonDeterministes = new HashMap<>();
            correspondanceEtatsDeterminisitesEtatsNonDeterministes.put(resultat.getEtatInitial(), 
                    new HashSet<Etat>(Arrays.asList(new Etat[]{automate.getEtatInitial()})));
            Stack<Etat> etatsATraites = new Stack<>();
            etatsATraites.add(resultat.getEtatInitial());
            Etat etatCourant, destinationAutomateDeterministe;
            Set<Lettre> lettres = automate.lettres();
            Set<Etat> destinations;
            while(!etatsATraites.empty()) {
                etatCourant = etatsATraites.pop();
                for(Lettre lettre: lettres) {
                    destinations = etatsAccessibles(automate,
                            correspondanceEtatsDeterminisitesEtatsNonDeterministes.get(etatCourant),
                            lettre);
                    if(!destinations.isEmpty()) {
                        if(!correspondanceEtatsDeterminisitesEtatsNonDeterministes.containsValue(destinations)) {
                            destinationAutomateDeterministe = new Etat();
                            correspondanceEtatsDeterminisitesEtatsNonDeterministes.put(destinationAutomateDeterministe, destinations);
                            etatsATraites.push(destinationAutomateDeterministe);
                        } else {
                            destinationAutomateDeterministe = retrouverEtatAutomateDeterministe(correspondanceEtatsDeterminisitesEtatsNonDeterministes,
                                    destinations);
                        }
                        resultat.addTransition(etatCourant, lettre, destinationAutomateDeterministe);

                    }
                }
            }
            for(Etat etat: resultat.getEtats())
                for(Etat etatFinal: automate.getEtatsFinaux())
                    if(correspondanceEtatsDeterminisitesEtatsNonDeterministes.get(etat).contains(etatFinal))
                        resultat.addEtatFinal(etat);
            return resultat;
        } else
            return automate.copie();        
    }    
    
    private static void fermetureTransitiveRecursive(Automate automate, Etat etatCourant, Map<Lettre, Set<Etat>> etatsAccessibles){
        Set<Etat> etatsAccessiblesDepuisUneLettre;
        for(Lettre lettre: automate.getTransitions(etatCourant).lettres())
            if(!lettre.estEpsilon()) {
                if(!etatsAccessibles.containsKey(lettre))
                    etatsAccessibles.put(lettre, new HashSet<>());
                etatsAccessiblesDepuisUneLettre=etatsAccessibles.get(lettre);
                for(Etat etat: automate.getTransitions(etatCourant).etatsDestination(lettre))
                    etatsAccessiblesDepuisUneLettre.add(etat);
                
            } else 
                for(Etat etat: automate.getTransitions(etatCourant).etatsDestination(lettre))
                    fermetureTransitiveRecursive(automate, etat, etatsAccessibles);        
    }
    
    /**
     *
     * @param automate
     * @param source
     * @return
     */
    private static Map<Lettre, Set<Etat>> fermetureTransitive(Automate automate, Etat source) {
        Map<Lettre, Set<Etat>> resultat = new HashMap<>();
        
        for(Lettre lettre: automate.getTransitions(source).lettres())
            if(lettre.estEpsilon())
                for(Etat etat: automate.getTransitions(source).etatsDestination(lettre))
                    fermetureTransitiveRecursive(automate, etat, resultat);
            else
                resultat.put(lettre, automate.getTransitions(source).etatsDestination(lettre));
        return resultat;
    }
    
    private static boolean existeCheminEntreDeuxEtats(Automate automate, Etat source, Etat destination) {
        Set<Etat> etatsAccessibles = automate.etatsAccessiblesDirectement(source);
        if(etatsAccessibles.isEmpty())
            return false;
        if(etatsAccessibles.contains(destination))
            return true;
        for(Etat etat: etatsAccessibles)
            if(existeCheminEntreDeuxEtats(automate, etat, destination))
                return true;
        return false;
    }
    
    private static boolean existeEpsilonTransitionsEntreDeuxEtats(Automate automate, Etat source, Etat destination) {
        Set<Etat> etatsAccessiblesParEpsilonTransition = automate.suivreTransition(source, Lettre.epsilon);
        if(etatsAccessiblesParEpsilonTransition.isEmpty())
            return false;
        if(etatsAccessiblesParEpsilonTransition.contains(destination))
            return true;
        for(Etat etat: etatsAccessiblesParEpsilonTransition)
            if(existeEpsilonTransitionsEntreDeuxEtats(automate, etat, destination))
                return true;
        return false;
    }
    
    private static Set<Etat> etatsPermmetantDAtteindreUnEtatFinalParEpsilonTransitions(Automate automate) {
        Set<Etat> resultat = new HashSet<>();
        for(Etat etat: automate.getEtats())
            for(Etat etatFinal: automate.getEtatsFinaux())
                if(existeEpsilonTransitionsEntreDeuxEtats(automate, etat, etatFinal))
                    resultat.add(etat);
        return resultat;
    }    
}
