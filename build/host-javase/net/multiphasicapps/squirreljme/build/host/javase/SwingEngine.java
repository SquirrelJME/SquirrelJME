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

/**
 * This is the display engine for Swing based systems.
 *
 * @since 2017/02/08
 */
public class SwingEngine
	implements DisplayEngine
{
	/** The title to use. */
	private volatile String _title;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void setTitle(String __s)
	{
		this._title = (__s != null ? __s : "SquirrelJME");
	}
}

