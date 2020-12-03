package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.DisabledMew;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * represents the cave of the pet
 */
public class GrotteMew extends ARPGArea{
	@Override
	public String getTitle() {
		return "GrotteMew";
	}

	@Override
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Door("zelda/Village", new DiscreteCoordinates(25, 17),Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(8, 2)));
		registerActor(new DisabledMew(this, Orientation.DOWN, new DiscreteCoordinates(8, 8)));
	}
}
