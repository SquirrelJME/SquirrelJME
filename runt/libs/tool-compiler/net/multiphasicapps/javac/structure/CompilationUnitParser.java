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

import net.multiphasicapps.javac.syntax.CompilationUnitSyntax;

/**
 * This class is used to parse compilation units.
 *
 * @since 2018/05/07
 */
public final class CompilationUnitParser
	implements Runnable
{
	/** The compilation unit being parsed. */
	protected final CompilationUnitSyntax input;
	
	/** The runtime information. */
	protected final RuntimeInput runtime;
	
	/** The output where it is to be stored. */
	protected final Structures output;
	
	/**
	 * Initializes the parser.
	 *
	 * @param __in The compilation unit input.
	 * @param __ri The run-time input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/07
	 */
	public CompilationUnitParser(CompilationUnitSyntax __in, RuntimeInput __ri)
		throws NullPointerException
	{
		if (__in == null || __ri == null)
			throw new NullPointerException("NARG");
		
		this.input = __in;
		this.runtime = __ri;
		this.output = __ri.structures();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final void run()
	{
		// Check if the package the classes are in has to be processed (if it
		// is in a package)
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
	}
}

