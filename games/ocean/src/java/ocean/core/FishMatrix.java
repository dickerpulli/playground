package ocean.core;

import java.util.LinkedList;
import java.util.Random;

import ocean.fish.Fish;

/**
 * A matrix of fish of constant size.
 * <p>
 * 
 * There are access method <code>getFish</code> and <code>setFish</code> to request or set a fish on givern coordinates.
 * 
 * It is possible to add a <em>direction</em> parameter <code>getFish</code> or <code>setFish</code>, to get/set the
 * position one step in that direction.
 * 
 * The simulation step is entirely done in moveAllFish().
 */
public abstract class FishMatrix {

	/**
	 * the matrix itself
	 */
	private Fish[][] matrix;

	/**
	 * Constructs an empty fish matrix with the given dimensions.
	 * 
	 * @param height
	 *            the number of rows
	 * @param width
	 *            the number of columns
	 */
	public FishMatrix(int height, int width) {
		matrix = new Fish[height][width];
	}

	/**
	 * Returns the height (number of rows) of the matrix.
	 */
	public int getHeight() {
		return matrix.length;
	}

	/**
	 * Returns the width (number of columns) of the matrix.
	 */
	public int getWidth() {
		return matrix[0].length;
	}

	/**
	 * clears matrix from all fish
	 */
	public void clear() {
		for (int y = 0; y < getHeight(); ++y)
			for (int x = 0; x < getWidth(); ++x)
				matrix[y][x] = null;
	}

	/**
	 * tells whether the cell (x,y) exists in the matrix
	 * 
	 * @param coord
	 *            The coordinate to check.
	 * @return true, if this is a valid coordinate
	 */
	public boolean validCoordinate(Coordinate coord) {
		return (0 <= coord.y && coord.y < getHeight()) && (0 <= coord.x && coord.x < getWidth());
	}

	/**
	 * tells whether there is a cell in the direction dir from the cell (x,y) in the matrix
	 * 
	 * @param from
	 *            The start position.
	 * @param dir
	 *            The direction to go to.
	 * @return true if a move in that direction is possible (ignoring other fish)
	 */
	public boolean canMove(Coordinate from, Direction dir) {
		if (!validCoordinate(from))
			throw new IllegalArgumentException("invalid coordinates: " + from);

		if (getCoordinate(from, dir) == null)
			return false;
		return true;
	}

	/**
	 * Returns the fish on the coordinates <code>(x,y)</code>.
	 *
	 * @param coord
	 *            the coordinate
	 *
	 * @return A fish object or <code>null</code> if there is no fish in that cell
	 * @throws IllegalArgumentException
	 *             if the cell does not exist.
	 */
	public Fish getFish(Coordinate coord) throws IllegalArgumentException {
		if (!(validCoordinate(coord)))
			throw new IllegalArgumentException("invalid coordinates : " + coord);
		return (matrix[coord.y][coord.x]);
	}

	/**
	 * Stores a fish on the coordinates <code>(x,y)</code>. It's allowed to pass <code>null</code> in order to clear
	 * this cell and delete the fish which was stored.
	 *
	 * @param coord
	 *            The coordinate
	 * @param fish
	 *            the fish to be stored
	 * 
	 * @throws IllegalArgumentException
	 *             if the cell does not exist
	 */
	public void setFish(Coordinate coord, Fish fish) throws IllegalArgumentException {
		if (validCoordinate(coord))
			matrix[coord.y][coord.x] = fish;
		else
			throw new IllegalArgumentException("invalid coordinates : " + coord);
	}

	/**
	 * Returns the Coordinate of the cell reached when starting in cell <code>from</code> and moving one step in
	 * direction <code>dir</code>.
	 *
	 * @param from
	 *            the Coordinate where we start
	 * @param dir
	 *            the direction we want to go
	 *
	 * @return the resulting new Coordinate or null, if this move is not allowed
	 */
	public Coordinate getCoordinate(Coordinate from, Direction dir) {
		Coordinate to = Coordinate.fromOffset(dir);

		to.x += from.x;
		to.y += from.y;

		to.x = (to.x + getWidth()) % getWidth(); // horizontal is always >= 0 and modulo width
		// vertical is not modulo!

		if (validCoordinate(to))
			return to;

		return null;
	}

	/**
	 * Returns the fish in the cell reached when starting in cell <code>(x,y)</code> and moving one step in direction
	 * <code>direction</code>.
	 *
	 * @param from
	 *            The start position.
	 * @param direction
	 *            the direction
	 *
	 * @return A fish object or <code>null</code> if there is no fish in that cell
	 * @throws IllegalArgumentException
	 *             if the cell does not exist.
	 */
	public Fish getFish(Coordinate from, Direction direction) throws IllegalArgumentException {
		Coordinate to = getCoordinate(from, direction);

		if (to == null)
			throw new IllegalArgumentException("Invalid movement: from " + from + " direction " + direction);

		return matrix[to.y][to.x];

	}

	/**
	 * Moves a single fish one step, possibly overwriting any fish the fish moves into.
	 * 
	 * @param fish
	 *            The fish to set at the new location.
	 * @param from
	 *            The coordinate where the fish should be removed.
	 * @param to
	 *            The coordinate where the fish should be moved to.
	 * 
	 * @return The new coordinate of the fish. This does not necessarily have to be to the same as to.
	 */
	protected abstract Coordinate moveFish(Fish fish, Coordinate from, Coordinate to);

	/**
	 * Moves all fish one iteration, the most important method of this class.
	 */
	public abstract void moveAllFish();

	/**
	 * Returns the coordinate of a Fish object in the matrix. Note that this does not use equals(), but really looks for
	 * the same object.
	 * 
	 * @param fish
	 *            The fish to find.
	 * @return The coordinate or null.
	 */
	public Coordinate whereIsMyFish(Fish fish) {
		Coordinate where;
		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getHeight(); y++) {
				where = new Coordinate(x, y);
				if (getFish(where) == fish)
					return where;
			}

		return null;
	}

	/**
	 * Inserts a List of Fish randomly into the Matrix.
	 * 
	 * @param manyFish
	 *            The List containing all the Fish to be inserted.
	 */
	public void fillMatrix(LinkedList<Fish> manyFish) {
		if (manyFish.size() > getHeight() * getWidth())
			throw new IllegalArgumentException("Too many fish to put in this matrix!");

		// Clear the ocean.
		clear();

		Random rand = new Random();

		while (!manyFish.isEmpty()) {

			Fish fish = manyFish.removeFirst();

			boolean okay = false;
			do {
				int x = rand.nextInt(getWidth());
				int y = rand.nextInt(getHeight());
				Coordinate where = new Coordinate(x, y);

				if (getFish(where) == null) {
					setFish(where, fish);
					okay = true;
				}
			} while (!okay);
		} // end if
	}

	/**
	 * Passes the doSomething-command to the fish at the specified position. If there is no fish, it reports that.
	 * 
	 * @param where
	 *            The coordinate that was prompted to do something.
	 * @return The response of the fish or that there is an empty field.
	 */
	public String doSomething(Coordinate where) {
		Fish fish = getFish(where);
		if (fish != null) {
			return fish.doSomething(this);
		} else {
			return "An empty field.";
		}

	}
} // class FishMatrix
