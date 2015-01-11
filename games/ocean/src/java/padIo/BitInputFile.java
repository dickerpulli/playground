package padIo;

/* You do not have to understand this code. Just use it.  If you make
   changes to this code let's hope you know what you are doing. */

import java.io.*;

/**
 * A BitInputFile lets an application read data bit by bit in addition
 * to reading other integral data types. It is meant
 * for files written using <code>BitOutputFile</code>.
 * You have to read your data IN THE SAME ORDER it was written into the file.
 * Like the <code>BitOutputFile</code>Instances of this class are always 
 * in one of two states:
 * <ul>
 *  <li>bit mode, or
 *  <li>not in bit mode.
 * </ul>
 * Only in bit mode data can be read bit by bit.
 * Bit mode can the turned on by <code>beginBitMode</code> and then has to be
 * turned off by <code>endBitMode</code> after bit input has been done.
 * In bit mode only bit input is allowed. Bit mode cannot be nested though
 * it can be used several times consecutively in a file.
 * See #main(String[]) below to learn how this class can be used.
 * 
 * @see BitOutputFile
 *
 * @author Olaf Jahn
 */
public class BitInputFile
{
  protected DataInputStream file; 
  protected boolean bitMode;         // True if we are in bit mode at the moment.
  protected int totalUnreadBits;    // Total number of bits in this bit mode.

  // We buffer input for better performance.
  protected static final int bufferSize = 1024;
  protected byte buffer[];
  protected int unreadBitsInBuffer;
  protected int nextBitInBuffer;

