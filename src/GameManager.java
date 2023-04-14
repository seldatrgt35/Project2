package gravity.src;

import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import gravity.src.entity.Player;
import gravity.src.entity.Stack;
import gravity.src.entity.Queue;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import static gravity.src.Constant.*;
import static java.awt.Font.BOLD;

public class GameManager {

    Random rnd = new Random();
    public int px, py, pxOld, pyOld; // player x,y position
    public int qx, qy, bx, by;
    public boolean gameOver = false;
    char[][] wholeGrid = new char[GAME_FIELD_X][GAME_FIELD_Y];

    Stack backpack = new Stack(BACKPACK_SIZE);

    Queue queue = new Queue(QUEUE_SIZE);
    private static int time = 0;
    private static final Player player = new Player(0, new Stack(STACK_SIZE), 3);

    private static enigma.console.Console cn;

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
        cn = Enigma.getConsole("Gravity - Team7", 75, GAME_FIELD_Y, 20, BOLD);

        //set console background color to white
        for (int i = 0; i < cn.getTextWindow().getColumns(); i++) {
            for (int j = 0; j < cn.getTextWindow().getRows(); j++) {
                cn.getTextWindow().output(i, j, EMPTY, CONSOLE_COLOR);
            }
        }

        setMenu();
        doNotChange();
        //1.	Walls are placed.
        initializeWallAndEarth();
        //2.	All empty squares are converted into earth squares.
        //3.	Random 180 earth squares are converted into boulders.
        //4.	Random 30 earth squares are converted into treasures (Random 1, 2 or 3 with equal probability).
        initializeBoulders();
        initializeTreasures();
        //5.	Random 200 earth squares are converted into empty squares.
        initializeEmptySquares();
        //6.	Random 7 earth squares are converted into robots.
        initializeRobots();
        //7.	Player P is placed on a random earth square.
        initializePlayer();
        //Input queue
        queueJob();

