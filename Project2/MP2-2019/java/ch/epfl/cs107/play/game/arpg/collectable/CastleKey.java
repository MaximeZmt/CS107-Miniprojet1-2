package ch.epfl.cs107.play.game.arpg.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

/**
 * item to open the CastleDoor
 * @see CastleDoor
 */
public class CastleKey extends CollectableAreaEntity {

	private Sprite keySprite;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public CastleKey(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		keySprite = new RPGSprite("zelda/key", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
	}	

	@Override
	public void draw(Canvas canvas) {
		keySprite.draw(canvas);		
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
	}

}
