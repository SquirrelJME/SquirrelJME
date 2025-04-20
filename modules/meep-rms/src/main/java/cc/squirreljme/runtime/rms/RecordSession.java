// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	/** The bucket to access. */
	@SquirrelJMEVendorApi
	protected final BucketBracket bucket;
	
	/** The file name within the bucket. */
	@SquirrelJMEVendorApi
	protected final String fileName;
	
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
		throw Debugging.todo();
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
}
