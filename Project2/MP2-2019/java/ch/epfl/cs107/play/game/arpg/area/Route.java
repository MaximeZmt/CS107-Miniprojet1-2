package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bridge;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.TextPnj;
import ch.epfl.cs107.play.game.arpg.actor.Waterfall;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * represents the route
 */
public class Route extends ARPGArea{

	@Override
	public String getTitle() {
		return "zelda/Route";
	}

	@Override
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Foreground(this)); 
		registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(18, 15), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(0, 15), new DiscreteCoordinates(0, 16)));
		registerActor(new Door("zelda/Village", new DiscreteCoordinates(29, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0)));
		registerActor(new Door("zelda/RouteChateau", new DiscreteCoordinates(9, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(9, 19), new DiscreteCoordinates(10, 19)));
		registerActor(new Door("zelda/RouteTemple", new DiscreteCoordinates(1, 4), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19, 11), new DiscreteCoordinates(19, 10), new DiscreteCoordinates(19, 9), new DiscreteCoordinates(19, 8)));
		registerActor(new Waterfall(new DiscreteCoordinates(15, 3))); 
		registerActor(new Bridge(this, Orientation.DOWN, new DiscreteCoordinates(15, 9)));
		registerActor(new TextPnj(this, Orientation.DOWN, new DiscreteCoordinates(15, 11),false,"My grandfather once told me that a mystical creature can build a bridge here !"));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(4, 14)));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(10, 7)));
		//REGISTER GRASS 
		for (int i = 5;i<=7;++i) {
			for (int j=6;j<=11;++j) {
				registerActor(new Grass(this,Orientation.DOWN,new DiscreteCoordinates(i, j)));
			}
		}
		//END GRASS REGISTER
		
	}

}
