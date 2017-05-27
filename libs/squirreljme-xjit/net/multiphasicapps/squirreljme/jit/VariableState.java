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
 * This is the base class which is used to represent the state of variables
 * within a basic block.
 *
 * @see ImmutableVariableState
 * @see MutableVariableState
 * @since 2017/05/25
 */
public abstract class VariableState
{
	/**
	 * This represents single variables within treads, a slot represents a
	 * single variable.
	 *
	 * @since 2017/05/26
	 */
	public abstract class BaseSlot
	{
	}
	
	/**
	 * This represents a tread of variables which store slots within.
	 *
	 * @since 2017/05/26
	 */
	public abstract class BaseTread
	{
	}
}

