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

public class Barco extends Base {
    private static final String P = "PAUSADO";
    private int direccion;
    
    public Barco(int posX, int posY) {
        super(posX, posY);
        
        URL bURL = this.getClass().getResource("imagesB/frame_000.gif");
        Image train0 = Toolkit.getDefaultToolkit().getImage(bURL );//this.getClass().getResource("/examen1jframe.Bueno/frame_000.gif"));
        
        URL b1URL = this.getClass().getResource("imagesB/frame_001.gif");
        Image train1 = Toolkit.getDefaultToolkit().getImage( b1URL );//this.getClass().getResource("/examen1jframe.Bueno/frame_001.gif"));
       
        URL b2URL = this.getClass().getResource("imagesB/frame_002.gif");
        Image train2 = Toolkit.getDefaultToolkit().getImage( b2URL);//this.getClass().getResource("/examen1jframe.Bueno/frame_002.gif"));
        

        anima = new Animacion();
        anima.sumaCuadro(train0, 200);
        anima.sumaCuadro(train1, 200);
        anima.sumaCuadro(train2, 200);
        
        direccion = -1;

    }

    /**
     * Metodo que sirve para regresar la variable estatica de clase P <code> String
     * </code>
     *
     * @return P es la variable string estatica de la clase
     */
    public static String getPausa() {
        return P;
    }
    
    public void setDireccion (int x){
        direccion = x;
    }
    
    public int getDireccion (){
        return direccion;
    }
    
}
