// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.javac.base.CompilerOutput;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This implements the compiler output which writes to a ZIP file for any
 * classes that were compiled by the compiler.
 *
 * @since 2016/09/18
 */
class __CompilerOutput__
	implements CompilerOutput
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The output ZIP. */
	protected final ZipStreamWriter zip;
	
	/** Outputs which are currently being written to (not closed). */
	private final Map<String, __Stream__> _waiting =
		new LinkedHashMap<>();
	
	/** Output streams which have been closed, to be written to the ZIP. */
	private final Map<String, __Stream__> _done =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the compiler output.
	 *
	 * @param __zip The output ZIP to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	__CompilerOutput__(ZipStreamWriter __zip)
		throws NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// Nothing to be done?
			Map<String, __Stream__> done = this._done;
			if (done.isEmpty())
				return;
			
			// Go through all the done files
			Iterator<Map.Entry<String, __Stream__>> it = done.entrySet().
				iterator();
			ZipStreamWriter zip = this.zip;
			while (it.hasNext())
			{
				// Get the details
				Map.Entry<String, __Stream__> e = it.next();
				String k = e.getKey();
				__Stream__ v = e.getValue();
				
				// Never write it again
				it.remove();
				
				// Write to the output
				try (OutputStream os = zip.nextEntry(k,
					ZipCompressionType.DEFAULT_COMPRESSION))
				{
					v.wrapped.writeTo(os);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public OutputStream output(String __n)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Setup new stream
			__Stream__ rv = new __Stream__(__n);
			
			// Store into the files being written
			Map<String, __Stream__> waiting = this._waiting;
			waiting.put(__n, rv);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Wrapped streams for potential flushing.
	 *
	 * @since 2016/09/20
	 */
	private final class __Stream__
		extends OutputStream
	{
		/** The wrapped stream to write to. */
		protected final ByteArrayOutputStream wrapped =
			new ByteArrayOutputStream();
		
		/** The name of this file. */
		protected final String name;
		
		/** Has this been closed? */
		private volatile boolean _closed;
		
		/**
		 * Initializes the stream.
		 *
		 * @param __n The name of this file.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/20
		 */
		private __Stream__(String __n)
			throws NullPointerException
		{
			// Check
			if (__n == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.name = __n;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void close()
			throws IOException
		{
			// Need to lock on the compiler output to prevent race conditions
			// where a file is closed multiple times and placed into the done
			// queue multiple times
			__CompilerOutput__ ccout = __CompilerOutput__.this;
			synchronized (ccout.lock)
			{
				// Ignore if closed
				if (this._closed)
					return;
			
				// Mark closed
				this._closed = true;
				
				// Get both queues
				Map<String, __Stream__> waiting = ccout._waiting;
				Map<String, __Stream__> done = ccout._done;
				
				// Remove from the waiting queue
				String name = this.name;
				waiting.remove(name);
				
				// And add to the done queue
				done.put(name, this);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void flush()
			throws IOException
		{
			// Flushing a byte array output is pointless
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void write(int __b)
			throws IOException
		{
			// {@squirreljme.error CI0f Cannot write a single byte after the
			// output has been closed.}
			if (this._closed)
				throw new IOException("CI0f");
			
			// Forward
			this.wrapped.write(__b);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IOException
		{
			// {@squirreljme.error CI0g Cannot write multiple bytes after the
			// output has been closed.}
			if (this._closed)
				throw new IOException("CI0g");
			
			// Forward
			this.wrapped.write(__b, __o, __l);
		}
	}
}

