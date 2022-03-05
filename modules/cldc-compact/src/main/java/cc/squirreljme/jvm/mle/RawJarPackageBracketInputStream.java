// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an input stream over the raw representation of a
 * {@link JarPackageBracket} which might not be in the form of a JAR or any
 * actual readable data.
 *
 * @since 2022/03/04
 */
public class RawJarPackageBracketInputStream
	extends InputStream
{
	/** The given library. */
	protected final JarPackageBracket jar;
	
	/** The size of the JAR. */
	protected final int jarSize;
	
	/** Single byte read, as only bulk read is supported. */
	private final byte[] _singleByte =
		new byte[1];
	
	/** The current read position. */
	private int _readPos;
	
	/**
	 * Initializes the input stream to read the raw JAR.
	 * 
	 * @param __jar The JAR to read raw data from.
	 * @throws IOException If reading from the given JAR in its
	 * raw data form is not possible.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/04
	 */
	public RawJarPackageBracketInputStream(JarPackageBracket __jar)
		throws IOException, NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3u The specified JAR cannot be accessed
		// directly. (The JAR path)}
		int jarSize = JarPackageShelf.rawSize(__jar);
		if (jarSize < 0)
			throw new IOException("ZZ3u " +
				JarPackageShelf.libraryPath(__jar));
		
		// Set for later
		this.jar = __jar;
		this.jarSize = jarSize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/04
	 */
	@Override
	public int available()
		throws IOException
	{
		return this.jarSize - this._readPos;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/04
	 */
	@Override
	public int read()
		throws IOException
	{
		// Keep trying to read a single byte
		byte[] singleByte = this._singleByte;
		for (;;)
		{
			// Try reading byte
			int read = this.read(singleByte, 0, 1);
			
			// EOF?
			if (read < 0)
				return -1;
			
			// Use the given byte assuming it was read
			if (read != 0)
				return singleByte[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/04
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Read in the JAR data
		int readPos = this._readPos;
		int count = JarPackageShelf.rawData(this.jar, readPos, __b, __o, __l);
		
		// EOF?
		if (count < 0)
		{
			this._readPos = this.jarSize;
			return -1;
		}
		
		// Count up what we read
		this._readPos = readPos + count;
		
		// And use our count!
		return count;
	}
}
