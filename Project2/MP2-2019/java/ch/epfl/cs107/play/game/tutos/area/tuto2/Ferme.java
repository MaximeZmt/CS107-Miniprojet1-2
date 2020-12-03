package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;

public class Ferme extends Tuto2Area {

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "zelda/Ferme";
	}

	@Override
	protected void createArea() {
		// TODO Auto-generated method stub
		registerActor(new Background(this));
		registerActor(new Foreground(this));
	}

//	public float getCameraScaleFactor() {
//		return 5.f; // Here change the camera zoom.
//	}
	
	
}
