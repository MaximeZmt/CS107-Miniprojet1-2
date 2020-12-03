package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * monster that can summon fire, flame skulls and can teleport
 * @see Monster
 * @see FlameSkull
 */
public class DarkLord extends Monster {
	
	/**
	 * possible state of the DarkLord
	 */
	private enum State {
		IDLE,
		ATTACKING,
		SUMMONING,
		CASTING_TELEPORTATION_SPELL,
		TELEPORTING;
	}

	private static final int ANIMATION_DURATION = 8;
	private static final int FIELD_OF_VIEW_RADIUS = 3;
	
	/// spells constants
	private static final int MIN_SPELL_WAIT_DURATION = 50;
	private static final int MAX_SPELL_WAIT_DURATION = 70;
	private static final int MAX_FIRE_STRENGTH = 5;
	private static final int MAX_TELEPORTATION_RADIUS = 10;

	/// innaction time constants
	private static final float MIN_IMMOBILE_TIME = 5;
	private static final float MAX_IMMOBILE_TIME = 20;
	private static final float MIN_MOBILE_TIME = 5;
	private static final float MAX_MOBILE_TIME = 20;
	
	private static final float MAX_HP = 10;
	
	private float remainingImmobileTime;
	private float remainingMobileTime;
	private boolean hasAttacked;
	
	/// animations and sprites
	private Sprite[][] idleSprites;
	private Sprite[][] attackingSprites;
	private Animation[] idleAnimations;
	private Animation[] attackingAnimations;
	
	/// spell test cycle
	private float n;
	
	private State state;
	private DiscreteCoordinates teleportedPosition;
	private ARPGDarkLordHandler handler;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
		remainingImmobileTime = RandomGenerator.getInstance().nextFloat()*
				(MAX_IMMOBILE_TIME-MIN_IMMOBILE_TIME)+MIN_IMMOBILE_TIME;
		remainingMobileTime = RandomGenerator.getInstance().nextFloat()*
				(MAX_MOBILE_TIME-MIN_MOBILE_TIME)+MIN_MOBILE_TIME;
		
		state = State.IDLE;
		
