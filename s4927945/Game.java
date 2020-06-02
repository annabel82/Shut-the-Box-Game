/**
 * This class manages the basic configuration of our game, such as throwing dice and
 * getting the total, setting up a String for our game board's message, handling box
 * state, setting up a new game, calculating if the current turn or current game is
 * over and returning the player's final score.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */


package s4927945;

public class Game
{
    private Die     die1, die2;
    //Single box object housing 9 boxes, because each box has a relationship with the others
    private Boxes   boxes;
    private String  message;
    private boolean isTurnSuccessful;
    private int     boxesChosenThisTurn, closedBoxSumThisTurn;
    private int[]   dice;



    // ---------------------------------------------------------------------------------
    /**
     * Instantiates our dice and the boxes objects. Gets the closure possibilities and
     * sets up all the conditions required to start a new game.
     */
    public Game ()
    {
        die1 = new Die();
        die2 = new Die();
        boxes = new Boxes();
        dice = new int[2];

        // Calculate box closure possibilities just the once
        boxes.getClosurePossibilities();
        setUpNewGame();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Gets the dice roll of both dice.
     *
     * @return The total of both dice as an integer.
     */
    private int getDiceTotal()
    {
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
    public int[] getDiceThrow()
    {
        int[] oldDice = dice.clone();

        // Ensure at least one dice changes. Due to identical successive rolls causing a
        // little player  confusion, because it left little to indicate to the player that
        // a new turn had begun. I felt this was the most elegant solution
        while (oldDice[0] == die1.getRoll())
        {
            die1.roll(6);
        }

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
    public int getBoxesLength()
    {
        return boxes.getLength();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Sets the message to be used in the game board's GUI after dice were rolled.
     */
    public void setDiceThrownMessage()
    {
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
    public void setBoxChoice(int choice)
    {
        // Add choice to the sum of choices this turn
        closedBoxSumThisTurn += choice;

        // Increment our choice counter
        boxesChosenThisTurn++;

        // Passes on as-is
        boxes.setBoxClosed(choice);
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
    public boolean getBoxState (int boxNumber)
    {
        return boxes.getBoxState(boxNumber);
    }



    // ---------------------------------------------------------------------------------
    /**
     * Allows the game board class access to whatever the current message is set to.
     *
     * @return the value of the current String variable "message:.
     */
    public String getMessage()
    {
        return message;
    }



    // ---------------------------------------------------------------------------------
    /**
     * Allows the game board class access to whether or not the current turn is successful.
     *
     * @return True or false based on if the turn is successful or not. True if it is; false if not.
     */
    public boolean getTurnSuccess()
    {
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
    public boolean getIsTheTurnOver(boolean turnWasReset)
    {
        if (closedBoxSumThisTurn == getDiceTotal())
        {
            isTurnSuccessful = true;
            closedBoxSumThisTurn = 0;
            boxesChosenThisTurn = 0;
            boxes.backup();

            return true;
        }
        else if (turnWasReset)
        {
            message = "Box choices reset, the dice roll total is " + getDiceTotal();
        }
        else if (boxesChosenThisTurn > 3)
        {
            // If we've made 4 choices and not reached our dice total set message and skip to end.
            message = "Failed to match the total dice roll of " + getDiceTotal();
        }
        else if (closedBoxSumThisTurn > getDiceTotal() && boxesChosenThisTurn == 1)
        {
            // If our choice has exceeded our dice total, set an appropriate message and skip to the end
            message = "Box " + closedBoxSumThisTurn + " exceeds the combined dice " + "roll of " + getDiceTotal() + ", please try again";
        }
        else if (closedBoxSumThisTurn > getDiceTotal())
        {
            // If our choices have exceeded our dice total, set an appropriate message and skip to the end.
            message = "Box choices totalling " + closedBoxSumThisTurn + " exceed the " + "combined dice roll of " + getDiceTotal() + ", please try again";
        }
        // If no success or failure conditions are met..
        else
        {
            // ..roll the dice again
            return false;
        }

        // If we haven't broken out of the loop by now we've reached some kind of failure condition
        isTurnSuccessful = false;
        closedBoxSumThisTurn = 0;
        boxesChosenThisTurn = 0;
        boxes.restore();

        // Turn ended
        return true;
    }



    // ---------------------------------------------------------------------------------
    /**
     * Determines whether or not the game is over and sets the message value appropriately.
     *
     * @return True if the game is over or false if it is not.
     */
    public boolean getIsGameOver()
    {
        if (boxes.getIsGameOver(getDiceTotal()))
        {
            if (boxes.getScore() == 0)
            {
                message = "Dice roll total was " + getDiceTotal() + " - game over! You got the best score possible! Well Done!";
            }
            else
            {
                message = "Dice roll total was " + getDiceTotal() + " - game over! You scored " + boxes.getScore() + ". Remember a lower score is better!";
            }

            return true;
        }
        else
        {
            // In else block for readability.
            return false;
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Gets the score at the end of a game.
     *
     * @return The sum of the opened boxes in our box object.
     */
    public int getFinalScore()
    {
        return boxes.getScore();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Sets up a new game. Sets our flag for turn success, resets our failure condition
     * counters, sets all boxes open and sets the message variable to reflect the new
     * dice roll.
     */
    public void setUpNewGame()
    {
        isTurnSuccessful = false;
        closedBoxSumThisTurn = 0;
        boxesChosenThisTurn = 0;
        boxes.setAllBoxesOpen();
        message = "Dice roll is " + getDiceTotal() + ", Good luck!";
    }
}
