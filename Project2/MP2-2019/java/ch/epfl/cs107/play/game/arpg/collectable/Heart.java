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
 * heals when taken
 */
public class Heart extends CollectableAreaEntity {

	private Sprite[] heartSpriteAnim;
	private Animation heartAnim;
	private final static int ANIMATION_DURATION = 8;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		heartSpriteAnim = RPGSprite.extractSprites("zelda/heart", 4, 1, 1, this , 16, 16);
		heartAnim = RPGSprite.createAnimations(ANIMATION_DURATION/2, heartSpriteAnim, true);
	}

	@Override
	public void draw(Canvas canvas) {
		heartAnim.draw(canvas);		
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		heartAnim.update(deltaTime);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
	}
}
