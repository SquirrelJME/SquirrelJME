// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler;

/**
 * This specifies the fields which are part of the class structure and is used
 * to store class information.
 *
 * @since 2018/02/26
 */
public enum ClassStructure
{
	/** Name of the class. */
	NAME,
	
	/** The has code for the class name, for quicker finding. */
	HASHCODE,
	
	/** Superclass pointer. */
	SUPER,
	
	/** Number of implemented interfaces. */
	INTERFACES_COUNT.
	
	/** Pointer to implemented interfaces. */
	INTERFACES,
	
	/** Interface method implementations, pointers to tables for methods. */
	INTERFACES_METHODS_TABLE,
	
	/** Class flags. */
	FLAGS,
	
	/** Default constructor pointer (used for {@code newInstance()}). */
	DEFAULT_CONSTRUCTOR,
	
	/** The flags for the default constructor. */
	DEFAULT_CONSTRUCTOR_FLAGS,
	
	/** Pointer to the method {@code static main(String[])}. */
	MAIN_METHOD,
	
	/** Flags the main method uses. */
	MAIN_METHOD_FLAGS,
	
	/** The size of the class in bytes, for allocation. */
	ALLOCATION_SIZE,
	
	/** The offset of the field in global space to the {@link Class} object. */
	GLOBAL_OBJECT_OFFSET,
	
	/** The resource group which contains the resources for the class. */
	RESOURCE_GROUP,
	
	/** Number of elements in the enumeration. */
	ENUM_COUNT,
	
	/** Offsets to fields in the global space representing enum fields. */
	ENUM_FIELD_OFFSETS,
	
	/** End. */
	;
}

