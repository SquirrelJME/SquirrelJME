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
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import java.io.IOException;
import java.util.List;
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
	 * Sets the common header for a given ROM filling it with the standard
	 * default settings.
	 * 
	 * @param __chunk The output chunk.
	 * @param __settings The settings to store.
	 * @param __header The header to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/04
	 */
	public static void storeCommonHeader(ChunkWriter __chunk,
		RomSettings __settings, HeaderStructWriter __header)
		throws IOException, NullPointerException
	{
		if (__chunk == null || __settings == null || __header == null)
			throw new NullPointerException("NARG");
		
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
