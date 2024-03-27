/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.compilation.lightgrep.expressionRationnelle;

import fr.insarouen.iti.compilation.lightgrep.analyseurs.AnalyseurSyntaxique;
import fr.insarouen.iti.compilation.lightgrep.analyseurs.ParseException;
import fr.insarouen.iti.compilation.lightgrep.automate.Automate;
import fr.insarouen.iti.compilation.lightgrep.automate.Etat;
import fr.insarouen.iti.compilation.lightgrep.automate.Lettre;
import fr.insarouen.iti.compilation.lightgrep.automate.LettreException;
import fr.insarouen.iti.compilation.lightgrep.automate.OutilsPourAutomate;
import fr.insarouen.iti.compilation.lightgrep.compilateur.AstEnAutomate;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author delestre
 */
public class ExpressionRationnelle {
    
    private boolean marqueurDebut, marqueurFin;
    private String lExpressionRationnelle, lExpressionRationnelleInitiale;
    private Automate lAutomate;
    private Automate lAutomateAvecEtatInitialComplete;

    private Automate obtenirAutomateAvecEtatInitialComplete(Automate lAutomate) {
        Automate automate = lAutomate.copie();
        Etat etatInitial = automate.getEtatInitial();
        Set<Lettre> lettres = automate.lettresDepuis(etatInitial);
        for (Lettre lettre: Lettre.toutesLesLettresPossibles())
            if (!lettres.contains(lettre))
                automate.addTransition(etatInitial, lettre, etatInitial);  
        return automate;
    }    
    
    public ExpressionRationnelle(String expressionRationnelle) throws Throwable, ParseException, LettreException {
        fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.ExpressionRationnelle ast;
        lExpressionRationnelleInitiale = expressionRationnelle;
        marqueurDebut = expressionRationnelle.startsWith("^");
        marqueurFin = expressionRationnelle.endsWith("$");
        if (marqueurDebut)
            expressionRationnelle = expressionRationnelle.substring(1);
        if (marqueurFin)
            expressionRationnelle = expressionRationnelle.substring(0, expressionRationnelle.length()-1);
        lExpressionRationnelle = expressionRationnelle;
        AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique(new StringReader(expressionRationnelle));
        ast = analyseurSyntaxique.expressionRationnelle();
        lAutomate = new AstEnAutomate(ast).getAutomate();   
        lAutomate = OutilsPourAutomate.supprimerTransitionEpsilon(lAutomate);
        lAutomate = OutilsPourAutomate.determiniser(lAutomate);
        lAutomateAvecEtatInitialComplete = obtenirAutomateAvecEtatInitialComplete(lAutomate);
    }        

    public String getExpressionRationnelle() {
        return lExpressionRationnelleInitiale;
    }

    public Automate getAutomate() {
        return lAutomate;
    }
    
    public boolean accepteCommeMot(String mot) {
        assert lAutomate.estDeterministe() && lAutomate.estSansEpsilonTransition();
        
        Etat etatCourant = lAutomate.getEtatInitial();
        for(int i=0;i<mot.length();i++) {
            Set<Etat> etatsDestination = lAutomate.suivreTransition(etatCourant, new Lettre(mot.charAt(i)));
            if(etatsDestination.isEmpty())
                return false;
            else
                for (Etat etat: etatsDestination)
                    etatCourant = etat;
        }
        return lAutomate.estUnEtatFinal(etatCourant);
    }
    
    private int estPresentAApartirDeEtSeTermineALaPosition(String chaine, int position) {
        assert lAutomate.estDeterministe() && lAutomate.estSansEpsilonTransition();
        Automate automate = lAutomate;
        String sousChaine=chaine.substring(position);
        boolean impasse=false;
        int i=0;
        Etat etatCourant = automate.getEtatInitial();
        while(i<sousChaine.length() && !impasse) {
           if (automate.estUnEtatFinal(etatCourant))
               return position+i-1;         
           Set<Etat> etatsDestination = automate.suivreTransition(etatCourant, new Lettre(sousChaine.charAt(i)));
           if(etatsDestination.isEmpty())
               impasse=true;
           else
               for (Etat etat: etatsDestination) {
                   etatCourant = etat;                
                   i++;
               }
        }  
        if (!automate.estUnEtatFinal(etatCourant))
            return -1;
        return position+i-1; 
    }

    public List<Intervalle> presences(String chaine) {
        List<Intervalle> res = new ArrayList<>();
        int debut, fin;
        int positionFin;
        
        
        int i=0;
        boolean termine = false;
        while (!termine && i<chaine.length()) {
            positionFin = estPresentAApartirDeEtSeTermineALaPosition(chaine, i);
            if (positionFin != -1) {
                debut = i;
                fin = positionFin;
                if ((!marqueurDebut && !marqueurFin) 
                    || (marqueurDebut && marqueurFin && debut==0 && fin==chaine.length()-1)    
                    || (marqueurDebut && !marqueurFin && debut==0)
                    || (!marqueurDebut && marqueurFin && fin==chaine.length()-1)) {
                    res.add(new Intervalle(debut, fin));
                    termine = positionFin == chaine.length();
                    i = positionFin+1;
                } else 
                    i++;
            } else
                i++;
        }
        return res;
    }
}
