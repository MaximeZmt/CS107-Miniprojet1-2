package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.areagame.actor.Monster.Vulnerability;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.Test;
import ch.epfl.cs107.play.game.arpg.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.collectable.WaterStaff;
import ch.epfl.cs107.play.game.arpg.gui.ARPGInventoryGUI;
import ch.epfl.cs107.play.game.arpg.gui.ARPGPlayerStatusGUI;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * represent a player of an ARPG Game
 */
public class ARPGPlayer extends Player implements Inventory.Holder, Test{
	
	///different state of the player
	public enum State{
		IDLE,
		ATTACKING_BOW,
		ATTACKING_SWORD,
		ATTACKING_STAFF,
		INVENTORY_OPEN,
		SHOPPING,
		BLOCKED;
	}
	
	
	private final static int ANIMATION_DURATION = 8; 
	private final static ARPGItem[] EXISTING_ITEM = ARPGItem.values();
	private final static float MAX_HP = 5;
	private final static float RECOVERY_TIME = 2.5f;
	private final static float BLINK_TIME = 0.05f;
	final ARPGPlayer PLAYER = this;
	private final Keyboard keyb;
	private final ARPGPlayerHandler handler;

	
	/// Sprite and animation variable for different state
	private Sprite[][] idleSprites;
	private Sprite[][] swordSprites;
	private Sprite[][] bowSprites;
	private Sprite[][] waterStaffSprites;
	private Animation[] idleAnimations;
	private Animation[] swordAnimations;
	private Animation[] bowAnimations;
	private Animation[] waterStaffAnimations;
	
	/// Player inventory and its GUI
	private ARPGInventory myInventory;
	private ARPGPlayerStatusGUI gui;
	private ARPGItem currentItem;
	private int counterChangeItem;
	private ARPGInventoryGUI myInvGUI;
	private boolean GUIVisible;
	
	/// Recovery
	private boolean isInjured;
	private float remainingRecoveryTime;
	private float remainingBlinkTime;
	private boolean visible;
	private float healthPoint;
	
	/// Pet
	private Pet myPet;
	private DisabledMew petSpawner;	
	
	/// Player current state
	private State currentState;
	
	/// booleans for unique interaction
	private boolean attacking;
	private boolean hurtedMonster;
	private boolean hasShooted;
	
