package ch.epfl.cs107.play.game.arpg.gui;

import java.awt.Color;
import java.util.HashMap;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * inventory graphics interface
 */
public class ARPGInventoryGUI{

	/**
	 * possible types of graphical inventory
	 */
	public enum GUIInventoryType {
		BUYABLE,
		PLAYER;
	}
	
	private final static float DEPTH = 1010;
	private final static ARPGItem[] EXISTING_ITEM = ARPGItem.values(); 
	private ARPGInventory inventory;
	private HashMap<ARPGItem, Integer> mapStock;
	private int selector = 0;
	private int nbDiffItem = 0;
	private Keyboard keyb;
	private HashMap<ARPGItem, String> mapGraphics;
	private boolean selectedItem;
	private ARPGPlayer player;
	private GUIInventoryType state;
	private Vector anchor;
	private TextGraphics textGraphics;


	/**
	 * constructor for the player
	 * @param owner
	 * @param inventory
	 * @param keyb
	 */
	public ARPGInventoryGUI(ARPGPlayer owner, ARPGInventory inventory, Keyboard keyb) {
		this.inventory = inventory;
		this.keyb = keyb;
		selectedItem = false;
		player = owner;
		generateTitle("My Inventory");
		HashMapForGraphicsInit();
		
		state = GUIInventoryType.PLAYER;
		
	}
	
	
	/**
	 * constructor for the shopKeeper
	 * @param owner
	 * @param inventory
	 * @param keyb
	 */
	public ARPGInventoryGUI(Entity owner, ARPGInventory inventory, Keyboard keyb) {
		this.inventory = inventory;
		this.keyb = keyb;
		selectedItem = false;
		HashMapForGraphicsInit();
		generateTitle("Shop Keeper");
		state = GUIInventoryType.BUYABLE;		
	}
	
	/**
	 * generates the title of the inventory
	 * @param text
	 */
	public void generateTitle(String text) {
		textGraphics = new TextGraphics(text, 1, Color.DARK_GRAY);
		textGraphics.setBold(true);
		textGraphics.setDepth(DEPTH+4);
	}
	
	/**
	 * initialize possible slots
	 */
	public void HashMapForGraphicsInit() {
		mapGraphics = new HashMap<ARPGItem, String>();
		mapGraphics.put(ARPGItem.BOMB, "zelda/bomb");
		mapGraphics.put(ARPGItem.ARROW, "zelda/arrow.icon");
		mapGraphics.put(ARPGItem.SWORD, "zelda/sword.icon");
		mapGraphics.put(ARPGItem.BOW, "zelda/bow.icon");
		mapGraphics.put(ARPGItem.WATER_STAFF, "zelda/staff_water.icon");
		mapGraphics.put(ARPGItem.KEY, "zelda/key");
	}

	/**
	 * fills the inventory with possessed items
	 */
	public void FillMapInventory() {
		mapStock = new HashMap<ARPGItem, Integer>();
		nbDiffItem = 0;
		for (ARPGItem i: EXISTING_ITEM) {
			if (inventory.isInsideInventory(i)) {
				mapStock.put(i,inventory.amountInsideInventory(i));
				++nbDiffItem;
			}
		}
	}
	
	/**
	 * moves the selector
	 * @param b
	 * @param selectModif
	 */
	public void SelectorMove(Button b, int selectModif) {
		if(b.isPressed()&&selector+selectModif>=0) {
			selector += selectModif;
		}
	}
	
	/**
	 * draws the selector in the inventory
	 * @param canvas
	 */
	public void drawSelector(Canvas canvas) {
		ImageGraphics invSlot;
		for (int i = 0;i<10;++i) {
			if (selector%10==i) {
				invSlot = new ImageGraphics (ResourcePath.getSprite("zelda/inventory.selector"), 2.5f, 2.5f, new RegionOfInterest (0 , 0, 64 , 64),anchor.add( new Vector (1+((i%5)*2.6f) , (int) (6+ 3*Math.floor(i/5)) )), 1, DEPTH+1  );
			}else {
				invSlot = new ImageGraphics (ResourcePath.getSprite("zelda/inventory.slot"), 2.5f, 2.5f, new RegionOfInterest (0 , 0, 64 , 64),anchor.add( new Vector (1+((i%5)*2.6f) , (int) (6+ 3*Math.floor(i/5)) )), 1, DEPTH+1  );
			}
			
			invSlot.draw(canvas);
		}
	}
	
