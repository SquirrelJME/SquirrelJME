// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.queue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;
import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Handles storing the file buffer for file output.
 *
 * @since 2023/08/12
 */
final class __QueuedOutput__
	extends OutputStream
{
	/** Temporary data buffer. */
	protected final ByteArrayOutputStream data =
		new ByteArrayOutputStream();
	
	/** The owning queue. */
	protected final Reference<ArchiveOutputQueue> owner;
	
	/** The name of the entry. */
	protected final String name;
	
	/** Has this been closed? */
	private volatile boolean _isClosed;
	
	/**
	 * Initializes the data queue.
	 *
	 * @param __owner The owner of this, where the data is written to on 
	 * close.
	 * @param __name The name of the entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	__QueuedOutput__(Reference<ArchiveOutputQueue> __owner, String __name)
		throws NullPointerException
	{
		if (__owner == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.owner = __owner;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close once
		if (this._isClosed)
			return;
		this._isClosed = true;
		
		// Get owning archive
		ArchiveOutputQueue archive = this.owner.get();
		if (archive == null)
			throw new IllegalStateException("GCGC");
		
		// Calculate checksum
		byte[] bytes = this.data.toByteArray();
		int checksum = CRC32Calculator.calculateZip(bytes);
		
		// Setup entry within the archive to write to
		ZipStreamWriter zip = archive.zip;
		try (OutputStream out = zip.nextEntry(this.name))
		{
			// Write all the written data
			out.write(bytes, 0, bytes.length);
			
			// Make sure the output is flushed properly
			out.flush();
		}
		
		// Make sure this Zip entry is written
		zip.flush();
		
		// Calculate data checksum, for file clashing
		archive.outputFiles.put(this.name, checksum);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		if (this._isClosed)
			throw new IOException("CLSD");
		
		this.data.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void write(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		if (this._isClosed)
			throw new IOException("CLSD");
	
		this.data.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		if (this._isClosed)
			throw new IOException("CLSD");
	
		this.data.write(__b, __o, __l);
	}
}
