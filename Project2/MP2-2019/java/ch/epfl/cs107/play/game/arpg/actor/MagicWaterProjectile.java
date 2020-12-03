package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
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
 * magic projectile that comes from a water staff
 */
public class MagicWaterProjectile extends Projectile {
	
	private static final int ANIMATION_DURATION = 8;

	private Sprite[] magicWaterProjectileSprites;
	private Animation animations;
	private boolean hurtedMonster;
	private ARPGMagicWaterProjectileHandler handler;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 * @param range
	 * @param speed
	 */
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int range, int speed) {
		super(area, orientation, position, range, speed);
		magicWaterProjectileSprites = RPGSprite.extractSprites("zelda/magicWaterProjectile", 4, 1, 1, this , 32, 32);
		animations = RPGSprite.createAnimations(ANIMATION_DURATION/2, magicWaterProjectileSprites);
		handler = new ARPGMagicWaterProjectileHandler();	
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		animations.update(deltaTime);
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
		animations.draw(canvas);
	}
	
	/**
	 * interaction handler
	 */
	private class ARPGMagicWaterProjectileHandler implements ARPGInteractionVisitor {
		
		@Override
		public void interactWith(Monster monster) {
			if (!hurtedMonster) {
				monster.takeDamage(new Vulnerability[] {Vulnerability.MAGICAL}, 1f);
				hurtedMonster = true;
				setBlocked(true);
			}
		}
		
		@Override
		public void interactWith(FireSpell fireSpell) {
			fireSpell.extinguish();
		}
	}

	

}
