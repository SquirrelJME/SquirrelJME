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
import cc.squirreljme.jvm.aot.summercoat.base.TableOfContentsEntry;
import cc.squirreljme.jvm.aot.summercoat.base.TableOfContentsWriter;
import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.PackFlag;
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import cc.squirreljme.jvm.summercoat.constants.PackTocProperty;
import cc.squirreljme.vm.DataContainerLibrary;
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
		
		// Start the base pack file accordingly
		StandardPackWriter pack = new StandardPackWriter(
			ClassInfoConstants.PACK_MAGIC_NUMBER,
			PackProperty.NUM_PACK_PROPERTIES,
			PackTocProperty.NUM_PACK_TOC_PROPERTIES);
		pack.initialize();
		
		// Get the used chunks.
		ChunkWriter mainChunk = pack.mainChunk;
		ChunkSection tocChunk = pack.tocChunk;
		
		// Write header information
		HeaderStructWriter header = pack.header();
		ChunkUtils.storeCommonPackHeader(mainChunk, __settings,
			header);
		
		// Our pack consists of regular Java classes
		header.set(PackProperty.BITFIELD_PACK_FLAGS,
			PackFlag.IS_SPRINGCOAT);
		
		// Process each library
		TableOfContentsWriter toc = pack.toc();
		for (VMClassLibrary lib : __libs)
		{
			// Setup Jar chunk
			ChunkSection jarChunk = mainChunk.addSection(
				ChunkWriter.VARIABLE_SIZE, 8);
			
			// Declare new entry
			TableOfContentsEntry entry = toc.add();
			ChunkUtils.storeCommonPackTocEntry(entry, lib, mainChunk,
				jarChunk);
			
			// Plain data container?
			if (lib instanceof DataContainerLibrary)
				ChunkUtils.copyDataContainer(
					(DataContainerLibrary)lib, jarChunk, entry);
			
			// Copy plain JAR class otherwise
			else
				ChunkUtils.copyPlainJarClasses(lib, jarChunk, entry);
		}
		
		// Prepare table of contents
		ChunkUtils.storeCommonPackToc(toc, tocChunk, header);
		
		// Write to wherever our output is going
		mainChunk.writeTo(__out);
		__out.flush();
	}
}
