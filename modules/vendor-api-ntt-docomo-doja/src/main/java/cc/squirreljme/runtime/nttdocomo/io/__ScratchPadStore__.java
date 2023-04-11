// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * Represents storage for a single scratch pad.
 *
 * @since 2021/12/02
 */
final class __ScratchPadStore__
{
	/** The static set of stores. */
	private static volatile __ScratchPadStore__[] _STORES;
	
	/** The record store key prefix. */
	private static final String _STORE_KEY_PREFIX = "SquirrelJME-i-Appli-";
	
	/** The scratch pad being accessed. */
	private final int _pad;
	
	/** The record store key. */
	private final String _storeKey;
	
	/** Data for this given store. */
	private final byte[] _data;
	
	/**
	 * Initializes the scratch pad store.
	 *
	 * @param __pad The pad to load.
	 * @param __length The length of bytes.
	 * @throws IOException On read errors.
	 * @since 2021/12/02
	 */
	__ScratchPadStore__(int __pad, int __length)
		throws IOException
	{
		this._pad = __pad;
		this._storeKey = __ScratchPadStore__._STORE_KEY_PREFIX + this._pad;
		
		// Read in the record store data that already exists, if any
		byte[] data = new byte[__length];
		this._data = data;
		try (RecordStore store = this.__rmsStore())
		{
			// Nothing was actually created ever?
			if (store.getNumRecords() <= 0)
				return;
			
			// Read in the data
			if (__length != store.getRecord(0, data, 0))
				Debugging.debugNote("i-appli record size mismatch?");
		}
		
		// {@squirreljme.error AH0m Could not read pre-existing data from
		// the record store.}
		catch (RecordStoreException __e)
		{
			throw new IOException("AH0m", __e);
		}
	}
	
	/**
	 * Flushes the output.
	 *
	 * @throws IOException If it could not be flushed.
	 * @since 2021/12/02
	 */
	public void flush()
		throws IOException
	{
		byte[] data = this._data;
		synchronized (this)
		{
			// We need to write this somewhere
			try (RecordStore store = this.__rmsStore())
			{
				// Either create or set the only record
				if (store.getNumRecords() <= 0)
					store.addRecord(data, 0, data.length);
				else
					store.setRecord(0, data, 0, data.length);
			}
			
			// {@squirreljme.error AH0l Could not write scratch pad to the
			// record store.}
			catch (RecordStoreException __e)
			{
				throw new IOException("AH0l", __e);
			}
		}
	}
	
	/**
	 * Opens an input stream to the given scratch pad data.
	 *
	 * @param __pos The position to open from.
	 * @param __length The number of bytes to read.
	 * @return The stream to the data bytes.
	 * @throws IndexOutOfBoundsException If the read is outside of bounds.
	 * @since 2021/12/02
	 */
	public InputStream inputStream(int __pos, int __length)
		throws IndexOutOfBoundsException
	{
		byte[] data = this._data;
		if (__pos < 0 || __length < 0 || (__pos + __length) < 0 ||
			(__pos + __length) > data.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return new ByteArrayInputStream(data, __pos, __length);
	}
	
	/**
	 * Opens an output stream for writing to the given scratch pad via a
	 * single transaction, all or nothing.
	 *
	 * @param __pos The position to open from.
	 * @param __len The number of bytes to write.
	 * @return The stream for writing the data.
	 * @throws IndexOutOfBoundsException If the position and/or length are
	 * not within the scratchpad bounds.
	 * @throws IOException On data copy errors.
	 * @since 2021/12/02
	 */
	public OutputStream outputStream(int __pos, int __len)
		throws IndexOutOfBoundsException, IOException
	{
		byte[] data = this._data;
		if (__pos < 0 || __len < 0 || (__pos + __len) < 0 ||
			(__pos + __len) > data.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return new __ScratchPadOutputTransaction__(this, __pos, __len);
	}
	
	/**
	 * Writes to the internal data buffer.
	 *
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the offset and/or length are not
	 * without bounds.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/02
	 */
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Perform the copy
		synchronized (this)
		{
			System.arraycopy(__b, 0, this._data, __o, __l);
		}
	}
	
	/**
	 * Attempts to open the record store.
	 *
	 * @return The record store.
	 * @throws RecordStoreException If it could not be opened or created.
	 * @since 2021/12/02
	 */
	private RecordStore __rmsStore()
		throws RecordStoreException
	{
		return RecordStore.openRecordStore(this._storeKey, true,
			RecordStore.AUTHMODE_ANY, true, null);
	}
	
	/**
	 * Opens a scratch pad store.
	 *
	 * @param __pad The scratch pad to open.
	 * @param __params The parameters for the scratch pad.
	 * @return The storage for the scratch pad.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/02
	 */
	static __ScratchPadStore__ __open(int __pad,
		__ScratchPadParams__ __params)
		throws IOException, NullPointerException
	{
		if (__params == null)
			throw new NullPointerException("NARG");
		
		synchronized (__ScratchPadStore__.class)
		{
			// Do we need to initialize the storage base?
			__ScratchPadStore__[] stores = __ScratchPadStore__._STORES;
			if (stores == null)
				__ScratchPadStore__._STORES =
					(stores = new __ScratchPadStore__[__params.count()]);
			
			// Do we need to initialize the already existing store?
			__ScratchPadStore__ store = stores[__pad];
			if (store == null)
				stores[__pad] = (store = new __ScratchPadStore__(__pad,
					__params.getLength(__pad)));
			
			return store;
		}
	}
}
