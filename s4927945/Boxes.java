/**
 * This class handles our boxes for our box game class. It provides ways to backup and
 * restore boxes, close boxes or open all them to reset. Get the current state of a box
 * and calculate game over status, final score and closure possibilities.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */


package s4927945;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Boxes {
    
    private boolean[]          boxes;
    private boolean[]          backup;                                                  // Built-in backup boxes we can save to and restore from.
    private List<List<String>> closurePossibilities;



    // ---------------------------------------------------------------------------------
    /**
     * Create our boxes and boxes backup boolean arrays and empty Arraylist for closure
     * possibilities.
     */
    public Boxes() {

        boxes = new boolean[9];
        backup = new boolean[9];
        closurePossibilities = new ArrayList<>(11);
    }



    // ---------------------------------------------------------------------------------
    /**
     * Creates a backup of our current box state. Typically occurs after a successful
     * turn.
     */
    public void backup() {
        
        backup = boxes.clone();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Restores boxes back to the last time they were backed up. Typically occurs after
     * a failed turn.
     */
    public void restore() {
        
        boxes = backup.clone();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Sets both our primary boxes and backup boxes to open. This happens at the
     * beginning of each individual game.
     */
    public void setAllBoxesOpen() {
        
        Arrays.fill(boxes, true);                                                       // Set all boxes open, including our backup copy as
        Arrays.fill(backup, true);                                                      // this is only invoked at the start of a new game.
    }



    // ---------------------------------------------------------------------------------
    /**
     * Sets the box the user selected from the GUI as closed in our boxes array.
     *
     * @param boxToClose The box the user wishes to close.
     */
    public void setBoxClosed(int boxToClose) {                                          // Passed box number is 1 digit higher than array element

        boxes[boxToClose - 1] = false;                                                  // Close the box
    }



    // ---------------------------------------------------------------------------------
    /**
     * Returns the current state (true/false aka open/closed) of the box associated with
     * the argument variable passed to the method.
     *
     * @param boxNumber Box number to check, passed one lower than the actual box is relates to in the Gui.
     * @return The state of the box (true/false aka open/closed)associated with the argument passed.
     */
    public boolean getBoxState(int boxNumber) {

        return boxes[boxNumber];
    }



    // ---------------------------------------------------------------------------------
    /**
     * Extensibility class. Allows various for loops in game board Gui and game logic
     * classes to know the size of the boxes array. Useful moving forward should we wish
     * to reuse classes for games featuring various numbers of boxes.
     *
     * @return Returns the size of the boxes array.
     */
    public int getLength() {
        
        return boxes.length;
    }



    // ---------------------------------------------------------------------------------
    /**
     * Calculates if the game is over based on the dice roll passed as the argument. It
     * does ths by going through the inner ArrayList of the closure possibilities
     * ArrayList that holds all the possible closure combinations for the dice roll
     * total and seeing if any are possible given the current state of our boxes array.
     *
     * If it finds one the game can't be over so return false because the game isn't
     * over. If it gets to the end of all possible closure combinations and the number
     * of failures equals the number of possibilities then game over so returns true.
     *
     * @param diceTotal The total of the dice rolled and the closure possibilities location to check.
     * @return True or false based on if the game is over.
     */
    public boolean getIsGameOver (int diceTotal) {                                      // Returns true of it's game over.

        int possFailCount = 0;
                                                                                        // Iterate through the list of possible combinations that
        for (String poss: closurePossibilities.get(diceTotal - 2)) {                    // pertains to current dice total.

            boolean possFail = false;                                                   // Reset whether or not this box close possibility has failed.
            
            for (int i = 0; i < poss.length() && !possFail; i++ ) {                     // Iterate through each char unless a box we try is already closed.

                if (!boxes[Character.getNumericValue(poss.charAt(i)) - 1]) {            // If the box we're checking is already false AKA closed.
                    
                    possFail = true;                                                    // Exit this possibility loop check
                    possFailCount++;                                                    // and increment our failure counter.
                }
            }
        }
        return (closurePossibilities.get(diceTotal - 2).size()) == possFailCount;       // If the amount of closure possibilities at the element which pertains to the dice
    }                                                                                   // roll total equals the number of closure possibilities then it must be game over.



    // ---------------------------------------------------------------------------------
    /**
     * Calculates the final score at the end of a game. The final score is the total of
     * all the elements in our boxes array that return true; all remaining open boxes.
     *
     * @return the final score as an int at the end of each game.
     */
    public int getScore() {
    
        int finalScore = 0;
    
        for (int i = 0; i < boxes.length; i++) {                                        // Iterate through our boxes array.
        
            if (boxes[i]) {
                
                finalScore += i + 1;                                                    // Calculating our final score (the sum of all open boxes).
            }
        }
        return finalScore;
    }



    // ---------------------------------------------------------------------------------
    /**
     * Calculates all possible box closure permutations for 9 boxes numbered 1-9 when the sum of each box closed equals
     * 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 and 12.
     *
     * Iterate through numbers 2-260 and use their binary strings as combinations for potential open/close box states. The
     * results are calculated once at runtime and stored in a list of lists where one inner list contains a dice roll's
     * possible box closures. A given dice rolls corresponding inner list is queried per turn to determine game over state.
     *
     * The number 14 in binary is 1110, which I reverse as the list is generated simply to make the code easier to read.
     *
     *      [8][4][2][1]        Binary                Try to close boxes 4, 3 and 2 on a dice roll of 9 to check for game
     *      [4][3][2][1]        Decimal (boxes)       over state. Of course all possible permutations for how to close
     *       1  1  1  0                               boxes that equal 9 are stored at the list location, not just this one
     *
     *
     * Resulting list of list that we generate once at runtime, but check through corresponding dice roll per turn:
     *
     *      Element   |  Dice roll  |  Inner list of closure possibilities.
     *                |             |
     *         0      |      2      |      [2]
     *         1      |      3      |      [21, 3]
     *         2      |      4      |      [31, 4]
     *         3      |      5      |      [32, 41, 5]
     *         4      |      6      |      [321, 42, 51, 6]
     *         5      |      7      |      [421, 43, 52, 61, 7]
     *         6      |      8      |      [431, 521, 53, 62, 71, 8]
     *         7      |      9      |      [432, 531, 54, 621, 63, 72, 81, 9]
     *         8      |     10      |      [4321, 532, 541, 631, 64, 721, 73, 82, 91]
     *         9      |     11      |      [5321, 542, 632, 641, 65, 731, 74, 821, 83, 92]
     *        10      |     12      |      [5421, 543, 6321, 642, 651, 732, 741, 75, 831, 84, 921, 93]
     */
    public void getClosurePossibilities() {
                
        for (int i = 1; i <= 11; i++) {
            
            closurePossibilities.add(new ArrayList<>());                                // Instantiate 11 inner Lists for dice throws 2 to 12
        }
        
        for (int i = 2; i <= 260; i++ ) {                                               // Start at 2 because it's 10 in binary,
                        
            if (Integer.bitCount(i) < 5) {                                              // Max 4 boxes closed at once
                
                String binStr = Integer.toBinaryString(i);
                String decStr = "";                                                     // Binary strings are in reverse for our needs, we read
                int boxNumTotal = 0;                                                    // the binary string left to right with the box number
                int boxNum = binStr.length();                                           // decreasing each time we move to the right
                
                for (int j = 0; j < binStr.length(); j++ ) {                            // Iterate through each char from left to right
            
                    if (binStr.charAt(j) == '1') {                                      // If the current char in our string is 1 (AKA close)
                        boxNumTotal += boxNum;                                          // Add corresponding box number to our total
                        decStr += boxNum;                                               // And build our box closure combo string
                    }
                    boxNum--;
                }
                
                if (boxNumTotal <= 12) {
                    closurePossibilities.get(boxNumTotal - 2).add(decStr);              // Add that binary string to the corresponding inner list
                }
            }
        }
    }
}