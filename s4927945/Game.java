/**
 * This class
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */


package s4927945;

public class Game {

	private Die     die1, die2;
	private Boxes   boxes;                                                              // Single box object housing 9 boxes, because
    private String  message;                                                            // each box has a relationship with the others.
	private boolean isTurnSuccessful;
	private int     boxesChosenThisTurn, closedBoxSumThisTurn;
	private int[]   dice;



    // ---------------------------------------------------------------------------------
    /**
     * Instantiates our dice and the boxes objects. Gets the closure possibilities and
     * sets up all the conditions required to start a new game.
     */
	public Game () {

        die1 = new Die();
        die2 = new Die();
        boxes = new Boxes();
        dice = new int[2];

		boxes.getClosurePossibilities();		            							// Calculate box closure possibilities just
		setUpNewGame();										            				// the once.
	}



    // ---------------------------------------------------------------------------------
    /**
     * Gets the dice roll of both dice.
     *
     * @return The total of both dice as an integer.
     */
	private int getDiceTotal() {

		return die1.getRoll() + die2.getRoll();
	}



    // ---------------------------------------------------------------------------------
    /**
     * Gets fresh dice rolls as an int array and insures the first of the two dice is
     * different each time. This helps with the visual representation to the user in the
     * GUI, since they do see at least one of the dice change to indicate they've been
     * rolled.
     *
     * @return An int array containing both dice rolls.
     */
    public int[] getDiceThrow() {
		
		int[] oldDice = dice.clone();
													    			        			// Ensure at least one dice changes, due to
		while (oldDice[0] == die1.getRoll()) {          								// identical successive rolls causing a little
													            						// confusion, because it left little to indicate
			die1.roll(6);  										            			// to the player that a new turn had begun. I
		}																	            // felt this was the most elegant solution.

		die2.roll(6);
		
		dice[0] = die1.getRoll();
		dice[1] = die2.getRoll();

		return dice;
	}



    // ---------------------------------------------------------------------------------
    /**
     * Extensibility class. Allows various for loops in game board GUI class to get the
     * size of the boxes array. Useful moving forward should we wish to reuse classes
     * for games featuring various numbers of boxes.
     *
     * @return Returns the size of the boxes array.
     */
    public int getBoxesLength() {
		
		return boxes.getLength();
	}



    // ---------------------------------------------------------------------------------
    /**
     * Sets the message to be used in the game board's GUI after dice were rolled.
     */
    public void setDiceThrownMessage() {
	
		message = "You rolled " + die1.getRoll() + " and " + die2.getRoll() + ", totalling " + getDiceTotal() + ".";
	}



    // ---------------------------------------------------------------------------------
    /**
     * Receives a box to close and closes it in the boxes array, then records then adds
     * the value of the box chosen to our running total of what each closed box adds up
     * to for verifying the total equals our dice roll.
     *
     * It also increments the other counter we use to determine a failed condition,
     * which is how many boxes have been closed this turn.
     *
     * @param choice receives box choice to be closed as a raw box number of 1-9
     */
    public void setBoxChoice(int choice) {									            // Receives 1-9.

		closedBoxSumThisTurn += choice;	            									// Add choice to the sum of choices this turn.
		boxesChosenThisTurn++;						            						// Increment our choice counter.
		boxes.setBoxClosed(choice);								            			// Passes on as-is.
		
	}



    // ---------------------------------------------------------------------------------
    /**
     * Passes on the value of the box state  associated with the passed argument
     * variable, (true for open and false for closed). This is used by the game board
     * class when setting the visual representation of the box state in the Gui.
     *
     * @param boxNumber box whose state we wish to check. Taken as a raw number that
     *                  exactly matches the box number in the Gui
     * @return          True or false based on if the box is open or closed respectively.
     */
    public boolean getBoxState (int boxNumber) {	                    				// Passes a boxes current state back to the
																                    	// caller.
		return boxes.getBoxState(boxNumber);
	}



    // ---------------------------------------------------------------------------------
    /**
     * Allows the game board class access to whatever the current message is set to.
     *
     * @return the value of the current String variable "message:.
     */
    public String getMessage() {
		
		return message;
	}



    // ---------------------------------------------------------------------------------
    /**
     * Allows the game board class access to whether or not the current turn is successful.
     *
     * @return True or false based on if the turn is successful or not. True if it is; false if not.
     */
    public boolean getTurnSuccess() {
		
		return isTurnSuccessful;
	}



