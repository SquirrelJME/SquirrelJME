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
 * This represents an active binding.
 *
 * Instances of this class are not required to be thread safe but are expected
 * to be mutable.
 *
 * @since 2017/02/23
 */
public interface ActiveBinding
	extends Binding
{
	/**
	 * Changes the binding information within the active binding.
	 *
	 * @param __t The type of change to perform.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/03
	 */
	public abstract void changeBinding(ActiveBindingChangeType __t)
		throws NullPointerException;
	
	/**
	 * Loads the information stored in the immutable binding and copies its
	 * state to this active binding.
	 *
	 * @param __b The binding to switch from.
	 * @throws JITException If the binding is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	public abstract void switchFrom(Binding __b)
		throws JITException, NullPointerException;
}

