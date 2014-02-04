package de.tbosch.tools.calculator.gui;

import java.text.NumberFormat;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import de.tbosch.tools.calculator.service.CalculatorService;
import de.tbosch.tools.calculator.service.Operator;

public class CalculatorController {

	private static final NumberFormat FORMAT = NumberFormat
			.getInstance(Locale.US);
	static {
		FORMAT.setGroupingUsed(false);
	}

	@FXML
	private TextField text;

	private final CalculatorService calculator = new CalculatorService();

	@FXML
	public void button1Clicked(ActionEvent event) {
		double number = calculator.appendNumber(1);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button2Clicked(ActionEvent event) {
		double number = calculator.appendNumber(2);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button3Clicked(ActionEvent event) {
		double number = calculator.appendNumber(3);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button4Clicked(ActionEvent event) {
		double number = calculator.appendNumber(4);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button5Clicked(ActionEvent event) {
		double number = calculator.appendNumber(5);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button6Clicked(ActionEvent event) {
		double number = calculator.appendNumber(6);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button7Clicked(ActionEvent event) {
		double number = calculator.appendNumber(7);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button8Clicked(ActionEvent event) {
		double number = calculator.appendNumber(8);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button9Clicked(ActionEvent event) {
		double number = calculator.appendNumber(9);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void button0Clicked(ActionEvent event) {
		double number = calculator.appendNumber(0);
		text.setText(FORMAT.format(number));
	}

	@FXML
	public void buttonPointClicked(ActionEvent event) {
		calculator.appendPoint();
	}

	@FXML
	public void buttonEqualsClicked(ActionEvent event) {
		double number = calculator.solve();
		if (Double.isNaN(number)) {
			text.setText("ERROR");
		} else {
			text.setText(FORMAT.format(number));
		}
	}

	@FXML
	public void buttonPlusClicked(ActionEvent event) {
		calculator.call(Operator.PLUS);
	}

	@FXML
	public void buttonMinusClicked(ActionEvent event) {
		calculator.call(Operator.MINUS);
	}

	@FXML
	public void buttonMultiplyClicked(ActionEvent event) {
		calculator.call(Operator.MULTIPLY);
	}

	@FXML
	public void buttonDivideClicked(ActionEvent event) {
		calculator.call(Operator.DIVIDE);
	}

	@FXML
	public void buttonClearClicked(ActionEvent event) {
		calculator.reset();
		text.setText("0");
	}

}
