// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.StaticClassInfoProperty;
import dev.shadowtail.classfile.xlate.DataType;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents the raw minimized header of a class.
 *
 * @since 2019/04/16
 */
public final class MinimizedClassHeader
{
	/** The magic number for the header. */
	public static final int MAGIC_NUMBER =
		0x00586572;
	
	/** Magic number for the end of file. */
	public static final int END_MAGIC_NUMBER =
		0x42796521;
	
	/**
	 * The size of the header without the magic number.
	 * 
	 * @deprecated The header size is now dynamic.
	 */
	@Deprecated
	public static final int HEADER_SIZE_WITHOUT_MAGIC =
		108;
	
	/**
	 * The size of the header with the magic number.
	 *
	 * @deprecated The header size is not dynamic. 
	 */
	@Deprecated
	public static final int HEADER_SIZE_WITH_MAGIC =
		MinimizedClassHeader.HEADER_SIZE_WITHOUT_MAGIC + 4;
	
	/** The format version of the class. */
	protected final short formatVersion;
	
	/** The properties of the class. */
	private final int[] _properties;
	
	/**
	 * Initializes the class header.
	 *
	 * @param __fVer The format version of the class.
	 * @param __props The property values.
	 * @throws IllegalArgumentException If there are too many properties.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public MinimizedClassHeader(short __fVer, int... __props)
		throws IllegalArgumentException, NullPointerException
	{
		if (__props == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC4t Too many properties were passed to the
		// class file.}
		if (__props.length > StaticClassInfoProperty.NUM_STATIC_PROPERTIES)
			throw new IllegalArgumentException("JC4t " + __props.length);
		
		this.formatVersion = __fVer;
		this._properties = Arrays.copyOf(__props,
			StaticClassInfoProperty.NUM_STATIC_PROPERTIES);
	}
	
	/**
	 * Gets the specified class information property.
	 * 
	 * @param __property The {@link StaticClassInfoProperty} to get.
	 * @return The property value.
	 * @throws IllegalArgumentException If the property is not valid.
	 * @since 2020/11/29
	 */
	public final int get(int __property)
		throws IllegalArgumentException
	{
		// {@squirreljme.error JC4s Invalid class property. (The property)}
		if (__property < 0 ||
			__property >= StaticClassInfoProperty.NUM_STATIC_PROPERTIES)
			throw new IllegalArgumentException("JC4s " + __property);
		
		return this._properties[__property];
	}
	
	/** Class flags. */
	@Deprecated
	public int getClassflags()
	{
		return this.get(StaticClassInfoProperty.INT_CLASS_FLAGS);
	}
	
	/** Interfaces in class. */
	@Deprecated
	public int getClassints()
	{
		return this.get(StaticClassInfoProperty.SPOOL_INTERFACES);
	}
	
	/** Name of class. */
	@Deprecated
	public int getClassname()
	{
		return this.get(StaticClassInfoProperty.SPOOL_THIS_CLASS_NAME);
	}
	
	/** Class source filename. */
	@Deprecated
	public int getClasssfn()
	{
		return this.get(StaticClassInfoProperty.SPOOL_SOURCE_FILENAME);
	}
	
	/** Super class name. */
	@Deprecated
	public int getClasssuper()
	{
		return this.get(StaticClassInfoProperty.SPOOL_SUPER_CLASS_NAME);
	}
	
	/** Class type. */
	@Deprecated
	public int getClasstype()
	{
		return this.get(StaticClassInfoProperty.INT_CLASS_TYPE);
	}
	
	/** Class version. */
	@Deprecated
	public int getClassvers()
	{
		return this.get(StaticClassInfoProperty.INT_CLASS_VERSION);
	}
	
	/** The data type of the class. */
	@Deprecated
	public DataType getDatatype()
	{
		return DataType.of(this.get(StaticClassInfoProperty.INT_DATA_TYPE));
	}
	
	/** File size. */
	@Deprecated
	public int getFilesize()
	{
		return this.get(StaticClassInfoProperty.INT_FILE_SIZE);
	}
	
	/** Instance field bytes. */
	@Deprecated
	public int getIfbytes()
	{
		return this.get(StaticClassInfoProperty.INT_INSTANCE_FIELD_BYTES);
	}
	
	/** Instance field count. */
	@Deprecated
	public int getIfcount()
	{
		return this.get(StaticClassInfoProperty.INT_INSTANCE_FIELD_COUNT);
	}
	
	/** Instance field objects. */
	@Deprecated
	public int getIfobjs()
	{
		return this.get(StaticClassInfoProperty.INT_INSTANCE_FIELD_OBJECTS);
	}
	
	/** Instance field data offset. */
	@Deprecated
	public int getIfoff()
	{
		return this.get(StaticClassInfoProperty.OFFSET_INSTANCE_FIELD_DATA);
	}
	
