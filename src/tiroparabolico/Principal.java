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
    private SoundClip happy;    //Objeto AudioClip 
    private SoundClip sad;    //Objeto AudioClip 
    private Image dbImage;	// Imagen a proyectar	
    private Graphics dbg;	// Objeto grafico
    private Color c; //para color de strings
    private Image fondo;	//Imagen de fondo del JFrame
    private boolean pausa; // bandera para manejar la pausa
    private boolean instrucciones; //bandera para manejar despliegue de instrucciones
    private Nube nubecita; // nube del juego
    private Barco barquito; // barco del juego
    private Rayo rayito; //rayo del juego
    private boolean click; //identificar click
    private int clickX; //coordenada de click en X
    private int clickY; //coordenada de click en Y
    private boolean limitesBarquitoIzquierda; // bandera para delimitar que el barquito no se salga por el lado izquierdo del jFrame
    private boolean limitesBarquitoDerecha; // bandera para delimitar que no se salga por el lado derecho 
    private double velocidadInicial;
    private double tiempo;
    private double angulo;
    private double velocidadX;
    private double velocidadY;
    private int score;
    private int vidas;
    private int contPerdidas;
    private double h, R; //altura maxima h y alcance maximo R (Formulas físicas de tiro parabolico) 

    //Constructor (aqui se pone todo lo del init)
    public Principal() {
        setTitle("JFrame HolaMundo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        this.setSize(800, 600);
        addKeyListener(this);
        addMouseListener(this);
        //SONIDOS
        happy = new SoundClip("Sounds/diamond.wav");
        sad = new SoundClip("Sounds/Explosion.wav");
        //FONDO
        //Carga la imagen de fondo
        fondo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("fondo/imagenmar.jpg"));
        //Pausa y clicks
        pausa = false;
        instrucciones = false;
        clickX = 0;
        clickY = 0;
        click = false;
        //inicializa score, contador, vidas
        score = 0;
        contPerdidas = 0;
        vidas = 5;
        //crea personajes
        nubecita = new Nube(0, getHeight() / 2);

        barquito = new Barco(getWidth() / 2, getHeight());
        barquito.setPosY(getHeight() - 2 * barquito.getAlto() + 55); //reposicionar en la parte de abajo del applet
        //rayito = new Rayo(20 + (nubecita.getAncho() / 2), getHeight() / 2);
        rayito = new Rayo(0, nubecita.getPosY() + 18);
        limitesBarquitoIzquierda = false;
        limitesBarquitoDerecha = false;

        //velocidadInicial = 60; // la v0 tmb debe cambiar por cada tiro
        //angulo = 1; // angulo debe cambiar por cada tiro
        //  do {
        velocidadInicial = Math.random() * (70 - 50)+50;
        
        //valor entre 10 y 100
        angulo = Math.random() * (1.1 - 0.9)+0.9; //entre 0 y 1.5 radianes
       
        tiempo = 0;
        velocidadX = velocidadInicial * (Math.cos(angulo)); // formula fisica
        //inicializar h(altura maxima del tiro), R (alcance maximo del tiro)
       // h = (velocidadInicial * velocidadInicial * Math.sin(angulo) * Math.sin(angulo)) / (2 * (9.8));
        //R = (velocidadInicial * velocidadInicial * Math.sin(2 * angulo)) / (9.8);
        // } while (h < getHeight() || R < getWidth()); // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();

    }

    public void run() {

        //Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();

        //Ciclo principal del Applet. Actualiza y despliega en pantalla la animaciÃ³n hasta que el Applet sea cerrado
        while (vidas > 0) {

            //si esta pausado no actualizas ni checas colision 
            if (!pausa) {
                actualiza();
                checaColision();
            }
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
    public void actualiza() {
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecuciÃ³n
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;
        nubecita.actualiza(tiempoActual);
        //barquito.actualiza(tiempoActual);
        //rayito.actualiza(tiempoActual);

        if (click) {
            rayito.actualiza(tiempoActual);
            //lanzar rayito
            rayito.setPosX(velocidadX * tiempo); // formulazo
            rayito.setPosY(-(velocidadInicial * (Math.sin(angulo)) * tiempo - 4 * tiempo * tiempo) + nubecita.getPosY()); // se le suma la posY de nube porque es la pos inicial
            // Formulas fisicas
            tiempo++;
        }

        switch (barquito.getDireccion()) {
            case 1: // se mueve a la izquierda
                 barquito.actualiza(tiempoActual);
                if (!limitesBarquitoIzquierda) {
                    barquito.setPosX(barquito.getPosX() - 15);
                } else {
                    barquito.setPosX(barquito.getPosX() + 15); // si esta chocando movemos 10 unidades a la derecha al barquito
                }

                break;
            case 2: // se mueve a la derecha
                 barquito.actualiza(tiempoActual);
                if (!limitesBarquitoDerecha) {
                    barquito.setPosX(barquito.getPosX() + 15);
                } else {
                    barquito.setPosX(barquito.getPosX() - 15); // si esta chocando, movemos 10 unidades a la izquierda al barquito
                }
                break;
                
        }

        barquito.setDireccion(-1); // detiene al barquito
        limitesBarquitoIzquierda = false; // reiniciamos para que se pueda mover hacia el lado contrario
        limitesBarquitoDerecha = false;
        // tiempo++;
    }

//funcion actualiza como cualquier otra
    public void checaColision() {
        if (barquito.getPosX() < getWidth() / 2) {
            limitesBarquitoIzquierda = true;
        }
        if (barquito.getPosX() + barquito.getAncho() > getWidth()) {
            limitesBarquitoDerecha = true;
        }

        if (rayito.intersecta(barquito)) {
            happy.play(); //tocar sonido
            click = false; //resetear click
            score += 2; //aumenta 2 por cada rayo atrapado
            //reiniciar valores del rayo para siguiente jugada
            rayito.setPosX(0);
            rayito.setPosY(nubecita.getPosY() + 18);
            tiempo = 0;
            //do {
                velocidadInicial = Math.random() * (70 - 50) + 50;//valor entre 10 y 100
                angulo = Math.random() * (1.1 - 0.9) + 0.9; //entre 0 y 1.5 radianes
                h = (velocidadInicial * velocidadInicial * Math.sin(angulo) * Math.sin(angulo)) / (2 * (9.8)) + nubecita.getPosY()+18;
                R = (velocidadInicial * velocidadInicial * Math.sin(2 * angulo)) / (9.8);
                velocidadX = velocidadInicial * (Math.cos(angulo)); // formula fisica
            //} while (h > 500 ||R > getWidth() || R<getWidth()/2);
            // while checa que la velocidad y el angulo no ocasionen que el rayo salga del applet (h altura max, R alcance max)
        }
        if (rayito.getPosY() > getHeight()) { // ESO DE CLICK NO SE SI VA
            sad.play(); //tocar sonido
            click = false; //resetear click
            contPerdidas++; //se agrega una perdida mas           
            //reiniciar valores del rayo para siguiente jugada
            rayito.setPosX(0);
            rayito.setPosY(nubecita.getPosY() + 18);
            tiempo = 0;
            //do {
                velocidadInicial = Math.random() * (70 - 50) + 50;//valor entre 10 y 100
                angulo = Math.random() * (1.1- 0.9) + 0.9; //entre 0 y 1.5 radianes
                h = (velocidadInicial * velocidadInicial * Math.sin(angulo) * Math.sin(angulo)) / (2 * (9.8))+nubecita.getPosY()+18;
                R = (velocidadInicial * velocidadInicial * Math.sin(2 * angulo)) / (9.8);
                velocidadX = velocidadInicial * (Math.cos(angulo)); // formula fisica
            //} while (h > 500 ||R > getWidth()|| R<getWidth()/2);
            // while checa que la velocidad y el angulo no ocasionen que el rayo salga del applet (h altura max, R alcance max)

        }
        if (contPerdidas >= 3) {
            vidas--;
            contPerdidas = 0;
        }
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
        if (vidas <= 0) {
            g.drawString("GAME OVER", getWidth() / 2 - 50, getHeight() / 2 - 10);
        } else if (instrucciones) {
            g.drawString("INSTRUCCIONES", getWidth() / 2 - 60, 100);
        } else {
            //Dibuja la imagen de fondo
            g.drawImage(fondo, 0, 0, getSize().width, getSize().height, this);
            g.drawString("Vidas: " + vidas, getWidth() - 200, 50);
            if (pausa) {
                g.drawString("PAUSA", getWidth() / 2 - 10, 200);
            }
            if ((nubecita != null) && (barquito != null) && (rayito != null)) {
                g.drawImage(rayito.getImagenI(), (int) rayito.getPosX(), (int) rayito.getPosY(), this);
                g.drawImage(nubecita.getImagenI(), (int) nubecita.getPosX(), (int) nubecita.getPosY(), this);
                g.drawImage(barquito.getImagenI(), (int) barquito.getPosX(), (int) barquito.getPosY(), this);

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {    //Presiono letra P
            pausa = !pausa; //cambio valor de pausa
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            //Presiono flecha izquierda
            barquito.setDireccion(1);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            //Presiono flecha derecha
            barquito.setDireccion(2);
        }

        if (e.getKeyCode() == KeyEvent.VK_I) {
            //Presiono tecla I
            instrucciones = !instrucciones; //quito o pongo instrucciones
            if (instrucciones) {  // pauso cuando hay instrucciones para evitar que el juego continue mientras aparecen estas
                pausa = true;
                //  } else {
                //      pausa = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

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
