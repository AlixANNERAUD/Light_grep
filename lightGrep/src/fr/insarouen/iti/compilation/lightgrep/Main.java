/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.compilation.lightgrep;

import fr.insarouen.iti.compilation.lightgrep.expressionRationnelle.ExpressionRationnelle;
import fr.insarouen.iti.compilation.lightgrep.analyseurs.ParseException;
import fr.insarouen.iti.compilation.lightgrep.automate.LettreException;
import fr.insarouen.iti.compilation.lightgrep.expressionRationnelle.Intervalle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author delestre
 */


public class Main {
    
    private static boolean parametresOK(String[] args) {
        if (args.length != 2)
            return false;
        File f = new File(args[1]);
        return f.exists();
    }
    
    private static void traiterFichier(String expRationnelle, String nomFichierTexte) {
        List<Intervalle> positionsExpressionRationnelle;
        try {
            ExpressionRationnelle expressionRationnelle = new ExpressionRationnelle(expRationnelle);

            BufferedReader fluxTexte = new BufferedReader(new FileReader(nomFichierTexte));
            String ligne = fluxTexte.readLine();
            while (ligne != null) {
                positionsExpressionRationnelle = expressionRationnelle.presences(ligne);
                if (!positionsExpressionRationnelle.isEmpty())
                    System.out.println(ligne);
                ligne = fluxTexte.readLine();
            }
        } catch (ParseException | LettreException e) {
            System.err.println(e);
        } catch (FileNotFoundException e) {            
        } catch (IOException e) {            
        } catch (Throwable e) {        
        }
    }
    
    
    private static void afficherAide() {
        System.out.println("Utilisation : expressionRationelle nomFichier");
    }
    
    /**
     * Programme principal
     * @param args : l'expression rationnelle et le nom du fichier
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        if (parametresOK(args)) 
            traiterFichier(args[0], args[1]);
        else
            afficherAide();
    }
}
