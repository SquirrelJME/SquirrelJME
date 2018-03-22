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
 * Represents the end of file and as such parsing is finished.
 *
 * @since 2018/03/13
 */
@Deprecated
final class __StateEndOfFile__
	extends __State__
{
	/**
	 * Initializes the state.
	 *
	 * @since 2018/03/13
	 */
	__StateEndOfFile__()
	{
		super(__State__.Area.END_OF_FILE);
	}
}

