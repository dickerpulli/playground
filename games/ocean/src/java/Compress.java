import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import padInterfaces.Valued;
import padTree.HuffmanTree;
// import java.io.IOException;
// import java.io.BufferedInputStream;
// import padIo.BitInputFile;
//class HuffmanTree (in padTree, Aufgabe B) verwenden

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
		int[] freq = new int[256]; // Array mit H�ufigkeiten der einzelnen
									// Zeichen
		int inFileRead = inFile.read(); // = -1 wenn inFile "leer" und l�scht
										// jeweiliges Zeichen
		while (inFileRead != -1) {
			freq[inFileRead]++;
			inFileRead = inFile.read();
		}

		/***** Aufbau des Huffman - Baumes *****/
		Valued obj = null;
		HuffmanTree[] hufftree = new HuffmanTree[256];
		for (int i = 0; i < 256; i++) {
			// hufftree[i] = new HuffmanTree(freq[i]);
		}

		/***** Tests *****/
		for (int i = 0; i < 256; i++) {
			if (freq[i] != 0) {
				System.out.println((char) i + " " + freq[i]);
			}
		}

		System.out.println("Total file size to read (in bytes) : " + size);

		int content;
		while ((content = inFile.read()) != -1) {
			// convert to char and display it
			System.out.print((char) content);
		}
	}
}
