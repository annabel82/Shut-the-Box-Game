/**
 * Convenience class to customise JButtons more succinctly and with some basic exception
 * handling for images used.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */


package s4927945;

import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class JButtonCustomised extends JButton {



    // ---------------------------------------------------------------------------------
    /**
     * Customises a JButton to suit our needs.
     *
     * @param   boxTxt          The font to us for the built-in button label.
     * @param   btnTxtColour    The colour to use for the build-in button label.
     * @param  	label			String to appear on the button
     * @param 	file			Default image to apply to the button
     * @param 	pressedFile		Image to show when the button is pressed
     * @param 	disabledFile	Image to show when the button is disabled
     */
    public JButtonCustomised(Font boxTxt, Color btnTxtColour, String label, String file, String pressedFile, String disabledFile) {

        ImageIcon icon = null;
        ImageIcon pressedIcon = null;
        ImageIcon disabledIcon = null;

        // -----------------------------------------------------------

        try {

            icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + file)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            pressedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + pressedFile)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + pressedFile + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            disabledIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + disabledFile)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + disabledFile + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        setFocusPainted(false);
        setContentAreaFilled(false);
        setFont(boxTxt);
        setForeground(btnTxtColour);
        setText("<html>" + label + "</html>");
        setIcon(icon);
        setPressedIcon(pressedIcon);
        setDisabledIcon(disabledIcon);

        setBorder(BorderFactory.createEmptyBorder());
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }



    // ---------------------------------------------------------------------------------
    /**
     * Customises a JButton to suit our needs. Specifically used for the box buttons,
     * which uses an additional JLabel to position the box number more precisely.
     *
     * @param layout       Layout manager to use.
     * @param file		   Default image to apply to the button
     * @param pressedFile  Image to show when the button is pressed
     * @param disabledFile Image to show when the button is disabled
     */
    public JButtonCustomised(LayoutManager layout, String file, String pressedFile, String disabledFile) {

        ImageIcon icon = null;
        ImageIcon pressedIcon = null;
        ImageIcon disabledIcon = null;

        // -----------------------------------------------------------

        try {

            icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + file)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            pressedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + pressedFile)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + pressedFile + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            disabledIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + disabledFile)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + disabledFile + " failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        setLayout(layout);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setIcon(icon);
        setPressedIcon(pressedIcon);
        setDisabledIcon(disabledIcon);
        setBorder(BorderFactory.createEmptyBorder());
    }



    // ---------------------------------------------------------------------------------
    /**
     * This function changes the icons used on a JButtonCustomised.
     *
     * @param file        Primary icon file.
     * @param pressedFile Icon to use when button is pressed.
     */
    public void changeIcons(String file, String pressedFile) {

        ImageIcon icon = null;
        ImageIcon pressedIcon = null;

        // -----------------------------------------------------------

        try {

            icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + file)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + file + " failed to load:" + e + ".\n" +
                    "You may OK this message and continue at your own risk.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        // -----------------------------------------------------------

        try {

            pressedIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/" + pressedFile)));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/" + pressedFile + " failed to load:" + e + ".\n" +
                    "You may OK this message and continue at your own risk.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }


        // -----------------------------------------------------------

        setIcon(icon);
        setPressedIcon(pressedIcon);
    }
}