package padTree;

import padInterfaces.Valued;
import padList.LinkedList;

public class HuffmanToken implements Valued {

	private byte character;

	private int freqency;

	private LinkedList<Boolean> bitcode;

	public byte getCharacter() {
		return character;
	}

	public void setCharacter(byte character) {
		this.character = character;
	}

	public int getFreqency() {
		return freqency;
	}

	public void setFreqency(int freqency) {
		this.freqency = freqency;
	}

	public LinkedList<Boolean> getBitcode() {
		return bitcode;
	}

	public void setBitcode(LinkedList<Boolean> bitcode) {
		this.bitcode = bitcode;
	}

	@Override
	public double doubleValue() {
		// TODO
		return 0;
	}

}
