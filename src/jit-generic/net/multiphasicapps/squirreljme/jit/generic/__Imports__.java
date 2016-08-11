// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This contains the static import table for the given namespace, it is used
 * by the runtime as essentially a table of pointers which refer to other
 * memory locations. Each import can refer to a class, method, field, or
 * string.
 *
 * Instance fields and methods are not directly pointed to, but are relative to
 * the object (in the case of methods there is a table of function pointers).
 *
 * Strings must act as if they {@link String#intern()} has been called on them.
 *
 * @since 2016/08/09
 */
final class __Imports__
{
	/** Imported classes. */
	protected final Map<ClassNameSymbol, __ImportClass__> impclasses =
		new HashMap<>();
	
	/**
	 * Initializes the import table.
	 *
	 * @since 2016/08/09
	 */
	__Imports__()
	{
	}
	
	/**
	 * Imports the given class.
	 *
	 * @param __n The name of the class to import.
	 * @return The import information.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__ImportClass__ __importClass(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Use a previously imported class if it exists
		Map<ClassNameSymbol, __ImportClass__> impclasses = this.impclasses;
		__ImportClass__ rv = impclasses.get(__n);
		if (rv != null)
			return rv;
		
		// Create new import
		rv = new __ImportClass__(__n);
		impclasses.put(__n, rv);
		
		// Return it
		return rv;
	}
	
	/**
	 * Imports an instance field.
	 *
	 * @param __c The class the field is in.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @return The import information.
	 * @since 2016/08/09
	 */
	__Import__ __importInstanceField(ClassNameSymbol __c, IdentifierSymbol __n,
		FieldSymbol __t)
		throws NullPointerException
	{
		// Check
		if (__c == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Imports an instance method.
	 *
	 * @param __c The class the method is in.
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @return The import information.
	 * @since 2016/08/09
	 */
	__Import__ __importInstanceMethod(ClassNameSymbol __c,
		IdentifierSymbol __n, MethodSymbol __t)
		throws NullPointerException
	{
		// Check
		if (__c == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Imports a static field.
	 *
	 * @param __c The class the field is in.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @return The import information.
	 * @since 2016/08/09
	 */
	__Import__ __importStaticField(ClassNameSymbol __c, IdentifierSymbol __n,
		FieldSymbol __t)
		throws NullPointerException
	{
		// Check
		if (__c == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Imports a static method.
	 *
	 * @param __c The class the method is in.
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @return The import information.
	 * @since 2016/08/09
	 */
	__Import__ __importStaticMethod(ClassNameSymbol __c, IdentifierSymbol __n,
		MethodSymbol __t)
		throws NullPointerException
	{
		// Check
		if (__c == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Imports the given string.
	 *
	 * @param __s The string to import.
	 * @return The import information.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__Import__ importString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

