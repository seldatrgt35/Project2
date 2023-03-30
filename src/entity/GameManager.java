package playy.src.entity;

import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import enigma.console.TextAttributes;
import java.awt.Color;
import java.util.Random;

public class GameManager {
	public enigma.console.Console cn = Enigma.getConsole("Mouse and Keyboard", 75, 25);
	public TextMouseListener tmlis;
	public KeyListener klis;

<<<<<<< HEAD:GameManager.java
	// ------ Standard variables for mouse and keyboard ------
	public int mousepr; // mouse pressed?
	public int mousex, mousey; // mouse text coords.
	public int keypr; // key pressed?
	public int rkey; // key (for press/release)
	// ----------------------------------------------------

	GameManager() throws Exception { // --- Contructor

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

		initializeWallAndEarth();
	

		int px = 5, py = 5;
		cn.getTextWindow().output(px, py, 'P');
		while (true) {
			if (mousepr == 1) { // if mouse button pressed
				cn.getTextWindow().output(mousex, mousey, '#'); // write a char to x,y position without changing cursor
																// position
				px = mousex;
				py = mousey;

				mousepr = 0; // last action
			}
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

				if (rkey == KeyEvent.VK_SPACE) {
					String str;
					str = cn.readLine(); // keyboardlistener running and readline input by using enter
					cn.getTextWindow().setCursorPosition(5, 20);
					cn.getTextWindow().output(str);
				}

				keypr = 0; // last action
			}
			Thread.sleep(20);
		}

	}
	

	public char[][] initializeWallAndEarth() {
		char[][] wholeGrid = new char[55][25];
		for (int i = 0; i < 55; i++) {

			for (int j = 0; j < 25; j++) {
				if (i == 0 || i == 54 || j == 0 || j == 24) {
					wholeGrid[i][j] = '#'; // First row of the outer walls
					cn.getTextWindow().output(i, j, '#');
				} else {
					wholeGrid[i][j] = ':'; // First row of the outer walls
					cn.getTextWindow().output(i, j, ':');
				}

			}
		}
//	      Convert boulders 
	      Random rndb= new Random();
	      int maxconvertBoulder=179;
	      int counterb=0;
	      while(!(counterb==maxconvertBoulder)) {
	    	  int x=rndb.nextInt(55);
	    	  int y =rndb.nextInt(25);
	    	  if(wholeGrid[x][y]==':'&&counterb<=maxconvertBoulder) {
	    		  wholeGrid[x][y]=' ';
	    		  char boulds='O';
	    		  wholeGrid[x][y]=boulds;
	    		  cn.getTextWindow().output(x,y,boulds);
	    		  counterb++;
	    	  }
	      }
		Random rnd=new Random();
	      int counterForTreasure=0;
	      int maxTreasure=29;
	      int counterForEmptySquares=0;
	      int maxEmptySquares=199;

	      while(!(counterForTreasure==maxTreasure&&counterForEmptySquares==maxEmptySquares)){
	         int randomi= rnd.nextInt(55);
	         int randomj= rnd.nextInt(25);
	         if(wholeGrid[randomi][randomj]==':' && counterForTreasure<=maxTreasure){
	            //ASCII codes: 49=1_50=2_51=3
	            char randomTreasure= (char) (rnd.nextInt(52 - 49) + 49);
	            wholeGrid[randomi][randomj] =randomTreasure ;
	            cn.getTextWindow().output(randomi,randomj,randomTreasure);
	            counterForTreasure++;
	         }
	         else if(wholeGrid[randomi][randomj]==':' && counterForEmptySquares<=maxEmptySquares){
	            wholeGrid[randomi][randomj]=' ';
	            cn.getTextWindow().output(randomi,randomj,' ');
	            counterForEmptySquares++;
	         }
	      }


	      return wholeGrid;
	  
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
=======
   // ------ Standard variables for mouse and keyboard ------
   public int mousepr;          // mouse pressed?
   public int mousex, mousey;   // mouse text coords.
   public int keypr;   // key pressed?
   public int rkey;    // key   (for press/release)
   // ----------------------------------------------------
   
   
   public GameManager() throws Exception {   // --- Contructor
                 
      // ------ Standard code for mouse and keyboard ------ Do not change
      tmlis=new TextMouseListener() {
         public void mouseClicked(TextMouseEvent arg0) {}
         public void mousePressed(TextMouseEvent arg0) {
            if(mousepr==0) {
               mousepr=1;
               mousex=arg0.getX();
               mousey=arg0.getY();
            }
         }
         public void mouseReleased(TextMouseEvent arg0) {}
      };
      cn.getTextWindow().addTextMouseListener(tmlis);
    
      klis=new KeyListener() {
         public void keyTyped(KeyEvent e) {}
         public void keyPressed(KeyEvent e) {
            if(keypr==0) {
               keypr=1;
               rkey=e.getKeyCode();
            }
         }
         public void keyReleased(KeyEvent e) {}
      };
      cn.getTextWindow().addKeyListener(klis);
      // ----------------------------------------------------
      
       initializeWallAndEarth();
     
      int px=5,py=5;
      cn.getTextWindow().output(px,py,'P');
      while(true) {
         if(mousepr==1) {  // if mouse button pressed
            cn.getTextWindow().output(mousex,mousey,'#');  // write a char to x,y position without changing cursor position
            px=mousex; py=mousey;
            
            mousepr=0;     // last action  
         }
         if(keypr==1) {    // if keyboard button pressed
            if(rkey==KeyEvent.VK_LEFT) px--;   
            if(rkey==KeyEvent.VK_RIGHT) px++;
            if(rkey==KeyEvent.VK_UP) py--;
            if(rkey==KeyEvent.VK_DOWN) py++;
            
            char rckey=(char)rkey;
            //        left          right          up            down
            if(rckey=='%' || rckey=='\'' || rckey=='&' || rckey=='(') cn.getTextWindow().output(px,py,'P'); // VK kullanmadan test teknigi
            else cn.getTextWindow().output(rckey);
            
            if(rkey==KeyEvent.VK_SPACE) {
               String str;         
               str=cn.readLine();     // keyboardlistener running and readline input by using enter 
               cn.getTextWindow().setCursorPosition(5, 20);
               cn.getTextWindow().output(str);
            }
            
            keypr=0;    // last action  
         }
         Thread.sleep(20);
      }
      
   }
   public char[][] initializeWallAndEarth(){
       char[][] wholeGrid = new char[55][25];
       cn.getTextWindow().output(0,24,'a');
       for (int i = 0; i < 55; i++){
    	   
           for (int j = 0; j < 25; j++){
               if (i == 0 || i==54 || j==0 || j==24) {
            	   wholeGrid[i][ j] = '#';
            	   cn.getTextWindow().output(i,j,'#');
               }
               else {
            	   wholeGrid[i][ j] = ':';
            	   cn.getTextWindow().output(i,j,':');
               }

           }
       }
      Random rnd=new Random();
      int counterForTreasure=0;
      int maxTreasure=29;
      int counterForEmptySquares=0;
      int maxEmptySquares=199;

      while(!(counterForTreasure==maxTreasure&&counterForEmptySquares==maxEmptySquares)){
         int randomi= rnd.nextInt(55);
         int randomj= rnd.nextInt(25);
         if(wholeGrid[randomi][randomj]==':' && counterForTreasure<=maxTreasure){
            //ASCII codes: 49=1_50=2_51=3
            char randomTreasure= (char) (rnd.nextInt(52 - 49) + 49);
            wholeGrid[randomi][randomj] =randomTreasure ;
            cn.getTextWindow().output(randomi,randomj,randomTreasure);
            counterForTreasure++;
         }
         else if(wholeGrid[randomi][randomj]==':' && counterForEmptySquares<=maxEmptySquares){
            wholeGrid[randomi][randomj]=' ';
            cn.getTextWindow().output(randomi,randomj,' ');
            counterForEmptySquares++;
         }
      }

      return wholeGrid;
   }
>>>>>>> ca6b6c8b3e603fce8c67544fe33456edad49a255:src/entity/GameManager.java
}
