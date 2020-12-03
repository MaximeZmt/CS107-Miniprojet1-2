package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;


/**
 * Door that needed of being blown off to go across
 */
public class CaveDoor extends Door {
	
	private static Logic isOpen = Logic.FALSE;
	private Sprite openDoorSprite;
	private Sprite closeDoorSprite;
		
	/**
	 * default constructor
	 * @param destination
	 * @param otherSideCoordinates
	 * @param area
	 * @param orientation
	 * @param position
	 */
    public CaveDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area, Orientation orientation,
			DiscreteCoordinates position) {
    	super(destination, otherSideCoordinates, isOpen, area, orientation, position);
		openDoorSprite = new RPGSprite("zelda/Cave.open", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
		closeDoorSprite = new RPGSprite("zelda/Cave.close", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
    }

	@Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    	((ARPGInteractionVisitor)v).interactWith(this);
    }
	
	
    @Override
    public boolean takeCellSpace() {
        return isOpen.isOff();
    }

    @Override
    public boolean isCellInteractable() {
        return isOpen.isOn();
    }

    @Override
    public boolean isViewInteractable(){
        return isOpen.isOff();
    }
    
    @Override
    public void draw(Canvas canvas) {
    	if (isOpen.isOn()) {
    		openDoorSprite.draw(canvas);
    	}else {
    		closeDoorSprite.draw(canvas);
    	}
    	
    }
    
    @Override
    public boolean isOpen() {
    	return isOpen.isOn();
    }
    
    /**
     * setter to open the door
     */
    protected void open() {
    	isOpen = Logic.TRUE;
    }
    
    
    /**
     * setter to close the door
     */
    protected void close() {
    	isOpen = Logic.FALSE;
    }
 
    
}
