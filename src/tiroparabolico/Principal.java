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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Principal extends JFrame implements Runnable, KeyListener, MouseListener {
    // Aqui declarar todas las variables

    //Variables de control de tiempo de la animacion
    private long tiempoActual;
    private long tiempoInicial;
    private SoundClip happy;    //Objeto AudioClip 
    private SoundClip sad;    //Objeto AudioClip 
    private Image dbImage;	// Imagen a proyectar	
    private Graphics dbg;	// Objeto grafico
    private Image fondo;	//Imagen de fondo del JFrame
    private Image pantallaInstr; //Imagen de instrucciones
    private Image pantallaPau; //Imagen de pausa
    private Image pantallaCreditos; //Imagen de creditos
    private boolean pausa; // bandera para manejar la pausa
    private boolean instrucciones; //bandera para manejar despliegue de instrucciones
    private Nube nubecita; // nube del juego
    private Barco barquito; // barco del juego
    private Rayo rayito; //rayo del juego
    private boolean click; //identificar click
    private int clickX; //coordenada de click en X
    private int clickY; //coordenada de click en Y
    private boolean sonidoActivado; //controla sonido
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
    private Image heart1; // corazones
    private Image heart2; // corazones
    private Image heart3; // corazones
    private Image heart4; // corazones
    private Image heart5; // corazones
    private int auxDificil; // esta variable ayuda a bajar el movimiento del barquito
    private boolean golpeAbajo; // bandera para indicar cuando cae el rayito en el fondo inferior
    private boolean golpeBarco; // bandera para indicar cuando el barquito atrapa al rayito
    
    private String nombreArchivo;    //Nombre del archivo.
    private boolean guarda; // bandera para identificar cuando guardar los datos del juego
    private boolean carga; // bandera para identificar cuando cargar los datos guardados al juego
    
    private Vector vec;    // Objeto vector para agregar el puntaje.
    private String[] arr;    //Arreglo del archivo divido.
        
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
        sonidoActivado = true;
        //FONDO
        //Carga la imagen de fondo, las pantallas y los corazones de vidas
        fondo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("fondo/imagenmar.jpg"));
        pantallaInstr = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/Instrucciones.jpg"));
        pantallaPau = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/Pausa.jpg"));
        pantallaCreditos = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/Creditos.jpg"));
        heart1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/heartif.png"));
        heart2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/heartif.png"));
        heart3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/heartif.png"));
        heart4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/heartif.png"));
        heart5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("pantallas/heartif.png"));
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
        barquito.setPosY(getHeight() - 2 * barquito.getAlto()); //reposicionar en la parte de abajo del applet
        rayito = new Rayo(0, nubecita.getPosY() + 18);
        limitesBarquitoIzquierda = false;
        limitesBarquitoDerecha = false;

        //velocidadInicial = Math.random() * (70 - 50)+50;
        rayito.setVelocidadInicial(Math.random() * (70 - 50) + 50);

        angulo = Math.random() * (1.1 - 0.9) + 0.9; //entre 0 y 1.5 radianes
        tiempo = 0;
        //velocidadX = velocidadInicial * (Math.cos(angulo)); // formula fisica
        rayito.setVelocidadX(rayito.getVelocidadInicial() * (Math.cos(angulo)));
        auxDificil = 0;

        golpeAbajo = false;
        golpeBarco = false;
        
        nombreArchivo = "Datos.txt";
        guarda = false;
        carga = false;
        
        vec = new Vector();

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
                try{
                   actualiza(); 
                } catch(IOException e) {

                     // System.out.println("Error en " + ex.toString());
                }
                //actualiza();
                
                checaColision();
            }
            repaint(); // Se actualiza el <code>Applet</code> repintando el contenido.
            try {
                // El thread se duerme.
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }

        }
    }

    //funcion actualiza como cualquier otra
    public void actualiza ()throws IOException {
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecuciÃ³n
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;
        nubecita.actualiza(tiempoActual);

        if (click) {
            rayito.actualiza(tiempoActual);
            //lanzar rayito
            rayito.setPosX(rayito.getVelocidadX() * tiempo); // formulazo
            rayito.setPosY(-(rayito.getVelocidadInicial() * (Math.sin(angulo)) * tiempo - 4 * tiempo * tiempo) + nubecita.getPosY()); // se le suma la posY de nube porque es la pos inicial
            // Formulas fisicas
            tiempo++;
        }

        switch (barquito.getDireccion()) {
            case 1: // se mueve a la izquierda
                barquito.actualiza(tiempoActual);
                if (!limitesBarquitoIzquierda) {
                    barquito.setPosX(barquito.getPosX() - 30 + auxDificil);
                } else {
                    barquito.setPosX(barquito.getPosX() + 15); // si esta chocando movemos 15 unidades a la derecha al barquito
                }

                break;
            case 2: // se mueve a la derecha
                barquito.actualiza(tiempoActual);
                if (!limitesBarquitoDerecha) {
                    barquito.setPosX(barquito.getPosX() + 30 - auxDificil);
                } else {
                    barquito.setPosX(barquito.getPosX() - 15); // si esta chocando, movemos 15 unidades a la izquierda al barquito
                }
                break;

        }

        if (golpeAbajo) {
            if (sonidoActivado) {
                sad.play(); //tocar sonido
            }
            click = false; //resetear click
            contPerdidas++; //se agrega una perdida mas           
            //reiniciar valores del rayo para siguiente jugada
            rayito.setPosX(0);
            rayito.setPosY(nubecita.getPosY() + 18);
            tiempo = 0;

            rayito.setVelocidadInicial(Math.random() * (70 - 50) + 50);
            rayito.setVelocidadX(rayito.getVelocidadInicial() * (Math.cos(angulo)));
            angulo = Math.random() * (1.1 - 0.9) + 0.9; //entre 0 y 1.5 radianes
        }

        if (golpeBarco) {
            if (sonidoActivado) {
                happy.play(); //tocar sonido
            }
            click = false; //resetear click
            score += 2; //aumenta 2 por cada rayo atrapado
            //reiniciar valores del rayo para siguiente jugada
            rayito.setPosX(0);
            rayito.setPosY(nubecita.getPosY() + 18);
            tiempo = 0;

            angulo = Math.random() * (1.1 - 0.9) + 0.9; //entre 0 y 1.5 radianes
            rayito.setVelocidadInicial(Math.random() * (70 - 50) + 50);
            rayito.setVelocidadX(rayito.getVelocidadInicial() * (Math.cos(angulo)));
        }
        
        if (guarda){
            //variables del barquito
            double posx = barquito.getPosX();
            double posy = barquito.getPosY();
            int dir = barquito.getDireccion();
            
            //variables del rayito
            double ang = angulo;
            double rx = rayito.getPosX();
            double ry = rayito.getPosY();
            double temp = tiempo;
            double vel0 = rayito.getVelocidadInicial();
            double velx = rayito.getVelocidadX();
            
            //otras variables importantes
            boolean choqueBar = golpeBarco;
            boolean limAbajo = golpeAbajo;
            boolean dispara = click;
            
            //guardar los datos en el archivo de texto
            PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
            
            fileOut.println(posx);
            fileOut.println(posy);
            fileOut.println(dir);
            
            fileOut.println(ang);
            fileOut.println(rx);
            fileOut.println(ry);
            fileOut.println(temp);
            fileOut.println(vel0);
            fileOut.println(velx);
            
            fileOut.println(choqueBar);
            fileOut.println(limAbajo);
            fileOut.println(dispara);
            
            fileOut.close();
            
        }
        
        if (carga){
           // BufferedReader fileIn = new BufferedReader(new FileReader(nombreArchivo));
            // trata de ¿gurdar? los datos sin hacer casts usando los mismos nombres de variables que use en el guardar
            BufferedReader fileIn = new BufferedReader(new FileReader(nombreArchivo));
                
            //leer variables del barquito del archivo
            double posx = Double.parseDouble(fileIn.readLine());
            double posy = Double.parseDouble(fileIn.readLine());
            int dir = Integer.parseInt(fileIn.readLine());
            //leer variables del rayito del archivo
            double ang = Double.parseDouble(fileIn.readLine());
            double rx = Double.parseDouble(fileIn.readLine());
            double ry = Double.parseDouble(fileIn.readLine());
            double temp = Double.parseDouble(fileIn.readLine());
            double vel0 = Double.parseDouble(fileIn.readLine());
            double velx = Double.parseDouble(fileIn.readLine());
            //leer otras variables importantes del archivo
            boolean choqueBar = Boolean.parseBoolean(fileIn.readLine());
            boolean limAbajo = Boolean.parseBoolean(fileIn.readLine());
            boolean dispara = Boolean.parseBoolean(fileIn.readLine());
             
            fileIn.close();
            
            //Asignar los datos leídos a los valores del juego
            barquito.setPosX(posx);
            barquito.setPosY(posy);
            barquito.setDireccion(dir);
            angulo = ang;
            rayito.setPosX(rx);
            rayito.setPosY(ry);
            tiempo = temp;
            rayito.setVelocidadInicial(vel0);
            rayito.setVelocidadX(velx);
            golpeBarco = choqueBar;
            golpeAbajo = limAbajo;
            click = dispara;
        }

        barquito.setDireccion(-1); // detiene al barquito
        limitesBarquitoIzquierda = false; // reiniciamos para que se pueda mover hacia el lado contrario
        limitesBarquitoDerecha = false;
        golpeAbajo = false;
        golpeBarco = false;
        guarda = false;
        carga = false;
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
            golpeBarco = true;
        }
        if (rayito.getPosY() > getHeight()) {
            golpeAbajo = true;
        }
        if (contPerdidas >= 3) {
            vidas--;
            contPerdidas = 0;
            auxDificil = auxDificil + 4; // si pierdes una vida, la velocidad del barquito disminuira en 4 unidades
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
            g.drawImage(pantallaCreditos, 0, 0, getSize().width, getSize().height, this);
        } else if (instrucciones) {
            g.drawImage(pantallaInstr, 0, 0, getSize().width, getSize().height, this);
        } else if (pausa) {
            g.drawImage(pantallaPau, 0, 0, getSize().width, getSize().height, this);
        } else {
            //Dibuja la imagen de fondo
            g.drawImage(fondo, 0, 0, getSize().width, getSize().height, this);
            g.drawString("Vidas: " + vidas, getWidth() - 200, 50);
            g.drawString("Score: " + score, getWidth() - 200, 70);
            //corazoncitos que también indican las vidas
            if (vidas == 5) {
                g.drawImage(heart5, getWidth() - 140, 35, this);
                g.drawImage(heart4, getWidth() - 120, 35, this);
                g.drawImage(heart3, getWidth() - 100, 35, this);
                g.drawImage(heart2, getWidth() - 80, 35, this);
                g.drawImage(heart1, getWidth() - 60, 35, this);
            } else if (vidas == 4) {
                g.drawImage(heart4, getWidth() - 120, 35, this);
                g.drawImage(heart3, getWidth() - 100, 35, this);
                g.drawImage(heart2, getWidth() - 80, 35, this);
                g.drawImage(heart1, getWidth() - 60, 35, this);
            } else if (vidas == 3) {
                g.drawImage(heart3, getWidth() - 100, 35, this);
                g.drawImage(heart2, getWidth() - 80, 35, this);
                g.drawImage(heart1, getWidth() - 60, 35, this);
            } else if (vidas == 2) {
                g.drawImage(heart2, getWidth() - 80, 35, this);
                g.drawImage(heart1, getWidth() - 60, 35, this);
            } else if (vidas == 1) {
                g.drawImage(heart1, getWidth() - 60, 35, this);
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
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            sonidoActivado = !sonidoActivado; //activo o desactivo el sonido
        }
        
        if (e.getKeyCode() == KeyEvent.VK_G) {
            guarda = true;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_C) {
            carga = true;
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
