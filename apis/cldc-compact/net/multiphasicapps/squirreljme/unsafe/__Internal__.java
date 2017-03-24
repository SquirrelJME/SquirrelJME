// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains internal implementations. This is used by
 * {@link SquirrelJME} to implement the unsafe methods. Since the other class
 * is public and is used as a wrapper to this one, this means that the
 * implementations of methods in this class do not have to have certain checks
 * because the publically visible class handles such things.
 *
 * @see SquirrelJME
 * @since 2017/03/24
 */
final class __Internal__
{
	/**
	 * Not used.
	 *
	 * @since 2017/03/24
	 */
	private __Internal__()
	{
	}
}

