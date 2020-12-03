package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * entity that animates the waterFall
 */
public class Waterfall extends Entity{

	private Sprite[] waterfallSprites;
	private Animation animations;
	private final static int ANIMATION_DURATION = 6;
	
	/**
	 * default constructor
	 * @param position
	 */
	public Waterfall(DiscreteCoordinates position) {
		super(new Vector(position.x,position.y));
		waterfallSprites = RPGSprite.extractSprites("zelda/waterfall", 3, 4, 4,
				this , 64, 64);
		animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, waterfallSprites, true);
	}


	@Override
	public void draw(Canvas canvas) {
		animations.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		animations.update(deltaTime);
	}

}
