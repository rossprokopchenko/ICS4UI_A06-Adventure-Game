package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Rostiku
 */
public class Game extends JComponent implements KeyListener {

    // Dimensions of the JFrame
    public static final int WIDTH = 640, HEIGHT = 480;

    // instance variables
    private LocationList list;
    private Controls c;
    private Scene scene;

    // arraylists with directions (N, E, S, W)
    private ArrayList<String> directions;

    // current location and direction of player
    private String location;
    private String direction;
    
    // START text in menu state
    private String text = "Start";

    // needed fonts, colors, location of START text
    private Font font = new Font("arial", Font.BOLD, 24);
    private Font menuFont = new Font("courier", Font.BOLD, 35);
    private Color startColor = Color.black;
    private Point textLocation = new Point(WIDTH / 2 - 35, HEIGHT / 2 - 12);

    // bools
    private boolean menuState = true;
    private boolean startHover = false;
    private boolean mouseClick = false;

    /**
     * *
     * The main game constructor, initializes the JFrame, controls, locations,
     * scenes, keyboard listener and mouse listener
     *
     * @param file the file to read the locations from
     */
    public Game(String file) {
        // dimensions of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // create JFrame
        JFrame frame = new JFrame("Adventure Game");
        // store all locations in LocationList
        list = new LocationList(file);
        c = new Controls();
        
        // set location and direction to the start location and start direction
        location = list.getStartLocation();
        direction = list.getStartDirection();

        // initialize directions
        directions = new ArrayList<>();
        // add directions 
        directions.add("N");
        directions.add("E");
        directions.add("S");
        directions.add("W");
        // set the starting scene to the proper location and direction
        scene = list.getLocation(location, direction);

        // JFrame initialization
        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        frame.addKeyListener(this);

        // a bad, lazy but fast way of implementing a mouse listener that i found on stackoverflow :)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClick = true;
                run();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseClick = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                checkForHover(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                checkForHover(e);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                checkForHover(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                checkForHover(e);
            }
        });
    }

    /**
     * Launches the game
     *
     * @param args
     */
    public static void main(String args[]) {
        Game test = new Game("src/images/images.txt");

        test.run();

    }

    /**
     * All images or text to be displayed in the JFrame
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        // clear the screen first
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // MENU STATE
        if (menuState) {
            // sets menu font + color
            g.setFont(menuFont);
            g.setColor(startColor);
            // draws START
            g.drawString(text, textLocation.x, textLocation.y);
            // draws controls
            g.setFont(font);
            g.drawString("arrow keys or WASD to move | ESC to exit the game", 20, HEIGHT - 20);

            // GAME STATE
        } else {
            // draws the current scene
            g.drawImage(scene.getImage(), 0, 0, this);
            // sets the font + color
            g.setFont(font);
            g.setColor(Color.red);
            // draws the current location and direction of the player
            g.drawString(location + " " + direction, 10, 30);

            // if front is blocked draw "BLOCKED" on top of the screen
            if (scene.isFrontBlocked()) {
                g.drawString("Blocked", WIDTH / 2 - 30, 30);
            }
        }
    }

    /**
     * Game logic
     */
    public void run() {
        // MENU STATE
        if (menuState) {
            // if clicked on START
            if (startHover && mouseClick) {
                // get out of menu state
                menuState = false;
            }

            // GAME STATE
        } else {
            // if player moved forward
            if (c.isMoveForward()) {
                // if front is clear
                if (!scene.isFrontBlocked()) {
                    // set the new location and direction to the next one
                    location = scene.getNextLocation();
                    direction = scene.getNextDirection();
                    // set the current scene to the one with new parameters
                    scene = list.getLocation(location, direction);

                }
                c.setMoveForward(false);

                // if turned left
            } else if (c.isTurnLeft()) {
                // set the new direction to the one that is left of the current
                direction = directions.get(leftDirectionNum(direction));
                // set the current scene to the one with new parameters
                scene = list.getLocation(location, direction);

                c.setTurnLeft(false);

                // if turned right
            } else if (c.isTurnRight()) {
                // set the new direction to the one that is right of the current
                direction = directions.get(rightDirectionNum(direction));
                // set the current scene to the one with new parameters
                scene = list.getLocation(location, direction);

                c.setTurnRight(false);

                // if moved backwards
            } else if (c.isMoveBackward()) {
                // if back is clear
                if (!getBackLocation(location, direction).equals("")) {
                    // set the location to the one that is behind the player
                    location = getBackLocation(location, direction);
                    // set the current scene to the one with new parameters
                    scene = list.getLocation(location, direction);

                }
                c.setMoveBackward(false);
            }
        }

        repaint();

    }

    /**
     * Checks for pressed keys
     * Controls
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {

            // IF PRESSED LEFT / A
            case KeyEvent.VK_LEFT:
                if (c.isLeftRelease()) {
                    c.setTurnLeft(true);
                    c.setLeftRelease(false);
                    run();
                }
                break;

            case KeyEvent.VK_A:
                if (c.isLeftRelease()) {
                    c.setTurnLeft(true);
                    c.setLeftRelease(false);
                    run();
                }
                break;

                // IF PRESSED RIGHT / D
            case KeyEvent.VK_RIGHT:
                if (c.isRightRelease()) {
                    c.setTurnRight(true);
                    c.setRightRelease(false);
                    run();
                }
                break;

            case KeyEvent.VK_D:
                if (c.isRightRelease()) {
                    c.setTurnRight(true);
                    c.setRightRelease(false);
                    run();
                }
                break;

                // IF PRESSED UP / W
            case KeyEvent.VK_UP:
                if (c.isForwardRelease()) {
                    c.setMoveForward(true);
                    c.setForwardRelease(false);
                    run();
                }
                break;

            case KeyEvent.VK_W:
                if (c.isForwardRelease()) {
                    c.setMoveForward(true);
                    c.setForwardRelease(false);
                    run();
                }
                break;

                // IF PRESSED DOWN / S
            case KeyEvent.VK_DOWN:
                if (c.isBackwardRelease()) {
                    c.setMoveBackward(true);
                    c.setBackwardRelease(false);
                    run();
                }
                break;

            case KeyEvent.VK_S:
                if (c.isBackwardRelease()) {
                    c.setMoveBackward(true);
                    c.setBackwardRelease(false);
                    run();
                }
                break;
        }
    }

    /**
     * Checks for released keys
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            // sets all release booleans for each key to true when released
            case KeyEvent.VK_LEFT:
                c.setLeftRelease(true);
                break;

            case KeyEvent.VK_A:
                c.setLeftRelease(true);
                break;

            case KeyEvent.VK_RIGHT:
                c.setRightRelease(true);
                break;

            case KeyEvent.VK_D:
                c.setRightRelease(true);
                break;

            case KeyEvent.VK_UP:
                c.setForwardRelease(true);
                break;

            case KeyEvent.VK_W:
                c.setForwardRelease(true);
                break;

            case KeyEvent.VK_DOWN:
                c.setBackwardRelease(true);
                break;

            case KeyEvent.VK_S:
                c.setBackwardRelease(true);
                break;

            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Returns the location that is to the left of the player
     *
     * @param direction current direction of player
     * @return the left location
     */
    public int leftDirectionNum(String direction) {
        switch (direction) {
            case "N":
                return 3;

            case "E":
                return 0;

            case "S":
                return 1;

            case "W":
                return 2;

            default:
                return -1;
        }
    }

    /**
     * Returns the location that is to the right of the player
     *
     * @param direction current direction of player
     * @return the right location
     */
    public int rightDirectionNum(String direction) {
        switch (direction) {
            case "N":
                return 1;

            case "E":
                return 2;

            case "S":
                return 3;

            case "W":
                return 0;

            default:
                return -1;
        }
    }

    /**
     * Checks if the mouse is hovering over the START button in the menu state
     *
     * @param e
     */
    public void checkForHover(MouseEvent e) {
        // gets metrics of menuFont
        FontMetrics metrics = getFontMetrics(menuFont);
        // implements graphics
        Graphics g = getGraphics();
        // bounds of the text box
        Rectangle textBounds = metrics.getStringBounds(text, g).getBounds();
        
        g.dispose();
        
        textBounds.translate(textLocation.x, textLocation.y);

        // if hovering over text
        if (textBounds.contains(e.getPoint())) {
            startColor = Color.red;
            startHover = true;
            // if not hovering over text
        } else {
            startColor = Color.black;
            startHover = false;
        }
        
        repaint(textBounds);
    }

    /**
     * Gets the location that is behind the player
     *
     * @param location current location of the player
     * @param direction current direction of the player
     * @return the back location
     */
    public String getBackLocation(String location, String direction) {
        // in case of every direction
        switch (direction) {
            case "N":
                // in case of every location
                // returns the back location of every location in the location list
                switch (location) {
                    case "Caf1":
                        return "Caf2";

                    case "Music1":
                        return "Gym";

                    case "Music2":
                        return "Music1";

                    case "Art2":
                        return "Staff";

                    case "Art1":
                        return "Art2";

                    case "Upstairs2":
                        return "Upstairs3";

                    case "Upstairs1":
                        return "Upstairs2";

                    case "Math3":
                        return "Eng1";

                    case "Eng1":
                        return "Math3";

                    case "Downstairs":
                        return "Eng3";
                }
                break;

            case "E":
                switch (location) {
                    case "Staff":
                        return "Glass";

                    case "Glass":
                        return "Upstairs3";

                    case "Upstairs3":
                        return "Math1";

                    case "Math1":
                        return "Math2";

                    case "Math2":
                        return "Math3";

                    case "Science1":
                        return "Science2";

                    case "Science2":
                        return "Art2";

                    case "Tech2":
                        return "Tech1";

                    case "Tech1":
                        return "Music1";

                    case "Gym":
                        return "Caf3";

                    case "Caf3":
                        return "Caf1";

                    case "Caf1":
                        return "Eng3";

                    case "Eng3":
                        return "Eng2";

                    case "Eng2":
                        return "Eng1";

                    case "Art1":
                        return "Music2";

                    case "Music2":
                        return "Art1";
                }

                break;

            case "S":
                switch (location) {
                    case "Caf2":
                        return "Caf1";

                    case "Gym":
                        return "Music1";

                    case "Music1":
                        return "Music2";

                    case "Staff":
                        return "Art2";

                    case "Art2":
                        return "Art1";

                    case "Upstairs3":
                        return "Upstairs2";

                    case "Upstairs2":
                        return "Upstairs1";

                    case "Science1":
                        return "Tech2";

                    case "Tech2":
                        return "Science1";

                    case "Upstairs1":
                        return "Downstairs";

                    case "Downstairs":
                        return "Upstairs1";

                    case "Eng3":
                        return "Downstairs";
                }

                break;

            case "W":
                switch (location) {
                    case "Glass":
                        return "Staff";

                    case "Upstairs3":
                        return "Glass";

                    case "Math1":
                        return "Upstairs3";

                    case "Math2":
                        return "Math1";

                    case "Math3":
                        return "Math2";

                    case "Science2":
                        return "Science1";

                    case "Art2":
                        return "Science2";

                    case "Tech1":
                        return "Tech2";

                    case "Music1":
                        return "Tech1";

                    case "Caf3":
                        return "Gym";

                    case "Caf1":
                        return "Caf3";

                    case "Eng3":
                        return "Caf1";

                    case "Eng2":
                        return "Eng3";

                    case "Eng1":
                        return "Eng2";
                }

                break;
        }
        // returns black if the back of the player is blocked (no back location)
        return "";
    }
}
