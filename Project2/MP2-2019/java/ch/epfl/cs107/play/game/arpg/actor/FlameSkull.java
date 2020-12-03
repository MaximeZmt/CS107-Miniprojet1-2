package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * flyable monster (a flying flame skull)
 */
public class FlameSkull extends Monster implements FlyableEntity {
	
	private static final int ANIMATION_DURATION = 8;
	private static final float MAX_LIFE_TIME = 10f;
	private static final float MIN_LIFE_TIME = 5f;
	private static final float INITIAL_LATENCE = 10;
	
	private float latence; /// waiting time before randomizing the orientation
	private Orientation randomizedOrientation;
	private float lifeTime;
	private Sprite[][] flameSkullSprites;
	private Animation[] animations;
	private ARPGFlameSkullHandler handler;

	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		randomizedOrientation = orientation;
		latence = 10;
		lifeTime = RandomGenerator.getInstance().nextFloat()*(MAX_LIFE_TIME-MIN_LIFE_TIME)+MIN_LIFE_TIME;
		flameSkullSprites = RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]
				{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
		animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, flameSkullSprites);
		handler = new ARPGFlameSkullHandler();
		
		setVulnerabilities(new Vulnerability[] {Vulnerability.MAGICAL, Vulnerability.PHYSICAL});
		setMaxLifePoints(1f);
		setLifePoints(1f);
	}
	
	/**
	 * orientates and move the FlameSkull
	 * @param orient
	 */
	private void moveOrientate(Orientation orient) {
		
		if(getOrientation()==orient) {
			move(ANIMATION_DURATION);
		} else {
			orientate(orient);
		}
	}
	
	/**
	 * returns a randomized orientation
	 * @return (Orientation) 
	 */
	private Orientation randomizeOrientation() {
		int probability = 60;
		int randomValue = RandomGenerator.getInstance().nextInt(100);
		
		if(randomValue<=probability) {
			return Orientation.fromInt(RandomGenerator.getInstance().nextInt(4));
		} else { 
			return getOrientation();
		}
	}
	
	/**
	 * defines the movement pattern of the FlameSkull
	 * @param deltaTime
	 */
	private void movementPattern(float deltaTime) {
		if (latence<=0) {
			randomizedOrientation = randomizeOrientation();
			latence = INITIAL_LATENCE;
		} else {
			moveOrientate(randomizedOrientation);
			--latence;
		}
	}
	
	@Override
	public boolean wantsViewInteraction() {
		return false;
	}
	
	@Override
	public boolean wantsCellInteraction() {
		if (isDead()) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (lifeTime<=0) {
			die();
		}
		else if (!isDead()) {
			movementPattern(deltaTime);
			
			animations[getOrientation().ordinal()].update(deltaTime);
			lifeTime -= deltaTime;	
		}
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}	

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);				
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);		
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (!isDead() && isVisible()) {
			animations[getOrientation().ordinal()].draw(canvas);
		}
		
	}
	
	/**
	 * interaction handler
	 */
	private class ARPGFlameSkullHandler implements ARPGInteractionVisitor{ 
		
		@Override
		public void interactWith(Grass grass) { 
			grass.slice();
		}
		
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
		}
		
		@Override
		public void interactWith(Monster monster) {
			monster.takeDamage(new Vulnerability[] {Vulnerability.FIRE}, 1.f);
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			player.hurt(1);
		}
		
	}

}
