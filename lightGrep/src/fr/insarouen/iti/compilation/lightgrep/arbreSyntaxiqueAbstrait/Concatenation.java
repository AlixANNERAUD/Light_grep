/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author delestre
 */
public class Concatenation extends ExpressionRationnelle {
    private List<ExpressionRationnelle> lesER;

    public Concatenation(List<ExpressionRationnelle> desEP) {
        lesER = new ArrayList<>();
        lesER.addAll(desEP);
    }
    
    public Collection<ExpressionRationnelle> getExpressionsRationelles() {
        return lesER;
    }    
}
