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
	
	/** End. */
	;
	
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
			case 0:		return NULL;
			case 1:		return STRING;
			case 2:		return CLASS_NAME;
			case 3:		return CLASS_NAMES;
			case 4:		return CLASS_POOL;
			case 5:		return ACCESSED_FIELD;
			case 6:		return INVOKED_METHOD;
			case 7:		return METHOD_DESCRIPTOR;
			case 8:		return INTEGER;
			case 9:		return FLOAT;
			case 10:	return LONG;
			case 11:	return DOUBLE;
			case 12:	return USED_STRING;
			case 13:	return METHOD_INDEX;
		}
		
		// {@squirreljme.error JC3s Unknown pool type. (The type)}
		throw new IllegalArgumentException("JC3s " + __i);
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
				return STRING;
			case "java.lang.Integer":
				return INTEGER;
			case "java.lang.Float":
				return FLOAT;
			case "java.lang.Long":
				return LONG;
			case "java.lang.Double":
				return DOUBLE;
			case "dev.shadowtail.classfile.nncc.AccessedField":
				return ACCESSED_FIELD;
			case "dev.shadowtail.classfile.nncc.ClassPool":
				return CLASS_POOL;
			case "dev.shadowtail.classfile.nncc.InvokedMethod":
				return INVOKED_METHOD;
			case "net.multiphasicapps.classfile.ClassName":
				return CLASS_NAME;
			case "net.multiphasicapps.classfile.ClassNames":
				return CLASS_NAMES;
			case "List net.multiphasicapps.classfile.ClassName":
				return CLASS_NAMES;
			case "net.multiphasicapps.classfile.MethodDescriptor":
				return METHOD_DESCRIPTOR;
			case "dev.shadowtail.classfile.nncc.UsedString":
				return USED_STRING;
			case "dev.shadowtail.classfile.nncc.MethodIndex":
				return METHOD_INDEX;
		}
		
		// {@squirreljme.error JC2o Class does not map to a pool entry
		// type. (The class)}
		throw new IllegalArgumentException("JC2o " + __cl);
	}
}

