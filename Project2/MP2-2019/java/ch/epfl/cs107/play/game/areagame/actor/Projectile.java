package ch.epfl.cs107.play.game.areagame.actor;

import java.util.Collections;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
/**
 * Represent all shootable entity (projectile)
 */
public abstract class Projectile extends MovableAreaEntity implements FlyableEntity, Interactor{

	
	private DiscreteCoordinates initialPosition;
	private int range;
	private boolean isBlocked;
	private int speed; /// the greater it gets the slower it will be
	private Orientation direction;
	
	/**
	 * Projectile constructor
	 * @param area
	 * @param orientation
	 * @param position
	 * @param range
	 * @param speed
	 */
	public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int range, int speed) {
		super(area, orientation, position);
		initialPosition = position;
		this.range = range;
		this.speed = speed;
		this.direction = orientation;
		
	}
	
	/**
	 * When the projectile is blocked.
	 * @param isBlocked
	 */
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	public Orientation getDirection() {
		return direction;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		/// All conditions to be unregistred 
		if (getCurrentMainCellCoordinates().x>=initialPosition.x+range 
				|| getCurrentMainCellCoordinates().x<=initialPosition.x-range
				|| getCurrentMainCellCoordinates().y>=initialPosition.y+range
				|| getCurrentMainCellCoordinates().y<=initialPosition.y-range
				|| isBlocked
				|| !getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(direction.toVector())))) {
			getOwnerArea().unregisterActor(this);
		} else {
			
			move(speed);
		}
	}


	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

}
