// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal;

/**
 * This interface is used to create instances of terminals using the standard
 * service loader lookup.
 *
 * @since 2016/09/11
 */
public interface TerminalProvider
{
	/**
	 * Returns the instance of the terminal.
	 *
	 * @return The terminal instance.
	 * @since 2016/09/11
	 */
	public abstract Terminal terminal();
	
	/**
	 * Returns the priority of this provider.
	 *
	 * @return The provider priority.
	 * @since 2016/09/11
	 */
	public abstract int priority();
}

