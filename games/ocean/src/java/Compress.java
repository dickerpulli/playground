import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import padTree.HuffmanToken;
import padTree.HuffmanTree;
// import java.io.IOException;
// import java.io.BufferedInputStream;
// import padIo.BitInputFile;
//class HuffmanTree (in padTree, Aufgabe B) verwenden
import padTree.HuffmanTree.Direction;

public class Compress {

	public static void main(String[] args) throws IOException {

		InputStream inFile = new BufferedInputStream(
				Compress.class.getResourceAsStream("/test.txt"));

		/*
		 * Methoden f�r inFile: available() - number of remaining bytes that can
		 * be read from this input stream close() - closes this file input
		 * stream finalize() - ensures that close() method is called
		 * getChannel() - returns the FileChannel object getFD() - returns the
		 * FileDescriptor object read() - reads a byte of a data from this input
		 * stream read(byte[] b) read up to b.length bytes of data into an array
		 * of bytes read(byte[] b, int off, int len) - reads up to len bytes
		 * skip(long n) - skips over and discards n bytes of data from input
		 * stream clone() equals() getClass() hashCode() notify() notifyAll()
		 * toString()
		 */

		/***** H�ufigkeiten z�hlen *****/
		// available() - anzahl der bytes
		int size = inFile.available();
		Map<Byte, Integer> frequencies = new HashMap<Byte, Integer>();// Map mit
																		// H�ufigkeiten
																		// der
																		// einzelnen
																		// Zeichen
		int inFileRead = inFile.read(); // = -1 wenn inFile "leer" und l�scht
										// jeweiliges Zeichen
		while (inFileRead != -1) {
			Integer frequency = frequencies.get((byte) inFileRead);
			frequency = frequency == null ? 1 : frequency + 1;
			frequencies.put((byte) inFileRead, frequency);

			inFileRead = inFile.read();
		}

		/***** Aufbau des Huffman - Baumes *****/
		HuffmanTree hufftree = new HuffmanTree(null);
		for (int i = 0; i < 256; i++) {
			hufftree.append(Direction.LEFT, new HuffmanToken());
		}

		/***** Tests *****/
		for (byte character : frequencies.keySet()) {
			Integer frequency = frequencies.get(character);
			System.out.println((char) character + " " + frequency);
		}

		System.out.println("Total file size to read (in bytes) : " + size);

		int content;
		while ((content = inFile.read()) != -1) {
			// convert to char and display it
			System.out.print((char) content);
		}
	}
}
