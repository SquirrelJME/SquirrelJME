// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.jvm.javase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This reads a stream of hexadecimal characters and converts it to a binary
 * input.
 *
 * @since 2016/05/15
 */
public class HexInputStream
	extends InputStream
{
	/** The source stream. */
	protected final Reader source;
	
	/** EOF? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the hex based input stream.
	 *
	 * @param __s The source hex bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/15
	 */
	public HexInputStream(Reader __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		source = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/15
	 */
	@Override
	public void close()
		throws IOException
	{
		// Always set EOF
		_eof = true;
		
		// Close it
		source.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/15
	 */
	@Override
	public int read()
		throws IOException
	{
		// EOF?
		if (_eof)
			return -1;
		
		// Read the high value
		int hi = -1;
		for (; hi < 0;)
		{
			// Read 
			int c = source.read();
			
			// EOF?
			if (c < 0)
				return -1;
			
			// Try a digit
			hi = Character.digit((char)c, 16);
		}
		
		// Read the low value
		int lo = -1;
		for (; lo < 0;)
		{
			// Read 
			int c = source.read();
			
			// EOF?
			if (c < 0)
				return -1;
			
			// Try a digit
			lo = Character.digit((char)c, 16);
		}
		
		// Merge together
		return (hi << 4) | lo;
	}
}

