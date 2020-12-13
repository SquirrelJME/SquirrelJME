// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.packfile;

import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.JarProperty;
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents the header for a minimized pack file, it mostly just
 * represents a number of JAR files which are combined into one.
 *
 * @since 2019/05/28
 */
public final class MinimizedPackHeader
{
	/** The size of the header without the magic number. */
	@Deprecated
	public static final int HEADER_SIZE_WITHOUT_MAGIC =
		52;
	
	/** The size of the header with the magic number. */
	@Deprecated
	public static final int HEADER_SIZE_WITH_MAGIC =
		56;
	
	/** The offset to the BootJAR offset (which has BootRAM), with magic. */
	@Deprecated
	public static final int OFFSET_OF_BOOTJAROFFSET =
		16;
	
	/** The offset to the BootJAR size, with magic. */
	@Deprecated
	public static final int OFFSET_OF_BOOTJARSIZE =
		20;
	
	/** Size of individual table of contents entry. */
	@Deprecated
	public static final int TOC_ENTRY_SIZE =
		20;
	
	/** The format version of the JAR. */
	protected final short formatVersion;
	
	/** The properties of the class. */
	private final int[] _properties;
	
	/**
	 * Initializes the JAR header.
	 * 
	 * @param __fV The format version of the JAR header.
	 * @param __properties The properties for the JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/09
	 */
	public MinimizedPackHeader(short __fV, int... __properties)
	{
		this.formatVersion = __fV;
		this._properties = Arrays.copyOf(__properties,
			JarProperty.NUM_JAR_PROPERTIES);
	}
	
	/**
	 * Gets the specified property.
	 * 
	 * @param __property The {@link PackProperty} to get.
	 * @return The property value.
	 * @throws IllegalArgumentException If the property is not valid.
	 * @since 2020/12/09
	 */
	public final int get(int __property)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BI03 Invalid Pack property. (The property)}
		if (__property < 0 ||
			__property >= PackProperty.NUM_PACK_PROPERTIES)
			throw new IllegalArgumentException("BI03 " + __property);
		
		return this._properties[__property];
	}
	
	/**
	 * Decodes the input pack header.
	 * 
	 * @param __in The stream to decode from.
	 * @return The resultant header.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/09
	 */
	public static MinimizedPackHeader decode(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		DataInputStream dis = new DataInputStream(__in);
		
		// {@squirreljme.error BI04 Invalid minimized Jar magic number.
		// (The magic number)}
		int magic;
		if (ClassInfoConstants.PACK_MAGIC_NUMBER != (magic = dis.readInt()))
			throw new InvalidClassFormatException(String.format("BI04 %08x",
				magic));
		
		// {@squirreljme.error BI05 Cannot decode pack file because the version
		// identifier is not known. (The format version of the pack file)}
		int formatVersion = dis.readUnsignedShort();
		if (formatVersion != ClassInfoConstants.CLASS_VERSION_20201129)
			throw new RuntimeException("BI05 " + formatVersion);
		
		// Read in all the data
		int numProperties = dis.readUnsignedShort();
		int[] properties = new int[numProperties];
		for (int i = 0; i < numProperties; i++)
			properties[i] = dis.readInt();
		
		// Setup finalized pack
		return new MinimizedPackHeader((short)formatVersion, properties);
	}
}

