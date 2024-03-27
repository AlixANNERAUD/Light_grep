/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarouen.iti.patronsConception.visiteur;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Interface pour les classes qui peuvent visitées un Visitable suivant 
 * le patron de conception Visiteur.
 * @author delestre
 */
public interface Visiteur {

    /**
     * Permet de visiter un Visitable
     * @param o le visitable
     * @throws Throwable
     */
    //public void visiter(Object o) throws Throwable;  
    default public void visiter(Object o) throws Throwable {
        Method methode;
        Class classeCourante = o.getClass();
	String nomMethode = "visiter";
        boolean methodeTrouvee = false;
        do {    
            try {
                methode = getClass().getMethod(nomMethode, new Class[] { classeCourante });
                methodeTrouvee = true;
                methode.invoke(this, new Object[] { o});
            } catch (NoSuchMethodException e) {
                if (classeCourante != Object.class)
                    classeCourante = classeCourante.getSuperclass();                
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        } while (!methodeTrouvee && classeCourante != Object.class);
    }      
}
