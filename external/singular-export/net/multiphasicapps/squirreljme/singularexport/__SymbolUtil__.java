// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This class contains utilities for symbols.
 *
 * @since 2016/09/30
 */
class __SymbolUtil__
{
	/**
	 * Virtualizes the specified class name.
	 *
	 * @param __n The class to virtualize.
	 * @return The virtualized class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	static ClassNameSymbol __virtualClass(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Virtualize it
		return ClassNameSymbol.of("$squirreljme$/" + __n);
	}
	
	/**
	 * Virtualizes the field symbol type.
	 *
	 * @param __f The field to virtualize.
	 * @return The virtualized symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	static FieldSymbol __virtualField(FieldSymbol __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Translate array type
		if (__f.isArray())
			throw new Error("TODO");
		
		// Primitives are unchanged
		else if (__f.isPrimitive())
			return __f;
		
		// Virtualize class
		else
			return __virtualClass(__f.binaryName().asClassName()).asField();
	}
	
	/**
	 * Virtualizes the identifier symbol.
	 *
	 * @param __n The symbol to virtualize.
	 * @return The virtualized symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	static IdentifierSymbol __virtualIdentifier(IdentifierSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Virtualize
		return IdentifierSymbol.of("$" + __n);
	}
	
	/**
	 * Virtualizes the method symbol.
	 *
	 * @param __m The method symbol to virtualize.
	 * @return The virtualized symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	static MethodSymbol __virtualMethod(MethodSymbol __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Build new one
		StringBuilder sb = new StringBuilder("(");
		
		// Handle arguments
		int n = __m.argumentCount();
		for (int i = 0; i < n; i++)
			sb.append(__virtualField(__m.get(i)));
		
		// Handle return value
		sb.append(')');
		FieldSymbol rv = __m.returnValue();
		if (rv == null)
			sb.append('V');
		else
			sb.append(__virtualField(rv));
		
		// Finish
		return MethodSymbol.of(sb.toString());
	}
}

