/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

/**
 *
 * @author Aaron
 */
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Rayo extends Base {

    private double velocidadInicial;
    private double velocidadX;

    /**
     * Metodo constructor de la clase Rayo
     *
     * @param posX es la posicion en el eje x
     * @param posY es la posicion en el eje y
     */
    public Rayo(double posX, double posY) {

        super(posX, posY);

        URL bURL = this.getClass().getResource("imagesR/frame_000.gif");
        Image train0 = Toolkit.getDefaultToolkit().getImage(bURL);//this.getClass().getResource("/examen1jframe.Bueno/frame_000.gif"));

        URL b1URL = this.getClass().getResource("imagesR/frame_001.gif");
        Image train1 = Toolkit.getDefaultToolkit().getImage(b1URL);//this.getClass().getResource("/examen1jframe.Bueno/frame_001.gif"));

        anima = new Animacion();
        anima.sumaCuadro(train0, 200);
        anima.sumaCuadro(train1, 200);
        velocidadInicial = 0;
        velocidadX = 0;
    }

    /**
     * Metodo para modificar la velocidad inicial del rayo, esta servira para
     * calcular la trayectoria parabolica
     *
     * @param x es la nueva velocidad inicial
     */
    public void setVelocidadInicial(double x) {
        velocidadInicial = x;
    }

    /**
     * Metodo para accesar la velocidad inicial de un rayo
     *
     * @return velocidadInicial es la velocidadInicial del rayo
     */
    public double getVelocidadInicial() {
        return velocidadInicial;
    }

    /**
     * Metodo para establecer la velocidad en el eje x del rayo, esta servira
     * para calcular la trayectoria
     *
     * @param x es la nueva velocidad en x del rayo
     */
    public void setVelocidadX(double x) {
        velocidadX = x;
    }

    /**
     * Metodo para accesar la velocidad en X de un rayo
     *
     * @return velocidadX es la velocidad en el eje x del rayo
     */
    public double getVelocidadX() {
        return velocidadX;
    }

}
