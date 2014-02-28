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
    
    public Rayo (double posX, double posY){
        
        super (posX, posY);
        
        URL bURL = this.getClass().getResource("imagesR/frame_000.gif");
        Image train0 = Toolkit.getDefaultToolkit().getImage(bURL );//this.getClass().getResource("/examen1jframe.Bueno/frame_000.gif"));
        
        URL b1URL = this.getClass().getResource("imagesR/frame_001.gif");
        Image train1 = Toolkit.getDefaultToolkit().getImage( b1URL );//this.getClass().getResource("/examen1jframe.Bueno/frame_001.gif"));
        
        anima = new Animacion();
        anima.sumaCuadro(train0, 200);
        anima.sumaCuadro(train1, 200);
        velocidadInicial = 0;
        velocidadX = 0;
    }
    
    public void setVelocidadInicial (double x){
        velocidadInicial = x;
    }
    
    public double getVelocidadInicial (){
        return velocidadInicial;
    }
    
    public void setVelocidadX (double x){
        velocidadX = x;
    }
    
    public double getVelocidadX(){
        return velocidadX;
    }
    
}
