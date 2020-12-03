package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.ItemDroppable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
 /**
  * represents a grass that can be cut and can drop an item
  */
public class Grass extends AreaEntity implements ItemDroppable {

	private final static int ANIMATION_DURATION = 4;
	private final static double PROBABILITY_TO_DROP_ITEM = 0.5;
	private final static double PROBABILITY_TO_DROP_HEART = 0.5;
	
	private Sprite grassSprite;
	private boolean isSliced;
	private Sprite[] slicedSprites;
	private Animation animations;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		grassSprite = new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
		slicedSprites = RPGSprite.extractSprites("zelda/grass.sliced", 4, 2, 2, this , 34, 34); //XXX YES we can change the size of the grass if it's better
		animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, slicedSprites, false);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return !isSliced;
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

	@Override
	public void draw(Canvas canvas) {
		if (!isSliced) {
			grassSprite.draw(canvas);
		}
		else if (!animations.isCompleted()) {
			animations.draw(canvas);
		}
	}
	
	/**
	 * cuts the grass
	 */
	public void slice() {
		if(!isSliced) {
			isSliced = true;
			if (RandomGenerator.getInstance().nextDouble()<PROBABILITY_TO_DROP_ITEM) {
				if (RandomGenerator.getInstance().nextDouble()<PROBABILITY_TO_DROP_HEART) {
					dropItem(new Heart(getOwnerArea(), Orientation.DOWN,getCurrentMainCellCoordinates()));
				} else {
					dropItem(new Coin(getOwnerArea(), Orientation.DOWN,getCurrentMainCellCoordinates()));
					
				}
			}
			
		}
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (animations.isCompleted()) {
			getOwnerArea().unregisterActor(this);
			if(getOwnerArea().canEnterAreaCells(this, getCurrentCells())) {
			}else {
			}
		}

		if (isSliced) {
			animations.update(deltaTime);
		}
	}
	
	
	

}
