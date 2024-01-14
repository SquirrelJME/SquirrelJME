// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that is capable of reading data sub-blocks within GIFs
 * as a single sequence of bytes. Closing the stream will advance the stream.
 *
 * @since 2022/07/02
 */
final class __GIFDataSubBlockInputStream__
	extends InputStream
{
	/** The stream input. */
	private final DataInputStream _in;
	
	/** Data left in current block. */
	private int _leftInBlock;
	
	/** Was EOF reached? */
	private boolean _reachedEof;
	
	/**
	 * Initializes the sub block reader.
	 * 
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/02
	 */
	public __GIFDataSubBlockInputStream__(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this._in = ((__in instanceof DataInputStream) ? (DataInputStream)__in :
			new DataInputStream(__in));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/02
	 */
	@Override
	public void close()
		throws IOException
	{
		for (;;)
		{
			// Stop on EOF
			if (this.read() < 0)
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/02
	 */
	@Override
	public int read()
		throws IOException
	{
		// EOF already reached?
		if (this._reachedEof)
			return -1;
		
		DataInputStream in = this._in;
		
		// Read loop since we need to retry
		for (;;)
		{
			// Read single available byte?
			int leftInBlock = this._leftInBlock;
			if (leftInBlock > 0)
			{
				int result = in.readUnsignedByte();
				
				this._leftInBlock = leftInBlock - 1;
				
				return result;
			}
			
			// Try to read more bytes from the block
			leftInBlock = in.readUnsignedByte();
			this._leftInBlock = leftInBlock;
			
			// Reached EOF condition?
			if (leftInBlock == 0)
			{
				this._reachedEof = true;
				return -1;
			}
		}
	}
}
