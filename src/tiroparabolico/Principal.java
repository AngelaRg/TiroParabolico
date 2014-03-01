/*
 * TAREA TIRO PARABOLICO
 * Angela Carina Romo Garza 1139764
 * Cesar Ruben Alejandro Rodriguez Garza 1036009
 */
package tiroparabolico;

//Importar todas las librerias a utilizar
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Angela Carina Romo Garza A01139764
 * @author Cesar Ruben Alejandro Rodriguez Garza A01036009
 */
public class Principal extends JFrame implements Runnable, KeyListener, MouseListener {

    // Declarar todas las variables
    private long tiempoActual; //Variables de control de tiempo de la animacion
    private long tiempoInicial; //Variables de control de tiempo de la animacion
    private SoundClip happy; //Objeto AudioClip 
    private SoundClip sad; //Objeto AudioClip 
    private Image dbImage;// Imagen a proyectar	
    private Graphics dbg; // Objeto grafico
    private Image fondo; //Imagen de fondo del JFrame
    private Image pantallaInstr; //Imagen de instrucciones
    private Image pantallaPau; //Imagen de pausa
    private Image pantallaCreditos; //Imagen de creditos
    private boolean pausa; // bandera para manejar la pausa
    private boolean instrucciones; //bandera para manejar despliegue de instrucciones
    private Nube nubecita; // nube del juego
    private Barco barquito; // barco del juego
    private Rayo rayito; //rayo del juego
    private boolean click; //identificar click
    private boolean sonidoActivado; //controla sonido
    private boolean limitesBarquitoIzquierda; // bandera para delimitar que el barquito no se salga por el lado izquierdo del jFrame
    private boolean limitesBarquitoDerecha; // bandera para delimitar que no se salga por el lado derecho 
    private double tiempo;
    private double angulo;
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
    private boolean auxCarga; // bandera para cuidar que no se carguen datos cuando el archivo esta vacio