		idleSprites = RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]
				{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
		attackingSprites = RPGSprite.extractSprites("zelda/darkLord.spell", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[]
				{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, idleSprites);
		attackingAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, attackingSprites, false);
		n = RandomGenerator.getInstance().nextFloat()*
				(MAX_SPELL_WAIT_DURATION-MIN_SPELL_WAIT_DURATION)+MIN_SPELL_WAIT_DURATION;
		handler = new ARPGDarkLordHandler();
		
		
		setVulnerabilities(new Vulnerability[] {Vulnerability.MAGICAL});
		setMaxLifePoints(MAX_HP);
		setLifePoints(MAX_HP);
		
		///drop CastleKey
		setDroppedItem(new CastleKey(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates()));
		
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		///every n cycle, orientates for casting a spell
		if (n>0) {
			--n;
		} else {
			n = RandomGenerator.getInstance().nextFloat()*
					(MAX_SPELL_WAIT_DURATION-MIN_SPELL_WAIT_DURATION)+MIN_SPELL_WAIT_DURATION;
			int probability = 50;
			int randomValue = RandomGenerator.getInstance().nextInt(100);
			if (randomValue<=probability) {
				
				state = State.ATTACKING;
			} else {
				state = State.SUMMONING;
			}
			orientateForSpell();
			
		}
		///
		
		
		switch(state) {
		case IDLE:
			movementPattern(deltaTime);
			if (isDisplacementOccurs()) {
				idleAnimations[getOrientation().ordinal()].update(deltaTime);	
			} else {
				idleAnimations[getOrientation().ordinal()].reset();
			}

			break;
			
		case ATTACKING:
			if (!hasAttacked) {
				if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))){
					getOwnerArea().registerActor(new FireSpell(getOwnerArea(), Orientation.DOWN,
							getCurrentMainCellCoordinates().jump(getOrientation().toVector()),
							MAX_FIRE_STRENGTH, getOrientation()));
					hasAttacked = true;
				}
				
			}
			if (attackingAnimations[getOrientation().ordinal()].isCompleted()) {
				attackingAnimations[getOrientation().ordinal()].reset();
				state = State.IDLE;
				hasAttacked = false;
			}
			attackingAnimations[getOrientation().ordinal()].update(deltaTime);

			break;
			
		case SUMMONING:
			if (!hasAttacked) {
				getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), Orientation.DOWN,
						getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
				hasAttacked = true;
			}
			if (attackingAnimations[getOrientation().ordinal()].isCompleted()) {
				attackingAnimations[getOrientation().ordinal()].reset();
				state = State.IDLE;
				hasAttacked = false;
			}
			attackingAnimations[getOrientation().ordinal()].update(deltaTime);

			break;
			
		case CASTING_TELEPORTATION_SPELL:
			if (!isDisplacementOccurs()) {
				if (attackingAnimations[getOrientation().ordinal()].isCompleted()) {
					attackingAnimations[getOrientation().ordinal()].reset();
					state = State.TELEPORTING;
				}
			}
			attackingAnimations[getOrientation().ordinal()].update(deltaTime);
			break;
			
		case TELEPORTING:
			teleportedPosition = new DiscreteCoordinates(
					getCurrentMainCellCoordinates().x-MAX_TELEPORTATION_RADIUS + RandomGenerator.getInstance().nextInt(2*MAX_TELEPORTATION_RADIUS),
					getCurrentMainCellCoordinates().y-MAX_TELEPORTATION_RADIUS + RandomGenerator.getInstance().nextInt(2*MAX_TELEPORTATION_RADIUS));
			if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(teleportedPosition))) {
				getOwnerArea().leaveAreaCells(this, getCurrentCells());
				setCurrentPosition(teleportedPosition.toVector());
				getOwnerArea().enterAreaCells(this, getCurrentCells());
				state = State.IDLE;
			}
			break;
		default:
		}
	}
	
	/**
	 * move and orienatates the DarkLord
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
	 * sets a random orientation for the DarkLord
	 */
	private void randomizeOrientation() {
		int probability = 60;
		int randomValue = RandomGenerator.getInstance().nextInt(100);
		
		if(randomValue<=probability) {
			orientate(Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
		}
	}
	
	/**
	 * defines the movement pattern of the DarkLord.
	 * the innaction time is implemented here with the immobile and mobile time
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

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return fieldOfViewCells(FIELD_OF_VIEW_RADIUS);
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
		if (!isDead()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (!isDead() && isVisible()) {
			switch(state) {
			case IDLE:
				idleAnimations[getOrientation().ordinal()].draw(canvas);
				break;
				
			case TELEPORTING:
				idleAnimations[getOrientation().ordinal()].draw(canvas);
				break;
				
			default:
				attackingAnimations[getOrientation().ordinal()].draw(canvas);
			}
		}
	}
	
	/**
	 * returns the field of view of the DarkLord
	 * @param radius
	 * @return list (List<DiscreteCoordinates>)
	 */
	private List<DiscreteCoordinates> fieldOfViewCells(int radius) {
		ArrayList<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
		for (int x=getCurrentMainCellCoordinates().x-radius ; x<=getCurrentMainCellCoordinates().x+radius ; ++x) {
			for (int y=getCurrentMainCellCoordinates().y-radius ; y<=getCurrentMainCellCoordinates().y+radius ; ++y) {
				list.add(new DiscreteCoordinates(x, y));
			}
		}
		return list;
	}
	
	/**
	 * orientate the DarkLord for casting a spell
	 */
	private void orientateForSpell() {
		Orientation possibleOrientation = Orientation.fromInt(RandomGenerator.getInstance().nextInt(4));
		if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(possibleOrientation.toVector())))) {
			orientate(possibleOrientation);
		}
	}
	
	/**
	 * fire spell of the DarkLord
	 */
	public class FireSpell extends AreaEntity implements Interactor {
		
		private static final float MIN_LIFE_TIME = 120; //120
		private static final float MAX_LIFE_TIME = 240; //240
		private static final float PROPAGATION_TIME_FIRE = 10;
		
		private float remainingPropagationTimeFire;
		private float lifeTime;
		private Orientation direction;
		private float strength;
		private Sprite[] fireSprites;
		private Animation animations;
		private ARPGFireSpellHandler handler;
		private boolean hasCreatedFire;

		/**
		 * default constructor
		 * @param area
		 * @param orientation
		 * @param position
		 * @param strength
		 * @param direction
		 */
		public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, float strength, Orientation direction) {
			super(area, orientation, position);
			remainingPropagationTimeFire = PROPAGATION_TIME_FIRE;
			lifeTime = RandomGenerator.getInstance().nextFloat()*(MAX_LIFE_TIME-MIN_LIFE_TIME)+MIN_LIFE_TIME;
			this.direction = direction;
			this.strength = strength;
			fireSprites = RPGSprite.extractSprites("zelda/fire", 7, 1, 1, this , 16, 16);
			animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, fireSprites);
			handler = new ARPGFireSpellHandler();

		}
		
		/**
		 * kills the fire
		 */
		public void extinguish() {
			lifeTime = 0;
		}
		
		@Override
		public void update(float deltaTime) {
			super.update(deltaTime);
			if (remainingPropagationTimeFire<=0 && strength>0 && !hasCreatedFire) {
				if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(direction.toVector())))) {
					getOwnerArea().registerActor(new FireSpell(getOwnerArea(), Orientation.DOWN,
							getCurrentMainCellCoordinates().jump(direction.toVector()), strength-1, direction));
					hasCreatedFire = true;
				}
			} else {
				--remainingPropagationTimeFire;
			}
			
			if (lifeTime<=0) {
				getOwnerArea().unregisterActor(this);
			} else {
				--lifeTime;
			}
			animations.update(deltaTime);
		}

		@Override
		public List<DiscreteCoordinates> getCurrentCells() {
			return Collections.singletonList(getCurrentMainCellCoordinates());
		}

		@Override
		public boolean takeCellSpace() {
			return false;
		}

		@Override
		public boolean isCellInteractable() {
			return true;
		}

		@Override
		public boolean isViewInteractable() {
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
			((ARPGInteractionVisitor)v).interactWith(this);		
		}

		@Override
		public void draw(Canvas canvas) {
			animations.draw(canvas);
		}

		@Override
		public List<DiscreteCoordinates> getFieldOfViewCells() {
			return Collections.singletonList(getCurrentMainCellCoordinates());
		}

		@Override
		public boolean wantsCellInteraction() {
			return true;
		}

		@Override
		public boolean wantsViewInteraction() {
			return false;
		}

		@Override
		public void interactWith(Interactable other) {
			other.acceptInteraction(handler);							
		}
		
		/**
		 * interaction handler
		 */
		private class ARPGFireSpellHandler implements ARPGInteractionVisitor { 
		
			@Override
			public void interactWith(Monster monster) {
				monster.takeDamage(new Vulnerability[] {Vulnerability.FIRE}, 0.5f);
			}
			
			@Override
			public void interactWith(ARPGPlayer player) {
				player.hurt(0.5f);
			}
			
			@Override
			public void interactWith(Grass grass) {
				grass.slice();
			}
			
			@Override
			public void interactWith(Bomb bomb) {
				bomb.explode();
			}
		}

		
	}

	/**
	 * interaction handler
	 */
	private class ARPGDarkLordHandler implements ARPGInteractionVisitor { 
		
		@Override
		public void interactWith(ARPGPlayer player) {
			if (state != State.TELEPORTING) {
				state = State.CASTING_TELEPORTATION_SPELL;
			}
		}
	}

	
}
