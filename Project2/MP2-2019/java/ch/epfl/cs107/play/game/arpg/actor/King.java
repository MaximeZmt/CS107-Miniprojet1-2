package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * TextPnj in the Castle
 * @see TextPnj
 */
public class King extends TextPnj{
	
	private Sprite[] sprites;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 * @param canMove
	 * @param text
	 */
	public King(Area area, Orientation orientation, DiscreteCoordinates position, boolean canMove, String... text) {
		super(area, orientation, position, canMove, text);
		sprites = RPGSprite.extractSpritesVert("zelda/king", 4, 1, 2, this, 16, 32);
		setSprite(sprites);
	}

}