  /**
   * Creates a new bit input file and opens it for reading. After
   * creation bit mode is turned off.
   *
   * @param filename the filename of the file.
   * @exception IOException if an I/O error occurs.
   */   
  public BitInputFile(String filename) throws IOException
  {
    file = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));
    bitMode = false;
    buffer = new byte[bufferSize];
    unreadBitsInBuffer = 0;
    totalUnreadBits = 0;
  }


  /**
   * Close the file. All buffered data is discarded.
   * Afterwards this instance of <code>BitInputFile</code>
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
   * Turn on bit mode and return the number of bits
   * that can be read. After reading these bytes by
   * <code>readBit</code>, the bit mode has to be turned 
   * off by </code>endBitMode</code>.
   * 
   * @see #readBit
   * @see #endBitMode
   * @return number of bits to be read.
   * @exception IOException if an I/O error occurs.
   */
  public int beginBitMode() throws IOException
  {
    if(bitMode)
      throw new IOException("Bit mode already turned on.");
    
    totalUnreadBits = file.readInt();
    unreadBitsInBuffer = 0;
    bitMode = true;
    
    return totalUnreadBits;
  }

  /**
   * Turn off bit mode. Calling this when not all
   * bits have been read causes an IOException.
   *
   * @see #beginBitMode
   * @exception IOException if an I/O error occurs.
   */
  public void endBitMode() throws IOException
  {
    if(!bitMode)
      throw new IOException("Not in bit mode!");
    
    if(totalUnreadBits > 0)
      throw new IOException("Not all bits read!");
    
    bitMode = false;
  }

  /** 
   * Returns the number of bytes that can be read from this input stream. 
   *
   * @return the number of bytes that can be read. 
   * @throws IOException if an I/O error occurs. 
   */
  public int available() throws IOException { return file.available(); }

  /**
   * Give the number of bits remaining to be read in this bit mode.
   * May only be called in bit mode.
   *
   * @see #beginBitMode
   * @return number of bits left.
   * @exception IOException if an I/O error occurs.
   */
  public int bitsLeft() throws IOException
  {
    if(!bitMode)
      throw new IOException("Not in bit mode!");
    
    return totalUnreadBits;
  }
   

  /**
   * Read a bit in bit mode.
   *
   * @see #beginBitMode
   * @return the value of the bit.
   * @exception IOException if an I/O error occurs.
   */
  public boolean readBit() throws IOException
  {
    if(!bitMode)
      throw new IOException("Not in bit mode!");

    if(totalUnreadBits == 0)
      throw new IOException("No bits left to read!");

   if(unreadBitsInBuffer == 0)
     {
       // Fill buffer.
       int bytesRemaining = (int)(totalUnreadBits / 8) 
	 + (totalUnreadBits % 8 > 0 ? 1 : 0);
       int bytesToReadNow;

       if(totalUnreadBits < buffer.length * 8)
	 {
	   // Cannot even fill buffer.
	   unreadBitsInBuffer = totalUnreadBits;
	   bytesToReadNow = bytesRemaining;
	 }
       else
	 {
	   unreadBitsInBuffer = buffer.length * 8;
	   bytesToReadNow = buffer.length;
	 }

       file.readFully(buffer, 0, bytesToReadNow);
       nextBitInBuffer = 0;
     }

   boolean result =  
     (buffer[(int)(nextBitInBuffer / 8)] 
      & (1 << (nextBitInBuffer % 8))) != 0 ? true : false;
   totalUnreadBits--;
   unreadBitsInBuffer--;
   nextBitInBuffer++;
   return result;
  }

  
  /**
   * Read an <code>int</code> from the file. Cannot be used in bit mode.
   *
   * @return the int read from the file.
   * @exception IOException if an I/O error occurs.
   */
  public int readInt() throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot read ints in bit mode!");
    return file.readInt();
  }


  /**
   * Read a <code>byte</code> from the file. Cannot be used in bit mode.
   * The byte is returned as int in the range 0..255.
   *
   * @return the byte read from the file.
   * @exception IOException if an I/O error occurs.
   */
  public int readByte() throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot read bytes in bit mode!");
    return file.readUnsignedByte();
  }


  /**
   * Read a <code>long</code> from the file. Cannot be used in bit mode.
   *
   * @return the long read from the file.
   * @exception IOException if an I/O error occurs.
   */
  public long readLong() throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot read bytes in bit mode!");
    return file.readLong();
  }


  /**
   * Read a <code>boolean</code> from the file. Cannot be used in bit mode.
   * A whole byte is read. If its value is 0 then <code>false</code> is
   * returned, otherwise <code>true</code>.
   *
   * @return the boolean read from the file.
   * @exception IOException if an I/O error occurs.
   */
  public boolean readBoolean() throws IOException
  {
    if(bitMode)
      throw new IOException("Cannot read bytes in bit mode!");
    return file.readBoolean();
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
   * Test these methods by generating a file generated by
   * <code>BitOutputFile.main</code>. Print the contents
   * in ASCII format to <code>argv[0] + ".ascin"</code>.
   *
   * @see BitOutputFile#main(String[])
   * @param argv Arguments: filename.
   */
  public static void main(String argv[])
  {
    if(argv.length != 1)
      {
	System.err.println("One argument exspected: filename.");
	return;
      }
    try
      {
	String filename = argv[0];

	// Open two files.
	BitInputFile bin = new BitInputFile(filename);  // For the binary data.
	PrintWriter aout = new PrintWriter(new FileOutputStream(filename + ".ascin"));

	for(int c = 0; c < 3; c++) /* Switch three times between bit field and
				      other data. */
	  {
	    // First read some data numbers with the other routines.
	    int i = bin.readInt();
	    aout.println(i);
	    
	    long l = bin.readLong();
	    aout.println(l);
	    
	    int b = bin.readByte();
	    aout.println(b);
	    
	    boolean bo = bin.readBoolean();
	    aout.println(bo);
	    
	    // Now read bit field.
	    bin.beginBitMode();
	    while(bin.bitsLeft() > 0)
	      {
		bo = bin.readBit();
		aout.print(bo ? "1" : "0");
	      }
	    bin.endBitMode();
	  }

	aout.close();
	bin.close();
      }
    catch(IOException e)
      {
	System.err.println("IOException: " + e);
	return;
      }
  }
}
