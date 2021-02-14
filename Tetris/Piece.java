import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Interface for the Tetris game piece
 * 
 * @author CSC 143
 */
public interface Piece {

	void draw(Graphics g);

	void move(Direction direction);

	Point[] getLocations();

	Color getColor();

	boolean canMove(Direction direction);
	
	void rotate();
	
	boolean canRotate();
	
	Square[] getSquareArray();
}
