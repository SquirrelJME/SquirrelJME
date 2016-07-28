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
	
	/** The name of this content entry. */
	protected final String contentname;
	
	/** Raw data output. */
	protected final OutputStream rawoutput;
	
	/** Start positon. */
	protected final long startpos;
	
	/**
	 * Initializes the base writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __name The name of this content entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	__BaseWriter__(GenericNamespaceWriter __nsw, String __name)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
		this.lock = __nsw._lock;
		this.contentname = __name;
		
		// Create raw output area
		synchronized (this.lock)
		{
			ExtendedDataOutputStream edos = __nsw.__output();
			OutputStream rawoutput = new __RawOutput__(edos);
			this.rawoutput = rawoutput;
	
			// Set start position
			this.startpos = edos.size();
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
		// Close the raw output instead
		try
		{
			this.rawoutput.close();
		}
		
		// {@squirreljme.error BA0c Failed to close the output.}
		catch (IOException e)
		{
			throw new JITException("BA0c", e);
		}
	}
	
	/**
	 * Raw output data.
	 *
	 * @since 2016/07/27
	 */
	private final class __RawOutput__
		extends OutputStream
	{
		/** The actual output. */
		protected final ExtendedDataOutputStream output;
		
		/** Has this been closed? */
		private volatile boolean _closed;
		
		/**
		 * Initializes the raw output.
		 *
		 * @param __edos The wrapped output stream.
		 * @throws NullPointerException On null arguments
		 * @since 2016/07/27
		 */
		private __RawOutput__(ExtendedDataOutputStream __edos)
			throws NullPointerException
		{
			// Check
			if (__edos == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.output = __edos;
			
			// Align to int
			try
			{
				synchronized (__BaseWriter__.this.lock)
				{
					while ((__edos.size() & 3) != 0)
						__edos.writeByte(0);
				}
			}
			
			// {@squirreljme.error BA0b Could not align the output.}
			catch (IOException e)
			{
				throw new JITException("BA0b", e);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/26
		 */
		@Override
		public void close()
			throws IOException
		{
			// Lock
			synchronized (__BaseWriter__.this.lock)
			{
				// Close if not closed
				if (!this._closed)
				{
					// mark closed
					this._closed = true;
					
					// Close in the writer
					__BaseWriter__.this.owner.__close(__BaseWriter__.this);
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/27
		 */
		@Override
		public void write(int __b)
			throws IOException
		{
			// Lock
			synchronized (__BaseWriter__.this.lock)
			{
				// {@squirreljme.error BA0f Cannot write a single byte after
				// the stream has been closed.}
				if (this._closed)
					throw new IOException("BA0f");
				
				// Write
				this.output.write(__b);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/27
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IOException
		{
			// Lock
			synchronized (__BaseWriter__.this.lock)
			{
				// {@squirreljme.error BA0g Cannot write multiple bytes after
				// the stream has been closed.}
				if (this._closed)
					throw new IOException("BA0g");
				
				// Write
				this.output.write(__b, __o, __l);
			}
		}
	}
}

