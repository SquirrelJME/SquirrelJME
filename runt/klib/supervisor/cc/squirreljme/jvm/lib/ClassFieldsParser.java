// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.io.BinaryBlob;

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
	 * Initializes the class file parser.
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
	 * Returns the name of the field.
	 *
	 * @param __dx The index of the field.
	 * @return The name of the field.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2019/11/24
	 */
	public final String name(int __dx)
		throws IndexOutOfBoundsException
	{
		return this.pool.entryAsString(false, this.blob.readJavaUnsignedShort(
			this.tocOffset(__dx) + NAME_USHORT_OFFSET));
	}
	
	/**
	 * Index of the entry in the table of contents.
	 *
	 * @param __dx The index.
	 * @return The table of contents offset.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2019/11/24
	 */
	public final int tocOffset(int __dx)
		throws IndexOutOfBoundsException
	{
		if (__dx < 0 || __dx > this.count)
			throw new IndexOutOfBoundsException("IOOB");
		
		return __dx * ENTRY_SIZE;
	}
}

