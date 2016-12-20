// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.streamproc;

import java.io.InputStream;
import java.io.IOException;

/**
 * This wraps an input stream and for any bytes read from the input it returns
 * as read data.
 *
 * @since 2016/12/20
 */
public class StreamProcessorInputStream
	extends InputStream
{
	/** Stream to read data from. */
	protected final InputStream in;
	
	/** The stream processor used. */
	protected final SteamProcessor processor;
	
	/**
	 * Initializes the input stream processor.
	 *
	 * @param __in Where to read data from.
	 * @param __out The processor used to process the data with.
	 * @since 2016/12/20
	 */
	public StreamProcessorInputStream(InputStream __in, SteamProcessor __sp)
		throws NullPointerException
	{
		// Check
		if (__in == null || __sp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.processor = __sp;
	}
}

