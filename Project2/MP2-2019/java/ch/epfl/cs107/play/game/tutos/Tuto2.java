package ch.epfl.cs107.play.game.tutos;

import java.util.ArrayList;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
	private GhostPlayer player;
	private ArrayList<Area> AList = new ArrayList<Area>();

	
	
	@Override
	public String getTitle() {
		return "Tuto2";
	}
	
	private void createAreas() {
		Area f = new Ferme();
		Area v = new Village();
		AList.add(f);
		AList.add(v);
		addArea(f);
		addArea(v);
		
	}
	
    @Override
    public void update(float deltaTime) {
    	super.update(deltaTime);
    	if (player.getVarIsPassingADoor()==true) {
    		Area current = getCurrentArea();
    		int cI=AList.indexOf(current);
    		cI = (cI+1)%2;
    		player.leaveArea();
    		current = setCurrentArea(AList.get(cI).getTitle(), false);
    		player.enterArea(current, new DiscreteCoordinates(5, 15));
    		player.isPassingDoorFalse();
    	}
    }
    
    public boolean begin(Window window, FileSystem fileSystem) {
    	if (super.begin(window, fileSystem)) {
    		createAreas();
    		Area ToFollow = setCurrentArea("zelda/Ferme", true);
    		player = new GhostPlayer(ToFollow,Orientation.DOWN , new DiscreteCoordinates(2, 10) ,"ghost.1");
    		ToFollow.registerActor(player);
    		ToFollow.setViewCandidate(player);
    		
    		return true;
    	}
    	else {
    		return false;
    	}
    }
	
	
	

}
