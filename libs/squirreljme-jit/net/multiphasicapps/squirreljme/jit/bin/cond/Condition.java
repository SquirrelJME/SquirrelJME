// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin.cond;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.bin.LinkerState;

/**
 * This is the base interface for a condition which is used to verifiy whether
 * a condition is met by the virtual machine.
 *
 * Classes which extend this class should be final.
 *
 * This is an abstract class and not an interface so that the standard methods
 * are forced to be implemented.
 *
 * @since 2017/07/07
 */
public abstract class Condition
{
	/** The string representation of the condition. */
	private volatile Reference<String> _string;
	
	/**
	 * Checks whether the given linker state passes the condition this checks.
	 *
	 * @param __ls The linker state to check.
	 * @return {@code true} if the condition is met.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/07
	 */
	public abstract boolean check(LinkerState __ls)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Returns the string representation of this condition.
	 *
	 * @return The string representation of this condition.
	 * @since 2017/07/07
	 */
	protected abstract String internalToString();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = internalToString()));
		
		return rv;
	}
}

