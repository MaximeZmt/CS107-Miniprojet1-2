package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * monster that is made of wood, can sleep and runs on the player
 */
public class LogMonster extends Monster {

	/**
	 * all possible states for LogMonster
	 */
	private enum State {
		IDLE,
		ATTACKING,
		GOING_TO_SLEEP,
		SLEEPING,
		WAKING_UP;
	}
	
	private static final int ANIMATION_DURATION = 8;
	private static final float MIN_SLEEPING_DURATION = 5;
	private static final float MAX_SLEEPING_DURATION = 15;
	private static final float MIN_IMMOBILE_TIME = 5;
	private static final float MAX_IMMOBILE_TIME = 20;
	private static final float MIN_MOBILE_TIME = 5;
	private static final float MAX_MOBILE_TIME = 20;
	private static final int MAX_HP = 3;
	
	private State state;
	private ARPGLogMonsterHandler handler;
	private float sleepingTime;
	private float remainingImmobileTime;
	private float remainingMobileTime;
	private boolean attacked;
	
	private Sprite[][] idleSprites;
	private Sprite[] sleepingSprites;
	private Sprite[] wakingUpSprites;
	
	private Animation[] idleAnimations;
	private Animation sleepingAnimations;
	private Animation wakingUpAnimations;

	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public LogMonster(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		state = State.IDLE;
		handler = new ARPGLogMonsterHandler();
		sleepingTime = 0;
		remainingImmobileTime = RandomGenerator.getInstance().nextFloat()*
				(MAX_IMMOBILE_TIME-MIN_IMMOBILE_TIME)+MIN_IMMOBILE_TIME;
		remainingMobileTime = RandomGenerator.getInstance().nextFloat()*
				(MAX_MOBILE_TIME-MIN_MOBILE_TIME)+MIN_MOBILE_TIME;
		idleSprites = RPGSprite.extractSprites("zelda/logMonster", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]
				{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
		sleepingSprites = RPGSprite.extractSpritesVert("zelda/logMonster.sleeping", 4, 2, 2, this, 32, 32, new Vector(-0.5f,0));
		wakingUpSprites = RPGSprite.extractSpritesVert("zelda/logMonster.wakingUp", 3, 2, 2, this, 32, 32, new Vector(-0.5f,0));
		
		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, idleSprites);
		sleepingAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sleepingSprites);
		wakingUpAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, wakingUpSprites, false);
		
		setVulnerabilities(new Vulnerability[] {Vulnerability.FIRE, Vulnerability.PHYSICAL});
		setMaxLifePoints(MAX_HP);
		setLifePoints(MAX_HP);
		///dropped item : Coin
		setDroppedItem(new Coin(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates()));

	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (sleepingTime<=0) {
			
			switch(state) {
			case IDLE:
				movementPattern(deltaTime);
				if (isDisplacementOccurs()) {
					idleAnimations[getOrientation().ordinal()].update(deltaTime);	
				} else {
					idleAnimations[getOrientation().ordinal()].reset();
				}
				randomizeSleeping();
				break;
				
			case ATTACKING:
				move(ANIMATION_DURATION);
				if (!isDisplacementOccurs() && isTargetReached()) {
					if (attacked) {
						state = State.GOING_TO_SLEEP;
					} else {
						attacked = true;
					}
				}
				idleAnimations[getOrientation().ordinal()].update(deltaTime);
				break;
				
			case GOING_TO_SLEEP:
				sleepingTime = RandomGenerator.getInstance().nextFloat()*
					(MAX_SLEEPING_DURATION-MIN_SLEEPING_DURATION)+MIN_SLEEPING_DURATION;
				state = State.SLEEPING;
				break;
				
			case SLEEPING:
				state = State.WAKING_UP;
				break;
				
			case WAKING_UP:
				wakingUpAnimations.update(deltaTime);
				if (wakingUpAnimations.isCompleted()) {
					state = State.IDLE;
				}
				break;
			}
		} else {
			sleepingAnimations.update(deltaTime);
			sleepingTime -= deltaTime;
		}
	}
	
	/**
	 * randomly sets the LogMonster to sleep
	 */
	private void randomizeSleeping() {
		int probability = 1;
		int randomValue = RandomGenerator.getInstance().nextInt(1000);
		
		if(randomValue<=probability) {
			state = State.GOING_TO_SLEEP;
		}
	}
	
	/**
	 * move and orientates the LogMonster
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
	 * randomly sets a new orientation for the LogMonster
	 */
	private void randomizeOrientation() {
		int probability = 60;
		int randomValue = RandomGenerator.getInstance().nextInt(100);
		
		if(randomValue<=probability) {
			orientate(Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
		}
	}
	
	/**
	 * defines the movement pattern for the LogMonster.
	 * innaction time is implemented here with the mobile and immobile time
	 * @param deltatime
	 */
	private void movementPattern(float deltatime) {
		if (remainingMobileTime>0) {
			moveOrientate(getOrientation());
			--remainingMobileTime ;
		}
		else if (remainingImmobileTime>0) {
			--remainingImmobileTime;
		}
		else {
			randomizeOrientation();
			remainingImmobileTime = RandomGenerator.getInstance().nextFloat()*
					(MAX_IMMOBILE_TIME-MIN_IMMOBILE_TIME)+MIN_IMMOBILE_TIME;
			remainingMobileTime = RandomGenerator.getInstance().nextFloat()*
					(MAX_MOBILE_TIME-MIN_MOBILE_TIME)+MIN_MOBILE_TIME;
		}
	}
	
	/**
	 * returns the field of view of the LogMonster
	 * @return list (List<DiscreteCoordinates>)
	 */
	private List<DiscreteCoordinates> idleFieldOfViewCells() {
		ArrayList<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
		list.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		for (int i=0 ; i<7 ; ++i) {
			list.add(list.get(i).jump(getOrientation().toVector()));
		}
		return list;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		if (state == State.ATTACKING) {
			return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		}
		return idleFieldOfViewCells();
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
	public boolean wantsCellInteraction() {
		return false;
	}
	
	@Override
	public boolean wantsViewInteraction() {
		if (!isDead() && (state == State.ATTACKING || state == State.IDLE)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (!isDead() && isVisible()) {
			switch(state) {
			case IDLE:
				idleAnimations[getOrientation().ordinal()].draw(canvas);
				break;
				
			case ATTACKING:
				idleAnimations[getOrientation().ordinal()].draw(canvas);
				break;
				
			case WAKING_UP:
				wakingUpAnimations.draw(canvas);
				break;
				
			default:
				sleepingAnimations.draw(canvas);
			}
		}
	}
	
	/** 
	 * interaction handler
	 */
	private class ARPGLogMonsterHandler implements ARPGInteractionVisitor {
		
		@Override
		public void interactWith(ARPGPlayer player) {
			if (state == State.ATTACKING) {
				player.hurt(2);
			} else {
				state = State.ATTACKING;
			}
		}
	}


	

}