    //Constructor (init y start)
    public Principal() {
        setTitle("JFrame HolaMundo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        this.setSize(800, 600); //tamaño del jframe
        addKeyListener(this);
        addMouseListener(this);
        //SONIDOS
        happy = new SoundClip("Sounds/diamond.wav");
        sad = new SoundClip("Sounds/Explosion.wav");
        sonidoActivado = true;
        //IMAGENES
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
        click = false;
        //inicializa score, contador, vidas
        score = 0;
        contPerdidas = 0;
        vidas = 5;
        //crea personajes y variables a usar
        nubecita = new Nube(0, getHeight() / 2);
        barquito = new Barco(getWidth() / 2, getHeight());
        barquito.setPosY(getHeight() - 2 * barquito.getAlto()); //reposicionar en la parte de abajo del applet
        rayito = new Rayo(0, nubecita.getPosY() + 18);
        limitesBarquitoIzquierda = false;
        limitesBarquitoDerecha = false;
        rayito.setVelocidadInicial(Math.random() * (70 - 50) + 50);
        angulo = Math.random() * (1.1 - 0.9) + 0.9; //entre 0 y 1.5 radianes
        tiempo = 0;
        rayito.setVelocidadX(rayito.getVelocidadInicial() * (Math.cos(angulo)));
        auxDificil = 0;
        golpeAbajo = false;
        golpeBarco = false;
        //variables para el uso de Archivos
        nombreArchivo = "Datos.txt";
        guarda = false;
        carga = false;
        auxCarga = false;
        //HILO
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se
     * incrementa la posicion en x o y dependiendo de la direccion, finalmente
     * se repinta el <code>JFrame</code> y luego manda a dormir el hilo.
     *
     */
    public void run() {

        //Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();

        //Ciclo principal del JFrame. Actualiza y despliega en pantalla hasta que se acaben las vidas
        while (vidas > 0) {

            //si esta pausado no actualizas ni checas colision 
            if (!pausa) {
                try {
                    actualiza();
                } catch (IOException e) {
                    System.out.println("Error en " + e.toString());
                }
                checaColision();
            }
            repaint(); // Se actualiza el <code>JFrame</code> repintando el contenido.
            try {
                // El thread se duerme.
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }

        }
    }

    /**
     * Metodo <I>actualiza</I>
     * Es usado para actualizar la posicion de los personajes y los valores de
     * las variables.
     */
    public void actualiza() throws IOException {
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecuciÃ³n
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;
        nubecita.actualiza(tiempoActual);

        //si hubo un click en la nube, lanza el rayito
        if (click) {
            rayito.actualiza(tiempoActual);
            //lanzar rayito
            rayito.setPosX(rayito.getVelocidadX() * tiempo); // formulazo
            rayito.setPosY(-(rayito.getVelocidadInicial() * (Math.sin(angulo)) * tiempo - 4 * tiempo * tiempo) + nubecita.getPosY()); // se le suma la posY de nube porque es la pos inicial
            // Formulas fisicas
            tiempo++;
        }

        //actualizar posición del barquito
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

        // actualizar si se notificó una colisión del rayo con el JFrame abajo
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

        //actualizar si se notificó una colision del rayo con el barco
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

        //revisa contador de rayitos perdidos y actualiza vidas 
        if (contPerdidas >= 3) {
            vidas--;
            contPerdidas = 0;
            auxDificil = auxDificil + 4; // si pierdes una vida, la velocidad del barquito disminuira en 4 unidades
        }

        //si se pidio guardar los valores actuales
        if (guarda) {
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
            int sc = score;
            int perdid = contPerdidas;
            int vid = vidas;
            
            boolean sonAct = sonidoActivado;

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
            fileOut.println(sc);
            fileOut.println(perdid);
            fileOut.println(vid);
            
            fileOut.println(sonAct);

            fileOut.close();
        }
        //Actualiza valores si se pidió cargar datos guardados (y esta permitido hacerlo)
        if (carga && auxCarga) {
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
            int sc = Integer.parseInt(fileIn.readLine());
            int perdid = Integer.parseInt(fileIn.readLine());
            int vid = Integer.parseInt(fileIn.readLine());
            boolean sonAct = Boolean.parseBoolean(fileIn.readLine());

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
            score = sc;
            contPerdidas = perdid;
            vidas = vid;
            sonidoActivado = sonAct;
        }
        //apago los valores booleanos despues de haberlos usado para evitar ciclos
        barquito.setDireccion(-1); // detiene al barquito
        limitesBarquitoIzquierda = false; // reiniciamos para que se pueda mover hacia el lado contrario
        limitesBarquitoDerecha = false; // reiniciamos para que se pueda mover al lado contrario
        golpeAbajo = false; //finaliza golpe con el piso del JFrame
        golpeBarco = false; //finaliza golpe con el barquito
        guarda = false; //apaga guardar
        carga = false; //apaga cargar
    }

    /**
     * Metodo <I>checaColision</I>
     * Metodo usado para checar las colisiones de los objetos barquito y rayito
     * entre sí y con las orillas del <code>JFrame</code>.
     */
    public void checaColision() {
        //revisa si el barco intenta salir del JFrame o de su mitad derecha del applet 
        if (barquito.getPosX() < getWidth() / 2) {
            limitesBarquitoIzquierda = true;
        }
        if (barquito.getPosX() + barquito.getAncho() > getWidth()) {
            limitesBarquitoDerecha = true;
        }
        //revisa si hubo colisión entre el rayito y el barco
        if (rayito.intersecta(barquito)) {
            golpeBarco = true;
        }
        //revisa si el rayito salió del JFrame por abajo
        if (rayito.getPosY() > getHeight()) {
            golpeAbajo = true;
        }
    }

    /**
     * Metodo <I>paint</I>
     * En este metodo lo que hace es actualizar el contenedor (Update)
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
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

    /**
     * Metodo <I>paint1</I>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia. (Paint)
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint1(Graphics g) {
        //Revisa si aun hay vidas o se ha acabado el juego
        if (vidas <= 0) {
            //Despliega creditos al finalizar
            g.drawImage(pantallaCreditos, 0, 0, getSize().width, getSize().height, this);
        } else if (instrucciones) {
            //Despliega instrucciones si son pedidas
            g.drawImage(pantallaInstr, 0, 0, getSize().width, getSize().height, this);
        } else if (pausa) {
            //Indica pausa si el juego ha sido pausado
            g.drawImage(pantallaPau, 0, 0, getSize().width, getSize().height, this);
        } else {
            //Dibuja la imagen de fondo y los strings de vidas y score
            g.drawImage(fondo, 0, 0, getSize().width, getSize().height, this);
            g.drawString("Vidas: " + vidas, getWidth() - 200, 50);
            g.drawString("Score: " + score, getWidth() - 200, 70);
            //Dibuja corazoncitos que también indican las vidas
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
            //Dibuja los personajes
            if ((nubecita != null) && (barquito != null) && (rayito != null)) {
                g.drawImage(rayito.getImagenI(), (int) rayito.getPosX(), (int) rayito.getPosY(), this);
                g.drawImage(nubecita.getImagenI(), (int) nubecita.getPosX(), (int) nubecita.getPosY(), this);
                g.drawImage(barquito.getImagenI(), (int) barquito.getPosX(), (int) barquito.getPosY(), this);
            }
        }
    }

    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que
     * no es de accion.
     *
     * @param e es el <code>evento</code> que se genera en al presionar las
     * teclas.
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la
     * tecla.
     *
     * @param e es el <code>evento</code> generado al presionar las teclas.
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {    //Presiono letra P
            pausa = !pausa; //cambio valor de pausa
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { //Presiono flecha izquierda
            barquito.setDireccion(1);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { //Presiono flecha derecha
            barquito.setDireccion(2);
        }

        if (e.getKeyCode() == KeyEvent.VK_I) { //Presiono tecla I
            instrucciones = !instrucciones; //quito o pongo instrucciones
            if (instrucciones) {  // pauso cuando hay instrucciones para evitar que el juego continue mientras aparecen estas
                pausa = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_S) { // Presiono tecla S
            sonidoActivado = !sonidoActivado; //activo o desactivo el sonido
        }

        if (e.getKeyCode() == KeyEvent.VK_G) { //Presiono tecla G
            if (!instrucciones) {
                guarda = true; //registro el cambio
                auxCarga = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_C) { //Presiono teclo C
            if (!instrucciones) {
                carga = true;
            } //registro el cambio

        }
    }

    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla
     * presionada.
     *
     * @param e es el <code>evento</code> que se genera en al soltar las teclas.
     */
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    /**
     * Metodo mouseClicked sobrescrito de la interface MouseListener. En este
     * metodo maneja el evento que se genera al hacer click con el mouse sobre
     * algun componente. e es el evento generado al hacer click con el mouse.
     */
    public void mouseClicked(MouseEvent e) {
        //si hay un click en la nubecita se registra y se guardan las coordenadas
        if (nubecita.clickEnPersonaje(e.getX(), e.getY())) {
            click = true;
        }
    }

    /**
     * Metodo mousePressed sobrescrito de la interface MouseListener. En este
     * metodo maneja el evento que se genera al presionar un botón del mouse
     * sobre algun componente. e es el evento generado al presionar un botón del
     * mouse sobre algun componente.
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Metodo mouseReleased sobrescrito de la interface MouseListener. En este
     * metodo maneja el evento que se genera al soltar un botón del mouse sobre
     * algun componente. e es el evento generado al soltar un botón del mouse
     * sobre algun componente.
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Metodo mouseEntered sobrescrito de la interface MouseListener. En este
     * metodo maneja el evento que se genera cuando el mouse entra en algun
     * componente. e es el evento generado cuando el mouse entra en algun
     * componente.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Metodo mouseExited sobrescrito de la interface MouseListener. En este
     * metodo maneja el evento que se genera cuando el mouse sale de algun
     * componente. e es el evento generado cuando el mouse sale de algun
     * componente.
     */
    public void mouseExited(MouseEvent e) {
    }

}