    // ---------------------------------------------------------------------------------
    /**
     * Calculates whether or not the _TURN_ is over or not. If we succeeded, set out
     * success flag, reset the sum of out chosen boxes plus the number of boxes chosen
     * and back our boxes chosen thus far and return true.
     *
     *
     * Or if we chose to reset the turn, set an appropriate message and skip to the end
     * without returning yet.
     *
     * Or if we've made 4 choices and not reached our total the game is over because
     * another choice would certainly exceed the highest possible dice roll of 12 so
     * set an appropriate message and skip to the end without returning yet.
     *
     * Or if our boxes chosen exceed our dice roll set an appropriate message and skip
     * to the end.
     *
     * Otherwise we haven't finished our turn, so return false.
     *
     * Any path that skipped to the end will require the failure conditions to be set
     * which includes the boxes restored, then return true because the turn is over
     * albeit unsuccessfully.
     *
     * @param turnWasReset Whether or not the turn is forced to be over.
     * @return             True if the _TURN_ is over or false if it is not.
     */
    public boolean getIsTheTurnOver(boolean turnWasReset) {								// Returns true if turn is over.

		if (closedBoxSumThisTurn == getDiceTotal()) {
			
			isTurnSuccessful = true;													// If we succeeded, set our success flag,
			closedBoxSumThisTurn = 0;													// reset the sum of our chosen boxes
			boxesChosenThisTurn = 0;													// plus the number of boxes chosen
			boxes.backup();																// and backup our box choices thus far.
			return true;	
		
		} else if (turnWasReset) {														// If we chose to reset we set the
			
			message = "Box choices reset, the dice roll total is " + getDiceTotal();	// message accordingly and skip to the end

		} else if (boxesChosenThisTurn > 3) {
			
			message = "Failed to match the total dice roll of " + getDiceTotal();		// If we've made 4 choices and not reached
                                                                                        // our dice total set message and skip to end.
		} else if (closedBoxSumThisTurn > getDiceTotal() && boxesChosenThisTurn == 1) {				
			
			message = "Box " + closedBoxSumThisTurn + " exceeds the combined dice "
					+ "roll of " + getDiceTotal() + ", please try again";				// If our choices have exceeded our dice
                                                                                        // total, set an appropriate message and skip
		} else if (closedBoxSumThisTurn > getDiceTotal()) {								// to the end.
			
			message = "Box choices totalling " + closedBoxSumThisTurn + " exceed the "
					+ "combined dice roll of " + getDiceTotal() + ", please try again";
		} else {																		// If no success or failure conditions are
                                                                                        // met the turn hasn't ended so return false
			return false;																// to roll the dice again.
		}
		
		isTurnSuccessful = false;														// If we haven't broken out of the loop by
		closedBoxSumThisTurn = 0;														// now we've reached some kind of failure
		boxesChosenThisTurn = 0;														// condition, so reset the boxes closed sum
		boxes.restore();																// to zero, restore our boxes to how they
                                                                                        // were when the turn started and return
		return true;																	// true because the turn has ended.
	}



    // ---------------------------------------------------------------------------------
    /**
     * Determines whether or not the game is over and sets the message value appropriately.
     *
     * @return True if the game is over or false if it is not.
     */
    public boolean getIsGameOver() {											    	// Returns true of game is over.
		
		if (boxes.getIsGameOver(getDiceTotal())) {

            if (boxes.getScore() == 0) {

                message = "Dice roll total was " + getDiceTotal() +
                          " - game over! You got the best score possible! Well Done!";
            } else {

                message = "Dice roll total was " + getDiceTotal() + " - game over! You scored "
                    + boxes.getScore() + ". Remember a lower score is better!";

            }

            return true;

        } else {

			return false;																// In else block for readability.
		}
	}



    // ---------------------------------------------------------------------------------
    /**
     * Gets the score at the end of a game.
     *
     * @return The sum of the opened boxes in our box object.
     */
    public int getFinalScore() {

        return boxes.getScore();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Sets up a new game. Sets our flag for turn success, resets our failure condition
     * counters, sets all boxes open and sets the message variable to reflect the new
     * dice roll.
     */
    public void setUpNewGame() {

		isTurnSuccessful = false;														// flag game is fresh and reset our success
		closedBoxSumThisTurn = 0;														// state. Then reset both our counters, set
		boxesChosenThisTurn = 0;														// all boxes to open (which includes the
		boxes.setAllBoxesOpen();														// backup boxes too).
		message = "Dice roll is " + getDiceTotal() + ", Good luck!";
	}
}