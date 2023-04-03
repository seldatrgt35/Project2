package gravity.src;

import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import static java.awt.Color.*;
import static java.awt.Font.BOLD;

public class GameManager {
    public static final int ROBOT_COUNT = 7;
    public static final int GAME_FIELD_X = 55;
    public static final int GAME_FIELD_Y = 25;
    public static final TextAttributes PLAYER_COLOR = new TextAttributes(GREEN, BLACK);
    public static final TextAttributes ROBOT_COLOR = new TextAttributes(YELLOW, BLACK);
    public static final TextAttributes WALL_COLOR = new TextAttributes(WHITE, BLACK);
    public static final TextAttributes EARTH_COLOR = new TextAttributes(BLACK, WHITE);
    public static final TextAttributes BOULDER_COLOR = new TextAttributes(BLACK, WHITE);
    public static final TextAttributes TREASURE_COLOR = new TextAttributes(RED, WHITE);
    public static final TextAttributes CONSOLE_COLOR = new TextAttributes(BLACK, WHITE);
    public static final TextAttributes TEXT_COLOR = new TextAttributes(BLACK, WHITE);

    Random rnd = new Random();
    public int px, py, pxOld, pyOld; // player x,y position
    public boolean moved;
    char[][] wholeGrid = new char[GAME_FIELD_X][GAME_FIELD_Y];

    private static enigma.console.Console cn;


    private static void setMenu() {
        //Input
        cn.getTextWindow().setCursorPosition(56, 0);
        cn.getTextWindow().output("Input", TEXT_COLOR);
        cn.getTextWindow().setCursorPosition(56, 1);
        cn.getTextWindow().output("<<<<<<<<<<<<<<<", WALL_COLOR);
        cn.getTextWindow().setCursorPosition(56, 3);
        cn.getTextWindow().output("<<<<<<<<<<<<<<<", WALL_COLOR);

        //Backpack
        for (int i = 6; i <= 13; i++) {
            cn.getTextWindow().output(62, i, '|', TEXT_COLOR);
            cn.getTextWindow().output(66, i, '|', TEXT_COLOR);
        }
        cn.getTextWindow().output(62, 14, '+', TEXT_COLOR);
        cn.getTextWindow().output(63, 14, '-', TEXT_COLOR);
        cn.getTextWindow().output(64, 14, '-', TEXT_COLOR);
        cn.getTextWindow().output(65, 14, '-', TEXT_COLOR);
        cn.getTextWindow().output(66, 14, '+', TEXT_COLOR);
        cn.getTextWindow().setCursorPosition(61, 15);
        cn.getTextWindow().output("Backpack", TEXT_COLOR);

        //Teleport, Score, Time
        cn.getTextWindow().setCursorPosition(56, 18);
        cn.getTextWindow().output("Teleport :", TEXT_COLOR);
        cn.getTextWindow().setCursorPosition(56, 20);
        cn.getTextWindow().output("Score    :", TEXT_COLOR);
        cn.getTextWindow().setCursorPosition(56, 22);
        cn.getTextWindow().output("Time     :", TEXT_COLOR);
    }

    public TextMouseListener tmlis;
    public KeyListener klis;

    // ------ Standard variables for mouse and keyboard ------
    public int mousepr; // mouse pressed?
    public int mousex, mousey; // mouse text coords.
    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    // ----------------------------------------------------


