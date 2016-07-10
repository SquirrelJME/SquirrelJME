// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is an extended input stream which is better suited for general binary
 * data file reading compared to the standard {@link DataInputStream}.
 *
 * Streams default to big endian.
 *
 * @since 2016/07/10
 */
public class ExtendedDataInputStream
	extends InputStream
	implements DataInput, SettableEndianess
{
	/** The original input stream. */
	protected final DataInputStream input;
	
	/**
	 * Initializes the extended input stream.
	 *
	 * @param __is The stream to read data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/10
	 */
	public ExtendedDataInputStream(InputStream __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = ((__is instanceof DataInputStream) ?
			(DataInputStream)__is : new DataInputStream(__is));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void close()
		throws IOException
	{
		this.input.close();
	}
}

