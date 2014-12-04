package ocean.gui;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * A enum-type which knows (and loads) all the images.
 * @author dressler
 *
 */
public enum ImageData {

	// only a single image for the following
	HERRING("herring.gif"),
	SHARK("shark.gif"),
	TUNA_YOUNG("tuna1.gif"),
	TUNA_OLD("tuna2.gif"),
	SUBMARINE("submarine.gif"),
	
	// animated objects have lists of images
	WHALE( new String[] {
		"whale_1.gif", "whale_2.gif", "whale_3.gif", "whale_4.gif",
		"whale_5.gif", "whale_6.gif", "whale_7.gif", "whale_8.gif", 
		"whale_9.gif", "whale_10.gif", "whale_11.gif"
	} ),
	
	BUBBLE( new String[]  { "bubble1.gif", "bubble2.gif", 
			"bubble3.gif", "bubble4.gif"}),
	
	SHIP( new String[] 	 
	{
			"ship_1.gif", "ship_2.gif", "ship_3.gif", "ship_4.gif"
	}),

	WEED1( new String[] { "weed1_1.gif", "weed1_2.gif", "weed1_3.gif", "weed1_4.gif",
			"weed1_5.gif", "weed1_6.gif", "weed1_7.gif" }
	),
	
	WEED2( new String[] { "weed2_1.gif", "weed2_2.gif", "weed2_3.gif", "weed2_4.gif",
			"weed2_5.gif", "weed2_6.gif", "weed2_7.gif", "weed2_8.gif",
			"weed2_9.gif", "weed2_10.gif", "weed2_11.gif", "weed2_12.gif",
			"weed2_13.gif", "weed2_14.gif", "weed2_15.gif", "weed2_16.gif" }
	),
	
	WEED3( new String[] { "weed3_1.gif", "weed3_2.gif", "weed3_3.gif", "weed3_4.gif", 
			"weed3_5.gif", "weed3_6.gif", "weed3_7.gif", "weed3_8.gif",
			"weed3_9.gif", "weed3_10.gif", "weed3_11.gif", "weed3_12.gif",
			"weed3_13.gif", "weed3_14.gif"}
	);
	
	/** Now routines follow for _each_ of the values above! **/
	
	// the array to store the picture list in
	private Image[] images;
	
	// Base directory for the Fish images.
	private final String baseDirectory = "src/ocean/gifs/";
	
    // constructor from a single image
	private ImageData(String filename) {		
		images = new Image[1];
		images[0] = new ImageIcon(baseDirectory + filename).getImage();		
	}

    // constructor from many images
	private ImageData(String[] filenames) {
		images = new Image[filenames.length];
		for (int i = 0; i < filenames.length; i++) {
			images[i] = new ImageIcon(baseDirectory + filenames[i]).getImage();
		}
	}
	
	// get the first image
	public Image getImage() {
		return images[0];
	}
	
	// get all images
	public Image[] getAllImages() {
		return images;
	}
	
	// get the number of images
	public int getLength() {
		return images.length;
	}

}
