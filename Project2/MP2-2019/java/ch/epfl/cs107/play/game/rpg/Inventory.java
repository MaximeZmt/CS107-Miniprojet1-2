package ch.epfl.cs107.play.game.rpg;

import java.util.HashMap;

/**
 * represents a place where items are stored with no specific order
 */
public class Inventory {
	
	public interface Holder {
		 boolean possess(InventoryItem i);
	}
	
	private static float weightTotal;
	private float weightMax; 
	
	/**
	 * default constructor
	 * @param weightMax
	 */
	public Inventory (float weightMax){
		this.weightMax = weightMax;
		weightTotal = 0;
	}
	
	private HashMap<InventoryItem, Integer> inventory = new HashMap<InventoryItem, Integer>();
	
	/**
	 * adds an item into the inventory
	 * @param item
	 * @param qte
	 * @return
	 */
	protected boolean addItem(InventoryItem item, int qte) {
		computeWeight();
		if ((item.getWeight()*qte)+weightTotal<weightMax) {
			if (inventory.containsKey(item)) {
				int currentValue = inventory.get(item);
				inventory.replace(item, (currentValue+qte));
				computeWeight();
				return true;
			}
			else {
				inventory.put(item, qte);
				computeWeight();
				return true;
			}
		}else {
			return false;
		}

	}
	
	/**
	 * removes an item from the inventory
	 * @param item
	 * @param qte
	 * @return
	 */
	protected boolean removeItem(InventoryItem item, int qte) {
		if (inventory.containsKey(item)) {
			int currentValue = inventory.get(item);
			if (currentValue>qte) {
				inventory.replace(item, currentValue-qte);
				return true;
			} else if (currentValue == qte ) {
				inventory.remove(item);
				return true;
			} 
		}
		return false;
	}
	
	/**
	 * checks if the item is inside the inventory
	 * @param item
	 * @return (boolean): true if item is inside
	 */
	public boolean isInsideInventory(InventoryItem item) {
		if(inventory.containsKey(item)){
			return true;
		}
		return false;
	}
	
	/**
	 * return the amount of an item inside an inventory
	 * @param item
	 * @return amount (int)
	 */
	public int amountInsideInventory(InventoryItem item) {
		return inventory.get(item);
	}
	
	/**
	 * computes the total weight of the inventory
	 */
	public void computeWeight() {
		weightTotal = 0;
		inventory.forEach((item,qte)->{
			weightTotal += item.getWeight()*qte;
		});
	}
	
	
}
