package padIo;

/* You do not have to understand this code. Just use it.  If you make
   changes to this code let's hope you know what you are doing. */

import java.io.*;

/**
 * A BitOutputFile lets an application write data bit by bit in addition
 * to writing other integral data types.
 * Instances of this class are always in one of two states:
 * <ul>
 *  <li>bit mode, or
 *  <li>not in bit mode.
 * </ul>
 * Only in bit mode data can be written bit by bit.
 * Bit mode can the turned on by <code>beginBitMode</code> and then has to be
 * turned off by <code>endBitMode</code> after bit output has been done.
 * In bit mode only bit output is allowed. Bit mode cannot be nested though
 * it can be used several times consecutively in a file.
 * Upon calling <code>endBitMode</code> the last byte of the bit field 
 * is padded on its left hand side with zeros in case it has not been
 * filled completely by <code>writeBit</code>. 
 * See #main(String[]) below to learn how this class can be used.
 * 
 * @see BitInputFile
 *
 * @author Olaf Jahn
 */
public class BitOutputFile
{
  protected RandomAccessFile file;  /* We need random access as we have to 
				       write the length of each bit block in
				       front of it. The length is not known
				       beforehand. */
  protected boolean bitMode;         // True if we are in bit mode at the moment.
  protected int totalBits;           // Total number of bits in this bit mode.

  // We buffer output for better performance.
  protected static final int bufferSize = 1024;
  protected byte buffer[];
  protected int bitsInBuffer;

  protected long counterPosition; /* Index in file where at the end of bit mode
				     the number of bits has to be written. */

  /**
   * Creates a new bit output file and opens it for writing. After
   * creation bit mode is turned off.  If the file already existed it
   * gets overwritten.
   *
   * @param filename the filename of the file.
   * @exception IOException if an I/O error occurs.
   */   
  public BitOutputFile(String filename) throws IOException
  {
    // Opening via RandomAccessFile does not truncate the file. So delete it first.
    (new File(filename)).delete();  
    file = new RandomAccessFile(filename, "rw");
    bitMode = false;
    buffer = new byte[bufferSize];
    bitsInBuffer = 0;
    totalBits = 0;
  }


  /**
   * Close the file. All buffered data gets written first.
   * Afterwards this instance of <code>BitOutputFile</code>
   * cannot be used any more.
   *
   * @exception IOException if an I/O error occurs.
   */
  public void close() throws IOException
  {
    if(bitMode)
      endBitMode();
    file.close();
    file = null;
  }


  /**
   * Turn on bit mode. Now bits can be written via <a
   * href="#writeBit"><code>writeBit</code></a> Bit mode has to be
   * turned of by <a href="#endBitMode"><code>endBitMode</code></a> 
   *
   * @see #writeBit
   * @see #endBitMode
   * @exception if an I/O error occurs.
   */
  public void beginBitMode() throws IOException
  {
    if(bitMode)
      throw new IOException("Already in bit mode!");

    counterPosition = file.getFilePointer();
    bitsInBuffer = 0;
    totalBits = 0;
    file.writeInt(totalBits);  /* Write 0 for now. Gets overwritten in
				  endBitMode(). */
    bitMode = true;
  }

  /**
   * Turn off bit mode.
   *
   * @see #beginBitMode
   * @exception if an I/O error occurs.
   */
  public void endBitMode() throws IOException
  {
    if(!bitMode)
      throw new IOException("Not in bit mode!");
    
    // Set leading bits to zero.
    buffer[(int)(bitsInBuffer / 8)] &= 0xff >> (8 - bitsInBuffer % 8);
    
    // Write all remaining data of the buffer.	
    file.write(buffer, 0, (int)(bitsInBuffer / 8 + (bitsInBuffer % 8 > 0 ? 1 : 0)));
    bitsInBuffer = 0;
    
    /* Now write length of bit field at the correct position at the
       beginning of the field. */
    long currentPosition = file.getFilePointer();
    file.seek(counterPosition);
    file.writeInt(totalBits);
    file.seek(currentPosition);
    
    bitMode = false;
  }


