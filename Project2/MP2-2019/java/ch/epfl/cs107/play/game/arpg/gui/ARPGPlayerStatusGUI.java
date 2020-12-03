package ch.epfl.cs107.play.game.arpg.gui;

import java.util.HashMap;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * graphical interface for the player status
 */
public class ARPGPlayerStatusGUI { 
	private final static float DEPTH = 1001;
	private final static float DEPTH_VISIBLE = 1002;
	private HashMap<ARPGItem, String> mapGraphics; 
	private ARPGItem currentItem;
	private float hp;
	private float maxHp;
	private static int money;
	private Vector anchor;
	
	/**
	 * default constructor
	 */
	public ARPGPlayerStatusGUI() {
		hp = 0;
		maxHp = 0;
		money = 0;
		
		///Initialise Hashmap with ARPGItem
		mapGraphics = new HashMap<ARPGItem, String>();
		mapGraphics.put(ARPGItem.BOMB, "zelda/bomb");
		mapGraphics.put(ARPGItem.ARROW, "zelda/arrow.icon");
		mapGraphics.put(ARPGItem.SWORD, "zelda/sword.icon");
		mapGraphics.put(ARPGItem.BOW, "zelda/bow.icon");
		mapGraphics.put(ARPGItem.WATER_STAFF, "zelda/staff_water.icon");
		mapGraphics.put(ARPGItem.KEY, "zelda/key");
		
		
	}

	/**
	 * displays the graphical interface 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		anchor = canvas.getTransform().getOrigin().sub( new Vector( width/2 , height/2) );
		
		
		///BASIC GUI
		ImageGraphics gearDisplay = new ImageGraphics (ResourcePath.getSprite("zelda/gearDisplay"), 2f, 2f, new RegionOfInterest (0 , 0, 32 , 32),anchor.add( new Vector (0 , height-2f)), 1, DEPTH  );
		ImageGraphics coinsDisplay = new ImageGraphics (ResourcePath.getSprite("zelda/coinsDisplay"), 4f, 2f, new RegionOfInterest (0 , 0, 64 , 32),anchor.add( new Vector (0.25f , 1f)), 1, DEPTH  );
		gearDisplay.draw(canvas);
		coinsDisplay.draw(canvas);
		///END OF BASIC GUI
		
		
		///FILL CURRENT INV DISPLAY
		if (currentItem != null) { //avoid problem loading graph with null item.
		ImageGraphics itemDisplay = new ImageGraphics (ResourcePath.getSprite(mapGraphics.get(currentItem)), 1f, 1f, new RegionOfInterest (0 , 0, 16 , 16),anchor.add( new Vector (0.5f , height-1.5f)), 1, DEPTH_VISIBLE );
		itemDisplay.draw(canvas);
		}
		///END OF FILL CURRENT INV DISPLAY
		
		///HEALTH POINT DISPLAY
		int roundHp = (int)hp; 
		int counter = 0;
		while (counter<roundHp) {
			ImageGraphics heart = new ImageGraphics (ResourcePath.getSprite("zelda/heartDisplay"), 1f, 1f, new RegionOfInterest (32 , 0, 16 , 16),anchor.add( new Vector (2f+counter , height-1.5f)), 1, DEPTH_VISIBLE );
			heart.draw(canvas);
			++counter;
		}
		if ((hp-roundHp)>0) {
			ImageGraphics heart = new ImageGraphics (ResourcePath.getSprite("zelda/heartDisplay"), 1f, 1f, new RegionOfInterest (16 , 0, 16 , 16),anchor.add( new Vector (2f+counter , height-1.5f)), 1, DEPTH_VISIBLE );
			heart.draw(canvas);
			++counter;
		}
		while(counter<maxHp) {
			ImageGraphics heart = new ImageGraphics (ResourcePath.getSprite("zelda/heartDisplay"), 1f, 1f, new RegionOfInterest (0 , 0, 16 , 16),anchor.add( new Vector (2f+counter , height-1.5f)), 1, DEPTH_VISIBLE );
			heart.draw(canvas);
			++counter;
		}
		///END OF HEALTH POINT DISPLAY
		
		
		showMoney(money, canvas);
		
	}

	
	
	public void updateLifeBar(float myLife, float maxLife) {
		hp = myLife;
		maxHp = maxLife;
	}
	
	public void changeItemDisplayGraphics(ARPGItem newItem) {
		currentItem = newItem;
	}
	
	public void moneyDisplayUpdate(int myMoney) {
		money = myMoney;
	}
	
	///MONEY
	public void showMoney(int money, Canvas canvas) {
		int centaine = money/100;
		int dixaine = (money-(centaine*100))/10;
		int unite = money-(centaine*100)-(dixaine*10);
		
		ImageGraphics centaineDigit = new ImageGraphics (ResourcePath.getSprite("zelda/digits"), 0.9f, 0.9f, new RegionOfInterest (16*xModifier(centaine) , 16*yModifier(centaine), 16 , 16),anchor.add( new Vector (1.75f , 1.6f)), 1, DEPTH_VISIBLE );
		centaineDigit.draw(canvas);
		ImageGraphics dixaineDigit = new ImageGraphics (ResourcePath.getSprite("zelda/digits"), 0.9f, 0.9f, new RegionOfInterest (16*xModifier(dixaine) , 16*yModifier(dixaine), 16 , 16),anchor.add( new Vector (2.5f , 1.6f)), 1, DEPTH_VISIBLE );
		dixaineDigit.draw(canvas);
		ImageGraphics uniteDigit = new ImageGraphics (ResourcePath.getSprite("zelda/digits"), 0.9f, 0.9f, new RegionOfInterest (16*xModifier(unite) , 16*yModifier(unite), 16 , 16),anchor.add( new Vector (3.25f , 1.6f)), 1, DEPTH_VISIBLE );
		uniteDigit.draw(canvas);
		
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
	///END OF MONEY
	

}
