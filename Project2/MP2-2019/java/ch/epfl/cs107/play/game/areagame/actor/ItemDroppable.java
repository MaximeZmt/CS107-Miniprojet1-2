package ch.epfl.cs107.play.game.areagame.actor;

/**
 * Represents any object that can drop a collectable item
 */
public interface ItemDroppable {
	
	/**
	 * drops in the current area an item
	 * @param item (Collectable): item to be dropped
	 */
	default void dropItem(CollectableAreaEntity item) {
		if (item != null) {
			item.getOwnerArea().registerActor(item);
		}
	}
}
