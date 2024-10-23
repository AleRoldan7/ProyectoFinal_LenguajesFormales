/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import java.awt.Color;

/**
 *
 * @author crisa
 */
public class ColorTexto {
    
    private String nToken;
    private int inicio;
    private int tama;
    private Color color;

    public ColorTexto(String nToken, int inicio, int tama, Color color) {
        this.nToken = nToken;
        this.inicio = inicio;
        this.tama = tama;
        this.color = color;
    }

    public String getnToken() {
        return nToken;
    }

    public void setnToken(String nToken) {
        this.nToken = nToken;
    }

    
    public int getInicio() {
        return inicio;
    }

    public int getTama() {
        return tama;
    }

    public Color getColor() {
        return color;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public void setTama(int tama) {
        this.tama = tama;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
    
}
