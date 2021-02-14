import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

class TetrisTest {

	/**
	 * Test for the row removal feature whether it behaves correctly.
	 * Fill up all grids with square except two grids at 4th and 6th column in row 11,
	 * after calling checkRows(),check if all filled grids are removed except
	 * two grids at 4th and 6th column in the bottom row.
	 */	
	@Test
	void testCheckRows() {
		Grid g = new Grid();
		for (int row = 0; row < Grid.HEIGHT; row++) {
			for (int col = 0; col < Grid.WIDTH; col++) {
				if(row != 10 || col == 3 || col == 5) {
					g.set(row, col, Color.MAGENTA);
				}
			}
		}
		g. checkRows();

		for (int row = 0; row < Grid.HEIGHT; row++) {
			for (int col = 0; col < Grid.WIDTH; col++) {
				if (row < Grid.HEIGHT - 1 || col != 3 && col != 5) {
					assertFalse(g.isSet(row, col));
				}
			}
		}
	}
		
	/**
	 * Test for the motion of the L-Shape whether it moves correctly
	 * L-Shape will be detected every time it moves to the right until it reaches all the way to right
	 */	
	@Test
	void testMove() {
		Grid g = new Grid();
		LShape l = new LShape(10, 0, g);
		
		for (int col = 0; col < Grid.WIDTH - 2; col++) {
			l.move(Direction.RIGHT);	
		}
		assertTrue(!l.canMove(Direction.RIGHT));
	}

	/**
	 * Test for the motion of the L-Shape when there is a square in the way
	 * The L-Shape will not be able to move right or left but down
	 */
	@Test
	void testCanMove() {
		Grid g = new Grid();
		LShape l = new LShape(10, 0, g);
		
		g.set(10,  1, Color.MAGENTA);
		assertTrue((!l.canMove(Direction.RIGHT)) && (!l.canMove(Direction.LEFT)) && (l.canMove(Direction.DOWN)));
		
	}

	/**
	 * Test for the Rotation feature:
	 * 1) Check if LShape and BarShape pieces do not rotate when there is a square blocking its way
	 * 2) Check if LShape and BarShape pieces rotate as designed when there is nothing to block its way
	 */
	@Test
	public void testRotate() {
		
		Grid g = new Grid();		
		int row = 5, col = 5;
		int setRow, setCol;
		
		Piece[] pieces = { new LShape(row, col, g), new BarShape(row, col, g) };
		
		for (Piece p : pieces) {
			
			// get original piece info
			Square[] original = p.getSquareArray();
			
			//-----------------------------------------------------------------
			// TEST 1: piece does not rotate when there is a square blocking its way
			//-----------------------------------------------------------------
			if (p.getClass() == LShape.class) {
				
				// set a square for blocking the Lshape rotation
				setRow = row;
				setCol = col - 1;
				g.set(setRow, setCol, Color.GREEN);
				
			} else {
				// set a square for blocking the Barshape rotation
				setRow = row + 1;
				setCol = col;
				g.set(setRow, setCol, Color.GREEN);
			}
			
			// rotate piece
			p.rotate();
			
			// get current piece info
			Square[] current = p.getSquareArray();
			
			// check if the piece remain at the same location
			for (int i = 0; i < original.length; i++) {
				assertEquals(original[i].getRow(),current[i].getRow());
			}
			
			// remove the blocking square
			g.set(setRow, setCol, Grid.EMPTY);
			
			//-----------------------------------------------------------------
			// TEST 2: piece rotates as designed when there is nothing to block its way
			//-----------------------------------------------------------------
			// rotate piece
			p.rotate();
			
			// set the expected square position after one rotation
			Square[]square = new Square[4];
			
			if (p.getClass() == LShape.class) {
				square[0] = new Square(g, row, col + 1, Color.magenta, true);
				square[1] = new Square(g, row, col, Color.magenta, true);
				square[2] = new Square(g, row, col - 1, Color.magenta, true);
				square[3] = new Square(g, row + 1, col - 1, Color.magenta, true);
			} else {
				square[0] = new Square(g, row - 1, col, Color.magenta, true);
				square[1] = new Square(g, row, col, Color.magenta, true);
				square[2] = new Square(g, row + 1, col, Color.magenta, true);
				square[3] = new Square(g, row + 2, col - 1, Color.magenta, true);
			}
			
			// check if the piece rotated at 90 degrees clockwise
			for (int i = 0; i < original.length; i++) {
				assertEquals(original[i].getRow(),current[i].getRow());
			}
		}
	}

}
