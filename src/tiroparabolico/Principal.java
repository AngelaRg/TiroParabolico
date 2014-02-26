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
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Toolkit;

public class Principal extends JFrame implements Runnable, KeyListener, MouseListener {
    // Aqui declarar todas las variables
    
    //Variables de control de tiempo de la animacion
    private long tiempoActual;
    private long tiempoInicial;
    private SoundClip bomb;    //Objeto AudioClip 
    private SoundClip miss;    //Objeto AudioClip 
    private Image dbImage;	// Imagen a proyectar	
    private Graphics dbg;	// Objeto grafico
    private Color c; //para color de strings
    private Image fondo;	//Imagen de fondo del JFrame
    
    //Constructor (aqui se pone todo lo del init)
    public Principal () {
        setTitle("JFrame HolaMundo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        this.setSize(800,600);
        addKeyListener(this);
        //SONIDOS
        bomb = new SoundClip("Sounds/Explosion.wav");
        miss = new SoundClip("Sounds/miss.wav");
        //FONDO
        //Carga la imagen de fondo
        fondo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("fondo/imagenmar.jpg"));
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
        
    }
    
    public void run() {
        
        //Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();

        //Ciclo principal del Applet. Actualiza y despliega en pantalla la animaciÃ³n hasta que el Applet sea cerrado
        while (true) {

            //si esta pausado no actualizas ni checas colision 
            //if (!pausa) {
                actualiza();
                checaColision();
            //}
            repaint(); // Se actualiza el <code>Applet</code> repintando el contenido.
            try {
                // El thread se duerme.
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }

        }
    }
    //funcion actualiza como cualquier otra
    public void actualiza (){
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecuciÃ³n
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;
        //togepi.actualiza(tiempoActual);
        
    }
    
    //funcion actualiza como cualquier otra
    public void checaColision (){
        
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
        // Inicializan el DoubleBuffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
        
    }
    
    // aqui va todo lo que iba en el paint
    public void paint1 (Graphics g) {
        //Dibuja la imagen de fondo
        g.drawImage(fondo, 0, 0, getSize().width, getSize().height, this);
              
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
