package ch.epfl.cs107.play.game.areagame.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * A collectable is an area entity that can be collected by the player
 * @see AreaEntity
 * @see Player
 */
public abstract class CollectableAreaEntity extends AreaEntity {

	/**
	 * Default Collectable constructor : same as AreaEntity
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
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


}
