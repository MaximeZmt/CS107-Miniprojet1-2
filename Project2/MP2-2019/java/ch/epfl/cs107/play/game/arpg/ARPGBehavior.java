package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

/**
 * AreaBehaviour for ARPG
 * @see AreaBehavior
 * @see ARPG
 */
public class ARPGBehavior extends AreaBehavior {
	private ARPGCellType cellType;
	
	/**
	 * Default constructor
	 * @param window
	 * @param name
	 */
	public ARPGBehavior(Window window, String name) {
		super(window, name);
		for (int x=0; x<getWidth();++x) {
			for (int y=0; y<getHeight();++y) {
				cellType = ARPGCellType.toType(getRGB(getHeight()-1-y, x));
				setCell(x, y, new ARPGCell(x, y, cellType));
			} 
		}
	}
	
	/**
	 * Link color to a cell type and its behaviour
	 */
	public enum ARPGCellType {
		NULL(0, false, false),
		WALL(-16777216, false, false),
		IMPASSABLE(-8750470, false, true),
		INTERACT(-256, true, true),
		DOOR(-195580, true, true),
		WALKABLE(-1, true, true);
		
		final int type;
		final boolean isWalkable;
		final boolean isFlyable;
		
		ARPGCellType(int type, boolean isWalkable, boolean isFlyable){
			this.type = type;
			this.isWalkable = isWalkable;
			this.isFlyable = isFlyable;
		}
		
		/**
		 * Return celltype associated to its color
		 * @param type
		 * @return
		 */
		public static ARPGCellType toType(int type) {
			for (ARPGCellType value : ARPGCellType.values()) {
				if (type == value.type) {
					return value;
				}
			}
			return null;			
		}
		
	}
	
	/**
	 * Cell of an ARPG
	 * @see Cell
	 */
	public class ARPGCell extends Cell{ 
		
		
		private final ARPGCellType type;

		/**
		 * Default constructor
		 * @param x (int): abscissa coordinates
		 * @param y (int): ordinate coordinates
		 * @param type
		 */
		private ARPGCell(int x, int y, ARPGCellType type) {
			super(x,y);
			this.type = type;
		}
		
		
		@Override
		public boolean isCellInteractable() { 
			return true;
		}

		@Override
		public boolean isViewInteractable() { 
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {		
		}

		@Override
		protected boolean canLeave(Interactable entity) {
			return true;
		}

		@Override
		protected boolean canEnter(Interactable entity) {
			if (entity instanceof FlyableEntity) {
				return type.isFlyable;
			}
			else if(entity.takeCellSpace() && hasNonTraversableContent()) {
				return false;
			}
			return type.isWalkable;
		}
		
	}

	
	
}












