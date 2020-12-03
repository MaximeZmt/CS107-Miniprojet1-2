package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represent movable area entity wich have life points and therefore can die
 */
public abstract class Monster extends MovableAreaEntity implements Interactor, ItemDroppable {
	
	/**
	 * Enumeration of the monster's possible vulnerabilities
	 */
	public enum Vulnerability {
		PHYSICAL,
		MAGICAL,
		FIRE;
	}

	private final static int ANIMATION_DURATION = 8;
	private final static float BLINK_TIME = 0.05f; ///Time for one blink
	private final static float RECOVERY_TIME = 1f; ///Total blinking time


	
	private float maxLifePoints;
	private float lifePoints;
	private boolean isDead;
	private Sprite[] vanishSprites;
	private Animation vanishAnimations;
	private Vulnerability[] vulnerabilities;
	private CollectableAreaEntity droppedItem;
	private float remainingRecoveryTime;
	private float remainingBlinkTime;
	private boolean isVisible;
	private boolean isInjured;
	
	/**
	 * Default Constructor for Monster
	 * @param area (Area)
	 * @param orientation (Orientation)
	 * @param position (DiscreteCoordinates)
	 */
	public Monster(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		vanishSprites = RPGSprite.extractSprites("zelda/vanish", 7, 2, 2, this, 32, 32, new Vector(-0.5f, 0));
		vanishAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, vanishSprites, false);
		remainingRecoveryTime = RECOVERY_TIME;
		remainingBlinkTime = BLINK_TIME;
		isVisible = true;	
	}
	
	/**
	 * This method makes the monster blink when hurted
	 * @param deltaTime
	 */
	private void blinking(float deltaTime) {
		if (remainingBlinkTime<=0) {
			isVisible = !isVisible;
			remainingBlinkTime = BLINK_TIME;
		}
		if (remainingRecoveryTime<=0) {
			isVisible = true;
			isInjured = false;
			remainingRecoveryTime = RECOVERY_TIME;
		}
		remainingBlinkTime -= deltaTime;
		remainingRecoveryTime -= deltaTime;
	}
	
	/**
	 * Getter for isVisible
	 * @return isVisible (boolean)
	 */
	protected boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * Setter for isVisible
	 * @param isVisible (boolean)
	 */
	protected void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * Getter for maxLifePoints
	 * @return maxLifePoints (float)
	 */
	protected float getMaxLifePoints() {
		return maxLifePoints;
	}
	
	/**
	 * Setter for maxLifePoints
	 * @param maxLifePoints (float)
	 */
	protected void setMaxLifePoints(float maxLifePoints) {
		this.maxLifePoints = maxLifePoints;
	}
	
	/**
	 * Getter for lifePoints
	 * @return lifePoints (float)
	 */
	protected float getLifePoints() {
		return lifePoints;
	}
	
	/**
	 * Setter for lifePoints
	 * @param lifePoints
	 */
	protected void setLifePoints(float lifePoints) {
		this.lifePoints = lifePoints;
	}
	
	/**
	 * Sets the item to be dropped
	 * @param droppedItem (CollectableAreaEntity)
	 */
	protected void setDroppedItem(CollectableAreaEntity droppedItem) {
		this.droppedItem = droppedItem;
	}
	
	/**
	 * Checks if dead
	 * @return isDead (boolean)
	 */
	protected boolean isDead() {
		return isDead;
	}
	
	/**
	 * kills the current monster instance
	 */
	protected void die() {
		isDead = true;
	}
	
	/**
	 * Decrease Monsters life if the attack corresponds his vulnerabilities
	 * @param attacks (Vulnerability[])
	 * @param damage (float)
	 */
	public void takeDamage(Vulnerability[] attacks, float damage) {
		for (Vulnerability attack : attacks) {
			for (Vulnerability vulnerability : vulnerabilities) {
				if (attack == vulnerability) {
					lifePoints -= damage;	
					isInjured = true;
				}
			}
		}
		
	}

	/**
	 * getter for vulnerabilities
	 * @return
	 */
	protected Vulnerability[] getVulnerabilities() {
		return vulnerabilities;
	}
	
	/**
	 * setter for vulnerabilities
	 * @param vulnerabilities
	 */
	protected void setVulnerabilities(Vulnerability[] vulnerabilities) {
		this.vulnerabilities = vulnerabilities;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (lifePoints<=0) {
			die();
		}
		
		if (isInjured) {
			blinking(deltaTime);
		}
		
		if (isDead) { 
			/// Animation for death particle
			vanishAnimations.update(deltaTime);
			if (vanishAnimations.isCompleted()) {
				DiscreteCoordinates itemPosition = getCurrentMainCellCoordinates();
				getOwnerArea().unregisterActor(this);
				if (droppedItem != null) {
					droppedItem.setCurrentPosition(itemPosition.toVector());
					dropItem(droppedItem);
				}
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (isDead && !vanishAnimations.isCompleted()) {
			vanishAnimations.draw(canvas);
		}
	}

	@Override
	public boolean takeCellSpace() {
		return !isDead;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

}
