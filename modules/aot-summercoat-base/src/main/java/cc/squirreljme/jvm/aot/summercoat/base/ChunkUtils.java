// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.base;

import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import cc.squirreljme.jvm.summercoat.constants.PackTocFlag;
import cc.squirreljme.jvm.summercoat.constants.PackTocProperty;
import cc.squirreljme.jvm.summercoat.ld.pack.PackRom;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.DataContainerLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.multiphasicapps.io.ChunkDataType;
import net.multiphasicapps.io.ChunkSection;
import net.multiphasicapps.io.ChunkWriter;

/**
 * Utilities for chunk writing.
 *
 * @since 2021/09/04
 */
public final class ChunkUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2021/09/04
	 */
	private ChunkUtils()
	{
	}
	
	/**
	 * Stores a data container library.
	 * 
	 * @param __lib The library to store.
	 * @param __jarChunk The chunk to store to.
	 * @param __entry The entry within the TOC.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void copyDataContainer(DataContainerLibrary __lib,
		ChunkSection __jarChunk, TableOfContentsEntry __entry)
		throws IOException, NullPointerException
	{
		if (__lib == null || __jarChunk == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Set entry details
		__entry.set(PackTocProperty.INT_FLAGS, PackTocFlag.RESOURCE);
		
		// Copy the data
		try (InputStream in = __lib.asStream())
		{
			StreamUtils.copy(in, __jarChunk);
		}
	}
	
	/**
	 * Stores a plain class file.
	 * 
	 * @param __lib The library to store.
	 * @param __jarChunk The chunk to store to.
	 * @param __entry The entry within the TOC.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void copyPlainClass(VMClassLibrary __lib,
		ChunkSection __jarChunk, TableOfContentsEntry __entry)
		throws IOException, NullPointerException
	{
		if (__lib == null || __jarChunk == null || __entry == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Sets the common header for a given ROM filling it with the standard
	 * default settings.
	 * 
	 * @param __chunk The output chunk.
	 * @param __headerChunk The header chunk.
	 * @param __settings The settings to store.
	 * @param __header The header to write.
	 * @param __pack The packing to use.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/04
	 */
	public static void storeCommonPackHeader(ChunkWriter __chunk,
		ChunkSection __headerChunk, RomSettings __settings,
		HeaderStructWriter __header, StandardPackWriter __pack)
		throws IOException, NullPointerException
	{
		if (__chunk == null || __settings == null || __header == null ||
			__pack == null)
			throw new NullPointerException("NARG");
		
		// Store shared header information
		ChunkUtils.storeCommonSharedHeader(__headerChunk,
			__pack.magic,
			ClassInfoConstants.CLASS_VERSION_20201129,
			__pack.header.properties.count());
		
		// Default version
		__header.set(PackProperty.INT_PACK_VERSION_ID,
			ClassInfoConstants.CLASS_VERSION_20201129);
		
		// Time and date of creation
		long createTime = System.currentTimeMillis();
		__header.set(PackProperty.TIME_DATE_HIGH,
			(int)(createTime >>> 32));
		__header.set(PackProperty.TIME_DATE_LOW,
			(int)createTime);
		
		// Size of the ROM
		__header.set(PackProperty.ROM_SIZE,
			__chunk.futureSize());
		
		// The main boot loader class
		String bootLoaderMainClass = __settings.bootLoaderMainClass;
		if (bootLoaderMainClass != null)
			__header.set(PackProperty.STRING_BOOTLOADER_MAIN_CLASS,
				ChunkUtils.writeString(__chunk, bootLoaderMainClass)
					.futureAddress());
		
		// The class path for the boot loader
		List<Integer> bootLoaderClassPath = __settings.bootLoaderClassPath;
		if (bootLoaderClassPath != null && !bootLoaderClassPath.isEmpty())
		{
			__header.set(PackProperty.COUNT_BOOTLOADER_CLASSPATH,
				__chunk.futureSize());
			__header.set(PackProperty.INTEGERS_BOOTLOADER_CLASSPATH,
				ChunkUtils.writeIntegers(__chunk, bootLoaderClassPath)
					.futureAddress());
		}
		
		// Store launcher main class
		String launcherMainClass = __settings.launcherMainClass;
		if (launcherMainClass != null)
			__header.set(PackProperty.STRING_LAUNCHER_MAIN_CLASS,
				ChunkUtils.writeString(__chunk, launcherMainClass)
					.futureAddress());
		
		// Store launcher arguments
		List<String> launcherArgs = __settings.launcherArgs;
		if (launcherArgs != null && !launcherArgs.isEmpty())
		{
			__header.set(PackProperty.COUNT_LAUNCHER_ARGS,
				__chunk.futureSize());
			__header.set(PackProperty.STRINGS_LAUNCHER_ARGS,
				ChunkUtils.writeStrings(__chunk, launcherArgs)
					.futureAddress());
		}
		
		// Store launcher class library
		List<Integer> launcherClassPath = __settings.launcherClassPath;
		if (launcherClassPath != null && !launcherClassPath.isEmpty())
		{
			__header.set(PackProperty.COUNT_LAUNCHER_CLASSPATH,
				__chunk.futureSize());
			__header.set(PackProperty.INTEGERS_LAUNCHER_CLASSPATH,
				ChunkUtils.writeIntegers(__chunk, launcherClassPath)
					.futureAddress());
		}
	}
	
	/**
	 * Stores common table of contents information for {@link PackRom}.
	 * 
	 * @param __toc The table of contents to write.
	 * @param __tocChunk The chunk to target.
	 * @param __header The pack header.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonPackToc(TableOfContentsWriter __toc,
		ChunkSection __tocChunk, HeaderStructWriter __header)
		throws IOException, NullPointerException
	{
		// How many and where is the TOC?
		__header.set(PackProperty.COUNT_TOC, __toc.futureCount());
		__header.set(PackProperty.OFFSET_TOC, __tocChunk.futureAddress());
		__header.set(PackProperty.SIZE_TOC, __tocChunk.futureSize());
		
		// This is the same for everything else!
		ChunkUtils.storeCommonSharedToc(__toc, __tocChunk);
	}
	
	/**
	 * Stores the common JAR table of contents.
	 * 
	 * @param __entry The entry to set.
	 * @param __lib The library being written.
	 * @param __mainChunk The main chunk, used to add strings.
	 * @param __jarChunk The JAR chunk.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonPackTocEntry(TableOfContentsEntry __entry,
		VMClassLibrary __lib, ChunkWriter __mainChunk,
		ChunkSection __jarChunk)
		throws IOException, NullPointerException
	{
		if (__entry == null || __lib == null || __mainChunk == null ||
			__jarChunk == null)
			throw new NullPointerException("NARG");
		
		// Build name information
		String name = __lib.name();
		ChunkSection nameChunk = ChunkUtils.writeString(__mainChunk, name);
		
		// Store the name info
		__entry.set(PackTocProperty.INT_NAME_HASHCODE, name.hashCode());
		__entry.set(PackTocProperty.OFFSET_NAME, nameChunk.futureAddress());
		__entry.set(PackTocProperty.SIZE_NAME, nameChunk.futureSize());
		
		// Store data information
		__entry.set(PackTocProperty.OFFSET_DATA, __jarChunk.futureAddress());
		__entry.set(PackTocProperty.SIZE_DATA, __jarChunk.futureSize());
	}
	
	/**
	 * Stores common header information.
	 * 
	 * @param __chunk The chunk to write to.
	 * @param __magic The magic number.
	 * @param __formatVersion The format version.
	 * @param __propertyCount The number of properties to store.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonSharedHeader(ChunkSection __chunk,
		int __magic, int __formatVersion, int __propertyCount)
		throws IOException, NullPointerException
	{
		if (__chunk == null)
			throw new NullPointerException("NARG");
		
		// Simple identifier
		__chunk.writeInt(__magic);
		
		// How the format is laid out and the number of used properties
		__chunk.writeUnsignedShortChecked(__formatVersion);
		__chunk.writeUnsignedShortChecked(__propertyCount);
	}
	
	/**
	 * Stores common table of contents information that is shared by every
	 * single format.
	 * 
	 * @param __toc The table of contents to write.
	 * @param __tocChunk The chunk to target.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonSharedToc(TableOfContentsWriter __toc,
		ChunkSection __tocChunk)
		throws IOException
	{
		int count = __toc.currentCount();
		int spanLength = __toc.spanLength;
		
		// Write starting info
		__tocChunk.writeUnsignedShortChecked(count);
		__tocChunk.writeUnsignedShortChecked(spanLength);
		
		// Write out the table of contents
		for (TableOfContentsEntry e : __toc.entries())
		{
			for (int i = 0; i < spanLength; i++)
				__tocChunk.writeFuture(ChunkDataType.INTEGER, e.get(i));
		}
	}
	
	/**
	 * Writes the given integer values.
	 * 
	 * @param __chunk The chunk to write to.
	 * @param __values The values to write.
	 * @return The section that was created
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/04
	 */
	public static ChunkSection writeIntegers(ChunkWriter __chunk,
		Iterable<Integer> __values)
		throws IOException, NullPointerException
	{
		if (__chunk == null || __values == null)
			throw new NullPointerException("NARG");
		
		ChunkSection section =
			__chunk.addSection(ChunkWriter.VARIABLE_SIZE, 4);
		
		for (Integer i : __values)
			section.writeInt(i);
		
		return section;
	}
	
	/**
	 * Writes the given string value.
	 * 
	 * @param __chunk The chunk to write to.
	 * @param __value The value to write.
	 * @return The section that was created
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/04
	 */
	public static ChunkSection writeString(ChunkWriter __chunk, String __value)
		throws IOException, NullPointerException
	{
		if (__chunk == null || __value == null)
			throw new NullPointerException("NARG");
		
		ChunkSection section =
			__chunk.addSection(ChunkWriter.VARIABLE_SIZE, 4);
		
		section.writeUTF(__value);
		
		return section;
	}
	
	/**
	 * Writes the given string values.
	 * 
	 * @param __chunk The chunk to write to.
	 * @param __values The values to write.
	 * @return The section that was created
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/04
	 */
	public static ChunkSection writeStrings(ChunkWriter __chunk,
		Iterable<String> __values)
		throws IOException, NullPointerException
	{
		if (__chunk == null || __values == null)
			throw new NullPointerException("NARG");
		
		ChunkSection section =
			__chunk.addSection(ChunkWriter.VARIABLE_SIZE, 4);
		
		for (String s : __values)
			section.writeUTF(s);
		
		return section;
	}
}
