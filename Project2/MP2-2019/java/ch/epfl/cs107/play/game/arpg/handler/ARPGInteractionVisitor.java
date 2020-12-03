package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.areagame.actor.Monster;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.Bridge;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.DisabledMew;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.Pet;
import ch.epfl.cs107.play.game.arpg.actor.ShopKeeper;
import ch.epfl.cs107.play.game.arpg.actor.TextPnj;
import ch.epfl.cs107.play.game.arpg.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.collectable.WaterStaff;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

/**
 * default interaction visitor
 */
public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
	default void interactWith(ARPGBehavior.ARPGCell cell) {}
	
	default void interactWith(ARPGPlayer player) {}
	
	default void interactWith(Grass grass) {}
	
	default void interactWith(Bomb bomb) {}
	
	default void interactWith(Coin coin) {}
	
	default void interactWith(Heart heart) {}
	
	default void interactWith(CastleKey cKey) {}
	
	default void interactWith(CastleDoor cDoor) {}
	
	default void interactWith(Monster monster) {}
	
	default void interactWith(Pet pet) {}
	
	default void interactWith(CaveDoor caveDoor) {}
	
	default void interactWith(ShopKeeper shopKeeper) {}
	
	default void interactWith(DisabledMew disabledMew) {}
	
	default void interactWith(FireSpell fireSpell) {}
	
	default void interactWith(TextPnj textPnj) {}
	
	default void interactWith(Bridge bridge) {}
	
	default void interactWith(WaterStaff waterStaff) {}
}
