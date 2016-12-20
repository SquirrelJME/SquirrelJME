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

import java.io.IOException;
import java.io.OutputStream;

/**
 * This wraps a stream processor so that any data written to the output stream
 * is ran through the processor and sent to the output stream.
 *
 * @since 2016/12/20
 */
public final class StreamProcessorOutputStream
	extends OutputStream
{
	/** Output stream to write to. */
	protected final OutputStream out;
	
	/** The stream processor used. */
	protected final SteamProcessor processor;
	
	/**
	 * Initializes the output stream processor.
	 *
	 * @param __os The output stream to write data to.
	 * @param __sp The stream processor used to process the data.
	 * @since 2016/12/20
	 */
	public StreamProcessorOutputStream(OutputStream __os, StreamProcessor __sp)
		throws NullPointerException
	{
		// Check
		if (__os == null || __sp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __os;
		this.processor = __sp;
	}
}

