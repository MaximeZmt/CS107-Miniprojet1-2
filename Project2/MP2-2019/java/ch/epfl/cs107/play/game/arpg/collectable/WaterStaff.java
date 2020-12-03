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
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * findable staff
 */
public class WaterStaff extends CollectableAreaEntity {

	private Sprite[] staffSpriteAnim;
	private Animation staffAnim;
	private final static int ANIMATION_DURATION = 8;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public WaterStaff(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		staffSpriteAnim = RPGSprite.extractSprites("zelda/staff", 8, 2, 2, this , 32, 32, new Vector(-0.5f,0));
		staffAnim = RPGSprite.createAnimations(ANIMATION_DURATION/2, staffSpriteAnim, true);
	}

	@Override
	public void draw(Canvas canvas) {
		staffAnim.draw(canvas);		
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		staffAnim.update(deltaTime);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
	}
}
