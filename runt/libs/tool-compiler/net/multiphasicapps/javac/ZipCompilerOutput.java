// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This is used to wrap output to a ZIP file.
 *
 * @since 2017/11/28
 */
public final class ZipCompilerOutput
	implements Closeable, CompilerOutput
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
	 * Initialize the output which writes to the given ZIP file.
	 *
	 * @param __out The output ZIP to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public ZipCompilerOutput(ZipStreamWriter __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.zip = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void close()
		throws IOException
	{
		// Flush and close the ZIP
		ZipStreamWriter zip = this.zip;
		zip.flush();
		zip.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void flush()
		throws CompilerException
	{
		// Lock
		synchronized (this.lock)
		{
			// Nothing to be done?
			Map<String, __Stream__> done = this._done;
			if (done.isEmpty())
				return;
				
			// This is for debug
			String last = null;
			
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
				try (OutputStream os = zip.nextEntry(k))
				{
					v.wrapped.writeTo(os);
				}
				
				// {@squirreljme.error AQ0a Failed to write the specified file
				// to the output stream. (The file name; The last file)}
				catch (IOException f)
				{
					throw new CompilerException(
						String.format("AQ0a %s %s", k, last), f);
				}
				
				// New last file, for debug
				last = k;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public OutputStream output(String __n)
		throws CompilerException, NullPointerException
	{
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
			ZipCompilerOutput ccout = ZipCompilerOutput.this;
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
				
				// Flush the compiler output early to write as many files as
				// possible so they do not crowd memory
				ccout.flush();
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
			// {@squirreljme.error AQ0b Cannot write a single byte after the
			// output has been closed.}
			if (this._closed)
				throw new IOException("AQ0b");
			
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
			// {@squirreljme.error AQ0c Cannot write multiple bytes after the
			// output has been closed.}
			if (this._closed)
				throw new IOException("AQ0c");
			
			// Forward
			this.wrapped.write(__b, __o, __l);
		}
	}
}

