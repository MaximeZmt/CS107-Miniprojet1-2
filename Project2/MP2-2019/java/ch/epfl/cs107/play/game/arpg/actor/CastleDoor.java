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
 * Door that need the interactor own a key to open it.
 */
public class CastleDoor extends Door {
	
	private static Logic isOpen = Logic.FALSE;
	private Sprite openDoorSprite;
	private Sprite closeDoorSprite;
	
	/**
	 * Constructor used if door contains more than one cell
	 * @param destination
	 * @param otherSideCoordinates
	 * @param area
	 * @param orientation
	 * @param position
	 * @param otherCells (DiscreteCoordinates[]): used to precise other coordinates of the door
	 */
	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area,
			Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates[] otherCells) {
		super(destination, otherSideCoordinates, isOpen, area, orientation, position, otherCells);
		openDoorSprite = new RPGSprite("zelda/castleDoor.open", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
		closeDoorSprite = new RPGSprite("zelda/castleDoor.close", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
	}
	
	/**
	 * default constructor of a CastleDoor composed of one cell
	 * @param destination
	 * @param otherSideCoordinates
	 * @param area
	 * @param orientation
	 * @param position
	 * @param discreteCoordinates
	 */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area, Orientation orientation,
			DiscreteCoordinates position, DiscreteCoordinates discreteCoordinates) {
    	super(destination, otherSideCoordinates, isOpen, area, orientation, position, discreteCoordinates);
		openDoorSprite = new RPGSprite("zelda/castleDoor.open", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
		closeDoorSprite = new RPGSprite("zelda/castleDoor.close", 2, 2, this, new RegionOfInterest(0, 0, 32, 32));
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
     * setter for opening the door
     */
    protected void open() {
    	isOpen = Logic.TRUE;
    }
    
    /**
     * setter for closing the door
     */
    protected void close() {
    	isOpen = Logic.FALSE;
    }
    
}
