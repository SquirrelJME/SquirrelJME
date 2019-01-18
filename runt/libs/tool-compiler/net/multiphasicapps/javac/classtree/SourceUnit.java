// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.classtree;

import net.multiphasicapps.javac.CompilerInput;

/**
 * This is a unit which represents a source file.
 *
 * @since 2019/01/17
 */
public final class SourceUnit
	extends Unit
{
	/** The input unit. */
	protected final CompilerInput input;
	
	/**
	 * Initializes the unit.
	 *
	 * @param __i The input unit.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/17
	 */
	public SourceUnit(CompilerInput __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.input = __i;
	}
}

