package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

/**
 * area for an ARPG
 * @see Area
 */
abstract public class ARPGArea extends Area{
	private ARPGBehavior behave;
	
	/**
	 * initialises areas
	 */
	protected abstract void createArea();

	@Override
	public float getCameraScaleFactor() {
		return 15.f; // Here change the camera zoom.
	}
	
	@Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
        	behave = new ARPGBehavior(window, getTitle());
        	setBehavior(behave);
            createArea();
            return true;
        }
        return false;
    }
	
	
}
