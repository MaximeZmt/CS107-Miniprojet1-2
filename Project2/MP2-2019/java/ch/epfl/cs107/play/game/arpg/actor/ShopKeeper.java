package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.gui.ARPGInventoryGUI;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * represents a AreaEntity with who you can buy stuff
 */
public class ShopKeeper extends AreaEntity{
	private Sprite[] sprites;
	private ARPGInventory shopInventory;
	private ARPGInventoryGUI guiInventory;
	private Keyboard keyb;
	private boolean isShopOpen;
	private ARPGPlayer pInteract;
	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param position
	 */
	public ShopKeeper(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sprites = RPGSprite.extractSpritesVert("zelda/character", 4, 1, 2, this, 16, 32);
		keyb = getOwnerArea().getKeyboard();
		shopInventory = new ARPGInventory(0);
		shopInventory.addItem(ARPGItem.BOW, 1);
		shopInventory.addItem(ARPGItem.BOMB, 1);
		shopInventory.addItem(ARPGItem.ARROW, 1);
		guiInventory = new ARPGInventoryGUI(this,shopInventory, keyb);
		isShopOpen = false;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		ArrayList<DiscreteCoordinates> tobeReturned = new ArrayList<DiscreteCoordinates>();
		tobeReturned.add(getCurrentMainCellCoordinates());
		tobeReturned.add(getCurrentMainCellCoordinates().down());
		return tobeReturned;
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
		if(isShopOpen) {
			guiInventory.draw(canvas, pInteract);
		}
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (keyb.get(Keyboard.I).isPressed()&&isShopOpen) {
			isShopOpen = false;
			pInteract.talkTo("Au revoir !");
		}
		
	}

	/**
	 * opens the shop
	 * @param p
	 */
	public void openShop(ARPGPlayer p) {
		orientate(p.getOrientation().opposite());
		isShopOpen = true;
		pInteract = p;
	}
	
	
}
