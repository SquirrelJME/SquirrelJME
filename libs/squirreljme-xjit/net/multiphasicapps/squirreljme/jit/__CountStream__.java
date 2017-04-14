// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is used to count the number of bytes which have been read.
 *
 * @since 2017/04/13
 */
class __CountStream__
	extends DataInputStream
{
	/** The counting stream. */
	protected final __Count__ count;
	
	/**
	 * Initializes the count stream.
	 *
	 * @param __is The stream to wrap.
	 * @since 2017/03/13
	 */
	__CountStream__(InputStream __is)
	{
		super((__is = new __Count__(__is)));
		
		// Set
		this.count = (__Count__)__is;
	}
	
	/**
	 * Returns the number of bytes which have been read.
	 *
	 * @return The read byte count.
	 * @since 2017/04/13
	 */
	public int count()
	{
		return this.count._read;
	}
	
	/**
	 * This counts the number of read bytes.
	 *
	 * @since 2017/04/13
	 */
	private static class __Count__
		extends InputStream
	{
		/** The wrapped stream. */
		protected final InputStream wrap;
		
		/** The bytes read. */
		private volatile int _read;
		
		/**
		 * Initializes the count stream.
		 *
		 * @param __in The input stream.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/04/13
		 */
		__Count__(InputStream __in)
			throws NullPointerException
		{
			// Check
			if (__in == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.wrap = __in;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/13
		 */
		@Override
		public int available()
			throws IOException
		{
			return this.wrap.available();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/13
		 */
		@Override
		public void close()
			throws IOException
		{
			this.wrap.close();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/13
		 */
		@Override
		public int read()
			throws IOException
		{
			int rv = this.wrap.read();
			
			// EOF?
			if (rv < 0)
				return rv;
			
			// Count
			this._read++;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/13
		 */
		@Override
		public int read(byte[] __b, int __o, int __l)
			throws IOException
		{
			int rv = this.wrap.read(__b, __o, __l);
			
			// EOF?
			if (rv < 0)
				return rv;
			
			// Count
			this._read += rv;
			return rv;
		}
	}
}

