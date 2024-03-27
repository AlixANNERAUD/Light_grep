/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author delestre
 */
public class Union extends ExpressionRationnelle{
    private Set<ExpressionRationnelle> lesER;
    
    public Union() {
        lesER = new HashSet<>();
    }
    
    public Union(Set<ExpressionRationnelle> desEP) {
        this();
        lesER.addAll(desEP);
    }
    
    public Collection<ExpressionRationnelle> getExpressionsRationelles() {
        return lesER;
    }
}
