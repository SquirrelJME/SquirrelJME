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
import net.multiphasicapps.javac.syntax.MemberSyntax;
import net.multiphasicapps.javac.syntax.TypeSyntax;

/**
 * This class parses individual classes.
 *
 * @since 2018/05/08
 */
public final class ClassSyntaxParser
	implements Runnable
{
	/** The parent class, may be {@code null}. */
	protected final ClassSyntax parent;
	
	/** The current class. */
	protected final ClassSyntax current;
	
	/** The run-time input. */
	protected final RuntimeInput runtime;
	
	/** The name lookup for this class. */
	protected final ClassNameLookup namelookup;
	
	/**
	 * Initializes the class syntax parser.
	 *
	 * @param __parent The parent class, may be {@code null}.
	 * @param __cur The current class being parsed.
	 * @param __pnl The parent name lookup.
	 * @param __ri The runtime input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/08
	 */
	public ClassSyntaxParser(ClassSyntax __parent, ClassSyntax __cur,
		NameLookup __pnl, RuntimeInput __ri)
		throws NullPointerException
	{
		if (__cur == null || __pnl == null || __ri == null)
			throw new NullPointerException("NARG");
		
		this.parent = __parent;
		this.current = __cur;
		this.runtime = __ri;
		this.namelookup = new ClassNameLookup(__pnl, __cur);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/08
	 */
	@Override
	public final void run()
	{
		ClassSyntax current = this.current;
		ClassNameLookup namelookup = this.namelookup;
		
		// Handle extends of the class
		for (TypeSyntax t : current.extending())
			throw new todo.TODO();
		
		// Handle implements of the class
		for (TypeSyntax t : current.implementing())
			throw new todo.TODO();
		
		// Handle class members
		for (MemberSyntax m: current.members())
			throw new todo.TODO();
		
		// Build class structure
		throw new todo.TODO();
	}
}

