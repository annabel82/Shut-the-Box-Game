/**
 * This class is intended to be a modular add-on for the menus class. It allows the
 * menus to be more easily re-usable across various game programs. The class sets up our
 * game board, comprising of the score board, 2 button menu, boxes, dice throw panel and
 * the message bar. It also handles various Gui related functions such as updating the
 * Gui to be inline with the game's current status, including resetting the game board
 * to a new fresh state, keeping scores, and visually handling game over conditions.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */


package s4927945;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class GameBoard
{
    private Menus              menus;
    private Game               game;
    private JPanel             gamePanel, boxesPanel, player1HolderPanel, player2HolderPanel;
    private JPanelBg           throwPanel;
    private JButton            resetButton;
    private JButton[]          boxButton;
    private JLabel[]           boxLabel;
    private GridBagConstraints gc;
    private JLabel             player1NameLabel, player2NameLabel, messageLabel, roundOverLabel;
    private JLabelRotated      dieLabel1, dieLabel2;
    private String[]           playerNames;
    private int                numberOfScores, numberOfRoundsRemaining, chosenBoxNo, player1Total, player2Total;
    private boolean            gameIsInProgress, hasClosedBox, isTwoPlayer, wasTwoPlayerGame, wasPlayerOnesTurn;



    // ---------------------------------------------------------------------------------
    /**
     * Sets up the basic game board layout. First we make a container panel, then we add
     * our score panel, then the panel that contains the two yellow buttons, then the
     * throw panel which contains the dice and logo. Finally we add our message bar to
     * the bottom of the screen to help inform the user of the games current status.
     *
     * @param menus          Allows this class to access functions in s4927945.Menus
     */
    public GameBoard(Menus menus)
    {
        this.menus = menus;

        wasPlayerOnesTurn = true;
        numberOfScores = 1;

        // gameIsInProgress determines if "Continue" or "New Game" button displays on the
        // main menu. hasClosedBox determines if "Are you sure?" menu appears when
        // starting a new game. They're not the same; we do want to see "Continue" during
        // a game, but we don't want to see "are you sure?" unless a box has been closed
        // during the game that's underway
        gameIsInProgress = false;
        hasClosedBox = false;
        game = new Game();
        gc = new GridBagConstraints();
        Font yellowButtonTxt = new Font("Serif", Font.PLAIN, 12);
        Font urban = new Font("The Urban Way", Font.BOLD, 15);
        Color yellowButtonTxtColor = new Color(0, 0, 0);

        // -----------------------------------------

        gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setPreferredSize(new Dimension(1280, 720));
        gamePanel.setOpaque(false);
        menus.containerPanel.add(gamePanel, BorderLayout.CENTER);

        roundOverLabel = new JLabel();
        roundOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roundOverLabel.setFont(new Font("The Urban Way", Font.BOLD, 20));
        roundOverLabel.setForeground(new Color(40, 40, 40));
        roundOverLabel.setText("Next round starts in 4");
        roundOverLabel.setHorizontalTextPosition(JLabel.CENTER);
        roundOverLabel.setVerticalTextPosition(JLabel.CENTER);

        ImageIcon bg = null;

        try
        {
            bg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/imgs/unified-round-over.png")));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog (menus, "File: resources/imgs/unified-round-over.png failed to load:" + e + ".\n" +
                    "You may OK this message and continue at your own risk.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        roundOverLabel.setIcon(bg);
        menus.glass.add(roundOverLabel, SwingConstants.CENTER);
        roundOverLabel.setVisible(false);

        // -----------------------------------------

        JPanelBg scorePanel = new JPanelBg("paper.png");
        scorePanel.setLayout(new GridLayout(1, 2));

        JPanel scorePanelLeft = new JPanel(new GridBagLayout());
        JPanel scorePanelRight = new JPanel(new GridBagLayout());

        scorePanelLeft.setOpaque(false);
        scorePanelRight.setOpaque(false);

        scorePanel.add(scorePanelLeft);
        scorePanel.add(scorePanelRight);

        player1NameLabel = new JLabel("", SwingConstants.CENTER);
        player2NameLabel = new JLabel("", SwingConstants.CENTER);

        player1NameLabel.setFont(urban);
        player2NameLabel.setFont(urban);

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;

        //             Margin Top, L, B, Right
        gc.insets = new Insets(50, 0, 0, 0);
        scorePanelLeft.add(player1NameLabel, gc);
        scorePanelRight.add(player2NameLabel, gc);

        player1HolderPanel = new JPanel(new GridLayout(20, 1));
        player2HolderPanel = new JPanel(new GridLayout(20, 1));

        player1HolderPanel.setOpaque(false);
        player2HolderPanel.setOpaque(false);

        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        scorePanelLeft.add(player1HolderPanel, gc);
        scorePanelRight.add(player2HolderPanel, gc);

        gc.weightx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        gc.insets = new Insets(30, 30, 30, 20);
        gamePanel.add(scorePanel, gc);

        // -----------------------------------------

        boxesPanel = new JPanel();

        // Create transparent boxes container one high and nine wide, with 10 pixels horizontally between each box
        boxesPanel.setLayout(new GridLayout(1, 9, 15, 0));

        boxesPanel.setOpaque(false);

        gc.weightx = 0;
        gc.weighty = 0.1;
        gc.gridx = 1;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.insets = new Insets(25, 0, 25, 30);
        gamePanel.add(boxesPanel, gc);

        // -----------------------------------------

        JPanel twoButtonPanel = new JPanel();

        // Create transparent sub-container for yellow buttons two high and one wide.
        twoButtonPanel.setLayout(new GridLayout(2, 1));

        twoButtonPanel.setOpaque(false);
        boxesPanel.add(twoButtonPanel);
        //                                                        Font          Text colour            Text              Image             Pressed Image            Disabled image
        JButtonCustomised menuButton = new JButtonCustomised(yellowButtonTxt, yellowButtonTxtColor, "<b>MENU</b>", "yellow-btn.png", "yellow-btn-pressed.png", "yellow-btn-pressed.png");
                         resetButton = new JButtonCustomised(yellowButtonTxt, yellowButtonTxtColor, "<b>RESET</b>", "yellow-btn.png", "yellow-btn-pressed.png", "yellow-btn-pressed.png");

        menuButton.setForeground(new Color(0, 0, 0));
        resetButton.setForeground(new Color(0, 0, 0));

        menuButton.addActionListener(new YellowMenuButtonHandler());
        resetButton.addActionListener(new YellowResetButtonHandler());

        twoButtonPanel.add(menuButton);
        twoButtonPanel.add(resetButton);

        // Calls the method below which creates our boxes
        setBoxDisplay();

        // -----------------------------------------

        throwPanel = new JPanelBg("green_felt.png");
        throwPanel.setLayout(new BorderLayout());

        gc.gridy = 1;
        gc.weighty = 1;
        //            Margin Top, L,  B,  Right
        gc.insets = new Insets(0, 10, 30, 30);
        gamePanel.add(throwPanel, gc);

        JPanel dicePanel = new JPanel();

        // Create invisible container for the 2, absolutely positioned dice.
        dicePanel.setOpaque(false);

        dicePanel.setLayout(null);

        // Add centrally positioned invisible dice container
        throwPanel.add(dicePanel, BorderLayout.CENTER);

        // Create placeholder dice labels.
        dieLabel1 = new JLabelRotated();
        dieLabel2 = new JLabelRotated();

        // Set the dice labels locations.
        dieLabel1.setBounds(40, 100, 100, 100);
        dieLabel2.setBounds(150, 25, 100, 100);
        dicePanel.add(dieLabel1);
        dicePanel.add(dieLabel2);

        setDiceDisplay(game.getDiceThrow());

        // -----------------------------------------

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBackground(new Color(20, 20, 20));
        messageLabel.setForeground(new Color(200, 200, 200));
        messageLabel.setOpaque(true);
        messageLabel.setFont(urban);

        messageLabel.setBorder(BorderFactory.createCompoundBorder(
               new MatteBorder(2, 2, 2, 2, new Color(100, 100, 100)),
               BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weighty = 0;
        gc.gridwidth = 3;
        gc.insets = new Insets(0, 30, 30, 30);
        gamePanel.add(messageLabel, gc);

        gamePanel.setVisible(false);
    }



    // ---------------------------------------------------------------------------------
    /**
     * Uses a simple loop to populate the boxesPanel of our GUI with the same number of
     * graphical buttons our box game uses (9). Each box has an action command equal to
     * the number of the box it represents (from 1-9). Called from createGamePanel and
     * generates our buttons which serve as the boxes for our game
     */
    private void setBoxDisplay ()
    {
        String boxNo;
        Font boxTxt = new Font("Serif", Font.PLAIN, 40);
        Color boxTxtColour = new Color(0, 0, 0);

        // Arrays of JButtons the same size as the number of boxes we have.
        boxButton = new JButton[game.getBoxesLength()];
        boxLabel = new JLabel[game.getBoxesLength()];
        BoxButtonHandler boxButtonHandler = new BoxButtonHandler();

        for (int i = 0; i < game.getBoxesLength(); i++)
        {

            boxNo = String.valueOf(i + 1);
            //                                  Layout      Image        Pressed Image     Disabled image
            boxButton[i] = new JButtonCustomised(null, "box-open.png", "box-closed.png", "box-closed.png");

            boxLabel[i] = new JLabel();
            boxLabel[i].setBounds(31, 120, 40, 40);
            boxLabel[i].setFont(boxTxt);
            boxLabel[i].setForeground(boxTxtColour);
            boxLabel[i].setText(boxNo);

            boxButton[i].add(boxLabel[i]);
            boxButton[i].setName("boxButton" + boxNo);
            boxButton[i].setActionCommand(boxNo);
            boxButton[i].addActionListener(boxButtonHandler);

            boxesPanel.add(boxButton[i]);
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * When no more boxes can be closed for the current round, this method gets the
     * user's final score from the game logic and appends the relevant score panel
     * accordingly to display the score to the user. In a single player game it also
     * clears the scoreboard every 20 games to ensure the scores don't run off the end
     * of the scoreboard.
     */
    private void getScoreBoardUpdate()
    {
        String score = String.valueOf(game.getFinalScore());

        // If it's not a two player or player1 is playing
        if (!isTwoPlayer || numberOfRoundsRemaining % 2 == 0)
        {
            // If score panel gets too full.
            if (numberOfScores > 20)
            {
                // Clear the score panel if it gets too full.
                player1HolderPanel.removeAll();
                numberOfScores = 0;
            }

            numberOfScores++;

            // Add score to player1's board
            player1HolderPanel.add(new JLabel(score, SwingConstants.CENTER));
            player1Total = player1Total + game.getFinalScore();
        }
        else
        {
            // Add score to player2's board
            player2HolderPanel.add(new JLabel(score, SwingConstants.CENTER));
            player2Total = player2Total + game.getFinalScore();
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method allows s4927945.Menus to check the if a game is currently in progress
     * so menus can be tailored accordingly
     *
     * @return Returns true if the game is in progress, otherwise false.
     */
    public boolean getIsGameInProgress()
    {
        return gameIsInProgress;
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method allows s4927945.Menus to check the if the user has closed a box
     * during the current game so that menus can be tailored accordingly
     *
     * @return Returns true if a user has successfully closed a box, otherwise false.
     */
    public boolean getHasClosedBox()
    {
        return hasClosedBox;
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method allows s4927945.Menus#setMenuVisible to set the gamePanel container
     * visible or invisible
     *
     * @param choice True or false depending on whether or not we should display the
     *               gamePanel container.
     */
    public void setGameVisible(boolean choice)
    {
        gamePanel.setVisible(choice);

        gamePanel.repaint();
        gamePanel.revalidate();
    }



    // ---------------------------------------------------------------------------------
    /**
     * This Method is called every time we start a new game
     * Sets up the new game for us. Appends the scoreboard's names as appropriate,
     * resets felt colour to green because the first player is always player 1. sets the
     * game state booleans for hasClosedBox and gameIsInProgress. Finally it tells game
     * logic to reset itself, and having done so it gets new dice throws, restores all
     * boxes to open and then gets a message bar update.
     *
     * @param numberOfRounds The number of rounds in the game we're going to play.
     * @param isTwoPlayer    Whether or not it's a two player game.
     * @param playerNames    A String array of up to 2 player names.
     */
    public void setupNewGame(int numberOfRounds, boolean isTwoPlayer, String[] playerNames)
    {
        this.isTwoPlayer = isTwoPlayer;
        this.playerNames = playerNames;
        this.numberOfRoundsRemaining = numberOfRounds;

        if (isTwoPlayer)
        {
            player2Total = 0;

            // Differs from isTwoPlayer because wasTwoPlayerGame is only altered within this function.
            wasTwoPlayerGame = true;
            player1HolderPanel.removeAll();
            player2NameLabel.setText(playerNames[1]);
            player2HolderPanel.removeAll();
        }
        else
        {
            // But was a 2 player game last time
            if (wasTwoPlayerGame)
            {
                wasTwoPlayerGame = false;
                player1HolderPanel.removeAll();
                player2HolderPanel.removeAll();
                player2NameLabel.setText("");
                numberOfScores = 0;
            }

            // If player 1's name has changed
            if (!player1NameLabel.getText().equalsIgnoreCase(playerNames[0]))
            {
                player1HolderPanel.removeAll();
                numberOfScores = 0;
            }
        }

        player1Total = 0;
        player1NameLabel.setText(playerNames[0]);

        throwPanel.setNewImage("green_felt.png");
        throwPanel.repaint();

        wasPlayerOnesTurn = true;
        hasClosedBox = false;
        gameIsInProgress = true;

        // We want to play again, so setup the back end accordingly, throw and display
        // some new dice, recurse through GUI post boxes.setAllOpen(), then get a
        // message bar update.
        game.setUpNewGame();

        setDiceDisplay(game.getDiceThrow());
        setPreviousGuiBoxesState();
        getMessageBarUpdate();
    }



    // ---------------------------------------------------------------------------------
    /**
     *  Does what's required when a game over state has occurred. Updates scores, gets a
     *  message bar update, sets game state booleans, shows the in game modal screen with
     *  game over conditions displayed if the whole game is over, of if we're halfway
     *  through a set of games in multi-player mode, we display the next user ready
     *  countdown screen.
     *
     *  In multi-player mode, when the timer is up, the felt background changes colour
     *  we set our game state boolean, tell the game logic to setup a new game, then get
     *  a new dice throw, reset our graphical boxes to open and finally get a message
     *  bar update. Called from BoxButtonAfterTimerHandler.
     */
    private void setGameOverCondition()
    {
        getScoreBoardUpdate();
        getMessageBarUpdate();

        numberOfRoundsRemaining--;

        if (numberOfRoundsRemaining == 0)
        {
            hasClosedBox = false;
            gameIsInProgress = false;
            menus.createInGameModal();
            menus.getGlassPane().setVisible(false);
        }
        else
        {
            // Set the text as it needs to be to begin with, avoids flashing a 0 before the counter setText kicks in.
            roundOverLabel.setText("Next round starts in 4");

            //Show our round over label.
            roundOverLabel.setVisible(true);

            Timer timer = new Timer(1100, new ActionListener()
            {
                int counter = 4;

                // Loop until counter hits 0
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // Show our countdown message
                    roundOverLabel.setText("Next round starts in " + (counter - 1));
                    counter--;

                    if (counter == 0)
                    {
                        ((Timer) e.getSource()).stop();

                        hasClosedBox = false;
                        game.setUpNewGame();
                        setDiceDisplay(game.getDiceThrow());

                        // Recurse through boxes resetting their visual status.
                        setPreviousGuiBoxesState();

                        if (wasPlayerOnesTurn)
                        {
                            throwPanel.setNewImage("blue_felt.png");
                            wasPlayerOnesTurn = false;
                            messageLabel.setText(game.getMessage() + " - Good Luck " + playerNames[1] + " !");

                        }
                        else
                        {
                            throwPanel.setNewImage("green_felt.png");
                            wasPlayerOnesTurn = true;
                            messageLabel.setText(game.getMessage() + " - Good Luck " + playerNames[0] + " !");
                        }

                        throwPanel.revalidate();
                        roundOverLabel.setVisible(false);
                        menus.getGlassPane().setVisible(false);
                    }
                }
            });

            // Totals 1000ms when coupled with BoxButtonHandler
            timer.setInitialDelay(600);
            timer.start();
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Builds the game over message String we show on the game over modal menu.
     *
     * @return A string with a brief message detailing which player won, or if a single
     *         player game, their final score.
     */
    public String getGameOverMessage()
    {
        if (isTwoPlayer)
        {
            if (player1Total < player2Total)
            {
                return playerNames[0] + " wins!";
            }
            else if (player1Total > player2Total)
            {
                return playerNames[1] + " wins!";
            }
            else
            {
                return "It's a draw!";
            }
        }
        else
        {
            return "You scored " + player1Total + "!";
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Upon a failed turn or at the start if a game, this method sets the Gui boxes to
     * reflect the state of the boolean boxes array the game logic interacts with. The
     * name of this method is somewhat misleading, in that "previous" can mean previous
     * to the game (so all boxes set open) or previous to the turn (so boxes as they
     * were before the failed turn). This method also disables the reset button after
     * the boxes have been set, because there can never be anything available to reset.
     */
    private void setPreviousGuiBoxesState()
    {
        // Iterate through each box in our boxes array.
        for (int i = 0; i < game.getBoxesLength(); i++)
        {
            // Do .setEnabled(true/false) (aka open/closed) as required
            boxButton[i].setEnabled(game.getBoxState(i));

            // If box returns true (aka open) add label back
            if (game.getBoxState(i))
            {
                boxLabel[i].setText(String.valueOf(i + 1));
            }
        }

        // We're resetting our boxes because a failed state occurred, so disable reset
        // until another box is chosen next turn
        resetButton.setEnabled(false);

        boxesPanel.revalidate();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Takes the current int dice array and sets the Gui label icons for the two dice as
     * appropriate. It then tells game to update
     * @param dice the dice array containing both die values to use when displaying the
     *             dice.
     */
    private void setDiceDisplay(int[] dice)
    {
        dieLabel1.setIcon("die" + dice[0] + ".png", 175);
        dieLabel2.setIcon("die" + dice[1] + ".png", 195);

        dieLabel1.getParent().repaint();
        game.setDiceThrownMessage();
    }



    // ---------------------------------------------------------------------------------
    /**
     * Updates our message bar to reflect whatever message has been set by the logic in
     * the game logic class.
     */
    private void getMessageBarUpdate()
    {
        messageLabel.setText(game.getMessage());
    }



    // ---------------------------------------------------------------------------------
    /**
     * Calls createInGameModal in the Menus class to create the in game menu modal when
     * the user clicks the yellow "Menu" button from the game panel.
     */
    public class YellowMenuButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            menus.createInGameModal();
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Resets whichever boxes the user has attempted to close this turn. Used to undo
     * user choices when the user realises they've made a mistake. This isn't simply
     * cosmetic, it updates game logic (boxes restore themselves) to this effect too.
     */
    public class YellowResetButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Force game reset by passing true to end the turn
            game.getIsTheTurnOver(true);

            // Recurse through GUI boxes setting states post boxes.restore..
            getMessageBarUpdate();

            // ..and get our message bar update to reflect the reset
            setPreviousGuiBoxesState();
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * Handles what happens when a user clicks an open box in an attempt to close it.
     *
     * Firstly we drop out glass pane to consume further mouse clicks until such a time
     * as we wish to receive them. Then we get the value of the box that was clicked
     * from the action command of the box and then set the box as disabled both
     * mechanically and cosmetically. The reset button gets enabled because a box has
     * been closed so a box can be undone.
     *
     * Then we start a timer that waits before activating the rest of the logic
     * contained in this classes BoxButtonAfterTimerHandler method. We use this timer to
     * slow the pace of the selection process down. If a box closure choice is invalid,
     * there's a slight pause before the reset logic happens which allows the user to
     * better understand what just happened. The same goes for a game over condition, the
     * pause gives the user time to see that their choice was made, and another outcome
     * happened as a direct cause of it. It just makes the game's logic more
     * understandable.
     */
    public class BoxButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            menus.getGlassPane().setVisible(true);

            JButton chosenBox = (JButton) e.getSource();

            // Box no 1-9
            chosenBoxNo = Integer.parseInt(e.getActionCommand());

            // We've made a choice so ensure reset can be pressed. Then Disable chosen
            // box and remove it's label, then wait 400ms using the timer below before
            // continuing on to BoxButtonAfterTimerHandler.
            boxLabel[chosenBoxNo - 1].setText("");
            chosenBox.setEnabled(false);
            resetButton.setEnabled(true);

            // Timer is here so that if an invalid box is chosen there is a pause before
            // it automatically resets, otherwise it seems too instant and can be confusing.
            Timer timer = new Timer(400, new BoxButtonAfterTimerHandler());
            timer.setRepeats(false);
            timer.start();
          }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method is called after BoxButtonHandler's timer finishes.
     *
     * First we set out box choice in the game logic, and then we test to see if the
     * _TURN_ is over which means we've successfully closed boxes equal to our dice
     * roll., if so we do a message bar update and remove our click intercepting glass
     * pane.
     *
     * Then we check to see if the turn was a success, and if it wasn't we roll back the
     * users choice by way of the setPreviousGuiBoxState method.
     *
     * Failing that we must still be in the middle of a turn so get a new dice roll,
     * disable the yellow reset button and flag out hasClosedBox boolean.
     *
     * Because one of the above possibilities has happened, we need to check for a game
     * over state and if our game is over, we handle the game over state condition.
     */
    public class BoxButtonAfterTimerHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            game.setBoxChoice(chosenBoxNo);

            // False here because we're not forcing the condition.
            if (game.getIsTheTurnOver(false)) {

                // If it's over without success re-paint our boxes after the boxes.restore
                // triggered by unsuccessful turn
                if (!game.getTurnSuccess())
                {
                    setPreviousGuiBoxesState();
                }
                else
                {
                    //If this turn successfully closed boxes we roll again and disable the
                    // reset button until we get another choice
                    setDiceDisplay(game.getDiceThrow());
                    resetButton.setEnabled(false);
                    hasClosedBox = true;
                }

                // And having rolled the dice, we test for a game over
                if (game.getIsGameOver())
                {
                    // If it's game over handle it whilst leaving the glass pane enabled
                    setGameOverCondition();
                }
                else
                {
                    // If the turn is over for whatever reason but it's not game over
                    // disable the glass pane
                    menus.getGlassPane().setVisible(false);
                    getMessageBarUpdate();
                }
            }
            else
            {
                // If the turn isn't over disable the glass pane.
                menus.getGlassPane().setVisible(false);
                getMessageBarUpdate();
            }
        }
    }
}
