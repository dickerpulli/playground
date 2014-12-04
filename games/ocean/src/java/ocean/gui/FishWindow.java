package ocean.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ocean.core.FishMatrix;
import ocean.core.FishMatrixComplex;
import ocean.core.FishMatrixOptional;
import ocean.core.FishMatrixSimple;

/**
 * The GUI and main program for the fish simulation.
 *
 * @author Martin Oellrich, Daniel Dressler
 */
public class FishWindow extends Frame {
	/**
	 * Not important ...
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <code>OceanCanvas</code> is a <code>Component</code> and can be added to the window. It draws everything and
	 * controls the background animations.
	 */
	OceanCanvas oceanCanvas;

	/**
	 * A status bar
	 */
	private Label status;

	/**
	 * A matrix of fish, allocated in init()
	 */
	FishMatrix fishMatrix;

	/**
	 * Dimensions of the ocean matrix
	 */
	final int numberOfRows = 10;
	final int numberOfColumns = 20;

	/**
	 * Do we want a grid to be drawn onto the Ocean?
	 */
	boolean wantGrid = false;

	/**
	 * TextFields for input and output of numbers
	 */
	TextField herringInput;
	TextField sharkInput;
	TextField tunaInput;

	/*** methods ***/

	/**
	 * The main method to launch the application.
	 */
	public static void main(String args[]) {
		// create an instance
		FishWindow ocean = new FishWindow();

		// initialize the GUI
		ocean.init();

		// show the GUI
		ocean.setVisible(true);

		// start the animation
		ocean.start();

		/* The main method is finished, but the window keeps the program running. */
	}

