import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class SierpinskiTriangle extends AbstractShape {

	private Point[] p = new Point[3];	// points that construct a triangle
	
	/**
	 *  Constructor of the SierpinskiTriangle class
	 */
	public SierpinskiTriangle(int level, Color color, Point[] p) {
    	// initialize field variables in super class
    	super(level, 10, 3, color);
    	// set points to the field variable
    	this.p = p;
	}
    
	/**
	 *  Draw triangles from the top level to all children level (recursion)
	 */    
	public void draw(Graphics g) {
		
		// draw triangle
		Polygon pol = new Polygon();
	    pol.addPoint(p[0].x, p[0].y);
	    pol.addPoint(p[1].x, p[1].y);
	    pol.addPoint(p[2].x, p[2].y);
	    g.setColor(this.color);
	    g.drawPolygon(pol);
		
	    // if there is children, call draw()
	    if(this.children[0]!= null) {
	    	for(int i = 0; i < this.children.length; i++) {
	    		this.children[i].draw(g);
	    	}
		}
	}

	/**
	 * Returns the mid point of the two given points 
	 */
	public Point midpoint(Point p1, Point p2) {
		// calculating midpoints
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	@Override
	/**
	 * Create three children for next level
	 */
	public void createChildren() {
		
		Point[] p1 = {this.p[0], midpoint(this.p[0], this.p[1]), midpoint(this.p[0], this.p[2])};
		Point[] p2 = {this.p[1], midpoint(this.p[1], this.p[0]), midpoint(this.p[1], this.p[2])};
		Point[] p3 = {this.p[2], midpoint(this.p[2], this.p[0]), midpoint(this.p[2], this.p[1])};
    	
    	children[0] = new SierpinskiTriangle(this.level + 1, randomColor(), p1);
    	children[1] = new SierpinskiTriangle(this.level + 1, randomColor(), p2);
    	children[2] = new SierpinskiTriangle(this.level + 1, randomColor(), p3);
	}	
	
	public void update(int value) {	
	//Modifies this shape in an interesting way given a value in the range 0-100. 
	//This method is only required for the extra-credit part.
	
	}

}



