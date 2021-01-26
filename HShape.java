import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class HShape extends AbstractShape {
		
	private Point[] p = new Point[4];	// Points that construct the boundary square of H shape
	private Point[][] pArr = new Point[9][4];	// 2D array that contains 9 squares with 4 points
	
	/**
	 *  Constructor of the HShape class
	 */
	public HShape(int level, Color color, Point[] p) {
		// initialize field variables in super class
		super(level, 5, 7, color);
		// set points to the field variable
		this.p = p;
		this.pArr = getSmallSquareArray();
    }
	
	/**
	 *  Draw H shapes from the top level to all children level (recursion)
	 */ 
	public void draw(Graphics g) {	
		
		// draw squares
		for(int i = 0; i < 9; i++) {

			// set points for the polygon 
			Polygon pol = new Polygon();
			for(int j = 0; j < 4; j++) {
				pol.addPoint(pArr[i][j].x, pArr[i][j].y);
			}
			
			// set 2nd and 8th square as empty area
			if(i == 1 || i == 7) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(this.color);				
			}
			
			// draw
		    g.fillPolygon(pol);
		    g.drawPolygon(pol);
		}
		
	    // if there is children, call draw()
	    if(this.children[0]!= null) {
	    	for(int i = 0; i < this.children.length; i++) {
	    		this.children[i].draw(g);
	    	}
		}
	}
	
	/**
	 * Break the space into 9 small squares and returns the points array 
	 */
	private Point[][] getSmallSquareArray() {
		
		Point[][] smallSquares = new Point[9][4];
		
		// small square's width and height
		int sWidth = (this.p[3].x - this.p[0].x + 1) / 3;
		int sHeight = (this.p[1].y - this.p[0].y + 1) / 3;

		// compute each small square's boundary points (counterclockwise)
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {

				int index = 3 * row + col;
				int x = this.p[0].x + (col * sWidth);
				int y = this.p[0].y + (row * sHeight);

				smallSquares[index][0] = new Point(x, y);
				smallSquares[index][1] = new Point(x, y + sHeight);
				smallSquares[index][2] = new Point(x + sWidth, y + sHeight);
				smallSquares[index][3] = new Point(x + sWidth, y);
			}
		}
		return smallSquares;
	}
	
	@Override
	public void createChildren() {
		
		int index = 0;
		// set the children array
		for(int i = 0; i < 9; i++) {
			
			// skip 2nd and 8th square because it is empty area
			if(i == 1 || i == 7) {
				continue;
			}
			// set 'index'th child
			this.children[index] = new HShape(this.level+1, randomColor(), pArr[i]);
			index++;
		}
	}
	
	public void update(int value) {	
		//Modifies this shape in an interesting way given a value in the range 0-100. 
		//This method is only required for the extra-credit part.
	}

}

