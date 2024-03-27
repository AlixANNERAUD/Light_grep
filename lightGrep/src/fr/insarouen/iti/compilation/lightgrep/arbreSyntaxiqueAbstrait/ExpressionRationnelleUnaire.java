/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait;

/**
 *
 * @author delestre
 */
public abstract class ExpressionRationnelleUnaire extends ExpressionRationnelle {
    private ExpressionRationnelle lER;
    public ExpressionRationnelleUnaire(ExpressionRationnelle uneER) {
        lER = uneER;
    }
    public ExpressionRationnelle getExpression() {
        return lER;
    }
}
