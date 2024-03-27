/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait;

import fr.insarouen.iti.compilation.lightgrep.automate.Lettre;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author delestre
 */
public class ExpressionRationnelleElementaire extends ExpressionRationnelle {
    private Set<Lettre> lettresReconnues;

    public ExpressionRationnelleElementaire(Lettre lettre) {
        lettresReconnues = new HashSet<>();
        lettresReconnues.add(lettre);
    }       
    
    public ExpressionRationnelleElementaire(Set<Lettre> lettres) {
        lettresReconnues = new HashSet<>();
        lettresReconnues.addAll(lettres);
    }   
    
    public Set<Lettre> getLettresReconnues() {
        return lettresReconnues;
    }
}