  /**
   * Write a single bit. Bit mode has to be turned on.
   *
   * @param b the bit to write
   * @exception if an I/O error occurs.
   */
  public void writeBit(boolean b) throws IOException
  {
    if(!bitMode)
      throw new IOException("Bit mode not turned on.");

    int byteInBuffer = (int)(bitsInBuffer / 8);
    int bitInByte = (int)(bitsInBuffer % 8);

    // Clear bit.
    buffer[byteInBuffer] &= ~(1 << bitInByte);
    // Set bit according to b.
    buffer[byteInBuffer] |= (b ? 1 : 0) << bitInByte;
    bitsInBuffer++;
    totalBits++;
    if(bitsInBuffer / 8 == bufferSize)  // Buffer full.
      {
	file.write(buffer);
	bitsInBuffer = 0;
      }
  }


  /**
   * Write an <code>int</code> to the file. Cannot be used in bit mode.
   *
   * @param i the integer.
   * @exception if an I/O error occurs.
   */
  public void writeInt(int i) throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot write ints in bit mode!");
    
    file.writeInt(i);
  }


  /**
   * Write a <code>byte</code> to the file. Cannot be used in bit mode.
   *
   * @param b the byte as int.
   * @exception if an I/O error occurs.
   */
  public void writeByte(int b) throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot write bytes in bit mode!");
    if(b >= 256 || b < 0)
      throw new IOException("Byte is not in rage [0, 255].");
    
    file.writeByte(b);
  }


  /**
   * Write a <code>long</code> to the file. Cannot be used in bit mode.
   *
   * @param l the long.
   * @exception if an I/O error occurs.
   */
  public void writeLong(long l) throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot write longs in bit mode!");
    
    file.writeLong(l);
  }

  /**
   * Write a <code>boolean</code> to the file. Cannot be used in bit mode.
   * The <code>boolean</code> is written as one byte with the value
   * 1 if the boolean is <code>true</code> and 0 otherwise.
   *
   * @param b the boolean.
   * @exception if an I/O error occurs.
   */
  public void writeBoolean(boolean b) throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot write booleans in bit mode!");
    
    file.writeBoolean(b);
  }


  /**
   * Close the file. This method is ONLY meant to be called
   * by the Garbage Collector. NEVER attempt to call it yourself;
   * use <a href="#close"><code>close</code></a> instead.
   */
  public void finalize() throws IOException
  {
    if(file != null)
      file.close();
    file = null;
  }


  /**
   * Test these methods by generating a file full of random bits.
   * The binary file is names <code>argv[0]</code>.
   * In addition an ASCII representation of the same
   * data is written to <code>argv[0] + ".ascout"</code>.
   * The main routine of <code>BitInputFile</code> reads in
   * the binary file and generates an ASCII file of its own.
   * Then both ASCII files should have identical contents (test with cmp).
   *
   * @param argv Arguments: filename and length of bit field.
   */
  public static void main(String argv[])
  {
    if(argv.length != 2)
      {
	System.err.println("Two arguments exspected: filename and length.");
	return;
      }
    try
      {
	String filename = argv[0];
	int length = Integer.parseInt(argv[1]);
       
	// Open two files.
	BitOutputFile bout = new BitOutputFile(filename);  // For the binary data.
	PrintWriter aout = new PrintWriter(new FileOutputStream(filename + ".ascout"));

	for(int c = 0; c < 3; c++) /* Switch three times between bit field and
				      other data. */
	  {
	    // First write some random numbers with the other routines.
	    int i = (int)(Math.random() * Integer.MAX_VALUE);
	    aout.println(i);
	    bout.writeInt(i);
	    
	    long l = (long)(Math.random() * Long.MAX_VALUE);
	    aout.println(l);
	    bout.writeLong(l);
	    
	    int b = (int)(Math.random() * Byte.MAX_VALUE);
	    aout.println(b);
	    bout.writeByte(b);
	    
	    boolean bo = (int)(Math.random() * 10) % 2 != 0 ? true : false;
	    aout.println(bo);
	    bout.writeBoolean(bo);
	    
	    // Now generate bits.
	    bout.beginBitMode();
	    for(i = 0; i <length; i++)
	      {
		bo = (int)(Math.random() * 10) % 2 != 0 ? true : false;
		bout.writeBit(bo);
		aout.print(bo ? "1" : "0");
	      }
	    bout.endBitMode();
	  }

	/* These two lines are important. */
	aout.close();
	bout.close();
      }
    catch(IOException e)
      {
	System.err.println("IOException: " + e);
	return;
      }
    catch(NumberFormatException e)
      {
	System.err.println("NumberFormatException: " + e);
	return;
      }
  }
}
