// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a stack which is used to manage which volatile registers are used.
 *
 * @since 2019/05/24
 */
public final class VolatileRegisterStack
{
	/** The base register. */
	protected final int base;
	
	/** Registers being used. */
	private final Collection<Integer> _used =
		new ArrayList<>();
	
	/**
	 * Initializes the volatile stack.
	 *
	 * @param __b The base register.
	 * @since 2019/05/25
	 */
	public VolatileRegisterStack(int __b)
	{
		this.base = __b;
	}
	
	/**
	 * Clears all of the used volatile reisters.
	 *
	 * @since 2019/05/25
	 */
	public final void clear()
	{
		this._used.clear();
	}
	
	/**
	 * Returns the next volatile register.
	 *
	 * @return The next volatile register.
	 * @throws IllegalStateException If no registers are available.
	 * @since 2019/05/24
	 */
	public final int get()
		throws IllegalStateException
	{
		// Find next register to use from the base, use any register which
		// was not previously recorded
		int at = this.base;
		Collection<Integer> used = this._used;
		while (used.contains(at))
			at++;
		
		// {@squirreljme.error JC4l Exceeded maximum permitted registers.
		// (The base register)}
		if (at >= NativeCode.MAX_REGISTERS)
			throw new IllegalStateException("JC4l " + this.base);
		
		// Record it
		used.add(at);
		return at;
	}
	
	/**
	 * Removes the given volatile register from usage.
	 *
	 * @param __r The register to remove.
	 * @throws IllegalStateException If it was never used.
	 * @since 2019/05/24
	 */
	public final void remove(int __r)
		throws IllegalStateException
	{
		// {@squirreljme.error JC13 Register to remove was never previously
		// returned or was removed multiple times. (The register)}
		if (!this._used.remove(__r))
			throw new IllegalStateException("JC13 " + __r);
	}
}

