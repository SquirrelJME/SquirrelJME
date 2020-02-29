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
 * This is a parser for methods within a class.
 *
 * @since 2019/11/29
 */
public final class ClassMethodsParser
{
	/** The size of entries in the field list. */
	public static final byte ENTRY_SIZE =
		20;
	
	/** Flags offset. */
	public static final byte FLAGS_INT_OFFSET =
		0;
	
	/** Index of method. */
	public static final byte INDEX_USHORT_OFFSET =
		4;
	
	/** Name of method. */
	public static final byte NAME_USHORT_OFFSET =
		6;
	
	/** Type of method. */
	public static final byte TYPE_USHORT_OFFSET =
		8;
	
	/** Address of the code. */
	public static final byte CODE_ADDRESS_INT_OFFSET =
		10;
	
	/** Size of the code. */
	public static final byte CODE_SIZE_INT_OFFSET =
		14;
	
	/** The constant pool for the class. */
	protected final ClassDualPoolParser pool;
	
	/** The blob for the method data. */
	protected final BinaryBlob blob;
	
	/** The number of fields available. */
	protected final int count;
	
	/**
	 * Initializes the class method parser.
	 *
	 * @param __cp The dual pool parser.
	 * @param __b The binary blob.
	 * @param __n The method count.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/29
	 */
	public ClassMethodsParser(ClassDualPoolParser __cp, BinaryBlob __b,
		int __n)
		throws NullPointerException
	{
		if (__cp == null || __b == null)
			throw new NullPointerException("NARG");
		
		this.pool = __cp;
		this.blob = __b;
		this.count = __n;
	}
	
	/**
	 * Returns the blob to the method code.
	 *
	 * @param __dx The index of the method.
	 * @return The blob for the method.
	 * @throws IndexOutOfBoundsException If the method is out of bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/12/14
	 */
	public final BinaryBlob code(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		BinaryBlob blob = this.blob;
		int offset = this.tocOffset(__dx);
		
		return blob.subSection(
			blob.readJavaInt(offset + ClassMethodsParser.CODE_ADDRESS_INT_OFFSET),
			blob.readJavaInt(offset + ClassMethodsParser.CODE_SIZE_INT_OFFSET));
	}
	
	/**
	 * Finds the given method and returns the index of it.
	 *
	 * @param __name The method name.
	 * @param __type The method type, may be {@code null} if not needed.
	 * @return The found index or {@code -1} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/27
	 */
	public final int findMethod(String __name, String __type)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Locate
		for (int i = 0, n = this.count; i < n; i++)
		{
			// Name does not match
			if (!__name.equals(this.name(i)))
				continue;
			
			// Type does not match
			if (__type != null && __type.equals(this.type(i)))
				continue;
			
			return i;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns the name of the method.
	 *
	 * @param __dx The index of the method.
	 * @return The method name.
	 * @throws IndexOutOfBoundsException If the method is out of bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/12/14
	 */
	public final String name(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.pool.entryAsString(false, this.blob.readJavaUnsignedShort(
			this.tocOffset(__dx) + ClassMethodsParser.NAME_USHORT_OFFSET));
	}
	
	/**
	 * Index of the entry in the table of contents.
	 *
	 * @param __dx The index.
	 * @return The table of contents offset.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/12/14
	 */
	public final int tocOffset(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		if (__dx < 0 || __dx > this.count)
			throw new IndexOutOfBoundsException("IOOB");
		
		return __dx * ClassMethodsParser.ENTRY_SIZE;
	}
	
	/**
	 * Returns the tyoe of the method.
	 *
	 * @param __dx The index of the method.
	 * @return The method type.
	 * @throws IndexOutOfBoundsException If the method is out of bounds.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @since 2019/12/14
	 */
	public final PoolMethodDescriptor type(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.pool.entryAsMethodDescriptor(false,
			this.blob.readJavaUnsignedShort(this.tocOffset(__dx) + ClassMethodsParser.TYPE_USHORT_OFFSET));
	}
}

