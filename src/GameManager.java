package gravity.src;

import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameManager {
    public static final int ROBOT_COUNT = 7;
    public static final int GAME_FIELD_X = 55;
    public static final int GAME_FIELD_Y = 25;
    Random rnd = new Random();
    public int px, py; // player x,y position
    char[][] wholeGrid = new char[GAME_FIELD_X][GAME_FIELD_Y];

    public enigma.console.Console cn = Enigma.getConsole("Gravity - Team7", 75, GAME_FIELD_Y);
    public TextMouseListener tmlis;
    public KeyListener klis;

    // ------ Standard variables for mouse and keyboard ------
    public int mousepr; // mouse pressed?
    public int mousex, mousey; // mouse text coords.
    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    // ----------------------------------------------------


    private void doNotChange(){
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
//            if (mousepr == 1) { // if mouse button pressed
//                cn.getTextWindow().output(mousex, mousey, '#'); // write a char to x,y position without changing cursor
//                // position
//                px = mousex;
//                py = mousey;
//
//                mousepr = 0; // last action
//            }
            if (keypr == 1) { // if keyboard button pressed
                if (rkey == KeyEvent.VK_LEFT)
                    px--;
                if (rkey == KeyEvent.VK_RIGHT)
                    px++;
                if (rkey == KeyEvent.VK_UP)
                    py--;
                if (rkey == KeyEvent.VK_DOWN)
                    py++;

                char rckey = (char) rkey;
                // left right up down
                if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(')
                    cn.getTextWindow().output(px, py, 'P'); // VK kullanmadan test teknigi
                else
                    cn.getTextWindow().output(rckey);

//                if (rkey == KeyEvent.VK_SPACE) {
//                    String str;
//                    str = cn.readLine(); // keyboardlistener running and readline input by using enter
//                    cn.getTextWindow().setCursorPosition(5, 20);
//                    cn.getTextWindow().output(str);
//                }
                keypr = 0; // last action
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
            cn.getTextWindow().output(px, py, 'P');
        }
    }

    private void initializePlayer() {
        while (wholeGrid[px][py] != ':') {
            px = rnd.nextInt(GAME_FIELD_X);
            py = rnd.nextInt(GAME_FIELD_Y);
        }
        cn.getTextWindow().output(px, py, 'P');
    }


    public char[][] initializeWallAndEarth() {
        for (int i = 0; i < GAME_FIELD_X; i++) {
            for (int j = 0; j < GAME_FIELD_Y; j++) {
                if (i == 0 || i == 54 || j == 0 || j == 24) {
                    wholeGrid[i][j] = '#'; // First row of the outer walls
                    cn.getTextWindow().output(i, j, '#');
                } else {
                    wholeGrid[i][j] = ':'; // First row of the outer walls
                    cn.getTextWindow().output(i, j, ':');
                }

            }
        }
        //TODO: we need to extract this functionality to initializeBoulders() method
//	      Convert boulders 
        int maxconvertBoulder = 179;
        int counterb = 0;
        while (!(counterb == maxconvertBoulder)) {
            int x = rnd.nextInt(GAME_FIELD_X);
            int y = rnd.nextInt(GAME_FIELD_Y);
            if (wholeGrid[x][y] == ':' && counterb <= maxconvertBoulder) {
                wholeGrid[x][y] = ' ';
                char boulds = 'O';
                wholeGrid[x][y] = boulds;
                cn.getTextWindow().output(x, y, boulds);
                counterb++;
            }
        }
        int counterForTreasure = 0;
        int maxTreasure = 29;
        int counterForEmptySquares = 0;
        int maxEmptySquares = 199;

        //TODO: we need to extract this functionality to initializeTreasures() method
        //TODO: infinite loop
//        while (!(counterForTreasure == maxTreasure && counterForEmptySquares == maxEmptySquares)) {
//            int randomi = rnd.nextInt(55);
//            int randomj = rnd.nextInt(25);
//            if (wholeGrid[randomi][randomj] == ':' && counterForTreasure <= maxTreasure) {
//                //ASCII codes: 49=1_50=2_51=3
//                char randomTreasure = (char) (rnd.nextInt(52 - 49) + 49);
//                wholeGrid[randomi][randomj] = randomTreasure;
//                cn.getTextWindow().output(randomi, randomj, randomTreasure);
//                counterForTreasure++;
//            } else if (wholeGrid[randomi][randomj] == ':' && counterForEmptySquares <= maxEmptySquares) {
//                wholeGrid[randomi][randomj] = ' ';
//                cn.getTextWindow().output(randomi, randomj, ' ');
//                counterForEmptySquares++;
//            }
//        }

        return wholeGrid;
    }
}
