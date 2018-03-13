// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import net.multiphasicapps.classfile.BinaryName;

/**
 * This represents a single import statement which has been parsed.
 *
 * @since 2018/03/13
 */
public final class ImportStatement
{
	/**
	 * Initializes the import statement.
	 *
	 * @param __static Is this import static?
	 * @param __what What is being imported?
	 * @param __wild Is this a wildcard import?
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/13
	 */
	public ImportStatement(boolean __static, BinaryName __what, boolean __wild)
		throws NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/13
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof ImportStatement))
			return false;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/13
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/13
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

