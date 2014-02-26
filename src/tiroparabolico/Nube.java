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

public class Nube extends Base {

    public Nube(int posX, int posY) {
        super(posX, posY);

        URL bURL = this.getClass().getResource("imagesN/frame_000.gif");
        Image train0 = Toolkit.getDefaultToolkit().getImage(bURL);

        URL b1URL = this.getClass().getResource("imagesN/frame_001.gif");
        Image train1 = Toolkit.getDefaultToolkit().getImage(b1URL);

        URL b2URL = this.getClass().getResource("imagesN/frame_002.gif");
        Image train2 = Toolkit.getDefaultToolkit().getImage(b2URL);

        URL b3URL = this.getClass().getResource("imagesN/frame_003.gif");
        Image train3 = Toolkit.getDefaultToolkit().getImage(b3URL);

        anima = new Animacion();
        anima.sumaCuadro(train0, 100);
        anima.sumaCuadro(train1, 100);
        anima.sumaCuadro(train2, 100);
        anima.sumaCuadro(train3, 100);
    }
}
