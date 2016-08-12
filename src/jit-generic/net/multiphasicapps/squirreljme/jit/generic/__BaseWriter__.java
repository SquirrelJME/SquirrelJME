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
	
	/** Single stream output. */
	protected final ExtendedDataOutputStream output;
	
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
		
		// Get output
		ExtendedDataOutputStream output;
		this.output = (output = __nsw.__output());
		
		// Align to 4 bytes
		try
		{
			__nsw.__align();
		}
		
		// {@squirreljme.error BA0k Could not align the output.}
		catch (IOException e)
		{
			throw new JITException("BA0k", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void close()
		throws JITException
	{
		// Just call close on the writer
		this.owner.__close(this);
	}
}

