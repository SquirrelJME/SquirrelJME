// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Queue that can handle writing of multiple files at any one time and write
 * to a target Zip archive, this is used for the multiples of individual files
 * that need creating.
 *
 * @since 2023/08/12
 */
public class ArchiveOutputQueue
	implements Closeable
{
	/** The Zip to write to. */
	protected final ZipStreamWriter zip;
	
	/**
	 * Initializes the archive output.
	 *
	 * @param __zip The Zip to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public ArchiveOutputQueue(ZipStreamWriter __zip)
		throws NullPointerException
	{
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Creates a new entry to write data to.
	 *
	 * @param __name The name of the file to write.
	 * @return The stream to write the file.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public OutputStream nextEntry(String __name)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
