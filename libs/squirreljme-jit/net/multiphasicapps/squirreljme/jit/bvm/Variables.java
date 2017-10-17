// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bvm;

/**
 * This class contains the variable treads for the stack, local, and
 * any needed temporary variables to be used when executing the program.
 *
 * @since 2017/10/17
 */
public final class Variables
{
	/** The number of temporary variables to allocate. */
	private static final int _NUM_TEMPORARIES =
		32;
	
	/** Local variables. */
	protected final VariableTread locals;
	
	/** Stack variables. */
	protected final VariableTread stack;
	
	/** Temporary and secondary stack variables. */
	protected final VariableTread temp =
		new VariableTread(_NUM_TEMPORARIES, true);
	
	/**
	 * Initializes variable storage.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @since 2017/10/17
	 */
	public Variables(int __ms, int __ml)
	{
		this.locals = new VariableTread(__ml, false);
		this.stack = new VariableTread(__ms, true);
	}
}

