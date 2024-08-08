// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.jvm.launch.IModeProperty;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.DoJaRuntime;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
@SquirrelJMEVendorApi
public final class ScratchPadStore
{
	/** The static set of stores. */
	private static volatile ScratchPadStore[] _STORES;
	
	/** The record store key prefix. */
	private static final String _STORE_KEY_PREFIX =
		"SquirrelJME-i-Appli-";
	
	/** STO Header size. */
	private static final int _STO_HEADER_LEN =
		64;
	
	/** STO Header entries. */
	private static final int _STO_ENTRIES =
		16;
	
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
	@SquirrelJMEVendorApi
	ScratchPadStore(int __pad, int __length)
		throws IOException
	{
		this._pad = __pad;
		this._storeKey = ScratchPadStore._STORE_KEY_PREFIX + this._pad;
		
		// Read in the record store data that already exists, if any
		byte[] data = new byte[__length];
		this._data = data;
		try (RecordStore store = this.__rmsStore())
		{
			// Nothing was actually created ever?
			if (store.getNumRecords() <= 0)
			{
				ScratchPadStore.__seed(__pad, data);
				return;
			}
			
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public OutputStream outputStream(int __pos, int __len)
		throws IndexOutOfBoundsException, IOException
	{
		byte[] data = this._data;
		if (__pos < 0 || __len < 0 || (__pos + __len) < 0 ||
			(__pos + __len) > data.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return new ScratchPadOutputTransaction(this, __pos, __len);
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
	@SquirrelJMEVendorApi
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
	static ScratchPadStore __open(int __pad,
		ScratchPadParams __params)
		throws IOException, NullPointerException
	{
		if (__params == null)
			throw new NullPointerException("NARG");
		
		synchronized (ScratchPadStore.class)
		{
			// Do we need to initialize the storage base?
			ScratchPadStore[] stores = ScratchPadStore._STORES;
			if (stores == null)
				ScratchPadStore._STORES =
					(stores = new ScratchPadStore[__params.count()]);
			
			// Do we need to initialize the already existing store?
			ScratchPadStore store = stores[__pad];
			if (store == null)
				stores[__pad] = (store = new ScratchPadStore(__pad,
					__params.getLength(__pad)));
			
			return store;
		}
	}
	
	/**
	 * Seeds the scratchpad data.
	 *
	 * @param __pad The scratchpad to access.
	 * @param __data The data to fill in.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	private static void __seed(int __pad, byte[] __data)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Is a seed specified?
		String libName = DoJaRuntime.getProperty(
			IModeProperty.SEED_SCRATCHPAD_PREFIX + "." + __pad);
		if (libName == null)
			libName = DoJaRuntime.getProperty(
				IModeProperty.SEED_SCRATCHPAD_PREFIX + ".0");
		
		// None was found?
		if (libName == null)
		{
			// Debug
			Debugging.debugNote("No seed for SP %d", __pad);
			
			return;
		}
		
		// Try to find the library that contains the seed
		JarPackageBracket lib = null;
		for (JarPackageBracket maybeLib : JarPackageShelf.libraries())
			if (libName.equals(JarPackageShelf.libraryPath(maybeLib)))
			{
				lib = maybeLib;
				break;
			}
		
		// Not found?
		if (lib == null)
		{
			// Debug
			Debugging.debugNote("Did not find seed library %s.",
				libName);
			
			return;
		}
		
		// Get the size of both
		int dataLen = __data.length;
		int seedLen = JarPackageShelf.rawSize(lib);
		
		// Invalid?
		if (seedLen < 0)
		{
			// Debug
			Debugging.debugNote("Seed %s invalid, raw length was %d.",
				libName, seedLen);
			
			return;
		}
		
		// There are different formats, one that has a header at the start
		// which derives scratchpads accordingly, or direct
		if (seedLen >= dataLen + ScratchPadStore._STO_HEADER_LEN)
		{
			// Do nothing if too far in
			if (__pad >= ScratchPadStore._STO_ENTRIES)
				return;
			
			// Read in raw header
			byte[] rawHeader = new byte[ScratchPadStore._STO_HEADER_LEN];
			JarPackageShelf.rawData(lib, 0,
				rawHeader, 0, rawHeader.length);
			
			// Setup buffers for position and size
			int[] position = new int[ScratchPadStore._STO_ENTRIES];
			int[] size = new int[ScratchPadStore._STO_ENTRIES];
			
			// Initial position is always after the header
			int at = ScratchPadStore._STO_HEADER_LEN;
			
			// Parse the sizes in the header
			try (DataInputStream dos = new DataInputStream(
				new ByteArrayInputStream(rawHeader)))
			{
				for (int i = 0; i < ScratchPadStore._STO_ENTRIES; i++)
				{
					// Always at the baseline position
					position[i] = at;
					
					// Read in size, if it is negative or larger than the
					// size of the scratchpad file then it is likely in
					// little endian and not big endian
					int padSize = dos.readInt();
					if (padSize > seedLen || padSize < 0)
						padSize = Integer.reverseBytes(padSize);
					
					// Keep it mapped in size
					int limit = Math.max(0, dataLen - at);
					if (padSize < 0)
						padSize = 0;
					else if (padSize > limit)
						padSize = limit;
					
					// Set current size
					size[i] = padSize;
					
					// Move position up
					at += padSize;
				}
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
				
				// Ignore
				return;
			}
			
			// Debug
			seedLen = size[__pad];
			Debugging.debugNote("Reading seed %s with dl=%d and sl=%d.",
				libName, dataLen, seedLen);
			
			// The limit is the smaller of the two
			int limit = Math.min(dataLen, seedLen);
			
			// Read the seed directly into the buffer
			JarPackageShelf.rawData(lib, position[__pad],
				__data, 0, limit);
		}
		
		// Flat seed
		else
		{
			// Debug
			Debugging.debugNote("Reading seed %s with dl=%d and sl=%d.",
				libName, dataLen, seedLen);
			
			// The limit is the smaller of the two
			int limit = Math.min(dataLen, seedLen);
			
			// Read the seed directly into the buffer
			JarPackageShelf.rawData(lib, 0,
				__data, 0, limit);
		}
	}
}
