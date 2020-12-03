package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.rpg.InventoryItem;
/**
 * All items that can be hold in an inventory
 */
public enum ARPGItem implements InventoryItem {
	ARROW("Arrow",0.1f,5),
	BOMB("Bomb",5,20), 
	KEY("Key",0.5f,1000),
	BOW("bow",2,20),
	WATER_STAFF("Water staff",2,1000),
	SWORD("Sword",10,50);
	/// price is for Fortune and shop
		
	ARPGItem(String name, float weight, int price)  {
		Item(name, weight, price);
	}

	private String name;
	private float weight;
	private int price;
	
	/**
	 * default constructor for Item
	 * @param name
	 * @param weight
	 * @param price
	 */
	private void Item(String name, float weight, int price) {
		this.name = name;
		this.weight = weight;
		this.price = price;		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getWeight() {
		return weight;
	}

	@Override
	public int getPrice() {
		return price;
	}

}
