package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.areagame.actor.Monster.Vulnerability;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Projectile;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord.FireSpell;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;


/**
 * Arrow projectile 
 */
public class Arrow extends Projectile {

	private Sprite[] arrowSprites;
	private boolean hurtedMonster;
	private ARPGArrowHandler handler;
	
	/**
	 * Default constructor when arrow is thrown
	 * @param area
	 * @param orientation
	 * @param position
	 * @param range
	 * @param speed
	 */
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int range, int speed) {
		super(area, orientation, position, range, speed);
		arrowSprites = RPGSprite.extractSprites("zelda/arrow", 4, 1, 1, this , 32, 32);
		handler = new ARPGArrowHandler();
	}
	

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);				
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);						
	}

	@Override
	public void draw(Canvas canvas) {
		switch(getDirection()) {
		case UP:
			arrowSprites[0].draw(canvas);
			break;
		case RIGHT:
			arrowSprites[1].draw(canvas);
			break;
		case DOWN:
			arrowSprites[2].draw(canvas);
			break;
		case LEFT:
			arrowSprites[3].draw(canvas);
			break;
		}
	}
	
	/**
	 * Interaction Handler
	 */
	private class ARPGArrowHandler implements ARPGInteractionVisitor {
		
		@Override
		public void interactWith(Monster monster) {
			if (!hurtedMonster) {
				monster.takeDamage(new Vulnerability[] {Vulnerability.PHYSICAL}, 0.5f);
				setBlocked(true);
				hurtedMonster = true;
			}
		}
		
		@Override
		public void interactWith(Grass grass) {
			grass.slice();
		}
		
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
			setBlocked(true);
		}
		
		@Override
		public void interactWith(FireSpell fireSpell) {
			fireSpell.extinguish();
		} 
	}

	

}
