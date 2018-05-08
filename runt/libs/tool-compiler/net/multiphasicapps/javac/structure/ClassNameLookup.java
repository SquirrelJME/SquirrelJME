// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.javac.syntax.ClassSyntax;
import net.multiphasicapps.javac.syntax.TypeParameterSyntax;
import net.multiphasicapps.javac.syntax.TypeParametersSyntax;

/**
 * This is used to lookup fields, methods, and identifiers within a class
 * scope.
 *
 * @since 2018/05/08
 */
public final class ClassNameLookup
	implements NameLookup
{
	/** The parent name lookup. */
	protected final NameLookup parent;	
	
	/** The class to name lookup for. */
	protected final ClassSyntax current;
	
	/**
	 * Initializes the class name lookup.
	 *
	 * @param __parent The parent name lookup.
	 * @param __cur The current class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/08
	 */
	public ClassNameLookup(NameLookup __parent, ClassSyntax __cur)
		throws NullPointerException
	{
		if (__parent == null || __cur == null)
			throw new NullPointerException("NARG");
		
		this.parent = __parent;
		this.current = __cur;
		
		// Determine if there are any type parameters which need their meaning
		// defined
		for (TypeParameterSyntax typeparam : __cur.typeParameters())
			throw new todo.TODO();
	}
}

