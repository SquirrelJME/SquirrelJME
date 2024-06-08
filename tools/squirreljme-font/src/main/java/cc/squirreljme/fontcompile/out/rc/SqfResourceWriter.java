// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rc;

import cc.squirreljme.fontcompile.out.SqfWriter;
import cc.squirreljme.fontcompile.out.struct.SqfFontStruct;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.DataOutputStream;
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
	implements SqfWriter
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
		this.archive.flush();
		this.archive.close();
		
		this.out.flush();
		this.out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/04
	 */
	@Override
	public void write(SqfFontStruct __struct)
		throws IOException, NullPointerException
	{
		if (__struct == null)
			throw new NullPointerException("NARG");
		
		ArchiveOutputQueue archive = this.archive;
		
		// Determine base name for files
		String baseName = String.format("%s/%d/%X/",
			__struct.name, __struct.pixelHeight,
			__struct.codepointStart / 256);
		
		// Write base struct details
		try (OutputStream out = archive.nextEntry(baseName + "struct");
			DataOutputStream ds = new DataOutputStream(out))
		{
			ds.writeInt(__struct.pixelHeight);
			ds.writeInt(__struct.ascent);
			ds.writeInt(__struct.descent);
			ds.writeInt(__struct.bbx);
			ds.writeInt(__struct.bby);
			ds.writeInt(__struct.bbw);
			ds.writeInt(__struct.bbh);
			ds.writeInt(__struct.codepointStart);
			ds.writeInt(__struct.codepointCount);
			ds.writeInt(__struct.huffBitsSize);
			ds.writeInt(__struct.charBmpSize);
		}
		
		// Write array based data
		archive.writeEntry(baseName + "huffBits",
			__struct.huffBits());
		archive.writeEntry(baseName + "charWidths",
			__struct.charWidths());
		archive.writeEntry(baseName + "charXOffset",
			__struct.charXOffset());
		archive.writeEntry(baseName + "charYOffset",
			__struct.charYOffset());
		archive.writeEntry(baseName + "charFlags",
			__struct.charFlags());
		
		try (OutputStream out = archive.nextEntry(
				baseName + "charBmpOffset");
			DataOutputStream ds = new DataOutputStream(out))
		{
			for (int offset : __struct.charBmpOffset())
				ds.writeInt(offset);
		}
		
		archive.writeEntry(baseName + "charBmpScan",
			__struct.charBmpScan());
		archive.writeEntry(baseName + "charBmp",
			__struct.charBmp());
	}
}
