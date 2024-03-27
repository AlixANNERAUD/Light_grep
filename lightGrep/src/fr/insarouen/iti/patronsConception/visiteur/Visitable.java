/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.patronsConception.visiteur;

/**
 * Interface pour les classes qui peuvent être visitées par un visiteur suivant 
 * le patron de conception Visiteur.
 * @author delestre
 */
public interface Visitable {
    /**
     * Permet d'acceper un visiteur.
     * @param v le visiteur
     * @throws Throwable
     */
    default public void accepter(Visiteur v) throws Throwable {
        v.visiter(this);
    }
    
}
