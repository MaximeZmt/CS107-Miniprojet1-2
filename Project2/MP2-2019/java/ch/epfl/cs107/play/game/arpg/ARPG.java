package ch.epfl.cs107.play.game.arpg;

import java.util.ArrayList;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Chateau;
import ch.epfl.cs107.play.game.arpg.area.Ferme;
import ch.epfl.cs107.play.game.arpg.area.GrotteMew;
import ch.epfl.cs107.play.game.arpg.area.PetalburgTimmy;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.arpg.area.RouteChateau;
import ch.epfl.cs107.play.game.arpg.area.RouteTemple;
import ch.epfl.cs107.play.game.arpg.area.Temple;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

/**
 * Action RPG. It is a RPG where the player make actions in real time
 * @see RPG
 */
public class ARPG extends RPG {
	private ARPGPlayer player;
	private ArrayList<Area> AList = new ArrayList<Area>();
	private Area currentArea;
	
	/**
	 * Initialise all possible area
	 */
	private void createAreas() {
		AList.add(new Ferme());
		AList.add(new Village());
		AList.add(new Route());
		AList.add(new RouteChateau());
		AList.add(new Chateau());
		AList.add(new Temple());
		AList.add(new RouteTemple());
		AList.add(new GrotteMew());
		AList.add(new PetalburgTimmy());
		for (Area a : AList) {
			addArea(a);
		}
	}
	
	@Override
	public String getTitle() {
		return "The Legend of Mew";
	}
	
	
	@Override
    public boolean begin(Window window, FileSystem fileSystem) {
    	if (super.begin(window, fileSystem)) {
    		createAreas();
    		/// set beginning area for Player
    		currentArea = setCurrentArea("PetalburgTimmy", true);
    		player = new ARPGPlayer(currentArea, Orientation.DOWN, new DiscreteCoordinates(4, 4));
    		initPlayer(player);
    		return true;
    	}
    	return false;
    }
	
	
    @Override
    public void update(float deltaTime) {
    	super.update(deltaTime);
    }
	
    @Override
    public void end() {
    	
    }
    
    
}