	/// Other
	private Door deathDoor;
	private ARPGDialogManager dialogManager;
	

	
	/**
	 * default constructor
	 * @param area
	 * @param orientation
	 * @param coordinates
	 */
	public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);	
		/// GUI for Money display, current item display and life
		gui = new ARPGPlayerStatusGUI();
				
		/// animation + sprite
		idleSprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
		swordSprites = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
		bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
		waterStaffSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32, new Vector(-0.5f, 0), new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, idleSprites);
		swordAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, swordSprites, false);
		bowAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, bowSprites, false);
		waterStaffAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, waterStaffSprites, false);
		
		handler = new ARPGPlayerHandler();
		keyb = getOwnerArea().getKeyboard();
		healthPoint = MAX_HP;
		remainingRecoveryTime = RECOVERY_TIME;
		remainingBlinkTime = BLINK_TIME;
		
		//for GUI Inventory
		GUIVisible = false;
		counterChangeItem = 0;
		myInventory = new ARPGInventory(50);
		myInvGUI = new ARPGInventoryGUI(this,myInventory, keyb);
		if(Test.MODE) {
			myInventory.addItem(ARPGItem.BOMB, 10);
			myInventory.addItem(ARPGItem.ARROW, 10);
			myInventory.addItem(ARPGItem.BOW, 1);
			myInventory.addItem(ARPGItem.KEY, 1);
			myInventory.addItem(ARPGItem.WATER_STAFF, 1);
		}
		myInventory.addItem(ARPGItem.SWORD, 1);
		
		/// 
		isInjured = false;
		visible = true;
		currentState = State.IDLE;
		
		reloadCurrentItem();
		resetMotion();
		
		dialogManager = new ARPGDialogManager(this,keyb);
		
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return (keyb.get(Keyboard.E).isPressed() || currentState == State.ATTACKING_SWORD);
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);		
	}

	@Override
	public boolean takeCellSpace() {
		return true;
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
		dialogManager.draw(canvas, getOwnerArea());
		
		if (visible) {
			switch(currentState) {
			case ATTACKING_BOW:
				bowAnimations[getOrientation().ordinal()].draw(canvas);
				break;
				
			case ATTACKING_STAFF:
				waterStaffAnimations[getOrientation().ordinal()].draw(canvas);
				break;
			
			case ATTACKING_SWORD:
				swordAnimations[getOrientation().ordinal()].draw(canvas);
				break;
				
			default:
				idleAnimations[getOrientation().ordinal()].draw(canvas);
				break;
			}
		}
		
		///When press enter select Item in inventory GUI and close it
		if(keyb.get(Keyboard.ENTER).isPressed()&&GUIVisible) {
			myInvGUI.selector();
			myInvGUI.draw(canvas,PLAYER);
			GUIVisible = false;
			currentState = State.IDLE;
		}
		/// if GUI Inv is Displayed
		if (GUIVisible) {
			myInvGUI.draw(canvas,PLAYER);
		}
		
		/// 
		gui.draw(canvas);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		///player can move if inventory is closed and not blocked
		if(currentState != State.INVENTORY_OPEN && currentState != State.BLOCKED) {
			moveOrientate(keyb.get(Keyboard.A), Orientation.LEFT);
			moveOrientate(keyb.get(Keyboard.D), Orientation.RIGHT);
			moveOrientate(keyb.get(Keyboard.W), Orientation.UP);
			moveOrientate(keyb.get(Keyboard.S), Orientation.DOWN);
		}
		
		if (currentState == State.IDLE) {
			if(!isDisplacementOccurs()) {
				idleAnimations[getOrientation().ordinal()].reset();
			}
			idleAnimations[getOrientation().ordinal()].update(deltaTime);
		}
		
		if (attacking) {
			switch (getCurrentItem()) {
			case SWORD:
				currentState = State.ATTACKING_SWORD;
				swordAnimations[getOrientation().ordinal()].update(deltaTime);
				if (swordAnimations[getOrientation().ordinal()].isCompleted()) {
					swordAnimations[getOrientation().ordinal()].reset();
					currentState = State.IDLE;
					attacking = false;
					hurtedMonster = false;
				}
				break;
				
			case BOW:
				currentState = State.ATTACKING_BOW;
				bowAnimations[getOrientation().ordinal()].update(deltaTime);
				if (bowAnimations[getOrientation().ordinal()].isCompleted()) {
					bowAnimations[getOrientation().ordinal()].reset();
					currentState = State.IDLE;
					attacking = false;
					hasShooted = false;
					hurtedMonster = false;
				}
				break;
				
			case WATER_STAFF:
				currentState = State.ATTACKING_STAFF;
				waterStaffAnimations[getOrientation().ordinal()].update(deltaTime);
				if (waterStaffAnimations[getOrientation().ordinal()].isCompleted()) {
					waterStaffAnimations[getOrientation().ordinal()].reset();
					currentState = State.IDLE;
					attacking = false;
					hasShooted = false;
					hurtedMonster = false;
				}
				break;
				
			default:
				currentState = State.IDLE;
				attacking = false;
				hurtedMonster = false;
				if (!myInventory.isInsideInventory(getCurrentItem())) {
					reloadCurrentItem();
				}
			}
		}
		
		
		
		
		
		///MONEY GUI UPDATE
		gui.moneyDisplayUpdate(myInventory.getMoney());
		
		///Monster debug
		if (keyb.get(Keyboard.K).isPressed()&&Test.MODE) {
			hurt(5);
		}
		
		if (keyb.get(Keyboard.F).isPressed()&&Test.MODE) {
			placeFlameSkull();
		}
		
		if (keyb.get(Keyboard.C).isPressed()&&Test.MODE) {
			placeLogMonster();
		}
		
		if (keyb.get(Keyboard.Q).isPressed()&&Test.MODE) {
			placeDarkLord();
		}
		
		
		///Switch for opening/closing Inventory GUI
		if(keyb.get(Keyboard.I).isPressed()) {
			if(currentState == State.IDLE) {
				GUIVisible = true;
				currentState = State.INVENTORY_OPEN;
			}else if (currentState==State.INVENTORY_OPEN||currentState==State.SHOPPING){
				GUIVisible = false;
				currentState = State.IDLE;
			}
		}
		

		if(keyb.get(Keyboard.SPACE).isPressed()&&(currentState!=State.INVENTORY_OPEN && currentState!=State.SHOPPING && currentState!=State.BLOCKED)) {
			attacking = true;
			useItem();
		}
		
		if(keyb.get(Keyboard.TAB).isPressed()) {
			changeCurrentItem();
		}
		
		if (isInjured) {
			recovery(deltaTime);
		}
		
		/// if player has a pet, send him my current area
		if(myPet!=null) {
			myPet.myPlayerArea(getOwnerArea());
		}
		
		/// send current life to gui
		gui.updateLifeBar(healthPoint, MAX_HP);
		
		///When adopting a pet
		if (petSpawner != null) {
			if(petSpawner.isAnimCompleted()) {
				petSpawner = null;
				myPet = new Pet(getOwnerArea(),PLAYER);
				getOwnerArea().registerActor(myPet);
			}
		}
		
		//DEATH, teleportation to home
		if (healthPoint<=0) {
			healthPoint = MAX_HP;
			deathDoor = new Door("PetalburgTimmy", new DiscreteCoordinates(4, 4), Logic.TRUE, getOwnerArea(), Orientation.DOWN, this.getCurrentCells().get(0));
			setIsPassingADoor(deathDoor);
		}
		
		
		
		
		
	}
	
	
	///ONLY FOR DEBUG
	private void placeFlameSkull() {
		getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), Orientation.LEFT,
				getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
	}
	
	private void placeLogMonster() {
		getOwnerArea().registerActor(new LogMonster(getOwnerArea(), Orientation.DOWN,
				getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
	}
	
	private void placeDarkLord() {
		getOwnerArea().registerActor(new DarkLord(getOwnerArea(), Orientation.DOWN,
				getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
	}
	///END ONLY FOR DEBUG
	
	/// Spawn Entity
	private void placeArrow() {
		getOwnerArea().registerActor(new Arrow(getOwnerArea(), getOrientation(),
				getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 5, 3));
	}
	
	private void placeMagicWaterProjectile() {
		getOwnerArea().registerActor(new MagicWaterProjectile(getOwnerArea(), getOrientation(),
				getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 5, 3));
	}
	
	
	/**
	 * When player is hurted, invincibility 
	 * @param deltaTime
	 */
	private void recovery(float deltaTime) {
		if (remainingBlinkTime<=0) {
			visible = !visible;
			remainingBlinkTime = BLINK_TIME;
		}
		if (remainingRecoveryTime<=0) {
			visible = true;
			isInjured = false;
			remainingRecoveryTime = RECOVERY_TIME;
		}
		remainingBlinkTime -= deltaTime;
		remainingRecoveryTime -= deltaTime;
	}
	
	/**
	 * Move and orientate the player
	 * @param b
	 * @param orient
	 */
	private void moveOrientate(Button b, Orientation orient) {
		if (b.isDown()) {
			if(getOrientation()==orient) {
				move(ANIMATION_DURATION);
			} else {
				orientate(orient);
			}
		}
	}

	@Override
	public boolean possess(InventoryItem item) {
		return myInventory.isInsideInventory(item); 
	}
	
	/**
	 * reload currently selected item
	 */
	public void reloadCurrentItem() {
		int avoided = 0;
		int counter = 0;
		/// count number of elements in inventory
		for (ARPGItem item:EXISTING_ITEM) {
			if (possess(item)) {
				++counter;
			}
		}
		/// if the number of inventory has decresed, adapt the selector
		counterChangeItem = counterChangeItem%counter;
		/// Find the current selected item
		for (ARPGItem item:EXISTING_ITEM) {
			if(possess(item)) {
				if(avoided<counterChangeItem) {
					++avoided;
				}
				else {
					currentItem = item;
					// for test purpose print new item selected
					if(Test.MODE) {
					}
					
					///send to GUI new item selected
					gui.changeItemDisplayGraphics(currentItem);
					break;
				}
			}
		}
	}
	
	/**
	 * getter for current item
	 * @return
	 */
	public ARPGItem getCurrentItem() {
		return currentItem;
	}
	
	/**
	 * use the current selected item and implementation for its use
	 * @return
	 */
	private boolean useItem(){
		switch(getCurrentItem()) {
		case BOMB:
			Bomb b = new Bomb(this.getOwnerArea(), Orientation.DOWN, this.getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
			if (getOwnerArea().canEnterAreaCells(b, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))){
				if (myInventory.useItem(ARPGItem.BOMB)) {
					getOwnerArea().registerActor(b);
				}
			}
			break;
			
		case BOW:
			if (!hasShooted) {
				if (myInventory.useItem(ARPGItem.ARROW)) {
					placeArrow();
					hasShooted = true;
				}
				
			}
			break;
			
		case WATER_STAFF:
			if (!hasShooted) {
				placeMagicWaterProjectile();
				hasShooted = true;
			}
			break;
			
		default:
			break;
		}		
		return false;
	}

	/**
	 * When use tab to select next item, add +1 to the selector.
	 */
	private void changeCurrentItem() {
		counterChangeItem++;
		reloadCurrentItem();
	}
	
	
	/**
	 * hurt the player
	 * @param damage (float): give the amount of lost life
	 */
	public void hurt(float damage) {
		if(!isInjured) {
			isInjured = true;
			if (healthPoint-damage<0) {
				healthPoint = 0;
			}
			else {
				healthPoint -= damage;	
			}
		}
	}
	
	/**
	 * heal the player of a certain amount
	 * @param health
	 */
	public void heal(float health) {
		if (healthPoint+health > MAX_HP) {
			healthPoint = MAX_HP;
		}else {
			healthPoint += health;
		}
		
	}
	
	/**
	 * method used by the graphic interface (extension) to force 
	 * the selection of a certain item with it's index 
	 * @param select
	 */
	public void forceCurrentItem(int select) {
		counterChangeItem = select;
		reloadCurrentItem();
	}
	
	/**
	 * getter for money (used by GUI)
	 * @return
	 */
	public int getMoney() {
		return myInventory.getMoney();
	}
	
	/**
	 * method used to pay a shop keeper
	 * @param money
	 * @return
	 */
	public boolean pay(int money) {
		return myInventory.pay(money);
	}
	
	
	/**
	 * method that a shop keeper call to give the player the item that he bought
	 * @param item
	 */
	public void buyItemReception(ARPGItem item) {
		if(!myInventory.addItem(item, 1)) {
			/// if overweight and cannot add item, refund
			myInventory.addmoney(item.getPrice()); 
		}
	}

	/**
	 * add a text dialog to the queue of the dialog manager
	 * @param message
	 */
	public void talkTo(String message) {
		dialogManager.addDialog(message);
	}
	
	/**
	 * know if the player has a dialog displayed
	 * @return
	 */
	public boolean leftTalking() {
		return dialogManager.finishTalking();
	}
	
	/**
	 * change state of player and force block him.
	 */
	public void block() {
		currentState = State.BLOCKED;
		idleAnimations[getOrientation().ordinal()].reset();
	}
	
	/**
	 * change state of player and force unblock him
	 */
	public void unBlock() {
		currentState = State.IDLE;
	}
	
	private class ARPGPlayerHandler implements ARPGInteractionVisitor{
		
		@Override
		public void interactWith(CastleDoor cDoor) {
			if (keyb.get(Keyboard.E).isPressed()) {
				if (possess(ARPGItem.KEY)&&!cDoor.isOpen()) {
					cDoor.open();
				} 
				
			} else if (cDoor.isOpen()) {
				setIsPassingADoor(cDoor);
				cDoor.close();
			}
		}
		
		@Override
		public void interactWith(Door door) {
			setIsPassingADoor(door);
		}
		
		@Override
		public void interactWith(Grass grass) {
			if (currentState != State.IDLE || Test.MODE) {
				grass.slice();
			}
		}
		
		@Override
		public void interactWith(Coin coin) {
			myInventory.addmoney(50);	
			getOwnerArea().unregisterActor(coin);
		}
		
		@Override
		public void interactWith(Heart heart) {
			heal(1);
			getOwnerArea().unregisterActor(heart);
		}
		
		@Override
		public void interactWith(CastleKey cKey) {
			myInventory.addItem(ARPGItem.KEY, 1);
			getOwnerArea().unregisterActor(cKey);
		}
		
		@Override
		public void interactWith(Monster monster) {
			if (currentState == State.ATTACKING_SWORD && !hurtedMonster && !keyb.get(Keyboard.E).isPressed()) {
				monster.takeDamage(new Vulnerability[] {Vulnerability.PHYSICAL}, 1.f);
				hurtedMonster = true;
			}
		}	
		
		@Override
		public void interactWith(Pet pet) {
			pet.moveFromMe();
			if(pet.getOrientation()== getOrientation().opposite() && keyb.get(Keyboard.E).isPressed()) {
				dialogManager.addDialog("Meeeeeewww!!");
			}
		}
		
		@Override
		public void interactWith(CaveDoor caveDoor) {
			if(caveDoor.isOpen()) {
				setIsPassingADoor(caveDoor);
			}
		}
		
		@Override
		public void interactWith(ShopKeeper shopKeeper) {
			if(keyb.get(Keyboard.E).isPressed() && currentState!=State.BLOCKED) {
				shopKeeper.openShop(PLAYER);
				currentState = State.INVENTORY_OPEN;
			}
		}
		
		@Override
		public void interactWith(DisabledMew disabledMew) {
			disabledMew.enableIt();
			petSpawner = disabledMew;
		}
		
		@Override
		public void interactWith(Bomb bomb) {
			if (currentState == State.ATTACKING_SWORD) {
				bomb.explode();
			}
		}
		
		@Override
		public void interactWith(Bridge bridge) {
			if(myPet!=null || Test.MODE) {
				bridge.build();
			}
		}
		
		@Override
		public void interactWith(WaterStaff waterStaff) {
			myInventory.addItem(ARPGItem.WATER_STAFF, 1);
			getOwnerArea().unregisterActor(waterStaff);
		}
		
	}
}
