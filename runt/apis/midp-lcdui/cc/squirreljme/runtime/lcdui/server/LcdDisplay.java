// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

/**
 * This class represents a single display that is available, displayables may
 * be attached to it accordingly.
 *
 * @since 2018/03/18
 */
public abstract class LcdDisplay
{
	/** The index of this display. */
	protected final int index;
	
	/**
	 * Initiazes the display.
	 *
	 * @param __dx The index of this display.
	 * @since 2018/03/18
	 */
	public LcdDisplay(int __dx)
	{
		this.index = __dx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * Returns the display index.
	 *
	 * @return The display index.
	 * @since 2018/03/18
	 */
	public final int index()
	{
		return this.index;
	}
}

