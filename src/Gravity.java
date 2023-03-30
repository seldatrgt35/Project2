package playy.src;

import playy.src.entity.GameManager;

public class Gravity {
	public static void main(String[] args) throws Exception {
		GameManager myGame = new GameManager();
		myGame.initializeWallAndEarth();
	}
}
