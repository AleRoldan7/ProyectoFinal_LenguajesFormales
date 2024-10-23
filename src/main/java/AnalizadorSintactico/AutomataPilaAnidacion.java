/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorSintactico;

import java.util.Stack;

/**
 *
 * @author crisa
 */
public class AutomataPilaAnidacion {
    
    private Stack<Character> pila;

    public AutomataPilaAnidacion() {
        this.pila = new Stack<>();
    }
    
    
    public boolean procesarCaracter(char caracter) {
        
        if (caracter == '(') {
            pila.push(caracter);
        } 
       
        else if (caracter == ')') {
            if (pila.isEmpty()) {
                return false;  
            }

            char tope = pila.pop();
            if ((caracter == ')' && tope != '(')) {
                return false;  
            }
        }

        return true;
    }

   
    public boolean esValido() {
        return pila.isEmpty();
    }
}
