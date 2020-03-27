// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

/**
 * This represents the entry type in the pool.
 *
 * @since 2019/04/14
 */
public enum MinimizedPoolEntryType
{
	/** Nothing. */
	NULL,
	
	/** String. */
	STRING,
	
	/** Name of class. */
	CLASS_NAME,
	
	/** Class names (used for interfaces). */
	CLASS_NAMES,
	
	/** The constant pool for the given class. */
	CLASS_POOL,
	
	/** Accessed Field. */
	ACCESSED_FIELD,
	
	/** Invoked Method. */
	INVOKED_METHOD,
	
	/** Method Descriptor. */
	METHOD_DESCRIPTOR,
	
	/** Integer. */
	INTEGER,
	
	/** Float. */
	FLOAT,
	
	/** Long. */
	LONG,
	
	/** Double. */
	DOUBLE,
	
	/** A plain string that was used. */
	USED_STRING,
	
	/** The index of a method. */
	METHOD_INDEX,
	
	/** Class information. */
	CLASS_INFO_POINTER,
	
	/** A string which has been noted for debug purposes. */
	NOTED_STRING,
	
	/* End. */
	;
	
	/**
	 * Can this be in the runtime pool?
	 *
	 * @return If this can be in the runtime pool.
	 * @since 2019/07/20
	 */
	public final boolean isRuntime()
	{
		switch (this)
		{
			case NULL:
			case CLASS_INFO_POINTER:
			case CLASS_POOL:
			case ACCESSED_FIELD:
			case INVOKED_METHOD:
			case USED_STRING:
			case METHOD_INDEX:
			case NOTED_STRING:
				return true;
		}
		
		return false;
	}
	
	/**
	 * Can this be in the static pool?
	 *
	 * @return If this can be in the static pool.
	 * @since 2019/07/20
	 */
	public final boolean isStatic()
	{
		switch (this)
		{
			case NULL:
			case STRING:
			case CLASS_NAME:
			case CLASS_NAMES:
			case METHOD_DESCRIPTOR:
			case INTEGER:
			case FLOAT:
			case LONG:
			case DOUBLE:
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the type for the specified index.
	 *
	 * @param __i The index to get.
	 * @return The type for this index.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @since 2019/04/17
	 */
	public static final MinimizedPoolEntryType of(int __i)
		throws IllegalArgumentException
	{
		switch (__i)
		{
			case 0:		return MinimizedPoolEntryType.NULL;
			case 1:		return MinimizedPoolEntryType.STRING;
			case 2:		return MinimizedPoolEntryType.CLASS_NAME;
			case 3:		return MinimizedPoolEntryType.CLASS_NAMES;
			case 4:		return MinimizedPoolEntryType.CLASS_POOL;
			case 5:		return MinimizedPoolEntryType.ACCESSED_FIELD;
			case 6:		return MinimizedPoolEntryType.INVOKED_METHOD;
			case 7:		return MinimizedPoolEntryType.METHOD_DESCRIPTOR;
			case 8:		return MinimizedPoolEntryType.INTEGER;
			case 9:		return MinimizedPoolEntryType.FLOAT;
			case 10:	return MinimizedPoolEntryType.LONG;
			case 11:	return MinimizedPoolEntryType.DOUBLE;
			case 12:	return MinimizedPoolEntryType.USED_STRING;
			case 13:	return MinimizedPoolEntryType.METHOD_INDEX;
			case 14:	return MinimizedPoolEntryType.CLASS_INFO_POINTER;
			case 15:	return MinimizedPoolEntryType.NOTED_STRING;
		}
		
		// {@squirreljme.error JC0e Unknown pool type. (The type)}
		throw new IllegalArgumentException("JC0e " + __i);
	}
	
	/**
	 * Returns the entry type that is used for the specified class.
	 *
	 * @param __cl The class to check.
	 * @return The entry type for the class.
	 * @throws IllegalArgumentException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public static final MinimizedPoolEntryType ofClass(Class<?> __cl)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Easier to do it just by name
		switch (__cl.getName())
		{
			case "java.lang.String":
				return MinimizedPoolEntryType.STRING;
			case "java.lang.Integer":
				return MinimizedPoolEntryType.INTEGER;
			case "java.lang.Float":
				return MinimizedPoolEntryType.FLOAT;
			case "java.lang.Long":
				return MinimizedPoolEntryType.LONG;
			case "java.lang.Double":
				return MinimizedPoolEntryType.DOUBLE;
			case "dev.shadowtail.classfile.pool.AccessedField":
				return MinimizedPoolEntryType.ACCESSED_FIELD;
			case "dev.shadowtail.classfile.pool.ClassPool":
				return MinimizedPoolEntryType.CLASS_POOL;
			case "dev.shadowtail.classfile.pool.InvokedMethod":
				return MinimizedPoolEntryType.INVOKED_METHOD;
			case "net.multiphasicapps.classfile.ClassName":
				return MinimizedPoolEntryType.CLASS_NAME;
			case "net.multiphasicapps.classfile.ClassNames":
				return MinimizedPoolEntryType.CLASS_NAMES;
			case "List net.multiphasicapps.classfile.ClassName":
				return MinimizedPoolEntryType.CLASS_NAMES;
			case "net.multiphasicapps.classfile.MethodDescriptor":
				return MinimizedPoolEntryType.METHOD_DESCRIPTOR;
			case "dev.shadowtail.classfile.pool.UsedString":
				return MinimizedPoolEntryType.USED_STRING;
			case "dev.shadowtail.classfile.pool.MethodIndex":
				return MinimizedPoolEntryType.METHOD_INDEX;
			case "dev.shadowtail.classfile.pool.ClassInfoPointer":
				return MinimizedPoolEntryType.CLASS_INFO_POINTER;
			case "dev.shadowtail.classfile.pool.NotedString":
				return MinimizedPoolEntryType.NOTED_STRING;
			case "dev.shadowtail.classfile.pool.NullPoolEntry":
				return MinimizedPoolEntryType.NULL;
		}
		
		// {@squirreljme.error JC0f Class does not map to a pool entry
		// type. (The class)}
		throw new IllegalArgumentException("JC0f " + __cl);
	}
}

