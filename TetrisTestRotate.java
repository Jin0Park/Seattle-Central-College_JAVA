import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

public class TetrisTestRotate {
	@Test
	public void testRotate() {
		Grid g = new Grid();

		int row = 5, col = 5;
		Piece[] pieces = { new BarShape(row, col, g), new LShape(row, col, g), new ZShape(row, col, g),
				new TShape(row, col, g), new JShape(row, col, g), new SShape(row, col, g) };

		for (Piece p : pieces) {
			System.out.println(p.getClass());

			// The piece should be able to rotate
			assertTrue(p.canRotate());

			// Place a square next to the piece to prevent it from rotating
			int setRow, setCol;
			if (p.getClass() == BarShape.class) {
				setRow = row + 1;
				setCol = col;
			} else if (p.getClass() == LShape.class) {
				setRow = row;
				setCol = col - 1;
			} else if (p.getClass() == ZShape.class) {
				setRow = row + 1;
				setCol = col - 1;
			} else if (p.getClass() == TShape.class) {
				setRow = row + 1;
				setCol = col - 1;
			} else if (p.getClass() == JShape.class) {
				setRow = row;
				setCol = col - 1;
			} else { // SShape
				setRow = row;
				setCol = col - 1;
			}
			g.set(setRow, setCol, Color.GREEN);
			assertFalse(p.canRotate());

			// clear the square for the next piece
			g.set(setRow, setCol, Grid.EMPTY);
		}
	}

}