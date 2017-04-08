/**
 * Intended to be a stand-alone class to provide a framework for menus for various
 * game programs. Provided is a menu bar and main menu screen, pls a how to play, about,
 * choose number of players, choose player names, in-game menu, game over menu, next
 * player get ready and a confirm (new game or quit) choice menu.
 *
 * Date: 3rd April 2017
 * @author: Anna Thomas - s4927945
 * Week 39
 * Semester 2 - Assignment
 */

package s4927945;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Menus extends JFrame {

    public  JPanelBg               containerPanel;
    public  JPanel                 glass;
    private StartNewGameBtnHandler startNewGameBtnHandler;
    private ContinueGameBtnHandler continueGameBtnHandler;
    private HowToPlayBtnHandler    howToPlayBtnHandler;
    private AboutBtnHandler        aboutBtnHandler;
    private ExitBtnHandler         exitBtnHandler;
    private GameBoard              gameBoard;
    private JPanel                 mainMenuContainerPanel;
    private JButton[]              mainMenuButtons;
    private JDialog                newGameMenuDialog, playerNameMenuDialog, inGameMenuDialog, confirmDialog, howToPlayDialog, aboutDialog;
    private GridBagConstraints     gc;
    private Color                  buttonTextColour, backgroundColour;
    private Font                   buttonText;
    private JTextField             player1Name, player2Name;
    private String[]               playerNames;
    private boolean                createNewGameMenuOpen, createPlayerNameModal, aboutMenuOpen, howToPlayMenuOpen, inGameChoiceMenuOpen,
                                   choiceConfirmationMenuOpen, confirmChoice;



    // ---------------------------------------------------------------------------------
    /**
     * This constructor sets up the initial player names, the colours, fonts and
     * grid bag constraints that will be used throughout the class. It then tailors the
     * JFrame this class extends to suit out needs, as well as adding a base container
     * panel to hold our background image and a glass frame to block user input as we
     * see fit. We create the main menu we see when we first load the program as well as
     * the menu bar. Finally we set the JFrame as visible and create an instance of our
     * game board.
      */
    public Menus() {

        playerNames = new String[] {"Player1", "Player2"};
        buttonTextColour = new Color(53, 41, 34);
        buttonText = new Font("Serif", Font.PLAIN, 28);
        backgroundColour = new Color(194, 147, 105);
        gc = new GridBagConstraints();

        System.setProperty("apple.laf.useScreenMenuBar", "true");

        try {

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("resources/fonts/the-urban-way.ttf")));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/fonts/the-urban-way.ttf failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }


        try {

            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("resources/imgs/die6.png")));

        } catch (Exception e) {

            JOptionPane.showMessageDialog (this, "File: resources/imgs/die6.png failed to load:" + e + ".\n" +
                                                 "You may OK this message and continue at your own risk.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
        }

        setBackground(backgroundColour);
        setTitle("Shut the Box");                                                       // Create our window frame
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        containerPanel = new JPanelBg("wood-with-trim.png");                            // Then the base container
        containerPanel.setPreferredSize(new Dimension(1280, 720));
        containerPanel.setOpaque(false);
        add(containerPanel, BorderLayout.CENTER);

        glass = new JPanel(new GridLayout(0, 1));                                       // Glass pane stops boxes being closed during 400ms timer delay.
        glass.setOpaque(false);
        setGlassPane(glass);

        glass.addMouseListener(new MouseAdapter() {                                     // Listen for clicks on glass pane
            public void mousePressed(MouseEvent e)
            {
                e.consume();                                                            // And discard them.

            }
        });

        createMainMenuPanel();                                                          // Create main menu
        createMenuBar();                                                                // Create menu bar

        gameBoard = new GameBoard(this);


        setVisible(true);
        pack();
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates our main menu panel which contains the "New Game" button if a
     * game isn't currently underway, otherwise it shows the "Continue" button. It also
     * contains the "How to Play", "About" and "Exit" button. This method is called from
     * Menus.java's constructor.
     */
    private void createMainMenuPanel() {

        mainMenuContainerPanel = new JPanel(new GridBagLayout());
        mainMenuContainerPanel.setPreferredSize(new Dimension(1280, 720));
        mainMenuContainerPanel.setOpaque(false);
        containerPanel.add(mainMenuContainerPanel, BorderLayout.CENTER);

        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        mainMenuPanel.setOpaque(false);
        mainMenuPanel.setPreferredSize(new Dimension(700, 420));
        mainMenuContainerPanel.add(mainMenuPanel);

        JPanelBg leftMenuPanel = new JPanelBg("big-logo.png");                          // I prefer to use my custom JPanelBg than a label, because I can trivially add
        leftMenuPanel.setSize(new Dimension(325,420));                                  // elements inside the JPanelBg if required, so more flexible moving forward.

        gc.fill = GridBagConstraints.BOTH;                                              // MY NOTES:
        gc.weightx = 1;                                                                 // Priority when affording this panel's width.
        gc.weighty = 1;                                                                 // Priority when affording this panel's height.
        gc.gridx = 0;                                                                   // Left most grid.
        gc.gridy = 0;                                                                   // Top most grid.
        gc.gridwidth = 1;                                                               // Spans one (adjacent) grid width.
        gc.gridheight = 4;                                                              // Spans 4 (adjacent) grid heights.
        gc.insets = new Insets(0, 0, 0, 40);                                            // Margin Top, Left, Bottom, Right. - This is purposefully repeated elsewhere.
        mainMenuPanel.add(leftMenuPanel, gc);

        // -----------------------------------------

        mainMenuButtons = new JButton[4];
        String[] mainMenuButtonText = new String[] {"New Game", "How to play", "About", "Exit"};

        startNewGameBtnHandler = new StartNewGameBtnHandler();
        continueGameBtnHandler = new ContinueGameBtnHandler();
        howToPlayBtnHandler = new HowToPlayBtnHandler();
        aboutBtnHandler = new AboutBtnHandler();

        exitBtnHandler = new ExitBtnHandler();
        ActionListener[] menuHandlers = new ActionListener[] {startNewGameBtnHandler, howToPlayBtnHandler, aboutBtnHandler, exitBtnHandler};

        gc.insets = new Insets(0, 0, 0, 0);
        gc.weightx = 0.2;
        gc.gridx = 1;
        gc.gridheight = 1;

        for (int i = 0; i < 4; i++) {
                                                       // Font          Text colour           Text             Image           Pressed Image           Disabled image
            mainMenuButtons[i] = new JButtonCustomised(buttonText, buttonTextColour, mainMenuButtonText[i], "wood-btn.png", "wood-btn-pressed.png", "wood-btn-pressed.png");
            mainMenuButtons[i].addActionListener(menuHandlers[i]);

            gc.gridy = i;                                                               // Vertical grid position goes down as each button is added.
            mainMenuPanel.add(mainMenuButtons[i], gc);
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates our new game modal, where we choose a one or two player game
     * or can opt to return to the main menu instead if we choose.
     */
    private void createNewGameModal() {

        newGameMenuDialog = new JDialog(this, true);
        newGameMenuDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newGameMenuDialog.setUndecorated(true);
        newGameMenuDialog.setModal(true);
        newGameMenuDialog.setSize(400, 450);
        newGameMenuDialog.setLayout(new BorderLayout());
        newGameMenuDialog.setBackground(backgroundColour);
        newGameMenuDialog.setLocationRelativeTo(this);
        newGameMenuDialog.addWindowListener(new WindowAdapter() {                       // For each modal we make we use a boolean to determine if it's open before
                                                                                        // we attempt to close it, this avoids null pointer issues.
            public void windowOpened(WindowEvent e) {

                createNewGameMenuOpen = true;
            }

            public void windowClosed(WindowEvent e) {

                createNewGameMenuOpen = false;
            }
        });

        // -----------------------------------------

        JPanelBg newGameMenuPanel = new JPanelBg("popup-bg.jpg");
        newGameMenuPanel.setPreferredSize(new Dimension(400, 450));
        newGameMenuPanel.setLayout(new GridBagLayout());
        newGameMenuDialog.add(newGameMenuPanel);

        // -----------------------------------------

        JPanelBg newGameMenuTitle = new JPanelBg("new-game-title.png");

        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(0, 0, 0, 0);                                             // Margin Top, Left, Bottom, Right.
        newGameMenuPanel.add(newGameMenuTitle, gc);

        // -----------------------------------------

        JButton[] overlayButtons = new JButton[3];
        String[] overlayButtonNames = new String[]{"1 Player Game", "2 Player Game", "Back"};

        gc.weighty = 0.15;                                                              // Ensure the menu doesn't get deformed.

        for (int i = 0; i < overlayButtonNames.length; i++) {
                                                      // Font          Text colour           Text             Image           Pressed Image           Disabled image
            overlayButtons[i] = new JButtonCustomised(buttonText, buttonTextColour, overlayButtonNames[i], "wood-btn.png", "wood-btn-pressed.png", "wood-btn-placeholder.png");

            gc.gridy = (i + 1);

            if (i == 2) {

                gc.insets = new Insets(0, 0, 32, 0);
            }

            newGameMenuPanel.add(overlayButtons[i], gc);
        }

        overlayButtons[0].addActionListener(new EnterOnePlayerNameBtnHandler());
        overlayButtons[1].addActionListener(new EnterTwoPlayerNamesBtnHandler());
        overlayButtons[2].addActionListener(new CloseNewGameBtnHandler());

        newGameMenuDialog.setVisible(true);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates our modal for entering the player's name at the beginning of
     * a one player game.
     */
    private void createOnePlayerNameModal() {                                           // I had 1 and 2 player menus as one function, it was less lines but
                                                                                        // so much less readable, even with human friendly variable names.
        playerNameMenuDialog = new JDialog(this, true);
        playerNameMenuDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        playerNameMenuDialog.setUndecorated(true);
        playerNameMenuDialog.setModal(true);
        playerNameMenuDialog.setSize(400, 330);
        playerNameMenuDialog.setLayout(new BorderLayout());
        playerNameMenuDialog.setBackground(backgroundColour);
        playerNameMenuDialog.setLocationRelativeTo(this);
        playerNameMenuDialog.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {

                createPlayerNameModal = true;
            }

            public void windowClosed(WindowEvent e) {

                createPlayerNameModal = false;
            }
        });

        // -----------------------------------------

        JPanelBg playerNameMenuPanel = new JPanelBg("popup-bg-vsmall.png");
        playerNameMenuPanel.setPreferredSize(new Dimension(400, 330));
        playerNameMenuPanel.setLayout(new GridBagLayout());
        playerNameMenuDialog.add(playerNameMenuPanel);

        JPanelBg playerNameMenuTitle = new JPanelBg("enter-name-title.png");
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        playerNameMenuPanel.add(playerNameMenuTitle, gc);

        // -----------------------------------------

        JPanelBg player1Panel = new JPanelBg("name-field.png");
        player1Panel.setLayout(new BorderLayout());
        player1Panel.setOpaque(false);
        gc.weighty = 0.25;
        gc.gridy = (2);
        gc.insets = new Insets(0, 50, 0, 50);                                           // Margin Top, Left, Bottom, Right.
        playerNameMenuPanel.add(player1Panel, gc);

        player1Name = new JTextField(playerNames[0]);
        player1Name.setOpaque(false);
        player1Name.setFont(buttonText);
        player1Name.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        player1Panel.add(player1Name, BorderLayout.CENTER);

        // -----------------------------------------

        player1Name.addKeyListener(new NameLengthListener());

                                                    // Font    Text colour    Text      Image           Pressed Image           Disabled image
        JButton playButton = new JButtonCustomised(buttonText, buttonTextColour, "Play", "wood-btn.png", "wood-btn-pressed.png", "wood-btn-placeholder.png");

        playButton.setActionCommand("1");
        playButton.addActionListener(new EnterGameBtnHandler());

        gc.insets = new Insets(0, 0, 32, 0);
        gc.weighty = 0.15;
        gc.gridy = (4);
        playerNameMenuPanel.add(playButton, gc);

        playerNameMenuDialog.setVisible(true);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates our modal for entering both player's name at the beginning of
     * a two player game. It also allows the user to choose to have a best of 3 games
     * each (so 6 games in total) or 5 games each (10 in total). The enter game button
     * calles the same method, but the two buttons have different action commands.
     */
    private void createTwoPlayerNameModal() {                                           // I had 1 and 2 player menus as one function, it was less lines but
                                                                                        // so much less readable, even with human friendly variable names.
        playerNameMenuDialog = new JDialog(this, true);
        playerNameMenuDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        playerNameMenuDialog.setUndecorated(true);
        playerNameMenuDialog.setModal(true);
        playerNameMenuDialog.setSize(400, 490);
        playerNameMenuDialog.setLayout(new BorderLayout());
        playerNameMenuDialog.setBackground(backgroundColour);
        playerNameMenuDialog.setLocationRelativeTo(this);
        playerNameMenuDialog.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {

                createPlayerNameModal = true;
            }

            public void windowClosed(WindowEvent e) {

                createPlayerNameModal = false;
            }
        });

        // -----------------------------------------

        JPanelBg playerNameMenuPanel = new JPanelBg("popup-bg-large.png");
        playerNameMenuPanel.setPreferredSize(new Dimension(400, 490));
        playerNameMenuPanel.setLayout(new GridBagLayout());
        playerNameMenuDialog.add(playerNameMenuPanel);

        JPanelBg playerNameMenuTitle = new JPanelBg("enter-names-title.png");
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(0, 0, 0, 0);                                             // Margin Top, Left, Bottom, Right.
        playerNameMenuPanel.add(playerNameMenuTitle, gc);

        // -----------------------------------------

        JPanelBg player1Panel = new JPanelBg("name-field.png");
        player1Panel.setLayout(new BorderLayout());
        player1Panel.setOpaque(false);
        gc.weighty = 0.25;
        gc.gridy = (2);
        gc.insets = new Insets(0, 50, 0, 50);
        playerNameMenuPanel.add(player1Panel, gc);

        player1Name = new JTextField(playerNames[0]);
        player1Name.setOpaque(false);
        player1Name.setFont(buttonText);
        player1Name.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        player1Panel.add(player1Name, BorderLayout.CENTER);

        // -----------------------------------------

        JPanelBg player2Panel = new JPanelBg("name-field.png");
        player2Panel.setLayout(new BorderLayout());
        player2Panel.setOpaque(false);
        gc.gridy = (3);
        playerNameMenuPanel.add(player2Panel, gc);

        player2Name = new JTextField(playerNames[1]);
        player2Name.setOpaque(false);
        player2Name.setFont(buttonText);
        player2Name.setBorder(BorderFactory.createEmptyBorder(6, 15, 4, 15));
        player2Panel.add(player2Name, BorderLayout.CENTER);

        NameLengthListener nameLengthListener = new NameLengthListener();
        player1Name.addKeyListener(nameLengthListener);
        player2Name.addKeyListener(nameLengthListener);
                                                           // Font          Text colour          Text            Image            Pressed Image            Disabled image
        JButton playThreeEachButton = new JButtonCustomised(buttonText, buttonTextColour, "Play Three Each", "wood-btn.png", "wood-btn-pressed.png", "wood-btn-placeholder.png");
        JButton playFiveEachButton = new JButtonCustomised(buttonText, buttonTextColour, "Play Five Each", "wood-btn.png", "wood-btn-pressed.png", "wood-btn-placeholder.png");

        EnterGameBtnHandler enterGameBtnHandler = new EnterGameBtnHandler();
        playThreeEachButton.setActionCommand("6");
        playFiveEachButton.setActionCommand("10");
        playThreeEachButton.addActionListener(enterGameBtnHandler);
        playFiveEachButton.addActionListener(enterGameBtnHandler);

        gc.insets = new Insets(0, 0, 0, 0);                                             // Margin Top, Left, Bottom, Right.
        gc.weighty = 0.15;
        gc.gridy = (4);
        playerNameMenuPanel.add(playThreeEachButton, gc);

        gc.insets = new Insets(0, 0, 32, 0);
        gc.gridy = (5);
        playerNameMenuPanel.add(playFiveEachButton, gc);

        playerNameMenuDialog.setVisible(true);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates out menu bar which has two drop downs. "Game" holds the "New
     * Game" and "Exit" buttons. The "help" drop down holds the "How to Play" and
     * "About" buttons. This method is called from Menus.java's constructor.
     */
    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();                                              // Create a menu bar.
        setJMenuBar(menuBar);

        // -----------------------------------------

        JMenu gameMenu = new JMenu("Game");                                             // Create 'Game' item on menu.
        gameMenu.setToolTipText("Game options");
        menuBar.add(gameMenu);

        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.setToolTipText("Start a new game");
        newGameMenuItem.addActionListener(startNewGameBtnHandler);
        gameMenu.add(newGameMenuItem);

        gameMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setToolTipText("Exit game");
        exitMenuItem.addActionListener(exitBtnHandler);
        gameMenu.add(exitMenuItem);

        // -----------------------------------------

        JMenu helpMenu = new JMenu("Help");                                             // Create 'Help' item on menu.
        helpMenu.setToolTipText("Help options");
        menuBar.add(helpMenu);

        JMenuItem helpMenuItem = new JMenuItem("How To Play");
        helpMenuItem.setToolTipText("Discover how to play");
        helpMenuItem.addActionListener(howToPlayBtnHandler);
        helpMenu.add(helpMenuItem);

        helpMenu.addSeparator();

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setToolTipText("About this game");
        aboutMenuItem.addActionListener(aboutBtnHandler);
        helpMenu.add(aboutMenuItem);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates our "About" modal which provides brief details about the
     * program version and author.
     */
    private void createAboutModal() {

        aboutDialog = new JDialog(this, true);
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        aboutDialog.setUndecorated(true);
        aboutDialog.setModal(true);
        aboutDialog.setSize(450, 440);
        aboutDialog.setLayout(new BorderLayout());
        aboutDialog.setBackground(backgroundColour);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {

                aboutMenuOpen = true;
            }

            public void windowClosed(WindowEvent e) {

                aboutMenuOpen = false;
            }
        });

        JPanelBg aboutPanel = new JPanelBg("about-bg.png");
        aboutPanel.setLayout(null);
        aboutDialog.add(aboutPanel);

        JLabel aboutTitle = new JLabel();
        aboutTitle.setBounds(80, 65, 300, 50);
        aboutTitle.setHorizontalAlignment(SwingConstants.CENTER);
        aboutTitle.setFont(new Font("The Urban Way", Font.BOLD, 24));
        aboutTitle.setForeground(new Color(40, 40, 40));
        aboutTitle.setText("About");

        JLabel about = new JLabel();
        about.setBounds(80, 105, 300, 140);
        about.setHorizontalAlignment(SwingConstants.CENTER);
        about.setFont(new Font("The Urban Way", Font.BOLD, 18));
        about.setForeground(new Color(50, 50, 50));
        about.setText("<html><h2 text-align:center>Shut the Box<br>Version: 1.0.1<br>Anna Thomas<br>s4927945");

        aboutPanel.add(aboutTitle);
        aboutPanel.add(about);
                                                    // Font      Text colour     Text         Image            Pressed Image        Disabled image
        JButton closeButton = new JButtonCustomised(buttonText, buttonTextColour, "Close", "close-btn.png", "close-btn-pressed.png", "close-btn.png");
        closeButton.addActionListener(new CloseAboutBtnHandler());
        closeButton.setBounds(120, 320, 221, 82);
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);

        aboutPanel.add(closeButton);
        aboutDialog.setVisible(true);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates our "About" modal which provides brief details about how to
     * play the game.
     */
    private void createHowToPlayModal() {

        howToPlayDialog = new JDialog(this, true);
        howToPlayDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        howToPlayDialog.setUndecorated(true);
        howToPlayDialog.setModal(true);
        howToPlayDialog.setSize(450, 605);
        howToPlayDialog.setLayout(new BorderLayout());
        howToPlayDialog.setBackground(backgroundColour);
        howToPlayDialog.setLocationRelativeTo(this);
        howToPlayDialog.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {

                howToPlayMenuOpen = true;
            }

            public void windowClosed(WindowEvent e) {

                howToPlayMenuOpen = false;
            }
        });

        JPanelBg howToPlayPanel = new JPanelBg("howtoplay-bg.png");
        howToPlayPanel.setLayout(null);
        howToPlayDialog.add(howToPlayPanel);

        JLabel howToPlayTitle = new JLabel();
        howToPlayTitle.setBounds(70, 40, 300, 50);
        howToPlayTitle.setHorizontalAlignment(SwingConstants.CENTER);
        howToPlayTitle.setFont(new Font("The Urban Way", Font.BOLD, 24));
        howToPlayTitle.setForeground(new Color(40, 40, 40));
        howToPlayTitle.setText("How to Play");

        JLabel howToPlay = new JLabel();
        howToPlay.setBounds(70, 75, 320, 350);
        howToPlay.setHorizontalAlignment(SwingConstants.CENTER);
        howToPlay.setFont(new Font("The Urban Way", Font.BOLD, 16));
        howToPlay.setForeground(new Color(50, 50, 50));
        howToPlay.setText("<html>You begin with 9 open boxes, the aim of the game is to shut as many boxes as possible."
            + "<br><br>Roll two dice, then close one or more boxes such that the sum of the boxes closed is "
            + "equal to your dice roll. The game ends when you're unable to close boxes that are equal to "
            + "your dice roll.<br><br>Remember, a lower score is better!</html>");

        howToPlayPanel.add(howToPlayTitle);
        howToPlayPanel.add(howToPlay);

                                                    // Font      Text colour      Text       Image            Pressed Image        Disabled image
        JButton closeButton = new JButtonCustomised(buttonText, buttonTextColour, "Close", "close-btn.png", "close-btn-pressed.png", "close-btn.png");
        closeButton.addActionListener(new CloseHowToPlayBtnHandler());
        closeButton.setBounds(120, 470, 221, 82);
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);

        howToPlayPanel.add(closeButton);
        howToPlayDialog.setVisible(true);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates the in game modal menu which appears either when the user
     * presses the yellow "Menu" button in the game panel, or at the end of a game.
     * If a game is in progress the "New game", "Continue" and "Main Menu" buttons are
     * visible, otherwise just the "New Game and "Main Menu" buttons are visible.
     */
    public void createInGameModal() {

        inGameMenuDialog = new JDialog(this, true);
        inGameMenuDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inGameMenuDialog.setUndecorated(true);
        inGameMenuDialog.setModal(true);
        inGameMenuDialog.setSize(400, 450);
        inGameMenuDialog.setLayout(new BorderLayout());
        inGameMenuDialog.setBackground(backgroundColour);
        inGameMenuDialog.setLocationRelativeTo(this);
        inGameMenuDialog.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {

                inGameChoiceMenuOpen = true;
            }

            public void windowClosed(WindowEvent e) {

                inGameChoiceMenuOpen = false;
            }
        });

        // -----------------------------------------

        JPanelBg inGameMenuPanel = new JPanelBg("popup-bg.jpg");
        inGameMenuPanel.setPreferredSize(new Dimension(400, 450));
        inGameMenuPanel.setLayout(new GridBagLayout());
        inGameMenuDialog.add(inGameMenuPanel);

        // -----------------------------------------

        JPanelBg inGameMenuTitle = new JPanelBg();

        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(0, 0, 0, 0);                                             // Margin Top, Left, Bottom, Right.
        inGameMenuPanel.add(inGameMenuTitle, gc);

        // -----------------------------------------

        JButtonCustomised[] overlayButtons = new JButtonCustomised[3];
        String[] overlayButtonNames = new String[]{"", "New Game", "Show Main Menu"};

        gc.weighty = 0.15;

        for (int i = 0; i < overlayButtonNames.length; i++) {
                                                      // Font        Text colour          Text                Image           Pressed Image            Disabled image
            overlayButtons[i] = new JButtonCustomised(buttonText, buttonTextColour, overlayButtonNames[i], "wood-btn.png", "wood-btn-pressed.png", "paper-text-btn.png");

            gc.gridy = (i + 1);

            if (i == 2) {

                gc.insets = new Insets(0, 0, 32, 0);
            }

            inGameMenuPanel.add(overlayButtons[i], gc);
        }

        overlayButtons[0].addActionListener(continueGameBtnHandler);
        overlayButtons[1].addActionListener(new StartNewGameBtnHandler());              // Action listener names describe the button's function
        overlayButtons[2].addActionListener(new ShowMainMenuBtnHandler());

        if (gameBoard.getIsGameInProgress()) {                                          // If the game is in progress

            inGameMenuTitle.setNewImage("menu-title.png");
            overlayButtons[0].addActionListener(continueGameBtnHandler);
            overlayButtons[0].changeIcons("wood-btn.png", "wood-btn-pressed.png");
            overlayButtons[0].setForeground(buttonTextColour);
            overlayButtons[0].setFont(buttonText);
            overlayButtons[0].setText("Continue");

        } else {

            inGameMenuTitle.setNewImage("game-over-title.png");                         // If the game is not in progress, display the "Game Over" title
            overlayButtons[0].removeActionListener(continueGameBtnHandler);
            overlayButtons[0].changeIcons("wood-btn-placeholder.png", "wood-btn-placeholder.png");
            overlayButtons[0].setForeground(new Color(10, 10, 10));
            overlayButtons[0].setFont(new Font("Serif", Font.PLAIN, 40));
            overlayButtons[0].setText(gameBoard.getGameOverMessage());
        }

        inGameMenuDialog.setVisible(true);
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method creates a confirmation modal so we can confirm certain choices,
     * namely when exiting the program or when starting a new game whilst an existing
     * game is under way. It's used in an if statement so that the menu is created and
     * two logic paths can be used depending on true or false (yes or not) choice.
     *
     * @return boolean returns true if the user confirms their choice, otherwise returns false.
     */
    private boolean getChoiceConfirmation() {

        confirmDialog = new JDialog(this, true);
        confirmDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        confirmDialog.setUndecorated(true);
        confirmDialog.setModal(true);
        confirmDialog.setSize(400, 235);
        confirmDialog.setLayout(new BorderLayout());
        confirmDialog.setBackground(backgroundColour);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {

                choiceConfirmationMenuOpen = true;
            }

            public void windowClosed(WindowEvent e) {

                choiceConfirmationMenuOpen = false;
            }
        });

        JPanelBg confirmMenuPanel = new JPanelBg("popup-bg-small.jpg");
        confirmMenuPanel.setLayout(new GridBagLayout());
        confirmDialog.add(confirmMenuPanel);

        // -----------------------------------------

        JPanelBg confirmMenuTitle = new JPanelBg("are-you-sure-title.png");

        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.insets = new Insets(0, 0, 0, 0);                                             // Margin Top, Left, Bottom, Right.
        confirmMenuPanel.add(confirmMenuTitle, gc);

        // -----------------------------------------

                                                        // Font      Text colour     Text         Image                Pressed Image                  Disabled image
        JButton confirmYesButton = new JButtonCustomised(buttonText, buttonTextColour, "Yes","wood-btn-small.png", "wood-btn-small-pressed.png", "wood-btn-small-pressed.png");
        JButton confirmNoButton = new JButtonCustomised(buttonText, buttonTextColour, "No", "wood-btn-small.png", "wood-btn-small-pressed.png", "wood-btn-small-pressed.png");

        confirmYesButton.addActionListener(new ConfirmYesBtnHandler());
        confirmNoButton.addActionListener(new ConfirmNoBtnHandler());

        gc.weighty = 0.15;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.insets = new Insets(0, 0, 10, 0);
        confirmMenuPanel.add(confirmNoButton, gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 10, 10, 0);
        confirmMenuPanel.add(confirmYesButton, gc);

        confirmDialog.setVisible(true);

        return confirmChoice;
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method sets either the main menu or the game board visible as well as
     * setting the container panel's background depending on menu visibility. Before
     * setting the main menu visible the menu is tailored to the current game's state
     * so that it shows "Continue" instead of "New Game" if a game is underway.
     *
     * @param isVisible true if we want the menu to be visible and false if we want the
     *                  game board to be visible.
     */
    private void setMenuVisible(boolean isVisible) {

        if (isVisible) {

            if (gameBoard.getIsGameInProgress()) {

                mainMenuButtons[0].removeActionListener(startNewGameBtnHandler);        // mainMenuButtons[0] is the top most menu button on the main menu
                mainMenuButtons[0].addActionListener(continueGameBtnHandler);
                mainMenuButtons[0].setText("Continue");

            } else {

                mainMenuButtons[0].removeActionListener(startNewGameBtnHandler);
                mainMenuButtons[0].addActionListener(startNewGameBtnHandler);
                mainMenuButtons[0].setText("New Game");
            }

            containerPanel.setNewImage("wood-with-trim.png");                           // Wanted png for its precision to retain trim sharpness

        } else {

            containerPanel.setNewImage("wood.jpg");

        }

        mainMenuContainerPanel.setVisible(isVisible);
        gameBoard.setGameVisible(!isVisible);                                           // Note the exclamation mark.

        mainMenuContainerPanel.repaint();                                               // Slight hack
        mainMenuContainerPanel.revalidate();

        containerPanel.repaint();
        containerPanel.revalidate();
    }



    // ---------------------------------------------------------------------------------
    /**
     * This method is called when the user clicks the "New Game" button. If a game is in
     * progress the method creates the get confirmation modal popup to check the user's
     * click was deliberate.
     */
    public class StartNewGameBtnHandler implements ActionListener {                     // Called from the in-game menu or main menu.

        public void actionPerformed(ActionEvent e) {

            if (gameBoard.getHasClosedBox()) {                                          // If user has successfully closed boxes.

                if (getChoiceConfirmation()) {                                          // Confirmed we want a new game.

                    createNewGameModal();                                               // Open choose no of players screen
                }

            } else {

                createNewGameModal();
            }

            if (createNewGameMenuOpen) {

                newGameMenuDialog.dispose();
            }

            if(inGameChoiceMenuOpen) {

                inGameMenuDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class creates the enter player1 name modal menu when the user selects a
     * single player game.
     */
    public class EnterOnePlayerNameBtnHandler implements ActionListener {               // Called from the in-game menu.

        public void actionPerformed(ActionEvent e) {

            createOnePlayerNameModal();

            if (createPlayerNameModal) {

                newGameMenuDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class creates the enter player1 and player2 names modal menu when the user
     * selects a two player game.
     */
    public class EnterTwoPlayerNamesBtnHandler implements ActionListener {              // Called from the in-game menu.

        public void actionPerformed(ActionEvent e) {

            createTwoPlayerNameModal();

            if (createPlayerNameModal) {

                newGameMenuDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class allows the user to cancel their choice of starting a new game when
     * they're at the choose 1 or 2 player game menu.
     */
    public class CloseNewGameBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (createNewGameMenuOpen) {

                newGameMenuDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class gets the number of rounds the player(s) has chosen to play. Possible
     * options are 1 if it's a single player game, 6 if it's a a two player game of
     * three each and 10 if it's a two player game of 5 each.
     *
     * This method also gets the names of the player(s) and passes the number of rounds,
     * whether or not it's a two player game and the player names to the setup new game
     * function in the game board class.
     */
    public class EnterGameBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            int numberOfRoundsToPlay = Integer.parseInt(e.getActionCommand());

            playerNames[0] = player1Name.getText();

            if (numberOfRoundsToPlay > 1) {                                             // It must be a two player game

                playerNames[1] = player2Name.getText();
                gameBoard.setupNewGame(numberOfRoundsToPlay, true, playerNames);        // True it is a two player game.

            } else {

                gameBoard.setupNewGame(numberOfRoundsToPlay, false, playerNames);       // False it is a one player game.
            }

            setMenuVisible(false);

            if (createPlayerNameModal) {

                playerNameMenuDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class allows the user to return to the main menu, it's called from the
     * action listener bound to the yellow "Menu" button in game board.
     */
    public class ShowMainMenuBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            setMenuVisible(true);

            if (inGameChoiceMenuOpen) {

                inGameMenuDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class creates the How to play modal menu when the users click the "How to
     * Play" button from the main menu or the menu bar.
     */
    public class HowToPlayBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            createHowToPlayModal();
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class closes the "How to Play" popup when the user clicks the close button.
     */
    public class CloseHowToPlayBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (howToPlayMenuOpen) {

                howToPlayDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class creates the About  modal menu when the users click the "About" button
     * from the main menu or the menu bar.
     */
    public class AboutBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            createAboutModal();
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class closes the "About" popup when the user clicks the close button.
     */
    public class CloseAboutBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (aboutMenuOpen) {

                aboutDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class closes the in game dialog modal menu popup if the user has that menu
     * open. Otherwise the user must have clicked this button from the main menu, in
     * which case display the game board screen.
     */
    public class ContinueGameBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (inGameChoiceMenuOpen) {                                                 // If the modal is open we must be at the game screen.

                inGameMenuDialog.dispose();

            } else {                                                                    // Otherwise we're at the main menu screen.

                setMenuVisible(false);                                                  // So close the main menu.
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class sets the confirmChoice variable to true when the user confirms their
     * choice of either starting a new game or quitting the program.
     */
    public class ConfirmYesBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            confirmChoice = true;

            if (choiceConfirmationMenuOpen) {

                confirmDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class sets the confirmChoice variable to false when the user cancels their
     * choice of either starting a new game or quitting the program.
     */
    public class ConfirmNoBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            confirmChoice = false;

            if (choiceConfirmationMenuOpen) {

                confirmDialog.dispose();
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class exits the program after the user has confirmed their choice of
     * wanting to quit the program.
     */
    public class ExitBtnHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (getChoiceConfirmation()) {

                System.exit(0);
            }
        }
    }



    // ---------------------------------------------------------------------------------
    /**
     * This class listens to the user's input for the player1 or player 2 name text
     * fields and stops the usr entering more than 11 characters per name. The value is
     * low because names have to fit in to the "Next Player" button and the scoreboard.
     */
    public class NameLengthListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

            JTextField textField = (JTextField) e.getSource();

            if (textField.getText().length() > 9) {
                e.consume();
            }
        }

        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
    }
}