/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insarouen.iti.compilation.lightgrep.compilateur;

import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.Concatenation;
import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.ExpressionRationnelle;
import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.ExpressionRationnelleElementaire;
import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.UnAN;
import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.Union;
import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.ZeroAN;
import fr.insarouen.iti.compilation.lightgrep.arbreSyntaxiqueAbstrait.ZeroOuUn;
import fr.insarouen.iti.compilation.lightgrep.automate.Automate;
import fr.insarouen.iti.compilation.lightgrep.automate.OutilsPourAutomate;
import fr.insarouen.iti.patronsConception.visiteur.Visiteur;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author delestre
 */
public class AstEnAutomate implements Visiteur {
    private ExpressionRationnelle lAST;
    private Automate lAutomate;
    
    public AstEnAutomate(ExpressionRationnelle unAST) {
        lAST = unAST;
        lAutomate = null;
    }
    
    public void visiter(UnAN unAN) throws Throwable  {
        lAutomate = OutilsPourAutomate.auMoinsUneFois(new AstEnAutomate(unAN.getExpression()).getAutomate());  
    }    
    
    public void visiter(ZeroAN zAN) throws Throwable  {
        lAutomate = OutilsPourAutomate.fermetureDeKleene(new AstEnAutomate(zAN.getExpression()).getAutomate());  
    }    
 
    public void visiter(ZeroOuUn zeroOuUn) throws Throwable  {
        lAutomate = OutilsPourAutomate.zeroOuUneFois(new AstEnAutomate(zeroOuUn.getExpression()).getAutomate());  
    }                
    
    public void visiter(ExpressionRationnelleElementaire expRElementaire) throws Throwable  {
        lAutomate = new Automate(expRElementaire.getLettresReconnues());
    }        
    
    public void visiter(Concatenation uneConcatenation) throws Throwable  {
        List<Automate> lesAutomates = new ArrayList<>();
        for(ExpressionRationnelle eR: uneConcatenation.getExpressionsRationelles())
            lesAutomates.add(new AstEnAutomate(eR).getAutomate());
        lAutomate = OutilsPourAutomate.concatenation(lesAutomates);  
    }
    
    public void visiter(Union uneUnion) throws Throwable {
        Set<Automate> lesAutomates = new HashSet<>();
        for(ExpressionRationnelle eR: uneUnion.getExpressionsRationelles())
            lesAutomates.add(new AstEnAutomate(eR).getAutomate());
        lAutomate = OutilsPourAutomate.union(lesAutomates);  
    }
    
    public Automate getAutomate() throws Throwable {
        lAST.accepter(this);
        return lAutomate;
    }
}
