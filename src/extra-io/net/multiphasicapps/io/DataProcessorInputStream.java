// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is {@link InputStream} which uses a given {@link DataProcessor} with
 * bytes that are read from a wrapped stream to provide output for usage.
 *
 * @since 2016/03/11
 */
public class DataProcessorInputStream
	extends InputStream
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Input source. */
	protected final InputStream in;
	
	/** The data processor which handles input byte sources. */
	protected final DataProcessor processor;
	
	/**
	 * Initializes the pipe from the given input stream to the given processor.
	 *
	 * @param __in Input stream to read source bytes from.
	 * @param __dp The data processor for handling data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public DataProcessorInputStream(InputStream __in, DataProcessor __dp)
		throws NullPointerException
	{
		// Check
		if (__in == null || __dp == null)
			throw new NullPointerException();
		
		// Set
		in = __in;
		processor = __dp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the wrapped stream
		in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	public int read()
		throws IOException
	{
		throw new Error("TODO");
	}
}

