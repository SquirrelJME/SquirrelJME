// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
 * Read of a class.
 *
 * @since 2018/03/09
 */
final class __AtClass__
	extends __At__
	implements __IsAnnotated__
{
	/** Is this an inner class? */
	public final boolean isinner;
	
	/**
	 * Initializes the class context.
	 *
	 * @param __inner Is this an inner class?
	 * @since 2018/03/09
	 */
	__AtClass__(boolean __inner)
	{
		super(ContextArea.CLASS);
		
		this.isinner = __inner;
	}
}
