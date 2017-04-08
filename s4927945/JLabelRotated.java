/**
 * Adds rotational functionality to JLabels.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */

package s4927945;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.*;


@SuppressWarnings("serial")
public class JLabelRotated extends JLabel {


	private Image     bg;
	private ImageIcon image;
	private int       degrees;
	private int       width;
	private int       height;


    // ---------------------------------------------------------------------------------
    /**
     * Adapted from: coderanch.com/t/344056/java/Rotating-image
     *
     * Generates a rotated JLabel with a background image by re-writing the
     * paintComponent swing method
     *
     * @param  	file		    File name of the image to use
     * @param 	degrees			Degrees of rotation to apply
     */
    public void setIcon (String file, int degrees) {

        try {

            bg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + file));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            image = new ImageIcon(bg);

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            width = image.getIconWidth() + 20;									    	// Add a little space for rotation
            height = image.getIconHeight() + 20;

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file +
                                                " failed to load, so width and height of image could not be calculated:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        this.degrees = degrees;

		setPreferredSize(new Dimension(width, height));
	}



    // ---------------------------------------------------------------------------------
    /**
     * Re-write the paint method for the customised JLabel to allow for a degree of
     * rotation.
     *
     * @param g Graphics component.
     */
	public void paintComponent (Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(null);

		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2.setRenderingHints(hints);
		g2.rotate(Math.toRadians(degrees), width / 2, height / 2);
		g2.drawImage(bg, 10, 10, this);
		g2.dispose();
	}
}