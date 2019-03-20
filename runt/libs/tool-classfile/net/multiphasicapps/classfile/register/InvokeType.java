// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * This represents the type of invocation that is performed.
 *
 * @since 2019/03/20
 */
public enum InvokeType
{
	/** Static invoke. */
	STATIC,
	
	/** Special invoke. */
	SPECIAL,
	
	/** End. */
	;
	
	/**
	 * Does this use an instance variable?
	 *
	 * @return If there is an instance variable that is used.
	 * @since 2019/03/20
	 */
	public final boolean hasInstance()
	{
		return this != STATIC;
	}
}

