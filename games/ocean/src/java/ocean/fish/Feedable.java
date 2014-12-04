package ocean.fish;

/**
 * An interface that designates a Fish as feedable. 
 * @author Andreas Wiese
 *
 */
public interface Feedable {
	
	/**
	 * Feeds a fish to a fish. Called when Feedable steps on Eatable.
	 * @param fish
	 */
	void feed(Eatable fish);
}
