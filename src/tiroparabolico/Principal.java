/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiroparabolico;

/**
 *
 * @author Aaron
 * sobre esta clase se va a hacer el codigo y las funciones run, paint etc etc
 */

//Aqui importar todas las librerias
import javax.swing.JFrame;
import java.awt.Graphics;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.applet.AudioClip;

public class Principal extends JFrame implements Runnable, KeyListener  {
    // Aqui declarar todas las variables
    
    //Constructor (aqui se pone todo lo del init)
    public Principal () {
        this.setSize(800,600);
    }
    
    public void run() {


    }
    //funcion actualiza como cualquier otra
    public void actualiza (){
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //aqui va todo lo que iba en el update
    public void paint (Graphics g) {
        
    }
    
    // aqui va todo lo que iba en el paint
    public void paint1 (Graphics g) {
        
    }
    
    
}
