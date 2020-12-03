package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.areagame.actor.Monster.Vulnerability;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;


/**
 * Bomb entity that damage his cell and its neighbours
 */
public class Bomb extends AreaEntity implements Interactor {
	
	private final static int ANIMATION_DURATION = 2;
	
	private final static int COUNTDOWN = 5*24;
	
	
	private int counter; 
	private boolean hasExploded;
	private Sprite bombSprite;
	private ARPGBombHandler handler;
	private Sprite[] explosionSprites;
	private Animation animations;
	private boolean hurtedMonster;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		counter = COUNTDOWN;
		bombSprite = new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
		handler = new ARPGBombHandler();
		explosionSprites =  RPGSprite.extractSprites("zelda/explosion", 7, 1, 1, this, 32, 32);
		animations = RPGSprite.createAnimations(ANIMATION_DURATION, explosionSprites, false);
	}
	
	/**
	 * make the bomb exploding, is called when countdown is finished or with some interraction
	 */
	public void explode() {
		hasExploded = true;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> coordinates = getCurrentMainCellCoordinates().getNeighbours();
		coordinates.add(getCurrentMainCellCoordinates());
		return coordinates;
	}
	

	@Override
	public boolean takeCellSpace() {
		// in our implementation the bomb does not take a cell space
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
		if (!hasExploded) {
			bombSprite.draw(canvas); 
		} else {
			if (!animations.isCompleted()) {
				animations.draw(canvas);
			}
		}
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (!hasExploded) {
			if (counter>0) {
				--counter;
			} else {
				explode();
			}
		} else {
			animations.update(deltaTime);
		}
		if (animations.isCompleted()) {
			getOwnerArea().unregisterActor(this);
		}
	}
	

	@Override
	public boolean wantsCellInteraction() {
		if (hasExploded) {
			return true;
		}
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		if (hasExploded) {
			return true;
		}
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);		
	}
	
	/**
	 * Interraction Handler
	 */
	private class ARPGBombHandler implements ARPGInteractionVisitor{ 
		
		@Override
		public void interactWith(Grass grass) { 
			grass.slice();
		}
		
		@Override
		public void interactWith(Monster monster) {
			if (!hurtedMonster) {
				monster.takeDamage(new Vulnerability[] {Vulnerability.PHYSICAL, Vulnerability.FIRE}, 1f);
				hurtedMonster = true;
			}
		}
		
		@Override
		public void interactWith(ARPGPlayer player) {
			player.hurt(1);
		}
		@Override
		public void interactWith(CaveDoor caveDoor) {
			if(!caveDoor.isOpen()) {
				caveDoor.open();
			}
		}
	}	
}