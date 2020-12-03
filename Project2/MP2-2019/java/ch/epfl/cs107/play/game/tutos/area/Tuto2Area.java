package ch.epfl.cs107.play.game.tutos.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

abstract public class Tuto2Area extends Area {
	private Tuto2Behavior behave;
	protected abstract void createArea();
	@Override
	public float getCameraScaleFactor() {
		// TODO Auto-generated method stub
		return 15.f;
	}
	
	
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
        	
            // Set the behavior map
        	behave = new Tuto2Behavior(window, getTitle());
        	setBehavior(behave);
            createArea();
            return true;
        }
        return false;
    }
    
    public boolean isADoor(DiscreteCoordinates coord) {
    	return (behave.isADoor(coord));
    }
    
	
}





