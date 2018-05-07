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
 * This parses the package information source file and for the most part is
 * used to handle the annotations accordingly if there are any.
 *
 * @since 2018/05/07
 */
public final class PackageInfoParser
	implements Runnable
{
	/** The compilation unit being parsed. */
	protected final CompilationUnitSyntax input;
	
	/** The output where it is to be stored. */
	protected final Structures output;
	
	/**
	 * Initializes the parser.
	 *
	 * @param __in The compilation unit input.
	 * @param __out The output structures where things are placed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/07
	 */
	public PackageInfoParser(CompilationUnitSyntax __in, Structures __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.input = __in;
		this.output = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final void run()
	{
		throw new todo.TODO();
	}
}

