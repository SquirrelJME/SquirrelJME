// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This is the base class for the class and resource writers.
 *
 * @since 2016/09/11
 */
abstract class __BaseWriter__
{
	/** The owning namespace writer. */
	protected final BasicNamespaceWriter namespace;
	
	/** The stream to write binary data to. */
	protected final ExtendedDataOutputStream output;
	
	/** The positioned data. */
	private final __Positioned__ _positioned;
	
	/**
	 * Initializes the resource writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __pos The positioned entry data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	__BaseWriter__(BasicNamespaceWriter __nsw, __Positioned__ __pos)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __pos == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __nsw;
		this._positioned = __pos;
		
		// Get the base output to write to, this is wrapped so sizes are known,
		// prevents closing the external output, and makes thing a bit more
		// sane
		ExtendedDataOutputStream base = __nsw._output;
		ExtendedDataOutputStream output = new ExtendedDataOutputStream(
			new __Filter__(base));
		output.setEndianess(base.getEndianess());
		this.output = output;
		
		// Set start position
		__pos._startpos = base.size();
	}
	
	/**
	 * This acts as a filter over the output stream.
	 *
	 * @since 2016/09/11
	 */
	private final class __Filter__
		extends OutputStream
	{
		/** Wrapped source. */
		protected final ExtendedDataOutputStream wrapped;
		
		/** Was this closed? */
		private volatile boolean _closed;
		
		/**
		 * Initializes the filter.
		 *
		 * @param __w The output source.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/11
		 */
		private __Filter__(ExtendedDataOutputStream __w)
			throws NullPointerException
		{
			// Check
			if (__w == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.wrapped = __w;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void close()
			throws IOException
		{
			// Ignore if closed
			if (this._closed)
				return;
			
			// Set closed, and mark the end position
			this._closed = true;
			__BaseWriter__.this._positioned._endpos = this.wrapped.size();
			
			// No longer current
			__BaseWriter__.this.namespace._current = null;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void write(int __v)
			throws IOException
		{
			// {@squirreljme.error BV04 Cannot write byte as the stream is
			// closed.}
			if (this._closed)
				throw new IOException("BV04");
			
			// Forward
			this.wrapped.write(__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IOException
		{
			// {@squirreljme.error BV05 Cannot multiples bytes as the stream is
			// closed.}
			if (this._closed)
				throw new IOException("BV05");
			
			// Forward
			this.wrapped.write(__b, __o, __l);
		}
	}
}

