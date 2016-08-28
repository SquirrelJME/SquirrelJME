// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents the verification state of local variables.
 *
 * @since 2016/08/28
 */
class __SMTLocals__
	extends __SMTTread__
{
	/**
	 * Initializes the local variable types.
	 *
	 * @param __n The number of local variables used.
	 * @since 2016/05/12
	 */
	__SMTLocals__(int __n)
	{
		super(__n);
	}
}

