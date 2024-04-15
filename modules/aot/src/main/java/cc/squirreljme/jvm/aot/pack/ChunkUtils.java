// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.pack;

import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.jvm.pack.constants.ClassInfoConstants;
import cc.squirreljme.jvm.pack.constants.JarProperty;
import cc.squirreljme.jvm.pack.constants.JarTocFlag;
import cc.squirreljme.jvm.pack.constants.JarTocProperty;
import cc.squirreljme.jvm.pack.constants.PackProperty;
import cc.squirreljme.jvm.pack.constants.PackTocFlag;
import cc.squirreljme.jvm.pack.constants.PackTocProperty;
import cc.squirreljme.jvm.pack.JarRom;
import cc.squirreljme.jvm.pack.PackRom;
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
	public static void copyPlainJarClasses(VMClassLibrary __lib,
		ChunkSection __jarChunk, TableOfContentsEntry __entry)
		throws IOException, NullPointerException
	{
		if (__lib == null || __jarChunk == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Start the base JAR file accordingly
		StandardPackWriter jar = new StandardPackWriter(
			ClassInfoConstants.JAR_MAGIC_NUMBER,
			JarProperty.NUM_JAR_PROPERTIES,
			JarTocProperty.NUM_JAR_TOC_PROPERTIES);
		jar.initialize();
			
		// Get the used chunks.
		ChunkWriter mainChunk = jar.mainChunk;
		ChunkSection tocChunk = jar.tocChunk;
		
		// Write header information
		HeaderStructWriter header = jar.header();
		ChunkUtils.storeCommonJarHeader(mainChunk, header, __lib);
		
		// Temporary buffer to use for data copy
		byte[] tempBuf = StreamUtils.buffer(null);
		
		// Manifest position, if valid
		int manifestIndex = -1;
		
		// Process each entry in the library
		TableOfContentsWriter toc = jar.toc();
		for (String resource : __lib.listResources())
		{
			// Setup entry chunk
			ChunkSection entryChunk = mainChunk.addSection(
				ChunkWriter.VARIABLE_SIZE, 8);
			
			// Is this a manifest entry?
			if (ChunkUtils.isJarManifest(resource))
				manifestIndex = toc.currentCount();
				
			// Declare new entry
			TableOfContentsEntry entry = toc.add();
			ChunkUtils.storeCommonJarTocEntry(entry, resource, mainChunk,
				entryChunk);
			
			// Copy the data accordingly
			try (InputStream in = __lib.resourceAsStream(resource))
			{
				StreamUtils.copy(in, entryChunk, tempBuf);
			}
		}
		
		// Prepare table of contents
		ChunkUtils.storeCommonJarToc(toc, tocChunk, header, manifestIndex);
		
		// Write to wherever our output is going
		mainChunk.writeTo(__jarChunk);
		__jarChunk.flush();
	}
	
	/**
	 * Returns the default entry flags for a JAR resource.
	 * 
	 * @param __resource The resource name.
	 * @return The default entry flags for this.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static int defaultJarEntryFlags(String __resource)
		throws NullPointerException
	{
		if (__resource == null)
			throw new NullPointerException("NARG");
		
		// Manifest file?
		if (ChunkUtils.isJarManifest(__resource))
			return JarTocFlag.RESOURCE | JarTocFlag.MANIFEST;
		
		// Class file?
		if (__resource.endsWith(".class"))
		{
			// There might be resources that are trying to cheat their way in 
			int lastSlash = __resource.lastIndexOf('/');
			int n = __resource.length() - ".class".length();
			if (n == 0 || (lastSlash >= 0 && n == (lastSlash + 1)))
				return JarTocFlag.RESOURCE;
			
			// Only compare the binary name (extension-less in ZIP) of the
			// class to determine if it is a compiled class 
			for (int i = 0; i < n; i++)
			{				
				char c = __resource.charAt(i);
				
				// Ignore slashes
				if (c == '/')
					continue;
				
				// Is this a valid character choice?
				if (!ChunkUtils.isValidJavaIdentifierChar(i == 0, c))
					return JarTocFlag.RESOURCE;
			}
			
			// Would be a class here
			return JarTocFlag.STANDARD_CLASS | JarTocFlag.EXECUTABLE_CLASS;
		}
		
		// Otherwise a resource
		return JarTocFlag.RESOURCE;
	}
	
	/**
	 * Is this a JAR manifest?
	 * 
	 * @param __resource The resource name.
	 * @return If this is a JAR manifest.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static boolean isJarManifest(String __resource)
		throws NullPointerException
	{
		if (__resource == null)
			throw new NullPointerException("NARG");
		
		return __resource.equals("META-INF/MANIFEST.MF");
	}
	
	/**
	 * Is this a valid Java identifier character?
	 * 
	 * @param __start Is this the starting character? 
	 * @param __c The character to check.
	 * @return If this is valid for a Java identifier.
	 * @since 2021/09/06
	 */
	private static boolean isValidJavaIdentifierChar(boolean __start, char __c)
	{
		// TODO: Put in a better way to do this? Since we do not have
		// TODO: Character.isJavaIdentifierStart/Part().
		
		// Numbers are valid after the start
		if (!__start && Character.isDigit(__c))
			return true;
		
		// Special characters that are always valid
		if (__c == '$' || __c == '_')
			return true;
		
		// Spaces are never valid
		if (Character.isWhitespace(__c))
			return false;
		
		// A letter
		if (Character.isUpperCase(__c) || Character.isLowerCase(__c))
			return true;
		
		// Not valid
		return false;
	}
	
	/**
	 * Stores common JAR header information.
	 * 
	 * @param __mainChunk The main chunk.
	 * @param __header The header data.
	 * @param __lib The library used.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonJarHeader(ChunkWriter __mainChunk,
		HeaderStructWriter __header, VMClassLibrary __lib)
		throws IOException, NullPointerException
	{
		if (__mainChunk == null || __header == null || __lib == null)
			throw new NullPointerException("NARG");
		
		// Build name information
		String name = __lib.name();
		ChunkSection nameChunk = ChunkUtils.writeString(__mainChunk, name);
		
		// Store the name info
		__header.set(JarProperty.HASHCODE_NAME, name.hashCode());
		__header.set(JarProperty.OFFSET_NAME, nameChunk.futureAddress());
		__header.set(JarProperty.SIZE_NAME, nameChunk.futureSize());
	}
	
	/**
	 * Stores common table of contents information for {@link JarRom}.
	 * 
	 * @param __toc The table of contents to write.
	 * @param __tocChunk The chunk to target.
	 * @param __header The pack header.
	 * @param __manifestIndex The index of the manifest.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonJarToc(TableOfContentsWriter __toc,
		ChunkSection __tocChunk, HeaderStructWriter __header,
		int __manifestIndex)
		throws IOException, NullPointerException
	{
		if (__toc == null || __tocChunk == null || __header == null)
			throw new NullPointerException("NARG");
		
		// How many and where is the TOC?
		__header.set(JarProperty.COUNT_TOC, __toc.futureCount());
		__header.set(JarProperty.OFFSET_TOC, __tocChunk.futureAddress());
		__header.set(JarProperty.SIZE_TOC, __tocChunk.futureSize());
		
		// The manifest index
		__header.set(JarProperty.RCDX_MANIFEST, __manifestIndex);
		
		// This is the same for everything else!
		ChunkUtils.storeCommonSharedToc(__toc, __tocChunk);
	}
	
	/**
	 * Stores the common JAR entry table of contents.
	 * 
	 * @param __entry The entry to set.
	 * @param __resource The resource name.
	 * @param __mainChunk The main chunk, used to add strings.
	 * @param __entryChunk The entry chunk.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonJarTocEntry(TableOfContentsEntry __entry,
		String __resource, ChunkWriter __mainChunk, ChunkSection __entryChunk)
		throws IOException, NullPointerException
	{
		// Build name information
		ChunkSection nameChunk = ChunkUtils.writeString(__mainChunk,
			__resource);
		
		// Default entry flags for this entry
		__entry.set(JarTocProperty.INT_FLAGS,
			ChunkUtils.defaultJarEntryFlags(__resource));
		
		// Store the name info
		__entry.set(JarTocProperty.HASHCODE_NAME, __resource.hashCode());
		__entry.set(JarTocProperty.OFFSET_NAME, nameChunk.futureAddress());
		__entry.set(JarTocProperty.SIZE_NAME, nameChunk.futureSize());
		
		// Store data information
		__entry.set(JarTocProperty.OFFSET_DATA, __entryChunk.futureAddress());
		__entry.set(JarTocProperty.SIZE_DATA, __entryChunk.futureSize());
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
	public static void storeCommonPackHeader(ChunkWriter __chunk,
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
				launcherArgs.size());
			__header.set(PackProperty.STRINGS_LAUNCHER_ARGS,
				ChunkUtils.writeStrings(__chunk, launcherArgs)
					.futureAddress());
		}
		
		// Store launcher class library
		List<Integer> launcherClassPath = __settings.launcherClassPath;
		if (launcherClassPath != null && !launcherClassPath.isEmpty())
		{
			__header.set(PackProperty.COUNT_LAUNCHER_CLASSPATH,
				launcherClassPath.size());
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
		__entry.set(PackTocProperty.HASHCODE_NAME, name.hashCode());
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
	 * @param __properties The properties to store.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void storeCommonSharedHeader(ChunkSection __chunk,
		int __magic, int __formatVersion, PropertySpan __properties)
		throws IOException, NullPointerException
	{
		if (__chunk == null)
			throw new NullPointerException("NARG");
		
		// Simple identifier
		__chunk.writeInt(__magic);
		
		// How the format is laid out and the number of used properties
		__chunk.writeUnsignedShortChecked(__formatVersion);
		__chunk.writeUnsignedShortChecked(__properties.count());
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
