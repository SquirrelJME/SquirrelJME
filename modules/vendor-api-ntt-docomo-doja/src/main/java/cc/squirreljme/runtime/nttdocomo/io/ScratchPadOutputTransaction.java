// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream which when finished will write to the scratch
 * pad via an entire transaction.
 *
 * @since 2021/12/02
 */
@SquirrelJMEVendorApi
public final class ScratchPadOutputTransaction
	extends OutputStream
{
	/** The data storage for this transaction. */
	private final byte[] _data;
	
	/** The number of bytes to write. */
	private final int _length;
	
	/** The position this is writing into. */
	private final int _pos;
	
	/** The transaction storage. */
	private final ScratchPadStore _store;
	
	/** Has this been closed? */
	private volatile boolean _isClosed;
	
	/** The current write position. */
	private volatile int _writePos;
	
	/**
	 * Initializes the scratch pad transaction.
	 *
	 * @param __store The store to write into.
	 * @param __pos The position into the store.
	 * @param __length The number of bytes to write.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/02
	 */
	@SquirrelJMEVendorApi
	ScratchPadOutputTransaction(ScratchPadStore __store, int __pos,
		int __length)
		throws IOException, NullPointerException
	{
		if (__store == null)
			throw new NullPointerException("NARG");
		
		this._store = __store;
		this._pos = __pos;
		this._length = __length;
		
		// Copy the input data for writing
		this._data = new byte[__length];
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void close()
		throws IOException
	{
		// Only close this one
		synchronized (this)
		{
			if (this._isClosed)
				return;
			this._isClosed = true;
		}
		
		// Perform the transaction write
		this.flush();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void flush()
		throws IOException
	{
		ScratchPadStore store = this._store;
		
		// Write our transaction data to the storage area then commit that
		// to the record store
		synchronized (this)
		{
			store.write(this._data, this._pos, this._writePos);
			store.flush();
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void write(int __b)
		throws IOException
	{
		// Ensure we are writing within bounds
		int writePos;
		synchronized (this)
		{
			// {@squirreljme.error AH0k Write exceeds the bounds of the
			// scratch pad.}
			writePos = this._writePos;
			if (writePos >= this._length)
				throw new IOException("AH0k");
			
			// Start at the next position
			this._writePos = writePos + 1;
		}
		
		// Store the byte 
		this._data[writePos] = (byte)__b;
	}
}
