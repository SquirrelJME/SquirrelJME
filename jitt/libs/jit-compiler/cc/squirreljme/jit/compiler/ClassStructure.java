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

import cc.squirreljme.jit.objectfile.StorageType;
import cc.squirreljme.jit.objectfile.StructureType;

/**
 * This specifies the fields which are part of the class structure and is used
 * to store class information.
 *
 * @since 2018/02/26
 */
public enum ClassStructure
	implements StructureType
{
	/** Name of the class. */
	NAME,
	
	/** The has code for the class name, for quicker finding. */
	HASHCODE,
	
	/** Superclass pointer. */
	SUPER,
	
	/** Number of implemented interfaces. */
	INTERFACES_COUNT,
	
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
	
	/** The static initializer method. */
	STATIC_INITIALIZER,
	
	/** End. */
	;
	
	/** Used for previous element determination. */
	private static final ClassStructure[] _VALUES =
		ClassStructure.values();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/26
	 */
	@Override
	public final ClassStructure offsetStructElement(int __o)
	{
		int now = this.ordinal() + __o;
		ClassStructure[] values = ClassStructure._VALUES;
		if (now < 0 || now >= values.length)
			return null;
		return values[now];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/26
	 */
	@Override
	public final StorageType storageType()
	{
		switch (this)
		{
			case NAME:
				return StorageType.POINTER;
			
			case HASHCODE:
				return StorageType.INTEGER;
			
			case SUPER:
				return StorageType.POINTER;
			
			case INTERFACES_COUNT:
				return StorageType.INTEGER;
			
			case INTERFACES:
				return StorageType.POINTER;
			
			case INTERFACES_METHODS_TABLE:
				return StorageType.POINTER;
			
			case FLAGS:
				return StorageType.INTEGER;
			
			case DEFAULT_CONSTRUCTOR:
				return StorageType.POINTER;
			
			case DEFAULT_CONSTRUCTOR_FLAGS:
				return StorageType.INTEGER;
			
			case MAIN_METHOD:
				return StorageType.POINTER;
			
			case MAIN_METHOD_FLAGS:
				return StorageType.INTEGER;
			
			case ALLOCATION_SIZE:
				return StorageType.POINTER;
			
			case GLOBAL_OBJECT_OFFSET:
				return StorageType.OFFSET;
			
			case RESOURCE_GROUP:
				return StorageType.POINTER;
			
			case ENUM_COUNT:
				return StorageType.INTEGER;
			
			case ENUM_FIELD_OFFSETS:
				return StorageType.POINTER;
			
			case STATIC_INITIALIZER:
				return StorageType.POINTER;
			
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

