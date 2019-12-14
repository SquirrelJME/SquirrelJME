// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

/**
 * Constants used when parsing the constant pool and such.
 *
 * @since 2019/10/13
 */
public interface ClassPoolConstants
{
	/** Offset to the tag type. */
	public static final byte OFFSET_OF_BYTE_ENTRY_TAG =
		0;
	
	/** Offset to the number of parts. */
	public static final byte OFFSET_OF_BYTE_ENTRY_NUMPARTS =
		1;
	
	/** Offset to the length of the data. */
	public static final byte OFFSET_OF_USHORT_ENTRY_LENGTH =
		2;
	
	/** Offset to the entry. */
	public static final byte OFFSET_OF_INT_ENTRY_OFFSET =
		4;
	
	/** Size of entries. */
	public static final byte ENTRY_SIZE =
		8;
	
	/** String type. */
	public static final byte TYPE_STRING =
		1;
	
	/** Class name. */
	public static final byte TYPE_CLASSNAME =
		2;
	
	/** Class names. */
	public static final byte TYPE_CLASSNAMES =
		3;
	
	/** Method descriptor. */
	public static final byte TYPE_METHOD_DESCRIPTOR =
		7;
}

