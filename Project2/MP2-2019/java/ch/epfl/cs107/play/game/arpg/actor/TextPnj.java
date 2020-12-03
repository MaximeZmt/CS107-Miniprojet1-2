package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * entities that can talk
 */
public class TextPnj extends MovableAreaEntity implements Interactor{

	private ARPGTextPnjHandler handler;
	private Sprite[] sprites;
	private final static int ANIMATION_DURATION = 8;
	private Logic hasAlreadyTalk;
	private String[] text;
	private boolean canMove;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 * @param canMove
	 * @param text
	 */
	public TextPnj(Area area, Orientation orientation, DiscreteCoordinates position,boolean canMove ,String... text) {
		super(area, orientation, position);
		handler = new ARPGTextPnjHandler();
		sprites = RPGSprite.extractSpritesVert("zelda/character", 4, 1, 2, this, 16, 32);
		hasAlreadyTalk = Logic.FALSE;
		this.text = text;
		this.canMove = canMove;
	}

	/**
	 * setter for the sprites
	 * @param sprites
	 */
	protected void setSprite(Sprite[] sprites) {
		this.sprites = sprites;
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
		
	}

	@Override
	public void draw(Canvas canvas) {
		sprites[getOrientation().ordinal()].draw(canvas);
		
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		ArrayList<DiscreteCoordinates> tobeReturned = new ArrayList<DiscreteCoordinates>();
		tobeReturned.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		tobeReturned.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().add(getOrientation().toVector())));
		return tobeReturned;
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return hasAlreadyTalk.isOff();
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);	
	}
	
	/**
	 * interface hanndler
	 */
	private class ARPGTextPnjHandler implements ARPGInteractionVisitor{ 
		@Override
		public void interactWith(ARPGPlayer player) {
			moveToPlayer(player);
		}
	}
	
	/**
	 * makes the pnj move towards the player
	 * @param player
	 */
	public void moveToPlayer(ARPGPlayer player) {
		double currentD = distance(player.getPosition(), this.getPosition());
		while(canMove){
			if(distance(player.getPosition(), this.getPosition().add(1,0))<currentD){// left
				orientate(Orientation.RIGHT);
				move(ANIMATION_DURATION);
				currentD = distance(player.getPosition(), this.getPosition().add(1,0));
			} 
			else if(distance(player.getPosition(), this.getPosition().add(-1,0))<currentD){ // right
				orientate(Orientation.LEFT);
				move(ANIMATION_DURATION);
				currentD = distance(player.getPosition(), this.getPosition().add(-1,0));
			}
			else if(distance(player.getPosition(), this.getPosition().add(0,1))<currentD){ // up
				orientate(Orientation.UP);
				move(ANIMATION_DURATION);
				currentD = distance(player.getPosition(), this.getPosition().add(0,1));
			}
			else if(distance(player.getPosition(), this.getPosition().add(0,-1))<currentD){ // down
				orientate(Orientation.DOWN);
				move(ANIMATION_DURATION);
				currentD = distance(player.getPosition(), this.getPosition().add(0,-1));
			}else {
				break;
			}
		}		
		for (String t:text) {
			player.talkTo(t);
		}
		hasAlreadyTalk = Logic.TRUE;

	}

	/**
	 * computes the distance between two vector
	 * @param v1
	 * @param v2
	 * @return (double)
	 */
	private double distance (Vector v1, Vector v2) {
		Vector vfinal = new Vector(v1.x-v2.x,v1.y-v2.y);
		return Math.sqrt(vfinal.x*vfinal.x+vfinal.y*vfinal.y);
	}
	

}
