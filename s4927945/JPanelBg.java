/**
 * Adds the ability to set a background image to a JPanel. This is useful as it makes
 * the image able to trivially deform to the size of the Panel. Adapted from:
 * stackoverflow.com/questions/7092067/adding-a-background-image-to-a-panel-containing-other-components
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */


package s4927945;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;


@SuppressWarnings("serial")
public class JPanelBg extends JPanel {

    private BufferedImage bg;



    // ---------------------------------------------------------------------------------
    /**
     * Base constructor class.
     */
    public JPanelBg() {

    }



    // ---------------------------------------------------------------------------------
    /**
     * Generates a custom JPanel with a background image that adapts to the panels size.
     *
     * @param file Filename of the image to use as a background
     */
    public JPanelBg(String file) {

        try {

            bg = ImageIO.read(getClass().getClassLoader().getResource("resources/imgs/" + file));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Changes the background image of the JPanel.
     *
     * @param file Filename of the image to use as a background
     */
    public void setNewImage(String file) {

        try {

            bg = ImageIO.read(getClass().getClassLoader().getResource("resources/imgs/" + file));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Re-write the paint method for the customised JPanel to allow for a background
     * image.
     *
     * @param g Graphics component.
     */
    @Override
    public void paintComponent(Graphics g) {
        
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}