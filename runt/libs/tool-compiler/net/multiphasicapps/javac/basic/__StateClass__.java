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

/**
 * This is used to store the state needed to parse classes.
 *
 * @since 2018/03/13
 */
class __StateClass__
	extends __State__
{
	/** Is this an inner class? */
	public final boolean isinner;
	
	/**
	 * Initializes the state.
	 *
	 * @param __inner Is this an inner class?
	 * @since 2018/03/13
	 */
	__StateClass__(boolean __inner)
	{
		super(__State__.Area.CLASS);
		
		this.isinner = __inner;
	}
}

