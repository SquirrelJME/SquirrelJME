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
import java.io.ByteArrayInputStream;
import javax.microedition.rms.RecordStoreException;

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
	
	/** The record ID. */
	@SquirrelJMEVendorApi
	protected final int id;
	
	/** Is this session read-only? */
	@SquirrelJMEVendorApi
	protected final boolean readOnly;
	
	/**
	 * Initializes the session.
	 *
	 * @param __bucket The bucket to access.
	 * @param __fileName The file name of the data.
	 * @param __lock The lock used for access.
	 * @param __id The session ID.
	 * @param __readOnly Is this session read-only?
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordSession(BucketBracket __bucket, String __fileName,
		Object __lock, int __id, boolean __readOnly)
		throws NullPointerException
	{
		if (__bucket == null || __fileName == null || __lock == null)
			throw new NullPointerException("NARG");
		
		this.bucket = __bucket;
		this.fileName = __fileName;
		this.lock = __lock;
		this.id = __id;
		this.readOnly = __readOnly;
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
		// Commit any data
		this.flush();
	}
	
	/**
	 * Flushes the record store information.
	 *
	 * @throws RecordStoreException If it could not be flushed.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void flush()
		throws RecordStoreException
	{
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
		try
		{
			return BucketShelf.lastModifiedTime(this.bucket, this.fileName);
		}
		catch (MLECallError __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreException(__e.getMessage()), __e);
		}
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
		try
		{
			if (!BucketShelf.exists(this.bucket, this.fileName))
				return 0;
			return (int)Math.min(Integer.MAX_VALUE,
				Math.max(0, BucketShelf.length(this.bucket, this.fileName)));
		}
		catch (MLECallError __e)
		{
			throw RecordUtils.wrap(
				new RecordStoreException(__e.getMessage()), __e);
		}
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
		
		synchronized (this.lock)
		{
			return BucketShelf.read(this.bucket, this.fileName,
				0, __buf, __off, __len);
		}
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
		BucketBracket bucket = this.bucket;
		String fileName = this.fileName;
		
		synchronized (this.lock)
		{
			// Is there an actual file here?
			long length = BucketShelf.length(bucket, fileName);
			if (length <= 0)
				return new byte[0];
			
			// Limit maximum size of read
			int limit = (int)Math.min(length, Integer.MAX_VALUE);
			
			// Read in all the data
			byte[] result = new byte[limit];
			BucketShelf.read(bucket, fileName,
				0, result, 0, limit);
			
			// Return resultant read
			return result;
		}
	}
	
	/**
	 * Sets all the data to be written to the buffer.
	 *
	 * @param __buf The buffer of data to set for writing.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If the data could not be written.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public void writeAll(byte[] __buf)
		throws NullPointerException, RecordStoreException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		// Forward
		this.writeAll(__buf, 0, __buf.length);
	}
	
	/**
	 * Sets all the data to be written to the buffer.
	 *
	 * @param __buf The buffer of data to set for writing.
	 * @param __off The buffer offset.
	 * @param __len The length of the data.
	 * @throws IndexOutOfBoundsException If the index and/or offset are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If the data could not be written.
	 * @since 2025/04/21
	 */
	@SquirrelJMEVendorApi
	public void writeAll(byte[] __buf, int __off, int __len)
		throws IndexOutOfBoundsException, NullPointerException,
			RecordStoreException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __buf.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Fail if readonly
		if (this.readOnly)
			throw new RecordStoreException("RORO");
		
		synchronized (this.lock)
		{
			// Write everything to file
			BucketShelf.write(this.bucket, this.fileName,
				0, __buf, __off, __len,
				BucketWriteMode.TRUNCATE);
		}
	}
}
