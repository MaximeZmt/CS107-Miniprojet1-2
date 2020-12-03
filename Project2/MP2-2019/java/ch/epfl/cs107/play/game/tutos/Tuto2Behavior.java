package ch.epfl.cs107.play.game.tutos;



import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2Behavior extends AreaBehavior {
	
	public Tuto2Behavior(Window window, String name) {
		super(window, name);
		Tuto2CellType cellType;
		for (int x=0; x<getWidth();++x) {
			for (int y=0; y<getHeight();++y) {
				cellType = Tuto2CellType.toType(getRGB(getHeight()-1-y, x));
				setCell(x, y, new Tuto2Cell(x, y, cellType));
			} 
		}
	}
	
	
	
	
	
	
	
	
	public enum Tuto2CellType{
		NULL(0, false),
		WALL(-16777216, false),
		IMPASSABLE(-8750470, false),
		INTERACT(-256, true),
		DOOR(-195580, true),
		WALKABLE(-1, true),;
		
		final int type;
		final boolean isWalkable;
		
		Tuto2CellType(int type, boolean isWalkable){
			this.type = type;
			this.isWalkable = isWalkable;
		}
		
		static Tuto2CellType toType(int type) {
			switch(type) {
			case -16777216:
				return WALL;
			case -8750470:
				return IMPASSABLE;
			case -256:
				return INTERACT;
			case -195580:
				return DOOR;
			case -1:
				return WALKABLE;
			default:
				return NULL;
			
			
			}
		}
		
	}
	
	
	public class Tuto2Cell extends Cell{ 
		
		
		private final Tuto2CellType type;

		
		private Tuto2Cell(int x, int y, Tuto2CellType type) {
			super(x,y);
			this.type = type;
		}
		
		
		@Override
		public boolean isCellInteractable() { // interaction de contact
			return true;
		}

		@Override
		public boolean isViewInteractable() { // interaction a distance
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
		//EMPTY			
		}

		@Override
		protected boolean canLeave(Interactable entity) {
			return true;
		}

		@Override
		protected boolean canEnter(Interactable entity) {
			return type.isWalkable;
		}

	}

	
	
	public boolean isADoor(DiscreteCoordinates coord) {
		int x = coord.x;
		int y = coord.y;
		Tuto2Cell c = (Tuto2Cell)getCell(x, y);
		if (c.type==Tuto2CellType.DOOR)
		{
			return true;
		}
		else {
			return false;
		}
	}	
	

	
	
}












