// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rc;

import cc.squirreljme.fontcompile.out.struct.SqfFontStruct;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.zip.queue.ArchiveOutputQueue;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Writes a SQF structure as a number of resources.
 *
 * @since 2024/06/04
 */
public class SqfResourceWriter
	implements Closeable
{
	/** The resultant output. */
	protected final OutputStream out;
	
	/** The archive being written. */
	protected final ArchiveOutputQueue archive;
	
	/**
	 * Initializes the SQF writer.
	 *
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public SqfResourceWriter(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
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
	 * @param __struct The struct to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public void write(SqfFontStruct __struct)
		throws NullPointerException
	{
		if (__struct == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