	/**
	 * displays the inventory
	 * @param canvas
	 * @param customer
	 */
	public void draw(Canvas canvas, ARPGPlayer customer) {
		
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		anchor = canvas.getTransform().getOrigin().sub( new Vector( width/2 , height/2) );
		
		FillMapInventory();
		
		ImageGraphics inventoryBackground = new ImageGraphics (ResourcePath.getSprite("zelda/inventory.background"), 15, 10, new RegionOfInterest (0 , 0, 240 , 240),anchor.add( new Vector (width-15 , height-10)), 1, DEPTH  );
		inventoryBackground.draw(canvas);
		
		SelectorMove(keyb.get(Keyboard.A), -1);
		SelectorMove(keyb.get(Keyboard.S), -5);
		SelectorMove(keyb.get(Keyboard.D), 1);
		SelectorMove(keyb.get(Keyboard.W), 5);
		
		textGraphics.setAnchor(anchor.add(width/3.2f,height*0.85f));
		textGraphics.draw(canvas);
		if (keyb.get(Keyboard.ENTER).isPressed() && state==GUIInventoryType.BUYABLE) {
			selectedItem = true;
		}

		selector = selector%nbDiffItem;

		drawSelector(canvas);

		int avoided = 0;
		int counter = 0;
		for (ARPGItem i: EXISTING_ITEM) {
			if (mapStock.containsKey(i)) {
				if((selector/10)*10>avoided&&counter<10) {
					++avoided;
				}else {
					ImageGraphics itemDisplay = new ImageGraphics (ResourcePath.getSprite(mapGraphics.get(i)), 2f, 2f, new RegionOfInterest (0 , 0, 16 , 16),anchor.add( new Vector (1.25f+((counter%5)*2.6f) , (float) (6.25f+ 3*Math.floor(counter/5)) ) ), 1, DEPTH+2 );
					itemDisplay.draw(canvas);
					
					
					if(state==GUIInventoryType.PLAYER) {
						TextGraphics itemQteDraw = new TextGraphics(Integer.toString(inventory.amountInsideInventory(i)), 1,Color.white);
						itemQteDraw.setAnchor(anchor.add( new Vector (1.25f+((counter%5)*2.6f) , (float) (6.25f+ 3*Math.floor(counter/5)) ) ));
						itemQteDraw.setDepth(DEPTH+5);
						itemQteDraw.setBold(true);
						itemQteDraw.setThickness(0.05f);
						itemQteDraw.setOutlineColor(Color.black);
						itemQteDraw.draw(canvas);
					}
					
					if(state==GUIInventoryType.PLAYER &&selectedItem && (selector%10)==counter) { //if it's player inventory that is shown
						selectedItem = false;
						player.forceCurrentItem(selector);
					}
					if(state==GUIInventoryType.BUYABLE && (selector%10)==counter) { //if it's shop keeper inventory
						showPrice(i.getPrice(), canvas);
						if(selectedItem) {
							selectedItem = false;
							if(customer.pay(i.getPrice())) {
								customer.buyItemReception(i);
							}
						}
					
					}
					++counter;
				}
			}
		}
		
	}
	
	/**
	 * show the prices of items in the inventory
	 * @param price
	 * @param canvas
	 */
	public void showPrice(int price, Canvas canvas) {
		int centaine = price/100;
		int dixaine = (price-(centaine*100))/10;
		int unite = price-(centaine*100)-(dixaine*10);
		
		ImageGraphics centaineDigit = new ImageGraphics (ResourcePath.getSprite("zelda/digits"), 0.9f, 0.9f, new RegionOfInterest (16*xModifier(centaine) , 16*yModifier(centaine), 16 , 16),anchor.add( new Vector (1.75f+9 , 5f)), 1, DEPTH+4 );
		centaineDigit.draw(canvas);
		ImageGraphics dixaineDigit = new ImageGraphics (ResourcePath.getSprite("zelda/digits"), 0.9f, 0.9f, new RegionOfInterest (16*xModifier(dixaine) , 16*yModifier(dixaine), 16 , 16),anchor.add( new Vector (2.5f+9 , 5f)), 1, DEPTH+4 );
		dixaineDigit.draw(canvas);
		ImageGraphics uniteDigit = new ImageGraphics (ResourcePath.getSprite("zelda/digits"), 0.9f, 0.9f, new RegionOfInterest (16*xModifier(unite) , 16*yModifier(unite), 16 , 16),anchor.add( new Vector (3.25f+9 , 5f)), 1, DEPTH+4 );
		uniteDigit.draw(canvas);
		ImageGraphics CoinLogo = new ImageGraphics(ResourcePath.getSprite("zelda/coin"), 0.9f, 0.9f, new RegionOfInterest (0 ,0 , 16 , 16),anchor.add( new Vector (4f+9 , 5f)), 1, DEPTH+4 );
		CoinLogo.draw(canvas);
		
	}

	private int xModifier(int i) {
		if (i>0) {
			return (i-1)%4;
		}
		return 1;
	}
	
	private int yModifier(int i) {
		if(i>0&&i<5) {
			return 0;
		}else if(i>4&&i<9) {
			return 1;
		}
		return 2;
	}

	/**
	 * selects the item
	 */
	public void selector() {
		selectedItem = true;
	}

}
