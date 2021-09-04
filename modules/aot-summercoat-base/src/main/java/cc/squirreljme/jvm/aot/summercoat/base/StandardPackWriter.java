// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.base;

import cc.squirreljme.jvm.summercoat.ld.pack.JarRom;
import cc.squirreljme.jvm.summercoat.ld.pack.PackRom;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.ChunkSection;
import net.multiphasicapps.io.ChunkWriter;

/**
 * Writer that is capable of writing anything based on table of contents
 * and headers such as {@link PackRom} and {@link JarRom}.
 *
 * @since 2021/09/03
 */
public final class StandardPackWriter
{
	/** The magic number to write. */
	protected final int magic;
	
	/** Header writer. */
	protected final HeaderStructWriter header;
	
	/**
	 * Writes a standard pack file.
	 * 
	 * @param __magic The magic number of the pack.
	 * @param __numProperties The number of properties to store.
	 * @throws IllegalArgumentException If the number of properties is zero
	 * or negative.
	 * @since 2021/09/03
	 */
	public StandardPackWriter(int __magic, int __numProperties)
		throws IllegalArgumentException
	{
		this.magic = __magic;
		this.header = new HeaderStructWriter(__numProperties);
	}
	
	/**
	 * Returns the header for writing.
	 * 
	 * @return The header to write into.
	 * @since 2021/09/03
	 */
	public HeaderStructWriter header()
	{
		return this.header;
	}
	
	/**
	 * Writes the data to the given output.
	 * 
	 * @param __headerChunk The header chunk.
	 * @param __tocChunk The table of contents chunk.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/03
	 */
	public final void writeTo(ChunkSection __headerChunk,
		ChunkSection __tocChunk)
		throws IOException, NullPointerException
	{
		if (__headerChunk == null || __tocChunk == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
