// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.magic;

/**
 * This is thrown when magic is called when it is forbidden to use it.
 *
 * There is no {@code sun.misc.Unsafe} exploitation here.
 *
 * @since 2016/03/17
 */
public final class ForbbidenMagicError
	extends Error
{
	/**
	 * Initializes the exception with a hidden message.
	 *
	 * @since 2016/03/17
	 */
	public ForbbidenMagicError()
	{
		super("The message `uaoxoedo0ve9ouaxx` reveals it twice.");
	}
}

