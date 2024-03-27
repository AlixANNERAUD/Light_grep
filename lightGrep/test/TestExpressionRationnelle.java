/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import fr.insarouen.iti.compilation.lightgrep.expressionRationnelle.ExpressionRationnelle;
import fr.insarouen.iti.compilation.lightgrep.expressionRationnelle.Intervalle;
import java.util.Arrays;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.core.IsEqual;
/**
 *
 * @author delestre
 */
public class TestExpressionRationnelle {
    
    @Test
    public void uneLettre() throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("c");
            assertThat(eR.presences("abcd"),
                    IsEqual.equalTo(Arrays.asList(new Intervalle(2,2))));
            eR = new ExpressionRationnelle("z");
            assertThat(eR.presences("abcd"),
                    IsEqual.equalTo(Arrays.asList()));
    }
    
    @Test
    public void concatenation()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("cd");
            assertThat(eR.presences("abcdef"),
                    IsEqual.equalTo(Arrays.asList(new Intervalle(2,3))));
            eR = new ExpressionRationnelle("cz");
            assertThat(eR.presences("abcd"),
                    IsEqual.equalTo(Arrays.asList()));
    }   

    @Test
    public void union()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("[cd]");
            assertThat(eR.presences("acef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(1,1))));
            eR = new ExpressionRationnelle("[cd]");
            assertThat(eR.presences("adef"),
                    IsEqual.equalTo(Arrays.asList(new Intervalle(1,1))));
            eR = new ExpressionRationnelle("[cd]");
            assertThat(eR.presences("aef"),
                    IsEqual.equalTo(Arrays.asList()));  
            eR = new ExpressionRationnelle("[^c-f]");
            assertThat(eR.presences("cgf"),
                    IsEqual.equalTo(Arrays.asList(new Intervalle(1,1))));             
    } 
    
    @Test
    public void zeroAN()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("ac*e");
            assertThat(eR.presences("aef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,1))));
            assertThat(eR.presences("acef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,2)))); 
             assertThat(eR.presences("accef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,3)))); 
            assertThat(eR.presences("af"),
                    IsEqual.equalTo(Arrays.asList()));                
    }
    
    @Test
    public void zeroOuUn()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("ac?e");
            assertThat(eR.presences("aef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,1))));
            assertThat(eR.presences("acef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,2)))); 
             assertThat(eR.presences("accef"), 
                    IsEqual.equalTo(Arrays.asList())); 
            assertThat(eR.presences("af"),
                    IsEqual.equalTo(Arrays.asList()));                
    }  
    
    @Test
    public void unOuN()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("ac+e");
            assertThat(eR.presences("acef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,2)))); 
             assertThat(eR.presences("accef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,3)))); 
            assertThat(eR.presences("acf"),
                    IsEqual.equalTo(Arrays.asList())); 
            assertThat(eR.presences("af"),
                    IsEqual.equalTo(Arrays.asList()));             
    }  

   @Test
    public void debut()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("^a");
            assertThat(eR.presences("acef"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,0)))); 
             assertThat(eR.presences("cacef"), 
                    IsEqual.equalTo(Arrays.asList()));              
    } 
    
   @Test
    public void fin()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("a$");
            assertThat(eR.presences("acefa"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(4,4)))); 
             assertThat(eR.presences("cacef"), 
                    IsEqual.equalTo(Arrays.asList()));              
    }    
    
   @Test
    public void point()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("a.c");
            assertThat(eR.presences("abc"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,2)))); 
             assertThat(eR.presences("ac"), 
                    IsEqual.equalTo(Arrays.asList()));              
    } 
    
   @Test
    public void antislash()  throws Throwable {
            ExpressionRationnelle eR;
            eR = new ExpressionRationnelle("a\\.c");
            assertThat(eR.presences("a.c"), 
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,2)))); 
            assertThat(eR.presences("ac"), 
                    IsEqual.equalTo(Arrays.asList()));  
            eR = new ExpressionRationnelle(".+\\(.+\\)");
            assertThat(eR.presences("toto(titi)"),
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,9))));
            assertThat(eR.presences("toto()"),
                    IsEqual.equalTo(Arrays.asList()));    
            eR = new ExpressionRationnelle(".+\\(");
            assertThat(eR.presences("toto(titi)"),
                    IsEqual.equalTo(Arrays.asList(new Intervalle(0,4))));             
    }     
}
