/**
 * This class allows other classes to roll a virtual N sided dice and then return the
 * value rolled.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */

package s4927945;

import java.security.SecureRandom;

public class Die {
    
    private int          faceValue;
    private SecureRandom rand;



    // ---------------------------------------------------------------------------------
    /**
     * Instantiates a new secure random.
     */
    public Die()  {

        rand = new SecureRandom();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Gets a new dice roll based from 1 - N sides.
     *
     * @param sides Uses for extensibility moving forward and allows the die to act as
     *              an N sided die.
     */
    public void roll(int sides){

        faceValue = rand.nextInt(sides) + 1;
    }



    // ---------------------------------------------------------------------------------
    /**
     * Typically called from game logic once per turn.
     *
     * @return Returns the value of the dice since it's last roll.
     */
    public Integer getRoll(){
        
        return faceValue;
    }
}