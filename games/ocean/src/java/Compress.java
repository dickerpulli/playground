import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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

		// Sortiere nach Häufigkeit
		frequencies = sortByValue(frequencies);

		/***** Aufbau des Huffman - Baumes *****/
		HuffmanTree hufftree = new HuffmanTree(null);
		Direction lastDirection = Direction.RIGHT;
		for (byte character : frequencies.keySet()) {
			Integer frequency = frequencies.get(character);
			padList.LinkedList<Boolean> bitcode = null;
			Direction direction = lastDirection == Direction.RIGHT ? Direction.LEFT
					: Direction.RIGHT;
			hufftree.append(direction, new HuffmanToken(character, frequency,
					bitcode));
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

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Collections.reverse(list);

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
