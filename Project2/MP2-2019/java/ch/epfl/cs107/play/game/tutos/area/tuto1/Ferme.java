package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;

public class Ferme extends SimpleArea{

	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}

	@Override
	protected void createArea() {
		// TODO Auto-generated method stub
		registerActor(new Background(this));
	}


}
