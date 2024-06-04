// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.source;

import cc.squirreljme.c.CFile;
import cc.squirreljme.c.out.AppendableCTokenOutput;
import cc.squirreljme.c.out.PrettyCTokenOutput;
import cc.squirreljme.fontcompile.out.struct.SqfFontStruct;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import net.multiphasicapps.zip.queue.ArchiveOutputQueue;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Not Described.
 *
 * @since 2024/06/04
 */
public class SqfSourceWriter
	implements Closeable
{
	/** The input font. */
	protected final SqfFontStruct in;
	
	/** The resultant output. */
	protected final CFile out;
	
	/**
	 * Initializes the SQF writer.
	 *
	 * @param __in The input font.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public SqfSourceWriter(SqfFontStruct __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		try
		{
			this.out = new CFile(new PrettyCTokenOutput(
				new AppendableCTokenOutput(new PrintStream(__out,
					true, "utf-8"))));
		}
		catch (UnsupportedEncodingException __e)
		{
			throw new RuntimeException(__e);
		}
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
