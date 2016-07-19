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
	/** The minumum size of the local header. */
	private static final int _MINIMUM_HEADER_SIZE =
		30;
	
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
		this.data = new ExtendedDataInputStream(q);
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
				byte lha = localheader[0], lhb = localheader[1],
					lhc = localheader[2], lhd = localheader[3];
				if (lha != 0x50 && lhb != 0x4B &&
					lhc != 0x03 && lhd != 0x04)
				{
					// The header could just be a few bytes away, so instead
					// of looping on single bytes skip a few if it is known
					// there will never be a local header there.
					input.read(localheader, 0, (lhb == 0x50 ? 1 :
						(lhc == 0x50 ? 2 : lhd == 0x50 ? 3 : 4)));
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
				
				throw new Error("TODO");
			}
		}
	}
}