	/** Instance field data size. */
	@Deprecated
	public int getIfsize()
	{
		return this.get(StaticClassInfoProperty.SIZE_INSTANCE_FIELD_DATA);
	}
	
	/** Instance method count. */
	@Deprecated
	public int getImcount()
	{
		return this.get(StaticClassInfoProperty.INT_INSTANCE_METHOD_COUNT);
	}
	
	/** Instance method data offset. */
	@Deprecated
	public int getImoff()
	{
		return this.get(StaticClassInfoProperty.OFFSET_INSTANCE_METHOD_DATA);
	}
	
	/** Instance method data size. */
	@Deprecated
	public int getImsize()
	{
		return this.get(StaticClassInfoProperty.SIZE_INSTANCE_METHOD_DATA);
	}
	
	/** Runtime constant pool offset. */
	@Deprecated
	public int getRuntimepooloff()
	{
		return this.get(StaticClassInfoProperty.OFFSET_RUNTIME_POOL);
	}
	
	/** Runtime constant pool size. */
	@Deprecated
	public int getRuntimepoolsize()
	{
		return this.get(StaticClassInfoProperty.SIZE_RUNTIME_POOL);
	}
	
	/** Static field bytes. */
	@Deprecated
	public int getSfbytes()
	{
		return this.get(StaticClassInfoProperty.INT_STATIC_FIELD_BYTES);
	}
	
	/** Static field count. */
	@Deprecated
	public int getSfcount()
	{
		return this.get(StaticClassInfoProperty.INT_STATIC_FIELD_COUNT);
	}
	
	/** Static field objects. */
	@Deprecated
	public int getSfobjs()
	{
		return this.get(StaticClassInfoProperty.INT_STATIC_FIELD_OBJECTS);
	}
	
	/** Static field data offset. */
	@Deprecated
	public int getSfoff()
	{
		return this.get(StaticClassInfoProperty.OFFSET_STATIC_FIELD_DATA);
	}
	
	/** Static field data size. */
	@Deprecated
	public int getSfsize()
	{
		return this.get(StaticClassInfoProperty.SIZE_STATIC_FIELD_DATA);
	}
	
	/** Static method count. */
	@Deprecated
	public int getSmcount()
	{
		return this.get(StaticClassInfoProperty.INT_STATIC_METHOD_COUNT);
	}
	
	/** Static method data offset. */
	@Deprecated
	public int getSmoff()
	{
		return this.get(StaticClassInfoProperty.OFFSET_STATIC_METHOD_DATA);
	}
	
	/** Static method data size. */
	@Deprecated
	public int getSmsize()
	{
		return this.get(StaticClassInfoProperty.SIZE_STATIC_METHOD_DATA);
	}
	
	/** The index of the method which is __start. */
	@Deprecated
	public int getStartmethodindex()
	{
		return this.get(StaticClassInfoProperty.INT_BOOT_METHOD_INDEX);
	}
	
	/** Static constant pool offset. */
	@Deprecated
	public int getStaticpooloff()
	{
		return this.get(StaticClassInfoProperty.OFFSET_STATIC_POOL);
	}
	
	/** Static constant pool size. */
	@Deprecated
	public int getStaticpoolsize()
	{
		return this.get(StaticClassInfoProperty.SIZE_STATIC_POOL);
	}
	
	/** High bits for UUID. */
	@Deprecated
	public int getUuidhi()
	{
		return this.get(StaticClassInfoProperty.INT_UUID_HI);
	}
	
	/** Low bits for UUID. */
	@Deprecated
	public int getUuidlo()
	{
		return this.get(StaticClassInfoProperty.INT_UUID_LO);
	}
	
	/**
	 * Decodes the minimized class header.
	 *
	 * @param __is The bytes to decode from.
	 * @return The resulting minimized class header.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public static MinimizedClassHeader decode(InputStream __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		DataInputStream dis = new DataInputStream(__is);
		
		// {@squirreljme.error JC04 Invalid minimized class magic number.
		// (The magic number)}
		int readMagic;
		if (MinimizedClassHeader.MAGIC_NUMBER != (readMagic = dis.readInt()))
			throw new InvalidClassFormatException(String.format("JC04 %08x",
				readMagic));
		
		// {@squirreljme.error JC4u Cannot decode class because the version
		// identifier is not known. (The format version of the class)}
		int formatVersion = dis.readUnsignedShort();
		if (formatVersion != ClassInfoConstants.VERSION_20201129)
			throw new RuntimeException("JC4u " + formatVersion);
		
		// Read in all the data
		int numProperties = dis.readUnsignedShort();
		int[] properties = new int[numProperties];
		for (int i = 0; i < numProperties; i++)
			properties[i] = dis.readInt();
		
		// Setup finalized class
		return new MinimizedClassHeader((short)formatVersion, properties);
	}	
}

