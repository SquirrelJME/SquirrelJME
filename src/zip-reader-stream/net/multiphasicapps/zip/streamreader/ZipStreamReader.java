// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamreader;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.crc32.CRC32DataSink;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataInputStream;
import net.multiphasicapps.io.dynhistin.DynamicHistoryInputStream;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This class supports stream based reading of input ZIP files.
 *
 * @since 2016/07/19
 */
public class ZipStreamReader
	implements Closeable
{
	/** The maximum support version for extracting. */
	private static final int _MAX_EXTRACT_VERSION =
		20;
	
	/** The minumum size of the local header. */
	private static final int _MINIMUM_HEADER_SIZE =
		30;
	
	/** The local header magic number. */
	private static final int _LOCAL_HEADER_MAGIC =
		0x04034B50;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The dynamic history stream. */
	protected final DynamicHistoryInputStream input;
	
	/** This is used after an input structure is detected. */
	protected final ExtendedDataInputStream data;
	
	/** This can hold the local header except for the comment and filename. */
	private final byte[] _localheader =
		new byte[_MINIMUM_HEADER_SIZE];
	
	/** The current entry being read, cannot next entry if this is the case. */
	private volatile ZipStreamEntry _entry;
	
	/** End of file reached? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the reader for input ZIP file data.
	 *
	 * @param __is The input stream to source bytes from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public ZipStreamReader(InputStream __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		DynamicHistoryInputStream q;
		this.input = (q = new DynamicHistoryInputStream(__is));
		ExtendedDataInputStream d;
		this.data = (d = new ExtendedDataInputStream(q));
		d.setEndianess(DataEndianess.LITTLE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// Mark EOF
			this._eof = true;
			
			// Close the source
			this.input.close();
			this.data.close();
		}
	}
	
	/**
	 * Returns the next entry in the streamed ZIP file for {@code null} if no
	 * such entry exists.
	 *
	 * @return The next entry or {@code null} if there is none.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public ZipStreamEntry nextEntry()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BG01 An entry is currently being read, it
			// must first be closed.}
			if (this._entry != null)
				throw new IOException("BG01");
			
			// End of file reached?
			if (this._eof)
				return null;
			
			// Read until an entry is found
			DynamicHistoryInputStream input = this.input;
			byte[] localheader = this._localheader;
			for (;;)
			{
				// Peek the magic number
				int rhcount = input.peek(0, localheader, 0, 4);
			
				// Could not fit the magic number, treat as EOF
				if (rhcount < 4)
				{
					this._eof = true;
					return null;
				}
			
				// Does not match the magic number for local headers
				int lhskip = __skipLocalHeader(localheader);
				
				// Not one
				if (lhskip > 0)
				{
					input.read(localheader, 0, lhskip);
					continue;
				}
				
				// Read the rest of the header
				rhcount = input.peek(0, localheader);
				
				// EOF reached (cannot fit a local header in this many bytes)
				// Ignore the somewhat malformed ZIP since it could be part of
				// another file structure due to polyglots
				if (rhcount < _MINIMUM_HEADER_SIZE)
				{
					this._eof = true;
					return null;
				}
				
				// Check the version needed for extracting
				int xver = __readUnsignedShort(4);
				boolean deny = false;
				deny |= (xver < 0 || xver >= _MAX_EXTRACT_VERSION);
				
				// Read bit flags
				int gpfs = __readUnsignedShort(6);
				if (true)
					throw new Error("TODO");
				
				// Read the compression method
				int cmeth = __readUnsignedShort(8);
				if (true)
					throw new Error("TODO");
				
				// Read CRC32
				if (true)
					throw new Error("TODO");
				
				// Read Compressed size
				if (true)
					throw new Error("TODO");
				
				// Uncompressed size
				if (true)
					throw new Error("TODO");
				
				// File name length
				if (true)
					throw new Error("TODO");
				
				// Comment length
				if (true)
					throw new Error("TODO");
				
				// If denying, read a single byte and try again, this could
				// just be very ZIP-like data.
				if (deny)
				{
					this.input.read();
					continue;
				}
				
				// Consume the input bytes and create an entry
				throw new Error("TODO");
			}
		}
	}
	
	/**
	 * Reads an unsigned short from the given byte array.
	 *
	 * @param __b The byte array to read from.
	 * @param __p The position to read from.
	 * @return The read value.
	 * @since 2016/07/19
	 */
	private static int __readUnsignedShort(byte[] __b, int __p)
	{
		return (__b[__p] & 0xFF) |
			((__b[__p + 1] & 0xFF) << 8);
	}
	
	/**
	 * Checks if the specified buffer starts with the local header magic
	 * number and if not returns the number of bytes to skip.
	 *
	 * @param __b The bytes to check, from the zero index.
	 * @return Zero means this is the local header, otherwise a value up to 4.
	 * @since 2016/07/19
	 */
	private static int __skipLocalHeader(byte[] __b)
	{
		// Read values
		byte lha = __b[0], lhb = __b[1], lhc = __b[2], lhd = __b[3];
		
		// Is this the magic number?
		if (lha == 0x50 && lhb == 0x4B && lhc == 0x03 && lhd == 0x04)
			return 0;
		
		// Next byte over
		else if (lhb == 0x50 && lhc == 0x4B && lhd == 0x03)
			return 1;
		
		// Skip two bytes
		else if (lhc == 0x50 && lhd == 0x4B)
			return 2;
		
		// Last byte could be it
		if (lhd == 0x50)
			return 3;
		
		// None of them
		return 4;
	}
}

