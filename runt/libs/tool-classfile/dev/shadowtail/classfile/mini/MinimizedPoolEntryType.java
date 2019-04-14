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
	
	/** Accessed Field. */
	ACCESSED_FIELD,
	
	/** Name of class. */
	CLASS_NAME,
	
	/** Invoked Method. */
	INVOKED_METHOD,
	
	/** Field Descriptor. */
	FIELD_DESCRIPTOR,
	
	/** Field Name. */
	FIELD_NAME,
	
	/** Field Reference. */
	FIELD_REFERENCE,
	
	/** Method Descriptor. */
	METHOD_DESCRIPTOR,
	
	/** Method Handle. */
	METHOD_HANDLE,
	
	/** Method name, */
	METHOD_NAME,
	
	/** End. */
	;
	
	/**
	 * Returns the entry type that is used for the specified class.
	 *
	 * @param __cl The class to check.
	 * @return The entry type for the class.
	 * @throws IllegalArgumentException On null arguments.
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
			case "dev.shadowtail.classfile.nncc.AccessedField":
				return ACCESSED_FIELD;
			case "dev.shadowtail.classfile.nncc.InvokedMethod":
				return INVOKED_METHOD;
			case "net.multiphasicapps.classfile.ClassName":
				return CLASS_NAME;
			case "net.multiphasicapps.classfile.FieldDescriptor":
				return FIELD_DESCRIPTOR;
			case "net.multiphasicapps.classfile.FieldName":
				return FIELD_NAME;
			case "net.multiphasicapps.classfile.FieldReference":
				return FIELD_REFERENCE;
			case "net.multiphasicapps.classfile.MethodDescriptor":
				return METHOD_DESCRIPTOR;
			case "net.multiphasicapps.classfile.MethodHandle":
				return METHOD_HANDLE;
			case "net.multiphasicapps.classfile.MethodName":
				return METHOD_NAME;
		}
		
		// {@squirreljme.error JC2o Class does not map to a pool entry
		// type. (The class)}
		throw new IllegalArgumentException("JC2o " + __cl);
	}
}

