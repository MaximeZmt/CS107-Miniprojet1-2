package ch.epfl.cs107.play.game.tutos.actor;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SimpleGhost extends Entity {

	private static float hp;
	private TextGraphics hpText;
	private Sprite ghostP;
	
	public SimpleGhost(Vector position, String spriteName) {
		super(position);
		hp = 10;
		ghostP = new Sprite (spriteName, 1, 1.f, this);
		hpText = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.RED);
		hpText.setParent(this);
		hpText.setAnchor(new Vector(-0.3f, 0.1f));
	}
	
	public boolean isWeak() {
		if (hp<=0) {
			return true;
		}else {
			return false;
		}
	}
	
	public void strengthen() {
		hp = 10;
	}

	@Override
	public void draw(Canvas canvas) { //print on canvas Otherwise don't print
		ghostP.draw(canvas);
		hpText.draw(canvas);
		
	}
	
	public void update(float deltaTime) {
		if (hp>0.f) {
			hp -= deltaTime;
			}
		hpText.setText(Integer.toString((int)hp));
	}
	
	public void moveUp(float delta) {
		setCurrentPosition(getPosition().add(0.f,delta));
	}
	public void moveDown(float delta) {
		setCurrentPosition(getPosition().add(0.f,-delta));
	}
	public void moveRight(float delta) {
		setCurrentPosition(getPosition().add(delta,0.f));
	}
	public void moveLeft(float delta) {
		setCurrentPosition(getPosition().add(-delta,0.f));
	}

}







