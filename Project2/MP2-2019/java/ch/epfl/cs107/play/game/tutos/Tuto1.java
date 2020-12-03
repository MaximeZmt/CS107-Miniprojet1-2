package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto1 extends AreaGame{
		private SimpleGhost player;
		private Keyboard keyboard;
		private Area[] AList = new Area[2];
	
		private void createAreas() {
			AList[0] = new Ferme();
			AList[1] = new Village();
			addArea(AList[0]);
			addArea(AList[1]);
			
		}
	    
	    public String getTitle() {
	    	return "Tuto1";
	    }
	    
	    @Override
	    public void end() {
	        // by default does nothing
	    	// can save the game states if wanted
	    }
	    
	    @Override
	    public void update(float deltaTime) {
	    	super.update(deltaTime);
	    	switchArea();
	    	keyboard = getWindow().getKeyboard();
	    	Button keyUP = keyboard.get(Keyboard.UP);
	    	Button keyDOWN = keyboard.get(Keyboard.DOWN);
	    	Button keyLEFT = keyboard.get(Keyboard.LEFT);
	    	Button keyRIGHT = keyboard.get(Keyboard.RIGHT);
	    	if (keyUP.isDown()) {
	    		System.out.println(keyUP.isDown());
	    		player.moveUp(0.05f);
	    	}
	    	if (keyDOWN.isDown()) {
	    		System.out.println(keyDOWN.isDown());
	    		player.moveDown(0.05f);
	    	}
	    	if (keyLEFT.isDown()) {
	    		System.out.println(keyLEFT.isDown());
	    		player.moveLeft(0.05f);
	    	}
	    	if (keyRIGHT.isDown()) {
	    		System.out.println(keyRIGHT.isDown());
	    		player.moveRight(0.05f);
	    	}
	    }
	    
	    public boolean begin(Window window, FileSystem fileSystem) {
	    	if (super.begin(window, fileSystem)) {
	    		createAreas();
	    		Area toFollow = setCurrentArea("zelda/Ferme", false);
	    		player = new SimpleGhost(new Vector(18, 7),"ghost.1");
	    		toFollow.registerActor(player);
	    		toFollow.setViewCandidate(player);
	    		
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
	    }
	    
	    public void switchArea() {
	    	if (player.isWeak()) {
	    		Area a = getCurrentArea();
	    		a.unregisterActor(player);
	    		Area aNew;
	    		int counter = 0;
	    		do {
	    			aNew = AList[counter];
	    			++counter;
	    		}while(aNew == a);
	    		aNew = setCurrentArea(aNew.getTitle(), false);
	    		aNew.registerActor(player);
	    		aNew.setViewCandidate(player); // do not forget to switch camera focus
	    		player.strengthen();
	    		System.out.print("SWITCH-"+aNew.getTitle());
	    	}
	    }
	    
	
}
