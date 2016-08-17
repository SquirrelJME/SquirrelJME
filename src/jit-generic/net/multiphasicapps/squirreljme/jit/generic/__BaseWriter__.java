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
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;

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
	
	/** The type of blob this is. */
	final BlobContentType _type;
	
	/** The owning namespace. */
	final GenericNamespaceWriter _owner;
	
	/** The data address. */
	final int _dataaddr;
	
	/** The global constant pool. */
	final __GlobalPool__ _gpool;
	
	/** The entry name. */
	final String _name;
	
	/** The end of the data. */
	volatile int _dataend;
	
	/**
	 * Initializes the base writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __dos The writer to use for output.
	 * @param __ct The type of blob this is.
	 * @param __name The name of this entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	__BaseWriter__(GenericNamespaceWriter __nsw,
		ExtendedDataOutputStream __dos, BlobContentType __ct, String __name)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __ct == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
		this._owner = __nsw;
		this.lock = __nsw._lock;
		this._gpool = __nsw._gpool;
		this._name = __name;
		this._type = __ct;
		
		// Could fail
		try
		{
			// Align
			__nsw.__align();
			
			// {@squirreljme.error BA0b Start of class or resource is at a
			// position beyond 2GiB.}
			long sa = __dos.size();
			if (sa < 0 || sa > Integer.MAX_VALUE)
				throw new JITException("BA0b");
			this._dataaddr = (int)sa;
			
			// Wrap output
			ExtendedDataOutputStream output;
			output = new ExtendedDataOutputStream(new __Output__(__dos));
			output.setEndianess(__dos.getEndianess());
			this.output = output;
		}
		
		// {@squirreljme.error BA0a Failed to write the entry start.}
		catch (IOException e)
		{
			throw new JITException("BA0a", e);
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
		try
		{
			// Close the output
			this.output.close();
		}
		
		// {@squirreljme.error BA0t Could not close the output.}
		catch (IOException e)
		{
			throw new JITException("BA0t", e);
		}
	}
	
	/**
	 * This wraps the output for writing.
	 *
	 * @since 2016/08/13
	 */
	private final class __Output__
		extends OutputStream
	{
		/** The real output to write to. */
		protected final ExtendedDataOutputStream real;
		
		/** Was this closed? */
		private volatile boolean _closed;
		
		/**
		 * Initializes the wrapped output.
		 *
		 * @param __r The real stream to write to.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/08/19
		 */
		private __Output__(ExtendedDataOutputStream __r)
			throws NullPointerException
		{
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.real = __r;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/13
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
					// Close
					this._closed = true;
					
					// {@squirreljme.error BA0k End address of a content entry
					// exceeds 2GiB.}
					ExtendedDataOutputStream real = this.real;
					long del = real.size();
					if (del < 0 || del > Integer.MAX_VALUE)
						throw new JITException("BA0k");
					int de = (int)del;
					__BaseWriter__.this._dataend = de;
					
					// {@squirreljme.error BA14 Entries have an upper limit to
					// their size. (The entry size; The maximum permitted entry
					// size)}
					long sz = de - __BaseWriter__.this._dataaddr;
					if (sz > GenericBlob.MAX_ENTRY_SIZE)
						throw new JITException(String.format("BA14 %d %d",
							sz, GenericBlob.MAX_ENTRY_SIZE));
				
					// Close it on this writer
					__BaseWriter__.this.owner.__close(__BaseWriter__.this);
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/13
		 */
		@Override
		public void flush()
			throws IOException
		{
			this.real.flush();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/13
		 */
		@Override
		public void write(int __b)
			throws IOException
		{
			// Lock
			synchronized (__BaseWriter__.this.lock)
			{
				// {@squirreljme.error BA0f Cannot write single byte after
				// being closed.}
				if (this._closed)
					throw new IOException("BA0f");
			
				// Write byte
				this.real.write(__b);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/13
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			// Lock
			synchronized (__BaseWriter__.this.lock)
			{
				// {@squirreljme.error BA0g Cannot write multiple bytes after
				// being closed.}
				if (this._closed)
					throw new IOException("BA0g");
				
				// Check
				if (__b == null)
					throw new NullPointerException("NARG");
				int n = __b.length;
				if (__o < 0 || __l < 0 || (__o + __l) > n)
					throw new IndexOutOfBoundsException("IOOB");
			
				// Write data
				this.real.write(__b, __o, __l);
			}
		}
	}
}

