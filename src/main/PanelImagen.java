/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author josel
 */
public class PanelImagen extends JFrame{
    
    public void paintComponent(Graphics g) {
        
    Dimension tam= getSize();
        ImageIcon imagen=new ImageIcon(new ImageIcon(getClass().getResource("/imagen/termometro.png")).getImage());
        g.drawImage(imagen.getImage(), 0, 0, tam.width, tam.height, null);
        
    
}
}