// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

import cc.squirreljme.jvm.boot.io.BinaryBlob;

/**
 * This class is used to parse the fields of a class.
 *
 * @since 2019/11/17
 */
public final class ClassFieldsParser
{
	/** The size of entries in the field list. */
	public static final byte ENTRY_SIZE =
		16;
	
	/** Offset to field flags. */
	public static final byte FLAGS_INT_OFFSET =
		0;
		
	/** Offset to offset within class base. */
	public static final byte OFFSET_USHORT_OFFSET =
		4;
	
	/** Offset to field size. */
	public static final byte SIZE_USHORT_OFFSET =
		6;
		
	/** Offset to field name. */
	public static final byte NAME_USHORT_OFFSET =
		8;
	
	/** Offset to field type. */
	public static final byte TYPE_USHORT_OFFSET =
		10;
		
	/** Offset to constant value. */
	public static final byte CVALUE_USHORT_OFFSET =
		12;
		
	/** Offset to the data type. */
	public static final byte DATATYPE_BYTE_OFFSET =
		14;
	
	/** The constant pool for the class. */
	protected final ClassDualPoolParser pool;
	
	/** The blob for the field data. */
	protected final BinaryBlob blob;
	
	/** The number of fields available. */
	protected final int count;
	
	/**
	 * Initializes the class fields parser.
	 *
	 * @param __cp The dual pool parser.
	 * @param __b The binary blob.
	 * @param __n The field count.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/17
	 */
	public ClassFieldsParser(ClassDualPoolParser __cp, BinaryBlob __b, int __n)
		throws NullPointerException
	{
		if (__cp == null || __b == null)
			throw new NullPointerException("NARG");
		
		this.pool = __cp;
		this.blob = __b;
		this.count = __n;
	}
	
	/**
	 * Returns the number of fields.
	 *
	 * @return The number of fields.
	 * @since 2019/11/17
	 */
	public final int count()
	{
		return this.count;
	}
	
	/**
	 * Returns the flags for the field.
	 *
	 * @param __dx The index of the field.
	 * @return The field flags.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/11/24
	 */
	public final int flags(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.blob.readJavaInt(this.tocOffset(__dx) + ClassFieldsParser.FLAGS_INT_OFFSET);
	}
	
	/**
	 * Returns the name of the field.
	 *
	 * @param __dx The index of the field.
	 * @return The name of the field.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/11/24
	 */
	public final String name(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.pool.entryAsString(false, this.blob.readJavaUnsignedShort(
			this.tocOffset(__dx) + ClassFieldsParser.NAME_USHORT_OFFSET));
	}
	
	/**
	 * Returns the offset for the field.
	 *
	 * @param __dx The index of the field.
	 * @return The field offset.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/11/24
	 */
	public final int offset(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.blob.readJavaUnsignedShort(this.tocOffset(__dx) + ClassFieldsParser.OFFSET_USHORT_OFFSET);
	}
	
	/**
	 * Returns the size for the field.
	 *
	 * @param __dx The index of the field.
	 * @return The size offset.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/11/24
	 */
	public final int size(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.blob.readJavaUnsignedShort(this.tocOffset(__dx) + ClassFieldsParser.SIZE_USHORT_OFFSET);
	}
	
	/**
	 * Index of the entry in the table of contents.
	 *
	 * @param __dx The index.
	 * @return The table of contents offset.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/11/24
	 */
	public final int tocOffset(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		if (__dx < 0 || __dx > this.count)
			throw new IndexOutOfBoundsException("IOOB");
		
		return __dx * ClassFieldsParser.ENTRY_SIZE;
	}
	
	/**
	 * Returns the type of the field.
	 *
	 * @param __dx The index of the field.
	 * @return The type of the field.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/11/25
	 */
	public final PoolClassName type(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.pool.entryAsClassName(false,
			this.blob.readJavaUnsignedShort(this.tocOffset(__dx) + ClassFieldsParser.TYPE_USHORT_OFFSET));
	}
}

