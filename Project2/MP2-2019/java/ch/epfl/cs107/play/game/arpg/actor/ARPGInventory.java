package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.arpg.ARPGItem;
import ch.epfl.cs107.play.game.rpg.Inventory;

/**
 * An Inventory with notion of fortune
 * @see Inventory
 */
public class ARPGInventory extends Inventory{
	
	private static final float MAX_WEIGHT = 100;
	private int myMoney;
	private int itemsFortune;
	
	/**
	 * Default Constructor with initial Money
	 * @param initialMoney
	 */
	public ARPGInventory(int initialMoney) {
		super(MAX_WEIGHT);
		itemsFortune = 0;
		myMoney = initialMoney;
	}
	
	/**
	 * Remove an Item from inventory
	 * @param item
	 * @return true if success
	 */
	protected boolean useItem(ARPGItem item) {
		if (removeItem(item, 1)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Add quantity of an Item in inventory
	 * @param item
	 * @param qte
	 * @return true if success
	 */
	protected boolean addItem(ARPGItem item, int qte) {
		if (super.addItem(item, qte)) {
			itemsFortune += item.getPrice()*qte;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Remove quantity of an Item in inventory
	 * @param item
	 * @param qte
	 * @return true if success
	 */
	protected boolean removeItem(ARPGItem item, int qte) {
		if(super.removeItem(item, qte)) {
			itemsFortune -= item.getPrice()*qte;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Add money
	 * @param money
	 */
	protected void addmoney(int money) {
		if(myMoney+money<1000) {
			myMoney += money;
		}else {
			myMoney = 999;
		}
	}
	
	/**
	 * getter for player money
	 * @return
	 */
	public int getMoney() {
		return myMoney;
	}
	
	/**
	 * getter for player fortune
	 * @return
	 */
	public int getFortune() {
		return itemsFortune+myMoney;
	}
	
	
	/**
	 * remove money
	 * @param money
	 * @return true if success
	 */
	public boolean pay(int money) {
		if(myMoney-money>=0) {
			myMoney -= money;
			return true;
		}
		return false;
	}

}
