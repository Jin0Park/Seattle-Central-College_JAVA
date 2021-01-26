import java.awt.Color;
import java.util.Random;

public abstract class AbstractShape implements Shape {
	protected int level;
	protected int maxLevel;
	protected AbstractShape[] children;
	protected Color color;
	
	public AbstractShape(int level, int maxLevel, int children, Color color) {
		this.level = level;
		this.maxLevel = maxLevel;
		this.children = new AbstractShape[children];
		this.color = color;
	}
	
	public abstract void createChildren();

	/**
	 * Adds a level to this shape.
	 * Returns true if the operation was successful 
	 * or false if the maximum level has been reached.
	 */
	public boolean addLevel() {
		
		boolean result = false;
		
		// return false if the maximum level has been reached
		if(level == maxLevel) {
			return false;
		}
		
		//base case : if its children are null
		if (children[0] == null) {
			// create children
			createChildren();
			result = true;
		
		} else {
			// loop over the children
			for (int i = 0; i < children.length; i++) {
				result = children[i].addLevel();
			}
		}
		
		return result;
	}	

	/**
	 * Removes a level from this shape.
	 * Returns true if the operation was successful 
	 * or false if the shape was at level 1.
	 */
	public boolean removeLevel() {
		
		boolean result = false;
		
		// return false if it has reached the first level
		if(level == 1 && children[0] == null) {
			return false;
		}
		
		// base case: if it does not have grandchildren
		if (children[0] != null && children[0].children[0] == null) {
			
			// set its children to null
			for (int i = 0; i < children.length; i++) {
				children[i] = null;
			}
			result = true;
			
		} else {
			// loop over the children
			for (int i = 0; i < children.length; i++) {
				result = children[i].removeLevel();
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the total number of shapes of this shape 
	 */
	public int countShapes() {
		
		int count = 0;
		
		// base case : if it does not have children
		if(children[0] == null) {
			return 1;
		}
		else{
			for (int i = 0; i < children.length; i++) {
				count += children[i].countShapes();
			}
			return  1 + count;
		}
	}
	
	/**
	 * Returns a color generated randomly
	 */
	public Color randomColor() {
		Random rand = new Random();
		return new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));

	}
}
