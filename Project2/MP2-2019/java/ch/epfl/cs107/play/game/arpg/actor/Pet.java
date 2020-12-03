package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * represents a creature that follows the player
 */
public class Pet extends MovableAreaEntity implements FlyableEntity{
	
	private Sprite mewSpriteD;
	private Sprite mewSpriteU;
	private Sprite mewSpriteL;
	private Sprite mewSpriteR;
	private ARPGPlayer p;
	private static final int ANIMATION_DURATION = 8;
	
	/**
	 * default constructor
	 * @param a
	 * @param p
	 */
	public Pet(Area a, ARPGPlayer p) {
		super(a, p.getOrientation(), p.getCurrentCells().get(0).jump(0,-1));
		this.p = p;
		mewSpriteD = new Sprite("mew.fixed", 1, 1, this, new RegionOfInterest(0, 0, 16, 21));
		mewSpriteU = new Sprite("mew.fixed", 1, 1, this, new RegionOfInterest(32, 0, 16, 21));
		mewSpriteL = new Sprite("mew.fixed", 1, 1, this, new RegionOfInterest(16, 0, 16, 21));
		mewSpriteR = new Sprite("mew.fixed", 1, 1, this, new RegionOfInterest(48, 0, 16, 21));
		mewSpriteD.setDepth(1005);
		mewSpriteU.setDepth(1005);
		mewSpriteL.setDepth(1005);
		mewSpriteR.setDepth(1005);
	}

	@Override
	public void draw(Canvas canvas) {
		if(getOrientation()==Orientation.DOWN) {mewSpriteD.draw(canvas);}
		else if(getOrientation()==Orientation.LEFT) {mewSpriteL.draw(canvas);}
		else if(getOrientation()==Orientation.RIGHT) {mewSpriteR.draw(canvas);}
		else {mewSpriteU.draw(canvas);}
		
		
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		followP();
	}

	/**
	 * makes the pet follow the player
	 */
	private void followP() {
		double dist = distance(p.getPosition(),this.getPosition());
		if(dist>1.5 && dist<3) {
			orientate(p.getOrientation());
			move(ANIMATION_DURATION);
		}else if(dist>=3){
			orientate(p.getOrientation());
			if(getOwnerArea().canEnterAreaCells(this, Collections.singletonList(p.getCurrentCells().get(0).left()))) {
				setCurrentPosition(p.getCurrentCells().get(0).left().toVector());
			}	
		}
	}
	
	/**
	 * returns the distance betweeen the pet and the player;
	 * @param v1
	 * @param v2
	 * @return (double)
	 */
	private double distance (Vector v1, Vector v2) {
		Vector vfinal = new Vector(v1.x-v2.x,v1.y-v2.y);
		return Math.sqrt(vfinal.x*vfinal.x+vfinal.y*vfinal.y);
	}
	
	
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return p.getCurrentCells();
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
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
	}
	
	/**
	 * defines the pattern of the pet
	 */
	protected void moveFromMe() {	
		if(distance(p.getPosition(),this.getPosition())==0) {
			orientate(p.getOrientation());
			if(this.getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().left()))) {
				setCurrentPosition(p.getCurrentCells().get(0).left().toVector());
			}else if (this.getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().right()))) {
				setCurrentPosition(p.getCurrentCells().get(0).right().toVector());
			}else if (this.getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().up()))) {
				setCurrentPosition(p.getCurrentCells().get(0).up().toVector());
			}else if (this.getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().down()))) {
				setCurrentPosition(p.getCurrentCells().get(0).down().toVector());
			}else{
			}
			
		}
		
		
	}

	/**
	 * sets a new position for the pet
	 * @param a
	 */
	protected void myPlayerArea(Area a) {
		if(!a.getTitle().equals(getOwnerArea().getTitle())) {

			getOwnerArea().unregisterActor(this);

			setOwnerArea(a);

			this.setCurrentPosition(p.getPosition());

			a.registerActor(this);

		}
	}
	

	
	
}
