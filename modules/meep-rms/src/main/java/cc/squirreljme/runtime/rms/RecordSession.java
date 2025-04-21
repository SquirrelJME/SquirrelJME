// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.BucketWriteMode;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import java.io.ByteArrayInputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import javax.microedition.rms.RecordStoreException;
import net.multiphasicapps.collections.ArrayUtils;

/**
 * Records and keeps a session for a record store.
 *
 * @since 2025/04/20
 */
@SquirrelJMEVendorApi
public class RecordSession
	implements AutoCloseable
{
	/** An empty record. */
	private static final byte[] _EMPTY = 
		new byte[0];
	
	/** The bucket to access. */
	@SquirrelJMEVendorApi
	protected final BucketBracket bucket;
	
	/** The file name within the bucket. */
	@SquirrelJMEVendorApi
	protected final String fileName;
	
	/** The access lock. */
	@SquirrelJMEVendorApi
	protected final Object lock;
	
	@SquirrelJMEVendorApi
	public final int id;
	
	/** The origin read data. */
	private volatile Reference<byte[]> _read;
	
	/** The target data to commit. */
	private volatile byte[] _commit;
	
	/**
	 * Initializes the session.
	 *
	 * @param __bucket The bucket to access.
	 * @param __fileName The file name of the data.
	 * @param __lock The lock used for access.
	 * @param __id The session ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordSession(BucketBracket __bucket, String __fileName,
		Object __lock, int __id)
		throws NullPointerException
	{
		if (__bucket == null || __fileName == null || __lock == null)
			throw new NullPointerException("NARG");
		
		this.bucket = __bucket;
		this.fileName = __fileName;
		this.lock = __lock;
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws RecordStoreException If the record could not be closed.
	 * @since 2025/04/20
	 */
	@Override
	public void close()
		throws RecordStoreException
	{
		// Get any data to commit
		byte[] commit;
		synchronized (this.lock)
		{
			commit = this._commit;
			this._commit = null;
		
			// Is there anything to commit?
			if (commit != null)
				try
				{
					// Debug
					if (Debugging.ENABLED)
					{
						Debugging.debugNote("Session %s", this);
						HexDumpOutputStream.dump(System.err, commit);
					}
					
					// Commit it
					BucketShelf.write(this.bucket, this.fileName, 0,
						commit, 0, commit.length,
						BucketWriteMode.TRUNCATE);
				}
				catch (MLECallError __e)
				{
					throw RecordUtils.wrap(
						new RecordStoreException(__e.getMessage()), __e);
				}
		}
	}
	
	/**
	 * Returns the last modified time of the session.
	 *
	 * @return The last modified time.
	 * @throws RecordStoreException If the record is not valid.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public long lastModified()
		throws RecordStoreException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the length of the record.
	 *
	 * @return The record length.
	 * @throws RecordStoreException If the length could not be determined.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public int length()
		throws RecordStoreException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Open an input stream over the record data.
	 *
	 * @return An input stream over the data.
	 * @throws RecordStoreException If the data could not be read.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public final ByteArrayInputStream read()
		throws RecordStoreException
	{
		// If the data does not exist, this will always be empty
		if (!BucketShelf.exists(this.bucket, this.fileName))
			return new ByteArrayInputStream(RecordSession._EMPTY);
		
		return new ByteArrayInputStream(this.readAll());
	}
	
	/**
	 * Reads all data from the record data.
	 *
	 * @param __buf The buffer to read into.
	 * @param __off The offset into the buffer.
	 * @param __len The maximum number of bytes to read.
	 * @return The number of bytes read.
	 * @throws NullPointerException On null arguments.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public final int read(byte[] __buf, int __off, int __len)
		throws NullPointerException, IndexOutOfBoundsException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || __len < 0 || (__off + __len) < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
	}
	
	/**
	 * Reads all record data.
	 *
	 * @return All the record data.
	 * @throws RecordStoreException If the data could not be read.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public byte[] readAll()
		throws RecordStoreException
	{
		synchronized (this.lock)
		{
			// Use committed data first
			if (this._commit != null)
				return this._commit;
			
			// Check to see if there is a cache of all the data already
			Reference<byte[]> ref = this._read;
			byte[] result = null;
			if (ref == null || (result = ref.get()) == null)
			{
				// Read in
				try
				{
					// Determine the data length
					BucketBracket bucket = this.bucket;
					String fileName = this.fileName;
					long length = BucketShelf.length(bucket, fileName);
					
					// Does not exist?
					if (length < 0)
						result = new byte[0];
						
					// Read in everything
					else
					{
						int capped = (int)Math.min(length, Integer.MAX_VALUE);
						result = new byte[capped];
						BucketShelf.read(bucket, fileName, 0,
							result, 0, capped);
					}
				}
				catch (MLECallError __e)
				{
					throw RecordUtils.wrap(
						new RecordStoreException(__e.getMessage()), __e);
				}
				
				// Cache it
				this._read = new WeakReference<>(result);
			}
			
			// Debug?
			if (Debugging.ENABLED)
				HexDumpOutputStream.dump(System.err, result);
			
			// Use what was read
			return result;
		}
	}
	
	/**
	 * Sets all the data to be written to the buffer.
	 *
	 * @param __buf The buffer of data to set for writing, for efficiency
	 * the buffer is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public void writeAll(byte[] __buf)
		throws NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		// Commit everything
		synchronized (this.lock)
		{
			// This gets committed for writing later
			this._commit = __buf;
			
			// Since we updated the data, we want to read the latest data
			// always
			this._read = new WeakReference<>(__buf);
		}
	}
	
	/**
	 * Sets all the data to be written to the buffer.
	 *
	 * @param __buf The buffer of data to set for writing, for efficiency
	 * the buffer is not copied.
	 * @param __off The buffer offset.
	 * @param __len The length of the data.
	 * @throws IndexOutOfBoundsException If the index and/or offset are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void writeAll(byte[] __buf, int __off, int __len)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __buf.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Copy bytes to new base array
		byte[] dup = new byte[__len];
		System.arraycopy(__buf, __off,
			dup, 0, __len);
		
		// Commit this data
		this.writeAll(dup);
	}
}
