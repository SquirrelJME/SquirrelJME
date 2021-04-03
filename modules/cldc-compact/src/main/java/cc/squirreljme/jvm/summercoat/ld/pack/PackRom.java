// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.pack;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.constants.ByteOrderType;
import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import cc.squirreljme.jvm.summercoat.ld.mem.AbstractReadableMemory;
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemory;
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemoryInputStream;
import cc.squirreljme.jvm.summercoat.ld.mem.RealMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * This class is used to access the pack ROM.
 *
 * @since 2021/02/09
 */
public final class PackRom
{
	/** The area where the ROM is. */
	protected final ReadableMemory rom;
	
	/** Properties for the ROM. */
	private final int[] _properties;
	
	/** Libraries which are already available. */
	private JarPackageBracket[] _libraries;
	
	/** The table of contents for this ROM. */
	private TableOfContents _toc;
	
	/**
	 * Initializes the pack ROM manager.
	 * 
	 * @param __rom The memory where the ROM is located.
	 * @param __properties Properties for the ROM.
	 * @since 2021/02/09
	 */
	private PackRom(ReadableMemory __rom, int[] __properties)
		throws NullPointerException
	{
		if (__rom == null)
			throw new NullPointerException("NARG");
		
		this.rom = __rom;
		this._properties = __properties;
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
		
		// We need the table of contents to look at libraries
		TableOfContents toc = this.__toc();
		
		// Debug
		Debugging.debugNote("Loading table of contents...");
		
		// Setup base array
		int count = toc.count();
		rv = new JarPackageBracket[count];
		
		// Debug
		Debugging.debugNote("Reading table of contents...");
		
		// Load in library references
		for (int i = 0; i < count; i++)
			throw Debugging.todo();
		
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
		int tocBase = this._properties[PackProperty.OFFSET_TOC];
		int tocSize = this._properties[PackProperty.SIZE_TOC];
		
		// {@squirreljme.error ZZ4u ROM has invalid table of contents
		// reference.}
		if (tocBase < 0 || tocSize <= 0)
			throw new InvalidRomException("ZZ4u");
		
		// Setup new table of contents
		this._toc = (rv = new TableOfContents(
			this.rom.subSection(tocBase, tocSize)));
		return rv;
	}
	
	/**
	 * Loads the PackRom from the given address.
	 * 
	 * @param __memAddr The memory address where the ROM is located.
	 * @return The pack ROM of the given address.
	 * @since 2021/02/14
	 */
	public static PackRom load(long __memAddr)
	{
		// Parse the header for the pack file
		AbstractReadableMemory headerMem = new RealMemory(__memAddr,
			ClassInfoConstants.PACK_MAXIMUM_HEADER_SIZE,
			ByteOrderType.BIG_ENDIAN);
		try (ReadableMemoryInputStream in = headerMem.inputStream())
		{
			// {@squirreljme.error ZZ43 Invalid ROM header. (Magic number)}
			int romMagic = in.readInt();
			if (ClassInfoConstants.PACK_MAGIC_NUMBER != romMagic)
				throw new InvalidRomException("ZZ43 " + romMagic);
				
			// Read the format version
			int formatVersion = in.readUnsignedShort();
			
			// {@squirreljme.error ZZ44 Cannot decode pack file because the
			// version identifier is not known. (The format version of the pack
			// file)}
			if (formatVersion != ClassInfoConstants.CLASS_VERSION_20201129)
				throw new InvalidRomException("ZZ44 " + formatVersion);
			
			// Read in all the data
			int numProperties = Math.min(in.readUnsignedShort(),
				PackProperty.NUM_PACK_PROPERTIES);
			int[] properties = new int[numProperties];
			for (int i = 0; i < numProperties; i++)
				properties[i] = in.readInt();
			
			// {@squirreljme.error ZZ46 PackROM size is not valid. (The size)}
			int romSize = properties[PackProperty.ROM_SIZE];
			if (romSize <= ClassInfoConstants.PACK_MAXIMUM_HEADER_SIZE)
				throw new InvalidRomException("ZZ46 " + romSize);
			
			// Debug
			Debugging.debugNote("ROM Size: %d (%xh)", romSize, romSize);
			
			// Build the final PackROM
			return new PackRom(new RealMemory(__memAddr, romSize,
				ByteOrderType.BIG_ENDIAN), properties);
		}
		
		// {@squirreljme.error ZZ42 The ROM is corrupted.}
		catch (IOException __e)
		{
			throw new InvalidRomException("ZZ42", __e); 
		}
	}
}
