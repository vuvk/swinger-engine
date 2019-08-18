/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author tai-prg3
 */
public final class ImageUtils {
    private ImageUtils(){}
    
    /**
     * Изменить размер изображения. Исходное изображение НЕ МЕНЯЕТСЯ
     * @param image изображение для изменения
     * @param width новая ширина
     * @param height новая высота
     * @return новое изображение с заданным размером
     */
    public static BufferedImage resizeImage(final BufferedImage image, final int width, final int height) {       
        final BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // если размеры те же самые, то вернуть копию    
        if (image.getWidth()  == width && 
            image.getHeight() == height && 
            image.getType()   == BufferedImage.TYPE_INT_ARGB
           ) {  
            outputImage.setData(image.getData());
        } else {            
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();
        }
        
        return outputImage;
    }    
}
