// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.pack.constants.PackProperty;
import cc.squirreljme.jvm.pack.constants.PackTocProperty;
import cc.squirreljme.jvm.pack.mem.MemoryUtils;
import cc.squirreljme.jvm.pack.mem.ReadableMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class is used to access the pack ROM.
 *
 * @since 2021/02/09
 */
public final class PackRom
{
	/** Mask for integers. */
	private static final long _INT_MASK =
		0xFFFFFFFFL;
	
	/** The area where the ROM is. */
	protected final ReadableMemory rom;
	
	/** Properties for the ROM. */
	protected final HeaderStruct header;
	
	/** Libraries which are already available. */
	private JarPackageBracket[] _libraries;
	
	/** The table of contents for this ROM. */
	private TableOfContents _toc;
	
	/**
	 * Initializes the pack ROM manager.
	 * 
	 * @param __rom The memory where the ROM is located.
	 * @param __header Properties for the ROM.
	 * @since 2021/02/09
	 */
	private PackRom(ReadableMemory __rom, HeaderStruct __header)
		throws NullPointerException
	{
		if (__rom == null)
			throw new NullPointerException("NARG");
		
		this.rom = __rom;
		this.header = __header;
	}
	
	/**
	 * Returns all of the libraries that are available for the ROM.
	 * 
	 * @return The libraries.
	 * @since 2021/02/09
	 */
	public final JarPackageBracket[] libraries()
	{
		// Return pre-existing set if it is done already
		JarPackageBracket[] rv = this._libraries;
		if (rv != null)
			return rv.clone();
		
		// Debug
		Debugging.debugNote("Loading table of contents...");
		
		// We need the table of contents to look at libraries
		TableOfContents toc = this.__toc();
		
		// Setup base array
		int count = toc.count();
		rv = new JarPackageBracket[count];
		
		// Debug
		Debugging.debugNote("Reading table of contents...");
		
		// Load in library references
		ReadableMemory rom = this.rom;
		for (int dx = 0; dx < count; dx++)
		{
			// Flags for the JAR, is it compressed?
			int flags = toc.get(dx, PackTocProperty.INT_FLAGS);
			
			// Load in the JAR name
			String name = MemoryUtils.loadString(
				rom, toc.get(dx, PackTocProperty.OFFSET_NAME));
			
			/* {@squirreljme.error ZZ52 JAR has no name?} */
			if (name == null)
				throw new InvalidRomException("ZZ52");
			
			/* {@squirreljme.error ZZ51 Name does not match hashed named.} */
			if (name.hashCode() != toc.get(dx,
				PackTocProperty.HASHCODE_NAME))
				throw new InvalidRomException("ZZ51");
			
			// The location in memory where the ROM is located
			ReadableMemory data = rom.subSection(
				toc.get(dx, PackTocProperty.OFFSET_DATA) &
					PackRom._INT_MASK,
				toc.get(dx, PackTocProperty.SIZE_DATA) &
					PackRom._INT_MASK);
			
			// Initialize ROM reference
			rv[dx] = new JarRom(flags, name, data);
		}
		
		// Cache and use a copy
		this._libraries = rv;
		return rv.clone();
	}
	
	/**
	 * Returns the table of contents.
	 * 
	 * @return The table of contents.
	 * @since 2021/02/22
	 */
	private TableOfContents __toc()
	{
		TableOfContents rv = this._toc;
		if (rv != null)
			return rv;
		
		// We need these to determine where to read the data
		int tocBase = this.header.getInteger(PackProperty.OFFSET_TOC);
		int tocSize = this.header.getInteger(PackProperty.SIZE_TOC);
		
		/* {@squirreljme.error ZZ4u ROM has invalid table of contents
		reference.} */
		if (tocBase < 0 || tocSize <= 0)
			throw new InvalidRomException("ZZ4u");
		
		// Debug
		Debugging.debugNote("TOC at %x (len %d)", tocBase, tocSize);
		
		// Setup new table of contents
		this._toc = (rv = new TableOfContentsMemory(
			this.rom.subSection(tocBase, tocSize)));
		return rv;
	}
}
