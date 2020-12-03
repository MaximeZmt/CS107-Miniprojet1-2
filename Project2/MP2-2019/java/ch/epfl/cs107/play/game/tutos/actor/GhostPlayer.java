package ch.epfl.cs107.play.game.tutos.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class GhostPlayer extends MovableAreaEntity{

	private boolean isPassingADoor;
	private final static int ANIMATION_DURATION = 8;
	private Sprite ghostP;
	
	public GhostPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
		super(area, orientation, coordinates);
		ghostP = new Sprite (spriteName, 1, 1.f, this);
		resetMotion();
	}
	
	private void isPassingDoorTrue(){
		isPassingADoor = true;
	}
	public void isPassingDoorFalse() {
		isPassingADoor = false;
	}
	public boolean getVarIsPassingADoor() {
		return isPassingADoor;
	}
	
	
	public void enterArea(Area area, DiscreteCoordinates position) {
		area.registerActor(this); //this, because this is related to the main class
		area.setViewCandidate(this);
		setOwnerArea(area);
		setCurrentPosition(position.toVector());
		resetMotion();
	}
	
	public void leaveArea() {
		getOwnerArea().unregisterActor(this); // 
	}
	
	
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Canvas canvas) {
		ghostP.draw(canvas); 
		
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		Keyboard keyb = getOwnerArea().getKeyboard();
		moveOrientate(keyb.get(Keyboard.LEFT), Orientation.LEFT);
		moveOrientate(keyb.get(Keyboard.RIGHT), Orientation.RIGHT);
		moveOrientate(keyb.get(Keyboard.UP), Orientation.UP);
		moveOrientate(keyb.get(Keyboard.DOWN), Orientation.DOWN);
		
		for(DiscreteCoordinates x :getCurrentCells()) {
			if(((Tuto2Area)getOwnerArea()).isADoor(x)) {
				isPassingDoorTrue();
			}
			else {
				isPassingDoorFalse();
			}
		}
	}

	
	
	
	
	public void moveOrientate(Button b, Orientation orient) {
		
		if (b.isDown()) {
			Orientation orientPlayer = getOrientation(); // orient of player
			if(orientPlayer==orient) {
				move(ANIMATION_DURATION);
			}
			else {
				orientate(orient);
			}
		}
	}

}

