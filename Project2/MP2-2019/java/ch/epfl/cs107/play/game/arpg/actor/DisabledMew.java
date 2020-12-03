package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * mew that is not yet adopted
 */
public class DisabledMew extends AreaEntity{

	private Sprite pokeballSprite;
	private Sprite pokeballSprite2;
	private int counter=0;
	private boolean animIsCompleted;
	private Sprite[] vanishSprites;
	private Animation vanishAnimations;
	private final static int ANIMATION_DURATION = 8;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public DisabledMew(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		pokeballSprite = new RPGSprite("Inball", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
		pokeballSprite2 = new RPGSprite("Inball.2", 1, 1, this, new RegionOfInterest(0, 0, 16, 16));
		animIsCompleted = false;
		vanishSprites = RPGSprite.extractSprites("zelda/vanish", 7, 2, 2, this, 32, 32,new Vector(-0.5f,0));
		vanishAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, vanishSprites, false);
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

	/**
	 * checks if the animations of the pokeball is completed
	 * @return animIsCompleted (boolean)
	 */
	public boolean isAnimCompleted() {
		return animIsCompleted;
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		if (counter>0) {
			if((counter/5000)%2==0) {
				pokeballSprite.draw(canvas);
			}else {
				pokeballSprite2.draw(canvas);
			}
			counter = counter + (counter/2);
			if (counter>900000) {
				counter = 900001;
				animIsCompleted = true;
				vanishAnimations.draw(canvas);
				if(vanishAnimations.isCompleted()) {
					getOwnerArea().unregisterActor(this);
				}	
			}
		} else {
			pokeballSprite.draw(canvas);
		}
		
	}
	
	/**
	 * enables the mew
	 */
	public void enableIt() {
		counter = 2;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(animIsCompleted) {
			vanishAnimations.update(deltaTime);
		}
	}
}
