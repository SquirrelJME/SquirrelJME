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
 * This utility exists for the parsing of SquirrelJME's class files and allows
 * the bootstrap and class loaders the ability to read them.
 *
 * @since 2019/10/06
 */
public final class ClassFileParser
{
	/** The blob of the class. */
	public final BinaryBlob blob;
	
	/**
	 * Initializes the class file parser.
	 *
	 * @param __blob The ROM blob.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/06
	 */
	public ClassFileParser(BinaryBlob __blob)
		throws NullPointerException
	{
		if (__blob == null)
			throw new NullPointerException("NARG");
		
		this.blob = __blob;
	}
	
	/**
	 * Returns the number of fields in the class.
	 *
	 * @param __is Get the static field count.
	 * @return The number of fields in the class.
	 * @since 2019/10/26
	 */
	public final int fieldCount(boolean __is)
	{
		return this.blob.readJavaUnsignedShort(
			(__is ? ClassFileConstants.OFFSET_OF_USHORT_SFCOUNT :
			ClassFileConstants.OFFSET_OF_USHORT_IFCOUNT));
	}
	
	/**
	 * Returns the field data offset.
	 *
	 * @param __is Get the static field data offset.
	 * @return The field data offset.
	 * @since 2019/11/17
	 */
	public final int fieldDataOffset(boolean __is)
	{
		return this.blob.readJavaInt(
			(__is ? ClassFileConstants.OFFSET_OF_INT_SFOFF :
			ClassFileConstants.OFFSET_OF_INT_IFOFF));
	}
	
	/**
	 * Returns the field data size.
	 *
	 * @param __is Get the static field data size.
	 * @return The field data size.
	 * @since 2019/11/17
	 */
	public final int fieldDataSize(boolean __is)
	{
		return this.blob.readJavaInt(
			(__is ? ClassFileConstants.OFFSET_OF_INT_SFSIZE :
			ClassFileConstants.OFFSET_OF_INT_IFSIZE));
	}
	
	/**
	 * Returns a parser for class fields.
	 *
	 * @param __is Get static fields?
	 * @return The parser for fields.
	 * @since 2019/11/17
	 */
	public final ClassFieldsParser fields(boolean __is)
	{
		BinaryBlob blob = this.blob;
		return new ClassFieldsParser(this.pool(),
			this.blob.subSection(this.fieldDataOffset(__is),
				this.fieldDataSize(__is)), this.fieldCount(__is));
	}
	
	/**
	 * Returns the size of all of the fields.
	 *
	 * @param __is Get the size of static fields?
	 * @return The number of bytes the field requires for consumption.
	 * @since 2019/10/21
	 */
	public final int fieldSize(boolean __is)
	{
		return this.blob.readJavaUnsignedShort(
			(__is ? ClassFileConstants.OFFSET_OF_USHORT_SFBYTES :
			ClassFileConstants.OFFSET_OF_USHORT_IFBYTES));
	}
	
	/**
	 * Returns a dual pool parser for this class.
	 *
	 * @return The dual pool parser.
	 * @since 2019/10/13
	 */
	public final ClassDualPoolParser pool()
	{
		throw new todo.TODO();
	}
}

