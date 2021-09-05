// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.springcoat;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.jvm.aot.summercoat.base.ChunkUtils;
import cc.squirreljme.jvm.aot.summercoat.base.HeaderStructWriter;
import cc.squirreljme.jvm.aot.summercoat.base.StandardPackWriter;
import cc.squirreljme.jvm.aot.summercoat.base.TableOfContentsWriter;
import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import cc.squirreljme.jvm.summercoat.constants.PackTocProperty;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import net.multiphasicapps.io.ChunkSection;
import net.multiphasicapps.io.ChunkWriter;

/**
 * Backend for SpringCoat operations.
 * 
 * {@squirreljme.error AI01 This operation is not available.}
 *
 * @since 2021/08/21
 */
public class SpringCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void dumpGlob(byte[] __inGlob, String __name, PrintStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public LinkGlob linkGlob(CompileSettings __settings, String __name,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public String name()
	{
		return "springcoat";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void rom(RomSettings __settings, OutputStream __out,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__settings == null || __out == null || __libs == null)
			throw new NullPointerException("NARG");
		
		// Setup chunk where everything is written to
		ChunkWriter chunk = new ChunkWriter();
		
		// Sections for chunks
		ChunkSection headerChunk = chunk.addSection(
			ChunkWriter.VARIABLE_SIZE, 8);
		ChunkSection tocChunk = chunk.addSection(
			ChunkWriter.VARIABLE_SIZE, 8);
		
		// Start the base pack file accordingly
		StandardPackWriter pack = new StandardPackWriter(
			ClassInfoConstants.PACK_MAGIC_NUMBER,
			PackProperty.NUM_PACK_PROPERTIES,
			PackTocProperty.NUM_PACK_TOC_PROPERTIES);
		
		// Write header information
		HeaderStructWriter header = pack.header();
		ChunkUtils.storeCommonHeader(chunk, __settings, header);
		
		// Process each library
		TableOfContentsWriter toc = pack.toc();
		for (VMClassLibrary lib : __libs)
		{
			// Setup Jar chunk
			ChunkSection jarChunk = chunk.addSection(
				ChunkWriter.VARIABLE_SIZE, 8);
			
			// Add table of contents information on this JAR
			if (true)
				throw Debugging.todo();
			
			throw Debugging.todo();
		}
		
		// Finalize the chunk
		pack.writeTo(headerChunk, tocChunk);
		
		// Write to wherever our output is going
		chunk.writeTo(__out);
		__out.flush();
	}
}
