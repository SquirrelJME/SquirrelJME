// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This represents the verification state of local variables.
 *
 * @since 2016/08/28
 */
public class ClassStackMapLocals
	extends ClassStackMapTread
{
	/**
	 * Initializes the local variable types.
	 *
	 * @param __n The number of local variables used.
	 * @since 2016/05/12
	 */
	ClassStackMapLocals(int __n)
	{
		super(__n);
	}
	
	/**
	 * Initializes local variable state from an existing one.
	 *
	 * @param __l The state to copy from.
	 * @since 2016/08/29
	 */
	ClassStackMapLocals(ClassStackMapLocals __l)
	{
		super(__l);
	}
}