    private void doNotChange() {
        // ------ Standard code for mouse and keyboard ------ Do not change
        tmlis = new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {
            }

            public void mousePressed(TextMouseEvent arg0) {
                if (mousepr == 0) {
                    mousepr = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }

            public void mouseReleased(TextMouseEvent arg0) {
            }
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------
    }

    public GameManager() throws Exception { // --- Contructor
        //set enigma font to bold
//        Enigma.getEnvironment().setProperty("enigma.console.font", "Courier New-bold-12");
        cn = Enigma.getConsole("Gravity - Team7", 75, GAME_FIELD_Y);

        //set console background color to white
        for (int i = 0; i < cn.getTextWindow().getColumns(); i++) {
            for (int j = 0; j < cn.getTextWindow().getRows(); j++) {
                cn.getTextWindow().output(i, j, ' ', CONSOLE_COLOR);
            }
        }

        setMenu();
        doNotChange();
        //1.	Walls are placed.
        //2.	All empty squares are converted into earth squares.
        //3.	Random 180 earth squares are converted into boulders.
        //4.	Random 30 earth squares are converted into treasures (Random 1, 2 or 3 with equal probability).
        //5.	Random 200 earth squares are converted into empty squares.
        initializeWallAndEarth();
        //6.	Random 7 earth squares are converted into robots.
        initializeRobots();
        //7.	Player P is placed on a random earth square.
        initializePlayer();


        while (true) {
            pxOld = px;
            pyOld = py;
            moved = false;
//            if (mousepr == 1) { // if mouse button pressed
//                cn.getTextWindow().output(mousex, mousey, '#',WALL_COLOR); // write a char to x,y position without changing cursor
//                // position
//                px = mousex;
//                py = mousey;
//
//                mousepr = 0; // last action
//            }
            if (keypr == 1) { // if keyboard button pressed
                if (rkey == KeyEvent.VK_LEFT && px > 1) {
                    px--;
                    moved = true;
                } else if (rkey == KeyEvent.VK_RIGHT && px < GAME_FIELD_X - 2) {
                    px++;
                    moved = true;
                } else if (rkey == KeyEvent.VK_UP && py > 1) {
                    py--;
                    moved = true;
                } else if (rkey == KeyEvent.VK_DOWN && py < GAME_FIELD_Y - 2) {
                    py++;
                    moved = true;
                }

                if (moved) {
                    wholeGrid[px][py] = 'P';    // VK kullanmadan test teknigi
                    wholeGrid[pxOld][pyOld] = ' ';
                }

//                if (rkey == KeyEvent.VK_SPACE) {
//                    String str;
//                    str = cn.readLine(); // keyboardlistener running and readline input by using enter
//                    cn.getTextWindow().setCursorPosition(5, 20);
//                    cn.getTextWindow().output(str);
//                }
                keypr = 0; // last action
            }
            //Print game field according to wholeGrid array with colors
            for (int i = 0; i < GAME_FIELD_X; i++) {
                for (int j = 0; j < GAME_FIELD_Y; j++) {
                    switch (wholeGrid[i][j]) {
                        case 'X':
                            cn.getTextWindow().output(i, j, 'X', ROBOT_COLOR);
                            break;
                        case 'P':
                            cn.getTextWindow().output(i, j, 'P', PLAYER_COLOR);
                            break;
                        case 'T':
                            cn.getTextWindow().output(i, j, 'T', TREASURE_COLOR);
                            break;
                        case 'O':
                            cn.getTextWindow().output(i, j, 'O', BOULDER_COLOR);
                            break;
                        case ':':
                            cn.getTextWindow().output(i, j, ':', EARTH_COLOR);
                            break;
                        case '#':
                            cn.getTextWindow().output(i, j, '#', WALL_COLOR);
                            break;
                        default:
                            cn.getTextWindow().output(i, j, ' ', CONSOLE_COLOR);
                            break;
                    }
                }
            }
            Thread.sleep(20);
        }
    }

    private void initializeRobots() {
        for (int i = 0; i < ROBOT_COUNT; i++) {
            while (wholeGrid[px][py] != ':') {
                px = rnd.nextInt(GAME_FIELD_X);
                py = rnd.nextInt(GAME_FIELD_Y);
            }
            wholeGrid[px][py] = 'X';
        }
    }

    private void initializePlayer() {
        while (wholeGrid[px][py] != ':') {
            px = rnd.nextInt(GAME_FIELD_X);
            py = rnd.nextInt(GAME_FIELD_Y);
        }
        wholeGrid[px][py] = 'P';
    }


    public char[][] initializeWallAndEarth() {
        for (int i = 0; i < GAME_FIELD_X; i++) {
            for (int j = 0; j < GAME_FIELD_Y; j++) {
                if (i == 0 || i == 54 || j == 0 || j == 24) {
                    wholeGrid[i][j] = '#'; // First row of the outer walls
                } else {
                    wholeGrid[i][j] = ':'; // First row of the outer walls
                }

            }
        }
        //TODO: we need to extract this functionality to initializeBoulders() method
//	      Convert boulders 
        int maxconvertBoulder = 179;    //TODO: Do not use magic numbers, initialize them as constants. Also this one should be 200, change statement accordingly.
        int counterb = 0;
        while (!(counterb == maxconvertBoulder)) {  //TODO !(x == y) is not correct, use x != y instead
            int x = rnd.nextInt(GAME_FIELD_X);
            int y = rnd.nextInt(GAME_FIELD_Y);
            if (wholeGrid[x][y] == ':' && counterb <= maxconvertBoulder) {
                wholeGrid[x][y] = ' ';
                char boulds = 'O';
                wholeGrid[x][y] = boulds;
                counterb++;
            }
        }
        int counterForTreasure = 0;
        int maxTreasure = 29;
        int counterForEmptySquares = 0;
        int maxEmptySquares = 199;  //TODO: Do not use magic numbers, initialize them as constants. Also this one should be 200, change statement accordingly.

        //TODO: we need to extract this functionality to initializeTreasures() method
        //TODO: infinite loop
//        while (!(counterForTreasure == maxTreasure && counterForEmptySquares == maxEmptySquares)) {   //TODO !(x == y) is not correct, use x != y instead
//            int randomi = rnd.nextInt(55);
//            int randomj = rnd.nextInt(25);
//            if (wholeGrid[randomi][randomj] == ':' && counterForTreasure <= maxTreasure) {
//                //ASCII codes: 49=1_50=2_51=3
//                char randomTreasure = (char) (rnd.nextInt(52 - 49) + 49);
//                wholeGrid[randomi][randomj] = randomTreasure;
//                counterForTreasure++;
//            } else if (wholeGrid[randomi][randomj] == ':' && counterForEmptySquares <= maxEmptySquares) {
//                wholeGrid[randomi][randomj] = ' ';
//                counterForEmptySquares++;
//            }
//        }

        return wholeGrid;
    }
}
