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

import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.javac.syntax.CompilationUnitSyntax;
import net.multiphasicapps.javac.syntax.TypeSyntax;

/**
 * This is a name lookup which looks within a compilation unit to determine the
 * name symbol of something.
 *
 * The Java language standard at the compilation unit level defines three
 * relevant lookups for ambigious identifiers:
 *
 * Otherwise, if a field of that name is declared in the compilation unit
 * (S7.3) containing the Identifier by a single-static-import declaration
 * (S7.5.3), or by a static-import-on-demand declaration (S7.5.4) then the
 * AmbiguousName is reclassified as an ExpressionName.
 *
 * And then:
 *
 * Otherwise, if a type of that name is declared in the compilation unit (S7.3)
 * containing the Identifier, either by a single-type-import declaration
 * (S7.5.1), or by a type-import-on-demand declaration (S7.5.2), or by a
 * single-static-import declaration (S7.5.3), or by a static-import-on-demand
 * declaration (S7.5.4), then the AmbiguousName is reclassified as a TypeName.
 *
 * And finally:
 *
 * Otherwise, the AmbiguousName is reclassified as a PackageName. A later step
 * determines whether or not a package of that name actually exists.
 *
 * @since 2018/05/08
 */
public class CompilationUnitNameLookup
	implements NameLookup
{
	/** The run-time to use for class lookup. */
	protected final RuntimeInput runtime;
	
	/**
	 * Initializes the compilation unit name lookup.
	 *
	 * @param __cus The compilation unit to initialize for.
	 * @param __ri The input
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/08
	 */
	public CompilationUnitNameLookup(CompilationUnitSyntax __cus,
		RuntimeInput __ri)
		throws NullPointerException
	{
		if (__cus == null || __ri == null)
			throw new NullPointerException("NARG");
		
		this.runtime = __ri;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final TypeSymbol lookupType(TypeSyntax __ts)
		throws NullPointerException, StructureException
	{
		if (__ts == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

