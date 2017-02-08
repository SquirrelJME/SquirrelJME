// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import net.multiphasicapps.squirreljme.lcdui.DisplayEngine;
import net.multiphasicapps.squirreljme.lcdui.DisplayEngineProvider;

/**
 * This provides access to the swing display engine.
 *
 * @since 2017/02/08
 */
public class SwingEngineProvider
	implements DisplayEngineProvider
{
	/** Engines are static. */
	private final DisplayEngine[] _engines =
		new DisplayEngine[]{new SwingEngine()};
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public DisplayEngine[] engines()
	{
		return this._engines.clone(); 
	}
}

