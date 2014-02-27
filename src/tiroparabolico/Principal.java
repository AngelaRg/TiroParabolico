/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

/**
 *
 * @author Aaron sobre esta clase se va a hacer el codigo y las funciones run,
 * paint etc etc
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
    private boolean pausa; // bandera para manejar la pausa
    private Nube nubecita; // nube del juego
    private Barco barquito; // barco del juego
    private Rayo rayito; //rayo del juego
    private boolean click; //identificar click
    private int clickX; //coordenada de click en X
    private int clickY; //coordenada de click en Y
    private boolean limitesBarquitoIzquierda; // bandera para delimitar que el barquito no se salga por el lado izquierdo del jFrame
    private boolean limitesBarquitoDerecha; // bandera para delimitar que no se salga por el lado derecho 
    private int velocidadInicial;
    private int tiempo;
    private double angulo;
    private int velocidadX;
    private int velocidadY;

    //Constructor (aqui se pone todo lo del init)
    public Principal() {
        setTitle("JFrame HolaMundo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        this.setSize(800, 600);
        addKeyListener(this);
        addMouseListener(this);
        //SONIDOS
        bomb = new SoundClip("Sounds/Explosion.wav");
        miss = new SoundClip("Sounds/miss.wav");
        //FONDO
        //Carga la imagen de fondo
        fondo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("fondo/imagenmar.jpg"));
        //Pausa y clicks
        pausa = false;
        clickX = 0;
        clickY = 0;
        click = false;
       nubecita = new Nube(0, getHeight() / 2);
       
        barquito = new Barco(getWidth() / 2, getHeight());
        barquito.setPosY(getHeight() - 2 * barquito.getAlto()); //reposicionar en la parte de abajo del applet
        //rayito = new Rayo(20 + (nubecita.getAncho() / 2), getHeight() / 2);
        rayito = new Rayo(0, nubecita.getPosY()+18);
        limitesBarquitoIzquierda = false;
        limitesBarquitoDerecha = false;

        velocidadInicial = 10;
        tiempo = 0;
        angulo = 0.1;
        velocidadX = velocidadInicial * ((int) Math.cos(angulo));
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
            if (!pausa) {
                actualiza();
                checaColision();
            }
            repaint(); // Se actualiza el <code>Applet</code> repintando el contenido.
            try {
                // El thread se duerme.
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }

        }
    }

    //funcion actualiza como cualquier otra
    public void actualiza() {
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecuciÃ³n
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;
        nubecita.actualiza(tiempoActual);
        barquito.actualiza(tiempoActual);
        rayito.actualiza(tiempoActual);

        if (click) {

            //lanzar rayito
           rayito.setPosX(velocidadX * tiempo);
           //rayito.setPosY( -(velocidadInicial*((int)Math.sin(angulo))*tiempo - 4*tiempo*tiempo ) + nubecita.getPosY() );

        }
        //click = false;

        switch (barquito.getDireccion()) {
            case 1: // se mueve a la izquierda
                if (!limitesBarquitoIzquierda) {
                    barquito.setPosX(barquito.getPosX() - 10);
                } else {
                    barquito.setPosX(barquito.getPosX() + 10); // si esta chocando movemos 10 unidades a la derecha al barquito
                }

                break;
            case 2: // se mueve a la derecha
                if (!limitesBarquitoDerecha) {
                    barquito.setPosX(barquito.getPosX() + 10);
                } else {
                    barquito.setPosX(barquito.getPosX() - 10); // si esta chocando, movemos 10 unidades a la izquierda al barquito
                }
                break;
        }

        barquito.setDireccion(-1); // detiene al barquito
        limitesBarquitoIzquierda = false; // reiniciamos para que se pueda mover hacia el lado contrario
        limitesBarquitoDerecha = false;
        tiempo++;
    }

//funcion actualiza como cualquier otra
    public void checaColision() {
        if (barquito.getPosX() < 0) {
            limitesBarquitoIzquierda = true;
        }
        if (barquito.getPosX() + barquito.getAncho() > getWidth()) {
            limitesBarquitoDerecha = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {    //Presiono flecha arriba
            pausa = !pausa;
        } // cambia la bandera de pausa si se presiona la tecla P
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            //Presiono flecha izquierda
            barquito.setDireccion(1);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            //Presiono flecha derecha
            barquito.setDireccion(2);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    //aqui va todo lo que iba en el update
    public void paint(Graphics g) {
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
    public void paint1(Graphics g) {
        //Dibuja la imagen de fondo
        g.drawImage(fondo, 0, 0, getSize().width, getSize().height, this);
        if (pausa) {
            g.drawString("PAUSA", getWidth() / 2, getHeight() / 2);
        }
        if ((nubecita != null) && (barquito != null) && (rayito != null)) {
            g.drawImage(rayito.getImagenI(), rayito.getPosX(), rayito.getPosY(), this);
            g.drawImage(nubecita.getImagenI(), nubecita.getPosX(), nubecita.getPosY(), this);
            g.drawImage(barquito.getImagenI(), barquito.getPosX(), barquito.getPosY(), this);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //si hay un click en la nubecita se registra y se guardan las coordenadas
        if (nubecita.clickEnPersonaje(e.getX(), e.getY())) {
            click = true;
            clickX = e.getX();
            clickY = e.getY();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
