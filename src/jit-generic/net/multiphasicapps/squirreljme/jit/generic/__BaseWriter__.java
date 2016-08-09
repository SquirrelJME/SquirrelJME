// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This is the base class for class and resource writers, since they share
 * similar logic when it comes to output..
 *
 * @since 2016/07/27
 */
abstract class __BaseWriter__
	implements AutoCloseable
{
	/** Internal lock. */
	protected final Object lock;
	
	/** The owning namespace writer. */
	protected final GenericNamespaceWriter owner;
	
	/** Code output. */
	protected final ExtendedDataOutputStream outcode;
	
	/** Data output. */
	protected final ExtendedDataOutputStream outdata;
	
	/** The string table. */
	final __StringTable__ _strings;
	
	/** The import table. */
	final __Imports__ _imports;
	
	/**
	 * Initializes the base writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __name The name of this content entry.
	 * @param __ct The type of content to use for this entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	__BaseWriter__(GenericNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
		this.lock = __nsw._lock;
		this._strings = __nsw._strings;
		this._imports = __nsw._imports;
		
		// Get code and data
		this.outcode = __nsw.__code();
		this.outdata = __nsw.__data();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void close()
		throws JITException
	{
		// Close the raw output instead
		try
		{
			if (false)
				throw new IOException("TODO");
			if (true)
				throw new Error("TODO");
		}
		
		// {@squirreljme.error BA0c Failed to close the output.}
		catch (IOException e)
		{
			throw new JITException("BA0c", e);
		}
	}
}

