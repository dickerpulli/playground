package padTree;

import padInterfaces.Valued;
import padList.LinkedList;

public class HuffmanToken implements Valued {

	private final byte character;

	private final int freqency;

	private final LinkedList<Boolean> bitcode;

	public HuffmanToken(byte character, int freqency,
			LinkedList<Boolean> bitcode) {
		this.character = character;
		this.freqency = freqency;
		this.bitcode = bitcode;
	}

	public byte getCharacter() {
		return character;
	}

	public int getFreqency() {
		return freqency;
	}

	public LinkedList<Boolean> getBitcode() {
		return bitcode;
	}

	@Override
	public double doubleValue() {
		// TODO
		return 0;
	}

}
