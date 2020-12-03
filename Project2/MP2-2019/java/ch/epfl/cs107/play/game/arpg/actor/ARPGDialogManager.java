package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

/**
 * Recieve all dialog that are shown to the player. Will display them in order of reception
 */
public class ARPGDialogManager {
	
	private List<String> dialog;
	private boolean isShowingSomething;
	private Dialog d;
	private Keyboard keyb;
	private ARPGPlayer owner;
	
	
	/**
	 * Default constructor
	 * @param player (ARPGPlayer): the owner of the manager
	 * @param keyb (Keyboard): to go forward in text reading
	 */
	public ARPGDialogManager(ARPGPlayer player ,Keyboard keyb) {
		dialog = new ArrayList<String>();
		isShowingSomething = false;
		this.keyb = keyb;
		owner = player;
	}
	
	
	/**
	 * Is called by player draw method. Display the dialog.
	 * @param canvas
	 * @param area
	 */
	protected void draw(Canvas canvas, Area area) {
		
		if (isShowingSomething == true) {
			if (keyb.get(Keyboard.ENTER).isPressed()||keyb.get(Keyboard.E).isPressed()) {
				if(d.push()) {
					isShowingSomething = false;
					owner.unBlock();
				}
			}
			d.draw(canvas);
		}
		
		if(dialog.size()>0 && isShowingSomething == false) {
			String toBeShown = dialog.get(0);
			dialog.remove(0);
			isShowingSomething = true;
			owner.block();
			d = new Dialog(toBeShown,"zelda/dialog",area);
		}

		
	}
	
	/**
	 * Add String text to the queue.
	 * @param text
	 */
	protected void addDialog(String text) {
		dialog.add(text);
	}
	
	
	/**
	 * getter to know if the player has a dialog currently displayed
	 * @return
	 */
	protected boolean finishTalking() {
		return isShowingSomething;
	}
	
}
