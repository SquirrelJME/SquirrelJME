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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class contains the variable treads for the stack, local, and
 * any needed temporary variables to be used when executing the program.
 *
 * @since 2017/10/17
 */
public final class Variables
{
	/** Local variables. */
	protected final VariableTread locals;
	
	/** Stack variables. */
	protected final VariableTread stack;
	
	/**
	 * Initializes variable storage.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @since 2017/10/17
	 */
	public Variables(int __ms, int __ml)
	{
		Reference<Variables> selfref = new WeakReference<>(this);
		this.locals = new VariableTread(selfref, __ml, false);
		this.stack = new VariableTread(selfref, __ms, true);
	}
}

