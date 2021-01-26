import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class MyShape extends AbstractShape {
	private Point[] m = new Point[2];	
    private double scalingFactor = 0.65;
	private int angle = 45;
    
	/**
	 *  Constructor of the MyShape class
	 */
	public MyShape(int level, Color color, Point[] m) {
    	super(level, 12, 2, color);
    	this.m = m;
    }
	
	/**
	 *  Draw branches from the top level to all children level (recursion)
	 */    
	public void draw(Graphics g) {
		
		Polygon pol = new Polygon();
	    pol.addPoint(m[0].x, m[0].y);
	    pol.addPoint(m[1].x, m[1].y);
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
	 * Returns the points of the next branches 
	 * determined by the scaled length and the angle from the given point 
	 */
	public Point[] nextPoints() {
		
		double xCos = (m[1].x - m[0].x) * Math.cos(Math.toRadians(angle));
		double xSin = (m[1].x - m[0].x) * Math.sin(Math.toRadians(angle));
		double yCos = (m[1].y - m[0].y) * Math.cos(Math.toRadians(angle));
		double ySin = (m[1].y - m[0].y) * Math.sin(Math.toRadians(angle));
		
		Point[] nextPoints = new Point[2]; 
		nextPoints[0] = new Point((int)(m[1].x + (scalingFactor * (xCos + ySin))), 
								  (int)(m[1].y + (scalingFactor * (-xSin + yCos))));
		nextPoints[1] = new Point((int)(m[1].x + (scalingFactor * (xCos - ySin))),
								  (int)(m[1].y + (scalingFactor * (xSin + yCos))));
		return nextPoints;
	}

	@Override
	/**
	 * Create three children for next level
	 */
	public void createChildren() {
		Point[] nextPoints = nextPoints();
		Point[] m1 = {this.m[1], nextPoints[0]};
		Point[] m2 = {this.m[1], nextPoints[1]};
		children[0] = new MyShape(this.level + 1, Color.red, m1);
		children[1] = new MyShape(this.level + 1, Color.blue, m2);	
	}
	
    public void update(int value) {	
	//Modifies this shape in an interesting way given a value in the range 0-100. 
	//This method is only required for the extra-credit part.
	}
}