	/**
	 * add the GUI elements to the north panel
	 */
	protected void add_north(Panel panel) {

		// A checkbox to enable us to switch the grid on and off.
		Checkbox gridbox = new Checkbox("Grid", wantGrid);
		gridbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				wantGrid = !wantGrid;
				oceanCanvas.setGrid(wantGrid);
			}
		});
		panel.add(gridbox);

		/*
		 * Entering these numbers fills the matrix with a random configuration of fish.
		 */
		Label herringLabel = new Label("Herrings:");
		herringInput = new TextField("5", 3);
		Label sharkLabel = new Label("Sharks:");
		sharkInput = new TextField("5", 3);
		Label tunaLabel = new Label("Young Tunas:");
		tunaInput = new TextField("5", 3);

		panel.add(herringLabel);
		panel.add(herringInput);
		panel.add(sharkLabel);
		panel.add(sharkInput);
		panel.add(tunaLabel);
		panel.add(tunaInput);

		// Clicking this button creates a new matrix
		Button createSimpleButton = new Button("Simple");
		createSimpleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillMatrixSimple();
			}
		});
		panel.add(createSimpleButton);

		// Clicking this button creates a new matrix
		Button createComplexButton = new Button("Complex");
		createComplexButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillMatrixComplex();
			}
		});
		panel.add(createComplexButton);

		// Clicking this button creates a new matrix
		Button createOptionalButton = new Button("Optional");
		createOptionalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillMatrixOptional();
			}
		});
		panel.add(createOptionalButton);

		// Clicking this button causes the fish to move one step (or whatever they do)
		Button moveButton = new Button("Move");
		moveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveAllFish();
			}
		});
		panel.add(moveButton);

	} // add_north()

	/**
	 * add GUI elements to south panel
	 * 
	 */
	protected void add_south(Panel panel) {
		// A long status bar, we might need it.
		status = new Label("                                                                        ");

		panel.add(status);

	} // add_south()

	/**
	 * Init the GUI. This has to be called manually. Try not to override this method. Instead use add_south and
	 * add_north to add new buttons, textfields etc, if necessary.
	 */
	public void init() {

		/* We want to be able to close the program. */
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				exit();
			}
		});

		setLayout(new BorderLayout());

		// No matrix yet.
		fishMatrix = null;

		/* The ocean needs the dimensions of the matrix is has to display. */
		oceanCanvas = new OceanCanvas(this, numberOfRows, numberOfColumns);

		// The ocean goes into the center.
		add("Center", oceanCanvas);

		// Panel for the "North".
		Panel panel = new Panel(new FlowLayout(FlowLayout.LEFT));
		add_north(panel);

		// the panel is done, now place it in the Frame
		add("North", panel);

		// Panel for the "South"
		panel = new Panel(new FlowLayout(FlowLayout.LEFT));
		add_south(panel);

		// the panel is done, now place it in the Frame
		add("South", panel);

		this.pack(); // sets the size automagically

	} // init()

	/**
	 * Fill the simple matrix with a random fish configuration
	 */
	protected void fillMatrixSimple() {
		status.setText("Using FishMatrixSimple.");

		int numberHerrings;
		int numberSharks;
		try {
			numberHerrings = Integer.parseInt(herringInput.getText());
			numberSharks = Integer.parseInt(sharkInput.getText());
		} catch (NumberFormatException e1) {
			status.setText("Those numbers are bad!");
			return;
		}

		FishMatrixSimple matrix = new FishMatrixSimple(numberOfRows, numberOfColumns);

		matrix.fillMatrix(numberHerrings, numberSharks);

		fishMatrix = matrix;
		oceanCanvas.setMatrix(fishMatrix);
	}

	/**
	 * Fill the complex matrix with a random fish configuration
	 */
	protected void fillMatrixComplex() {
		status.setText("Using FishMatrixComplex.");

		int numberHerrings;
		int numberSharks;
		try {
			numberHerrings = Integer.parseInt(herringInput.getText());
			numberSharks = Integer.parseInt(sharkInput.getText());
		} catch (NumberFormatException e1) {
			status.setText("Those numbers are bad!");
			return;
		}

		FishMatrixComplex matrix = new FishMatrixComplex(numberOfRows, numberOfColumns);

		matrix.fillMatrix(numberHerrings, numberSharks);

		fishMatrix = matrix;
		oceanCanvas.setMatrix(fishMatrix);
	}

	/**
	 * Fill the optional matrix with a random fish configuration
	 */
	protected void fillMatrixOptional() {
		status.setText("Using FishMatrixOptional.");

		int numberHerrings;
		int numberSharks;
		int numberTuna;
		try {
			numberHerrings = Integer.parseInt(herringInput.getText());
			numberSharks = Integer.parseInt(sharkInput.getText());
			numberTuna = Integer.parseInt(tunaInput.getText());
		} catch (NumberFormatException e1) {
			status.setText("Those numbers are bad!");
			return;
		}

		FishMatrixOptional matrix = new FishMatrixOptional(numberOfRows, numberOfColumns);

		matrix.fillMatrix(numberHerrings, numberSharks, numberTuna);

		fishMatrix = matrix;
		oceanCanvas.setMatrix(fishMatrix);
	}

	/**
	 * Moves all fish one iteration. This is the core of this class, which triggers the interesting things.
	 */
	protected void moveAllFish() {
		fishMatrix.moveAllFish();
		oceanCanvas.setMatrix(fishMatrix);
	}

	/**
	 * Set the status bar.
	 * 
	 * @param text
	 *            The text to display.
	 */
	public void setStatus(String text) {
		status.setText(text);
	}

	/**
	 * Called to start the animation of the pretty oceanCanvas
	 */
	public void start() {
		oceanCanvas.start();
	}

	/**
	 * Called to stop the animation of the pretty oceanCanvas, until it is started again. Not really needed right now.
	 */
	public void stop() {
		oceanCanvas.stop();
	}

	/**
	 * Called to exit the program. Makes sure to stop the animation (thread) of the oceanCanvas.
	 */
	public void exit() {
		oceanCanvas.destroy();
		System.exit(0);
	}

}
