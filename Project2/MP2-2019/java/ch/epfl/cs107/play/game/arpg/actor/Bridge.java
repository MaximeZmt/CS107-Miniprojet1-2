package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Bridge Entity that is built on the road if interract with mew
 */
public class Bridge extends AreaEntity {

	private boolean isEnable = false;
	private Sprite bridgeSprite;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public Bridge(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		bridgeSprite = new Sprite("zelda/bridge", 4, 3, this, new RegionOfInterest(0, 0, 64, 48));
		bridgeSprite.setDepth(-10);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		ArrayList<DiscreteCoordinates> toBeReturn = new ArrayList<DiscreteCoordinates>();
		toBeReturn.add(new DiscreteCoordinates(16, 10));
		toBeReturn.add(new DiscreteCoordinates(17, 10));
		return toBeReturn;
	}

	@Override
	public boolean takeCellSpace() {
		return !isEnable;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return !isEnable;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);	
	}

	@Override
	public void draw(Canvas canvas) {
		if (isEnable) {
			bridgeSprite.draw(canvas);
		}
		
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	/**
	 * Used by the player to construct the bridge
	 */
	public void build() {
		isEnable = true;
	}
	
}
