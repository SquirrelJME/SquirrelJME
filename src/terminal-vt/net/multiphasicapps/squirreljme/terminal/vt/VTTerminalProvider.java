// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal.vt;

import net.multiphasicapps.squirreljme.terminal.Terminal;
import net.multiphasicapps.squirreljme.terminal.TerminalProvider;

/**
 * This is a provider for standard VT-family terminals.
 *
 * Note that this provide interface only outputs to {@link System.out} and
 * does not handle terminal input.
 *
 * @since 2016/09/11
 */
public class VTTerminalProvider
	implements TerminalProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public Terminal terminal()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public int priority()
	{
		// Since this uses standard output, use as a last ditch fallback
		return 0;
	}
}

