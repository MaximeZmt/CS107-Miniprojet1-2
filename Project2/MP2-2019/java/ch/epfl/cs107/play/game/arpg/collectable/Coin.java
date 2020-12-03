package ch.epfl.cs107.play.game.arpg.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * add money when picked up
 */
public class Coin extends CollectableAreaEntity {

	private Sprite[] coinSpriteAnim;
	private Animation coinAnim;
	private final static int ANIMATION_DURATION = 8;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		coinSpriteAnim = RPGSprite.extractSprites("zelda/coin", 4, 1, 1, this , 16, 16);
		coinAnim = RPGSprite.createAnimations(ANIMATION_DURATION/2, coinSpriteAnim, true);
	}

	@Override
	public void draw(Canvas canvas) {
		coinAnim.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		coinAnim.update(deltaTime);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
	}
}
