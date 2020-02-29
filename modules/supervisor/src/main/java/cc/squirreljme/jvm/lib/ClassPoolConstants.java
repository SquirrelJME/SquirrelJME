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
	byte OFFSET_OF_BYTE_ENTRY_TAG =
		0;
	
	/** Offset to the number of parts. */
	byte OFFSET_OF_BYTE_ENTRY_NUMPARTS =
		1;
	
	/** Offset to the length of the data. */
	byte OFFSET_OF_USHORT_ENTRY_LENGTH =
		2;
	
	/** Offset to the entry. */
	byte OFFSET_OF_INT_ENTRY_OFFSET =
		4;
	
	/** Size of entries. */
	byte ENTRY_SIZE =
		8;
	
	/** String type. */
	byte TYPE_STRING =
		1;
	
	/** Class name. */
	byte TYPE_CLASSNAME =
		2;
	
	/** Class names. */
	byte TYPE_CLASSNAMES =
		3;
	
	/** Class pool pointer. */
	byte TYPE_CLASS_POOL_POINTER =
		4;
	
	/** Method descriptor. */
	byte TYPE_METHOD_DESCRIPTOR =
		7;
	
	/** A string that is used as a constant (cached). */
	byte TYPE_USED_STRING =
		12;
	
	/** Class information pointer. */
	byte TYPE_CLASS_INFO_POINTER =
		14;
	
	/** Noted string. */
	byte TYPE_NOTED_STRING =
		15;
}

