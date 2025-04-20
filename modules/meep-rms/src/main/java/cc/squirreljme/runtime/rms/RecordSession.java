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
	
	/** The target data to commit. */
	private volatile byte[] _commit;
	
	/**
	 * Initializes the session.
	 *
	 * @param __bucket The bucket to access.
	 * @param __fileName The file name of the data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordSession(BucketBracket __bucket, String __fileName)
		throws NullPointerException
	{
		if (__bucket == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		this.bucket = __bucket;
		this.fileName = __fileName;
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
		synchronized (this)
		{
			commit = this._commit;
			this._commit = null;
		}
		
		// Is there anything to commit?
		if (commit != null)
			try
			{
				// Debug
				if (Debugging.ENABLED)
					HexDumpOutputStream.dump(System.err, commit);
				
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
	
	/**
	 * Open an input stream over the record data.
	 *
	 * @return An input stream over the data.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public final ByteArrayInputStream read()
	{
		// If the data does not exist, this will always be empty
		if (!BucketShelf.exists(this.bucket, this.fileName))
			return new ByteArrayInputStream(RecordSession._EMPTY);
		
		throw Debugging.todo();
	}
	
	/**
	 * Reads data from the record data.
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
	 * Sets all the data to be written to the buffer.
	 *
	 * @param __buf The buffer of data to set for writing.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	public void writeAll(byte[] __buf)
		throws NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		// Commit everything
		synchronized (this)
		{
			this._commit = __buf.clone();
		}
	}
}
