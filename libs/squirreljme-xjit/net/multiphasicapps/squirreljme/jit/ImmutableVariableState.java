// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is an immutable variable state which contains one which never changes
 * once it is created.
 *
 * @since 2017/05/25
 */
public final class ImmutableVariableState
	extends VariableState
{
	/**
	 * Initializes the immutable variable state from the given state.
	 *
	 * @param __vs The state to base this state from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/28
	 */
	public ImmutableVariableState(VariableState __vs)
		throws NullPointerException
	{
		// Check
		if (__vs == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This represents an immutable variable within a tread.
	 *
	 * @since 2017/05/27
	 */
	public final class ImmutableSlot
		extends VariableState.Slot
	{
	}
	
	/**
	 * This represents an immutable tread of slots.
	 *
	 * @since 2017/05/27
	 */
	public final class ImmutableTread
		extends VariableState.Tread
	{
	}
}

