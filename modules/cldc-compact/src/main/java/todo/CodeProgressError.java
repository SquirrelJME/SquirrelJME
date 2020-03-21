// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

/**
 * This is the base class for code progress errors, where there is progress
 * on code that needs to be done somewhere.
 *
 * @since 2020/03/15
 */
public abstract class CodeProgressError
	extends Error
{
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message used.
	 * @since 2020/03/15
	 */
	public CodeProgressError(String __m)
	{
		super(__m);
	}
}