        int i = 0;
        while (!gameOver) {
            i++;
            pxOld = px;
            pyOld = py;
            gameOver = false;
            if (keypr == 1) { // if keyboard button pressed
                handleKey(rkey);
                keypr = 0; // last action
            }
            //Print game field according to wholeGrid array with colors
            colorfulPrintGameBoard();

            Thread.sleep(1000 / FPS);
            if (i % FPS == 0) {
                cn.getTextWindow().setCursorPosition(72 - String.valueOf(++time).length(), 22);
                cn.getTextWindow().output(String.valueOf(time), TEXT_COLOR);
            }
            if(i % FPS_QUEUE == 0){
                queueJob();
            }
        }
    }

    private void handleKey(int rkey) {
        if (rkey == KeyEvent.VK_LEFT && px > 1) {
            switch (wholeGrid[px - 1][py]) {
                case WALL:
                    break;
                case EARTH:
                case EMPTY:
                    movePlayer(--px, py);
                    break;
                case ROBOT:
                    gameOver = true;
                    break;
                case BOULDER:
                    if (wholeGrid[px - 2][py] == EMPTY) {
                        wholeGrid[px - 2][py] = BOULDER;
                        movePlayer(--px, py);
                    }
                    break;
                case '1':
                case '2':
                case '3':
                    movePlayer(--px, py);
                    addTheTreasure(wholeGrid[px - 1][py]);
                    break;
            }
        } else if (rkey == KeyEvent.VK_RIGHT && px < GAME_FIELD_X - 2) {
            switch (wholeGrid[px + 1][py]) {
                case WALL:
                    break;
                case EARTH:
                case EMPTY:
                    movePlayer(++px, py);
                    break;
                case ROBOT:
                    gameOver = true;
                    break;
                case BOULDER:
                    if (wholeGrid[px + 2][py] == EMPTY) {
                        wholeGrid[px + 2][py] = BOULDER;
                        movePlayer(++px, py);
                    }
                    break;
                case '1':
                case '2':
                case '3':
                    movePlayer(++px, py);
                    addTheTreasure(wholeGrid[px + 1][py]);
                    break;
            }
        } else if (rkey == KeyEvent.VK_UP && py > 1) {
            switch (wholeGrid[px][py - 1]) {
                case WALL:
                    break;
                case EARTH:
                case EMPTY:
                    movePlayer(px, --py);
                    break;
                case ROBOT:
                    gameOver = true;
                    break;
                case BOULDER:
                    if (wholeGrid[px][py - 2] == EMPTY) {
                        wholeGrid[px][py - 2] = BOULDER;
                        movePlayer(px, --py);
                    }
                    break;
                case '1':
                case '2':
                case '3':
                    movePlayer(px, --py);
                    addTheTreasure(wholeGrid[px][py - 1]);
                    break;
            }
        } else if (rkey == KeyEvent.VK_DOWN && py < GAME_FIELD_Y - 2) {
            switch (wholeGrid[px][py + 1]) {
                case WALL:
                    break;
                case EARTH:
                case EMPTY:
                    movePlayer(px, ++py);
                    break;
                case ROBOT:
                    gameOver = true;
                    break;
                case BOULDER:
                    if (wholeGrid[px][py + 2] == EMPTY) {
                        wholeGrid[px][py + 2] = BOULDER;
                        movePlayer(px, ++py);
                    }
                    break;
                case '1':
                case '2':
                case '3':
                    movePlayer(px, ++py);
                    addTheTreasure(wholeGrid[px][py + 1]);
                    break;
            }
        } else if (rkey == KeyEvent.VK_SPACE && player.getTeleportRight() > 0) {
            player.setTeleportRight(player.getTeleportRight() - 1);
            cn.getTextWindow().setCursorPosition(72 - String.valueOf(player.getTeleportRight()).length(), 18);
            cn.getTextWindow().output(String.valueOf(player.getTeleportRight()), TEXT_COLOR);
            wholeGrid[px][py] = EMPTY;
            initializePlayer();
        }
    }

    private void movePlayer(int px, int py) {
        backpackImplementation(wholeGrid, backpack, player);
        wholeGrid[px][py] = PLAYER;    // VK kullanmadan test teknigi
        wholeGrid[pxOld][pyOld] = EMPTY;
    }

    //TODO: Add the treasure to the player's BackPack
    private void addTheTreasure(char theTreasure) {
        switch (theTreasure) {
            case '1':
                break;
            case '2':
                break;
            case '3':
                break;
        }
    }

    private void colorfulPrintGameBoard() {
        for (int i = 0; i < GAME_FIELD_X; i++) {
            for (int j = 0; j < GAME_FIELD_Y; j++) {
                switch (wholeGrid[i][j]) {
                    case ROBOT:
                        cn.getTextWindow().output(i, j, ROBOT, ROBOT_COLOR);
                        break;
                    case PLAYER:
                        cn.getTextWindow().output(i, j, PLAYER, PLAYER_COLOR);
                        break;
                    case '1':
                    case '2':
                    case '3':
                        cn.getTextWindow().output(i, j, wholeGrid[i][j], TREASURE_COLOR);
                        break;
                    case BOULDER:
                        cn.getTextWindow().output(i, j, BOULDER, BOULDER_COLOR);
                        break;
                    case EARTH:
                        cn.getTextWindow().output(i, j, EARTH, EARTH_COLOR);
                        break;
                    case WALL:
                        cn.getTextWindow().output(i, j, WALL, WALL_COLOR);
                        break;
                    default:
                        cn.getTextWindow().output(i, j, EMPTY, CONSOLE_COLOR);
                        break;
                }
            }
        }
    }

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
        cn.getTextWindow().setCursorPosition(72 - String.valueOf(player.getTeleportRight()).length(), 18);
        cn.getTextWindow().output(String.valueOf(player.getTeleportRight()), TEXT_COLOR);

        cn.getTextWindow().setCursorPosition(56, 20);
        cn.getTextWindow().output("Score    :", TEXT_COLOR);
        cn.getTextWindow().setCursorPosition(72 - String.valueOf(player.getScore()).length(), 20);
        cn.getTextWindow().output(String.valueOf(player.getScore()), TEXT_COLOR);

        cn.getTextWindow().setCursorPosition(56, 22);
        cn.getTextWindow().output("Time     :", TEXT_COLOR);
        cn.getTextWindow().setCursorPosition(72 - String.valueOf(time).length(), 22);
        cn.getTextWindow().output(String.valueOf(time), TEXT_COLOR);
    }

    private void initializeTreasures() {
        for (int i = 0; i < TREASURE_COUNT; i++) {
            while (wholeGrid[px][py] != EARTH) {
                px = rnd.nextInt(GAME_FIELD_X);
                py = rnd.nextInt(GAME_FIELD_Y);
            }
            //ASCII codes: 49=1_50=2_51=3
            char randomTreasure = (char) (rnd.nextInt(52 - 49) + 49);
            wholeGrid[px][py] = randomTreasure;
        }
    }

    private void initializeEmptySquares() {
        for (int i = 0; i < EMPTY_SQUARE_COUNT; i++) {
            while (wholeGrid[px][py] != EARTH) {
                px = rnd.nextInt(GAME_FIELD_X);
                py = rnd.nextInt(GAME_FIELD_Y);
            }
            wholeGrid[px][py] = EMPTY;
        }
    }

    private void initializeRobots() {
        for (int i = 0; i < ROBOT_COUNT; i++) {
            while (wholeGrid[px][py] != EARTH) {
                px = rnd.nextInt(GAME_FIELD_X);
                py = rnd.nextInt(GAME_FIELD_Y);
            }
            wholeGrid[px][py] = ROBOT;
        }
    }

    private void initializePlayer() {
        while (wholeGrid[px][py] != EARTH) {
            px = rnd.nextInt(GAME_FIELD_X);
            py = rnd.nextInt(GAME_FIELD_Y);
        }
        wholeGrid[px][py] = PLAYER;
    }


    private void queueJob(){
        inputQueue();
        printQueue();
        while (wholeGrid[qx][qy] != EARTH && wholeGrid[qx][qy] != EMPTY) {
            qx = rnd.nextInt(GAME_FIELD_X);
            qy = rnd.nextInt(GAME_FIELD_Y);
        }
        //if the top element is a boulder a random boulder from the game area is conveerted into an earth square
        if((char)queue.peek()==BOULDER){
            while(wholeGrid[bx][by] != BOULDER){
                bx = rnd.nextInt(GAME_FIELD_X);
                by = rnd.nextInt(GAME_FIELD_Y);
            }
            wholeGrid[qx][qy] = EARTH;
        }
        wholeGrid[qx][qy] = (char)queue.peek();
        printQueue();
        queue.dequeue();
    }
    private void printQueue(){
        for(int i=0; i<QUEUE_SIZE ; i++){
            char topElement =(char)queue.dequeue();
            cn.getTextWindow().output((56+i), 2,topElement,TEXT_COLOR);
            queue.enqueue(topElement);
        }

    }
    //Filling the input queue with random game elements if the queue is not full
    private Queue inputQueue(){
        while(!queue.isFull()){
            queue.enqueue(getRandomCharacter());
        }return queue;
    }

    private char getRandomCharacter(){
        int randomPercentage = rnd.nextInt(1,41);
        int[] percentages = new int[] {6,11,15,16,26,35,40}; // cumulative percentages
        char[] characters = new char[] {TREASURE_1,TREASURE_2,TREASURE_3, ROBOT, BOULDER, EARTH, EMPTY};
        for(int i = 0; i< percentages.length; i++){
            if (randomPercentage < percentages[i]){
                return characters[i];
            }
        }
        return 0;
    }

    private void backpackImplementation(char wholeGrid[][], Stack backPack, Player player) {
        keepTrackOfScoreAndTeleportRight(player);
        if (wholeGrid[px][py] == TREASURE_1 || wholeGrid[px][py] == TREASURE_2 || wholeGrid[px][py] == TREASURE_3) {
            if (backPack.isFull()) {
                backPack.pop();
                backPack.push(wholeGrid[px][py]);
            } else {
                backPack.push(wholeGrid[px][py]);
            }
            printBackpackElementsOnMenu(backpack);
        }
    }

    private void keepTrackOfScoreAndTeleportRight(Player player) {
        if (backpack.size() > 1) {
            char checkTreasue = (char) backpack.pop();
            if (checkTreasue == (char) backpack.peek()) {
                backpack.pop();

                int score = player.getScore();
                int teleportRight = player.getTeleportRight();
                if (checkTreasue == TREASURE_1) {
                    score += 10;
                } else if (checkTreasue == TREASURE_2) {
                    score += 40;
                } else {
                    score += 90;
                    teleportRight += 1;
                }
                player.setScore(score);
                player.setTeleportRight(teleportRight);

                int a = backpack.size();
                String scoreString = String.valueOf(score);
                String teleportRightString = String.valueOf(teleportRight);
                cn.getTextWindow().output(64, 12 - a, ' ', TEXT_COLOR);
                cn.getTextWindow().output(64, 13 - a, ' ', TEXT_COLOR);
                cn.getTextWindow().setCursorPosition(72 - scoreString.length(), 20);
                cn.getTextWindow().output(scoreString, TEXT_COLOR);
                cn.getTextWindow().setCursorPosition(72 - teleportRightString.length(), 18);
                cn.getTextWindow().output(teleportRightString, TEXT_COLOR);
            } else {
                backpack.push(checkTreasue);
            }

        }
    }

    private void printBackpackElementsOnMenu(Stack backpack) {
        Stack tempStack = new Stack(8);
        while (!backpack.isEmpty()) {
            tempStack.push(backpack.pop());
        }
        int count = 0;
        while (!tempStack.isEmpty()) {
            char paste = (char) tempStack.peek();
            cn.getTextWindow().output(64, 13 - count, paste, TEXT_COLOR);
            count++;
            backpack.push(tempStack.pop());
        }
    }

    public char[][] initializeBoulders() {
        int counterb = 0;
        while (counterb != (EARTH_SQUARES - 1)) {
            int x = rnd.nextInt(GAME_FIELD_X);
            int y = rnd.nextInt(GAME_FIELD_Y);
            if (wholeGrid[x][y] == EARTH && counterb < EARTH_SQUARES) {
                wholeGrid[x][y] = EMPTY;
                char boulds = BOULDER;
                wholeGrid[x][y] = boulds;
                counterb++;
            }
        }
        return wholeGrid;
    }

    public char[][] initializeWallAndEarth() {
        for (int i = 0; i < GAME_FIELD_X; i++) {
            for (int j = 0; j < GAME_FIELD_Y; j++) {
                if ((i == 0 || i == 54 || j == 0 || j == 24) || (j == 8 && i >= 0 && i < 50)
                        || (j == 16 && i >= 5 && i < 54)) {

                    wholeGrid[i][j] = WALL; // First row of the outer walls
                } else {
                    wholeGrid[i][j] = EARTH; // First row of the outer walls
                }
            }
        }
        return wholeGrid;

    }
}

