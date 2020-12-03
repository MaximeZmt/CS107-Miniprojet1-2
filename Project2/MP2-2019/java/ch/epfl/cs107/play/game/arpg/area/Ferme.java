package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.Test;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.TextPnj;
import ch.epfl.cs107.play.game.arpg.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.collectable.Heart;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * represents the farm
 * @author Mike
 *
 */
public class Ferme extends ARPGArea implements Test{

	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}
	
	@Override
	protected void createArea() { 
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		registerActor(new Door("zelda/Route", new DiscreteCoordinates(1, 15), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19, 15), new DiscreteCoordinates(19, 16)));
		registerActor(new Door("zelda/Village", new DiscreteCoordinates(4, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 0)));
		registerActor(new Door("zelda/Village", new DiscreteCoordinates(14, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(13, 0), new DiscreteCoordinates(14,0), new DiscreteCoordinates(15,0))); 
		registerActor(new Door("PetalburgTimmy", new DiscreteCoordinates(3, 1), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(6, 11)));
		registerActor(new TextPnj(this, Orientation.RIGHT, new DiscreteCoordinates(4, 10),true,"Welcome to the Legend of Mew !","We need your help, there is a Darklord in front of the castle door ! Please Kill Him !!","You need a magical staff in order to defeat him !","Go to the village south from here !"));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(10, 13)));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(16, 10)));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(14, 3)));
		
		if(Test.MODE) {
			registerActor(new Coin(this, Orientation.DOWN,new DiscreteCoordinates(4, 4)));
			registerActor(new Heart(this, Orientation.DOWN,new DiscreteCoordinates(4, 5)));
			registerActor(new CastleKey(this, Orientation.DOWN,new DiscreteCoordinates(4, 6)));
		}
		
		
	}
	
}
