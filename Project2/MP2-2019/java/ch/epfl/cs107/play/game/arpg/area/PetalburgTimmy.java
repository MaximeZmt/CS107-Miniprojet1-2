package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * represents the house of the player
 */
public class PetalburgTimmy extends ARPGArea {
	@Override
	public String getTitle() {
		return "PetalburgTimmy";
	}

	@Override
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(6, 10),Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(3, 0), new DiscreteCoordinates(4, 0)));
	}
}
