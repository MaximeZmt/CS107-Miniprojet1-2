package ch.epfl.cs107.play.game.areagame.actor;

/**
 * Represent entities that can fly  
 */
public interface FlyableEntity {
	
	/**
	 * indicates by default that the entity can fly
	 * @return (Boolean): true if the entity can fly
	 */
	default public boolean canfly() {
		return true;
	}
}
