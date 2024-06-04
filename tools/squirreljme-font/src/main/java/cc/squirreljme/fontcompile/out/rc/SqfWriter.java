// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rc;

import cc.squirreljme.fontcompile.out.CompiledFont;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import net.multiphasicapps.zip.queue.ArchiveOutputQueue;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Writes the resultant output SQF file which stores a SquirrelJME font.
 *
 * @since 2024/06/04
 */
public class SqfWriter
	implements Closeable
{
	/** The input font. */
	protected final CompiledFont in;
	
	/** The resultant output. */
	protected final OutputStream out;
	
	/** The ZIP output. */
	protected final ZipStreamWriter zip;
	
	/** The archive being written. */
	protected final ArchiveOutputQueue archive;
	
	/**
	 * Initializes the SQF writer.
	 *
	 * @param __in The input font.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public SqfWriter(CompiledFont __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this.out = __out;
		this.archive = new ArchiveOutputQueue(new ZipStreamWriter(__out));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/04
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Writes the SQF output.
	 *
	 * @since 2024/06/04
	 */
	public void run()
	{
		throw Debugging.todo();
	}
}
