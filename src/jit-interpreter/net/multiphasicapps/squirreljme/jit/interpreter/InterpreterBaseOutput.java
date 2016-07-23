// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.interpreter;

import java.io.DataOutputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.os.interpreter.ContentType;

/**
 * This class acts as the base for interpreter outputs to either classes or
 * resources.
 *
 * @since 2016/07/22
 */
public abstract class InterpreterBaseOutput
	implements AutoCloseable
{
	/** The owning namespace writer. */
	protected final InterpreterNamespaceWriter writer;
	
	/** Lock. */
	protected final Object lock;
	
	/** The output. */
	protected final DataOutputStream output;
	
	/** The position where the data starts. */
	protected final int datastart;
	
	/** The name to write. */
	protected final String name;
	
	/** The content type to use. */
	protected final ContentType contenttype;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the base output.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __name The name of thing being written.
	 * @param __ct The content type to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public InterpreterBaseOutput(InterpreterNamespaceWriter __nsw,
		String __name, ContentType __ct)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.writer = __nsw;
		this.lock = __nsw.interpreterLock();
		DataOutputStream dos = __nsw.interpreterOutput();
		this.output = dos;
		this.name = __name;
		this.contenttype = __ct;
		
		// Get starting point
		synchronized (this.lock)
		{
			this.datastart = dos.size();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public void close()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Handle closing
			if (!this._closed)
			{
				// Set closed
				this._closed = true;
				
				// Get the end position, to determine the size
				int datastart = this.datastart;
				DataOutputStream output = this.output;
				int dataend = output.size();
				
				// {@squirreljme.error BV06 The size of a given content is
				// negative. It either ends before it starts or the namespace
				// exceeds 2GiB in size. (The content size)}
				int datasize = dataend - datastart;
				if (datasize < 0)
					throw new JITException(String.format("BV06 %s", datasize));
				
				// Get content type
				ContentType contenttype = this.contenttype;
				
				// Write content entry link
				try
				{
					// Round to a multiple of 4
					while ((output.size() & 3) != 0)
						output.writeByte(0);
					
					// Write the content start and size
					output.writeInt(datastart);
					output.writeInt(datasize);
					
					// Write string
					InterpreterNamespaceWriter nsw = this.writer;
					output.writeInt(nsw.interpreterAddString(this.name));
					
					// The type is just the ordinal value
					output.writeByte(contenttype.ordinal());
					
					// Close on the upper end
					nsw.__close(this);
				}
				
				// {@squirreljme.error BV09 Failed to write content entry
				// link.}
				catch (IOException e)
				{
					throw new JITException("BV09", e);
				}
			}
		}
	}
	
	/**
	 * Is writing closed?
	 *
	 * @return {@code true} if writing is closed.
	 * @since 2016/07/23
	 */
	public final boolean isClosed()
	{
		return this._closed;
	}
}

